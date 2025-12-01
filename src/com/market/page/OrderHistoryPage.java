package com.market.page;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import com.market.member.UserInIt;
import com.market.member.User;
import com.market.dao.UserDAO;
import com.market.dao.OrderDAO;
import com.market.dao.OrderSummaryRow;
import com.market.dao.OrderItemRow;

public class OrderHistoryPage extends JPanel {

    private JTable orderTable;
    private JTable detailTable;
    private DefaultTableModel orderModel;
    private DefaultTableModel detailModel;

    private static final String[] ORDER_HEADER = {
            "주문번호", "주문일시", "수령인", "연락처", "배송지", "총금액", "상품 개수"
    };

    private static final String[] ITEM_HEADER = {
            "도서ID", "도서명", "수량", "단가", "합계"
    };

    public OrderHistoryPage(JPanel panel) {
        Font ft = new Font("함초롬돋움", Font.BOLD, 15);
        setLayout(null);

        // MainWindow에서 넘어온 panel의 크기와 맞추기
        Rectangle rect = panel.getBounds();
        setPreferredSize(rect.getSize());

        // ================= 상단: 주문 목록 =================
        JPanel orderPanel = new JPanel();
        orderPanel.setBounds(0, 0, rect.width, 250);
        orderPanel.setLayout(new BorderLayout());
        add(orderPanel);

        JLabel orderTitle = new JLabel("주문 내역", SwingConstants.LEFT);
        orderTitle.setFont(ft);
        orderPanel.add(orderTitle, BorderLayout.NORTH);

        orderModel = new DefaultTableModel(ORDER_HEADER, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderModel);
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderPanel.add(orderScroll, BorderLayout.CENTER);

        // ================= 하단: 선택한 주문의 상세 =================
        JPanel detailPanel = new JPanel();
        detailPanel.setBounds(0, 260, rect.width, 230);
        detailPanel.setLayout(new BorderLayout());
        add(detailPanel);

        JLabel detailTitle = new JLabel("선택한 주문의 상세 내역", SwingConstants.LEFT);
        detailTitle.setFont(ft);
        detailPanel.add(detailTitle, BorderLayout.NORTH);

        detailModel = new DefaultTableModel(ITEM_HEADER, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        detailTable = new JTable(detailModel);
        JScrollPane detailScroll = new JScrollPane(detailTable);
        detailPanel.add(detailScroll, BorderLayout.CENTER);

        // ================= 데이터 로딩 =================
        loadOrderList();

        // 주문 목록 클릭 시 상세 조회
        orderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = orderTable.getSelectedRow();
                if (row < 0) return;

                long orderId = (long) orderTable.getValueAt(row, 0);
                loadOrderItems(orderId);
            }
        });
    }

    /**
     * 현재 로그인한(입력한) 고객의 주문 목록 읽어서 orderTable에 표현
     */
    private void loadOrderList() {
        orderModel.setRowCount(0); // 테이블 비우기

        User user = UserInIt.getmUser();
        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "고객 정보가 없습니다. 먼저 고객 정보를 입력하세요.",
                    "주문 내역",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = user.getName();
        String phone = user.getPhone();

        // 이름 + 전화번호로 user_id 확보 (없으면 INSERT 후 같은 id 사용)
        UserDAO userDao = new UserDAO();
        int userId = userDao.ensureUser(name, phone);

        if (userId <= 0) {
            JOptionPane.showMessageDialog(this,
                    "고객 정보를 DB에서 조회하는 중 오류가 발생했습니다.",
                    "주문 내역",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 확보한 userId로 주문 목록 조회
        OrderDAO orderDao = new OrderDAO();
        List<OrderSummaryRow> orders = orderDao.findOrdersByUser(userId);

        if (orders == null || orders.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "등록된 주문 내역이 없습니다.",
                    "주문 내역",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (OrderSummaryRow row : orders) {
            Object[] tableRow = new Object[]{
                    row.getOrderId(),
                    row.getOrderDate(),      // Timestamp 그대로 표시 (원하면 포맷 변경 가능)
                    row.getReceiverName(),
                    row.getReceiverPhone(),
                    row.getReceiverAddress(),
                    row.getTotalPrice(),
                    row.getItemCount()
            };
            orderModel.addRow(tableRow);
        }
    }

    /**
     * 특정 주문의 상세 항목(order_items + 도서명)을 detailTable에 표시
     */
    private void loadOrderItems(long orderId) {
        detailModel.setRowCount(0);

        OrderDAO orderDao = new OrderDAO();
        List<OrderItemRow> items = orderDao.findOrderItems(orderId);

        if (items == null || items.isEmpty()) {
            return;
        }

        for (OrderItemRow row : items) {
            Object[] tableRow = new Object[]{
                    row.getBookId(),
                    row.getBookName(),
                    row.getQuantity(),
                    row.getUnitPrice(),
                    row.getTotalPrice()
            };
            detailModel.addRow(tableRow);
        }
    }
}

