package com.market.page;

import javax.swing.*;
import java.awt.*;
import com.market.cart.Cart;
import com.market.member.UserInIt;
import java.awt.event.ActionEvent;
import com.market.dao.UserDAO;
import com.market.dao.OrderDAO;
import com.market.cart.CartItem;

public class CartShippingPage extends JPanel {

	Cart mCart;
	JPanel shippingPanel;
	JPanel radioPanel;

	private JTextField nameField;
    private JTextField phoneField;
    private JTextField addressField;
    
	public CartShippingPage(JPanel panel, Cart cart) {

		Font ft;
		ft = new Font("함초롬돋움", Font.BOLD, 15);

		setLayout(null);

		Rectangle rect = panel.getBounds();
		System.out.println(rect);
		setPreferredSize(rect.getSize());

		radioPanel = new JPanel();
		radioPanel.setBounds(300, 0, 700, 50);
		radioPanel.setLayout(new FlowLayout());
		add(radioPanel);
		JLabel radioLabel = new JLabel("배송받을 분은 고객정보와 같습니까?");
		radioLabel.setFont(ft);
		JRadioButton radioOk = new JRadioButton("예");
		radioOk.setFont(ft);
		JRadioButton radioNo = new JRadioButton("아니오");
		radioNo.setFont(ft);
		radioPanel.add(radioLabel);
		radioPanel.add(radioOk);
		radioPanel.add(radioNo);

		shippingPanel = new JPanel();
		shippingPanel.setBounds(200, 50, 700, 500);
		shippingPanel.setLayout(null);
		add(shippingPanel);

		radioOk.setSelected(true);
		radioNo.setSelected(false);
		UserShippingInfo(true);

		this.mCart = cart;

		radioOk.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {

				if (radioOk.isSelected()) {
					shippingPanel.removeAll();
					UserShippingInfo(true);
					shippingPanel.revalidate();
					shippingPanel.repaint();
					radioNo.setSelected(false);
				}
			}
		});

		radioNo.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {

				if (radioNo.isSelected()) {
					shippingPanel.removeAll();
					UserShippingInfo(false);
					shippingPanel.revalidate();
					shippingPanel.repaint();
					radioOk.setSelected(false);
				}
			}
		});
	}

	public void UserShippingInfo(boolean select) {

		Font ft;
		ft = new Font("함초롬돋움", Font.BOLD, 15);

		JPanel namePanel = new JPanel();
		namePanel.setBounds(0, 100, 700, 50);
		JLabel nameLabel = new JLabel("고객명 : ");
		nameLabel.setFont(ft);
		namePanel.add(nameLabel);

		nameField = new JTextField(15);
		nameField.setFont(ft);
		if (select) {

			nameField.setBackground(Color.LIGHT_GRAY);
			nameField.setText(UserInIt.getmUser().getName());

		}
		namePanel.add(nameField);
		shippingPanel.add(namePanel);

		JPanel phonePanel = new JPanel();
		phonePanel.setBounds(0, 150, 700, 50);
		JLabel phoneLabel = new JLabel("연락처 : ");
		phoneLabel.setFont(ft);
		phonePanel.add(phoneLabel);

		phoneField = new JTextField(15);
		phoneField.setFont(ft);
		if (select) {
			phoneField.setBackground(Color.LIGHT_GRAY);
			phoneField.setText(String.valueOf(UserInIt.getmUser().getPhone()));

		}
		phonePanel.add(phoneField);
		shippingPanel.add(phonePanel);

		JPanel addressPanel = new JPanel();
		addressPanel.setBounds(0, 200, 700, 50);
		JLabel label = new JLabel("배송지 : ");
		label.setFont(ft);
		addressPanel.add(label);

		addressField = new JTextField(15);
	    addressField.setFont(ft);
		addressPanel.add(addressField);
		shippingPanel.add(addressPanel);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(0, 300, 700, 100);

		JLabel buttonLabel = new JLabel("주문완료");
		buttonLabel.setFont(new Font("함초롬돋움", Font.BOLD, 15));
		JButton orderButton = new JButton();
		orderButton.add(buttonLabel);
		buttonPanel.add(orderButton);
		shippingPanel.add(buttonPanel);

		orderButton.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {

		        // 1) 입력값 읽기
		        String name = nameField.getText().trim();
		        String phone = phoneField.getText().trim();
		        String address = addressField.getText().trim();

		        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
		            JOptionPane.showMessageDialog(orderButton,
		                    "고객명 / 연락처 / 배송지를 모두 입력하세요.",
		                    "입력 오류",
		                    JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        try {
		            // 2) users 테이블에 유저 등록/조회
		            UserDAO userDao = new UserDAO();
		            int userId = userDao.ensureUser(name, phone);

		            if (userId <= 0) {
		                JOptionPane.showMessageDialog(orderButton,
		                        "고객 정보를 DB에 저장하는 중 오류가 발생했습니다.",
		                        "주문 오류",
		                        JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            // 3) 장바구니 총 금액 계산
		            int totalPrice = 0;
		            for (CartItem item : mCart.getmCartItem()) {
		                totalPrice += item.getTotalPrice();
		            }

		            // 4) orders 테이블에 주문 1건 저장
		            OrderDAO orderDao = new OrderDAO();
		            long orderId = orderDao.insertOrder(
		                    userId,
		                    name,
		                    phone,
		                    address,
		                    totalPrice
		            );

		            if (orderId <= 0) {
		                JOptionPane.showMessageDialog(orderButton,
		                        "주문 정보를 DB에 저장하는 중 오류가 발생했습니다.",
		                        "주문 오류",
		                        JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            // 5) order_items 테이블에 장바구니 항목들 저장
		            for (CartItem ci : mCart.getmCartItem()) {
		                orderDao.insertOrderItem(
		                        orderId,
		                        ci.getBookID(),
		                        ci.getQuantity(),
		                        ci.getItemBook().getUnitPrice(),
		                        ci.getTotalPrice()
		                );
		            }

		            // 6) 화면 교체 + 장바구니 비우기 (기존 코드 유지)
		            radioPanel.removeAll();
		            radioPanel.revalidate();
		            radioPanel.repaint();

		            shippingPanel.removeAll();
		            shippingPanel.add("주문 배송지", new CartOrderBillPage(shippingPanel, mCart));

		            mCart.deleteBook();
		            shippingPanel.revalidate();
		            shippingPanel.repaint();

		            JOptionPane.showMessageDialog(orderButton,
		                    "주문이 완료되었습니다.\n주문번호: " + orderId,
		                    "주문 완료",
		                    JOptionPane.INFORMATION_MESSAGE);

		        } catch (Exception ex) {
		            ex.printStackTrace();
		            JOptionPane.showMessageDialog(orderButton,
		                    "주문 처리 중 예기치 못한 오류가 발생했습니다.",
		                    "주문 오류",
		                    JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});
	}

}