package com.market.page;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.market.bookitem.Book;
import com.market.bookitem.BookInIt;
import com.market.cart.Cart;
import com.market.dao.BookDAO;
import com.market.dao.CartItemDAO;
import com.market.member.UserInIt;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CartAddItemPage extends JPanel {

    private static final String[] TABLE_HEADER = { "도서ID", "도서명", "가격", "저자", "설명", "분야", "출판일" };

    private ImageIcon imageBook;
    private int mSelectRow = 0;

    private Cart mCart;

    private JTable bookTable;              // 테이블을 필드로 관리
    private ArrayList<Book> booklist;      // 현재 화면에 보이는 도서 목록
    private JPanel imagePanel;             // 책 이미지 패널(검색 후에도 다시 쓰려고 필드로 변경)

    public CartAddItemPage(JPanel panel, Cart cart) {
        Font ft = new Font("함초롬돋움", Font.BOLD, 15);

        setLayout(null);

        Rectangle rect = panel.getBounds();
        setPreferredSize(rect.getSize());

        mCart = cart;

        // ===================== [왼쪽: 도서 이미지 패널] =====================
        imagePanel = new JPanel();
        imagePanel.setBounds(20, 0, 300, 400);
        imageBook = new ImageIcon("./images/ISBN1234.jpg");
        imageBook.setImage(imageBook.getImage().getScaledInstance(250, 300, Image.SCALE_DEFAULT));
        JLabel imageLabel = new JLabel(imageBook);
        imagePanel.add(imageLabel);
        add(imagePanel);

        // ===================== [오른쪽: 테이블 + 검색 영역] =====================
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(300, 0, 700, 400);
        tablePanel.setLayout(new BorderLayout());
        add(tablePanel);

        // 상단 검색 패널
        JPanel searchPanel = new JPanel();
        JLabel searchTypeLabel = new JLabel("검색 조건: ");
        String[] searchTypes = { "제목", "저자", "분야" };
        JComboBox<String> searchTypeCombo = new JComboBox<>(searchTypes);

        JLabel searchKeywordLabel = new JLabel("검색어: ");
        JTextField searchTextField = new JTextField(15);
        JButton searchButton = new JButton("검색");

        searchPanel.add(searchTypeLabel);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchKeywordLabel);
        searchPanel.add(searchTextField);
        searchPanel.add(searchButton);

        tablePanel.add(searchPanel, BorderLayout.NORTH);

        // 책 목록 초기 데이터: BookInIt에서 가져오기(이미 DB에서 불러오도록 변경된 상태)
        booklist = BookInIt.getmBookList();

        // 테이블 생성
        bookTable = new JTable();
        updateBookTable(booklist);      // 초기 전체 목록 세팅

        // 스크롤 포함해서 테이블 붙이기
        JScrollPane jScrollPane = new JScrollPane(bookTable);
        jScrollPane.setPreferredSize(new Dimension(600, 350));
        tablePanel.add(jScrollPane, BorderLayout.CENTER);

        // ===================== [아래: 장바구니 버튼 패널] =====================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0, 400, 1000, 400);
        add(buttonPanel);

        JLabel buttonLabel = new JLabel("장바구니에 담기");
        buttonLabel.setFont(ft);
        JButton addButton = new JButton();
        addButton.add(buttonLabel);
        buttonPanel.add(addButton);

        // ===================== [테이블 클릭 시 이미지 변경] =====================
        bookTable.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bookTable.getSelectedRow();
                if (row < 0) return;

                mSelectRow = row;

                Object value = bookTable.getValueAt(row, 0);   // 도서ID
                String str = value + ".jpg";

                imageBook = new ImageIcon("./images/" + str);
                imageBook.setImage(imageBook.getImage().getScaledInstance(250, 300, Image.SCALE_DEFAULT));
                JLabel label = new JLabel(imageBook);
                imagePanel.removeAll();
                imagePanel.add(label);
                imagePanel.revalidate();
                imagePanel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });

        // ===================== [장바구니 추가 버튼 동작] =====================
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int select = JOptionPane.showConfirmDialog(addButton, "장바구니에 추가하겠습니까?");
                if (select == JOptionPane.YES_OPTION) {
                    // 현재 테이블에서 선택된 행 기준으로 책 가져오기
                    int row = bookTable.getSelectedRow();
                    if (row < 0) {
                        JOptionPane.showMessageDialog(addButton, "도서를 먼저 선택하세요.");
                        return;
                    }

                    String bookId = (String) bookTable.getValueAt(row, 0);

                    // bookId로 현재 booklist에서 해당 도서 찾기
                    Book selectedBook = null;
                    for (Book b : booklist) {
                        if (b.getBookId().equals(bookId)) {
                            selectedBook = b;
                            break;
                        }
                    }

                    if (selectedBook == null) {
                        JOptionPane.showMessageDialog(addButton, "선택한 도서를 찾을 수 없습니다.");
                        return;
                    }
                    
                    //세션 ID 가져오기 (이름_전화번호숫자)
                    String sessionId = UserInIt.getSessionId();
                    CartItemDAO cartDao = new CartItemDAO();

                    if (!isCartInBook(selectedBook.getBookId())) {
                        mCart.insertBook(selectedBook);
                        
                     //DB 장바구니에도 +1
                        cartDao.upsertCartItem(sessionId, selectedBook.getBookId(), 1);
                        JOptionPane.showMessageDialog(addButton, "장바구니에 추가했습니다.");
                    } else {
                        // 이미 장바구니에 있던 도서면
                        // isCartInBook() 안에서 수량만 +1 된 상태

                        //DB 장바구니에도 수량 +1
                        cartDao.upsertCartItem(sessionId, selectedBook.getBookId(), 1);

                        JOptionPane.showMessageDialog(addButton, "이미 장바구니에 있는 도서입니다.\n수량을 1 증가시켰습니다.");
                    }
                }
            }
        });

        // ===================== [검색 버튼 동작 - BookDAO 연동] =====================
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchTextField.getText().trim();
                String type = (String) searchTypeCombo.getSelectedItem();

                if (keyword.isEmpty()) {
                    // 검색어가 비어있으면 전체 목록 다시 보여주기
                    booklist = BookInIt.getmBookList();
                    updateBookTable(booklist);
                    return;
                }

                BookDAO dao = new BookDAO();
                ArrayList<Book> result = null;

                if (type.equals("제목")) {
                    result = dao.findByTitle(keyword);
                } else if (type.equals("저자")) {
                    result = dao.findByAuthor(keyword);
                } else if (type.equals("분야")) {
                    result = dao.findByCategory(keyword);
                }

                if (result == null || result.isEmpty()) {
                    JOptionPane.showMessageDialog(searchButton, "검색 결과가 없습니다.");
                }

                booklist = (result != null) ? result : new ArrayList<>();
                updateBookTable(booklist);
            }
        });

    }

    // 현재 booklist를 기준으로 테이블 내용 갱신
    private void updateBookTable(ArrayList<Book> list) {
        Object[][] contents = new Object[list.size()][TABLE_HEADER.length];

        for (int i = 0; i < list.size(); i++) {
            Book bookitem = list.get(i);
            contents[i][0] = bookitem.getBookId();
            contents[i][1] = bookitem.getName();
            contents[i][2] = bookitem.getUnitPrice();
            contents[i][3] = bookitem.getAuthor();
            contents[i][4] = bookitem.getDescription();
            contents[i][5] = bookitem.getCategory();
            contents[i][6] = bookitem.getReleaseDate();
        }

        DefaultTableModel model = new DefaultTableModel(contents, TABLE_HEADER) {
            // 셀 편집 못 하게 막기
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable.setModel(model);

        if (list.size() > 0) {
            bookTable.setRowSelectionInterval(0, 0);
            mSelectRow = 0;

            // 첫 번째 책 이미지도 같이 세팅
            String firstId = list.get(0).getBookId();
            imageBook = new ImageIcon("./images/" + firstId + ".jpg");
            imageBook.setImage(imageBook.getImage().getScaledInstance(250, 300, Image.SCALE_DEFAULT));
            JLabel label = new JLabel(imageBook);
            imagePanel.removeAll();
            imagePanel.add(label);
            imagePanel.revalidate();
            imagePanel.repaint();
        } else {
            // 검색 결과 없으면 이미지 패널 비우기
            imagePanel.removeAll();
            imagePanel.revalidate();
            imagePanel.repaint();
        }
    }

    public boolean isCartInBook(String bookId) {
        return mCart.isCartInBook(bookId);
    }
}
