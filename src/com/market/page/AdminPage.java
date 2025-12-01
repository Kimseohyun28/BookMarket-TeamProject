package com.market.page;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import com.market.bookitem.Book;
import com.market.dao.BookDAO;

public class AdminPage extends JPanel {

    private JTable bookTable;
    private DefaultTableModel tableModel;

    public AdminPage(JPanel panel) {

        Font ft = new Font("함초롬돋움", Font.BOLD, 15);

        setLayout(null);

        Rectangle rect = panel.getBounds();
        setPreferredSize(rect.getSize());

        // 도서 ID는 현재 시간 기반으로 생성
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddhhmmss");
        String strDate = formatter.format(date);

        // ================== 상단 입력 폼 ==================
        JPanel idPanel = new JPanel();
        idPanel.setBounds(100, 0, 700, 50);
        JLabel idLabel = new JLabel("도서ID : ");
        idLabel.setFont(ft);
        JLabel idTextField = new JLabel();
        idTextField.setFont(ft);
        idTextField.setPreferredSize(new Dimension(290, 50));
        idTextField.setText("ISBN" + strDate);
        idPanel.add(idLabel);
        idPanel.add(idTextField);
        add(idPanel);

        JPanel namePanel = new JPanel();
        namePanel.setBounds(100, 50, 700, 50);
        JLabel nameLabel = new JLabel("도서명 : ");
        nameLabel.setFont(ft);
        JTextField nameTextField = new JTextField(20);
        nameTextField.setFont(ft);
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);
        add(namePanel);

        JPanel pricePanel = new JPanel();
        pricePanel.setBounds(100, 100, 700, 50);
        JLabel priceLabel = new JLabel("가   격 : ");
        priceLabel.setFont(ft);
        JTextField priceTextField = new JTextField(20);
        priceTextField.setFont(ft);
        pricePanel.add(priceLabel);
        pricePanel.add(priceTextField);
        add(pricePanel);

        JPanel authorPanel = new JPanel();
        authorPanel.setBounds(100, 150, 700, 50);
        JLabel authorLabel = new JLabel("저   자 : ");
        authorLabel.setFont(ft);
        JTextField authorTextField = new JTextField(20);
        authorTextField.setFont(ft);
        authorPanel.add(authorLabel);
        authorPanel.add(authorTextField);
        add(authorPanel);

        JPanel descPanel = new JPanel();
        descPanel.setBounds(100, 200, 700, 50);
        JLabel descLabel = new JLabel("설   명 : ");
        descLabel.setFont(ft);
        JTextField descTextField = new JTextField(20);
        descTextField.setFont(ft);
        descPanel.add(descLabel);
        descPanel.add(descTextField);
        add(descPanel);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setBounds(100, 250, 700, 50);
        JLabel categoryLabel = new JLabel("분   야 : ");
        categoryLabel.setFont(ft);
        JTextField categoryTextField = new JTextField(20);
        categoryTextField.setFont(ft);
        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryTextField);
        add(categoryPanel);

        JPanel datePanel = new JPanel();
        datePanel.setBounds(100, 300, 700, 50);
        JLabel dateLabel = new JLabel("출판일 : ");
        dateLabel.setFont(ft);
        JTextField dateTextField = new JTextField(20);
        dateTextField.setFont(ft);
        datePanel.add(dateLabel);
        datePanel.add(dateTextField);
        add(datePanel);

        // ================== 버튼들 (추가 / 수정 / 삭제 / 새로고침) ==================
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(100, 350, 700, 50);
        add(buttonPanel);

        // 추가
        JLabel addLabel = new JLabel("추가");
        addLabel.setFont(ft);
        JButton addButton = new JButton();
        addButton.add(addLabel);
        buttonPanel.add(addButton);

        // 수정
        JLabel updateLabel = new JLabel("수정");
        updateLabel.setFont(ft);
        JButton updateButton = new JButton();
        updateButton.add(updateLabel);
        buttonPanel.add(updateButton);

        // 삭제
        JLabel deleteLabel = new JLabel("삭제");
        deleteLabel.setFont(ft);
        JButton deleteButton = new JButton();
        deleteButton.add(deleteLabel);
        buttonPanel.add(deleteButton);

        // 새로고침(목록 다시 불러오기)
        JLabel refreshLabel = new JLabel("새로고침");
        refreshLabel.setFont(ft);
        JButton refreshButton = new JButton();
        refreshButton.add(refreshLabel);
        buttonPanel.add(refreshButton);

        // 취소(입력 내용 초기화)
        JLabel clearLabel = new JLabel("입력 초기화");
        clearLabel.setFont(ft);
        JButton clearButton = new JButton();
        clearButton.add(clearLabel);
        buttonPanel.add(clearButton);

        // ================== 하단 도서 목록 테이블 ==================
        JPanel tablePanel = new JPanel();
        tablePanel.setBounds(50, 410, 900, 260);
        tablePanel.setLayout(new BorderLayout());
        add(tablePanel);

        String[] columns = { "도서ID", "도서명", "가격", "저자", "설명", "분야", "출판일" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 직접 테이블에서 수정 못하게
            }
        };
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // 처음에 전체 도서 목록 로딩
        loadBookTable();

        // ================== 버튼 동작 구현 ==================

        //추가 버튼
        addButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                String bookId   = idTextField.getText().trim();
                String name     = nameTextField.getText().trim();
                String priceStr = priceTextField.getText().trim();
                String author   = authorTextField.getText().trim();
                String desc     = descTextField.getText().trim();
                String category = categoryTextField.getText().trim();
                String date     = dateTextField.getText().trim();

                if (bookId.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
                    JOptionPane.showMessageDialog(addButton,
                            "도서ID, 도서명, 가격은 반드시 입력해야 합니다.");
                    return;
                }

                int price;
                try {
                    price = Integer.parseInt(priceStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addButton, "가격은 숫자로 입력해야 합니다.");
                    return;
                }

                Book newBook = new Book(
                        bookId,
                        name,
                        price,
                        author,
                        desc,
                        category,
                        date
                );

                BookDAO dao = new BookDAO();
                int result = dao.insert(newBook);

                if (result > 0) {
                    JOptionPane.showMessageDialog(addButton, "새 도서 정보가 DB에 저장되었습니다.");
                    // ID 새로 생성 + 입력 내용 비우기
                    Date now = new Date();
                    SimpleDateFormat f = new SimpleDateFormat("yyMMddhhmmss");
                    String newId = "ISBN" + f.format(now);
                    idTextField.setText(newId);
                    nameTextField.setText("");
                    priceTextField.setText("");
                    authorTextField.setText("");
                    descTextField.setText("");
                    categoryTextField.setText("");
                    dateTextField.setText("");

                    loadBookTable(); // 테이블 갱신
                } else {
                    JOptionPane.showMessageDialog(addButton,
                            "도서 저장에 실패했습니다. (이미 존재하는 ID일 수 있습니다.)");
                }
            }
        });

        //수정 버튼
        updateButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookId   = idTextField.getText().trim();
                String name     = nameTextField.getText().trim();
                String priceStr = priceTextField.getText().trim();
                String author   = authorTextField.getText().trim();
                String desc     = descTextField.getText().trim();
                String category = categoryTextField.getText().trim();
                String date     = dateTextField.getText().trim();

                if (bookId.isEmpty()) {
                    JOptionPane.showMessageDialog(updateButton,
                            "수정할 도서ID가 없습니다. 테이블에서 도서를 선택하세요.");
                    return;
                }
                if (name.isEmpty() || priceStr.isEmpty()) {
                    JOptionPane.showMessageDialog(updateButton,
                            "도서명과 가격은 반드시 입력해야 합니다.");
                    return;
                }

                int price;
                try {
                    price = Integer.parseInt(priceStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(updateButton, "가격은 숫자로 입력해야 합니다.");
                    return;
                }

                Book book = new Book(
                        bookId,
                        name,
                        price,
                        author,
                        desc,
                        category,
                        date
                );

                BookDAO dao = new BookDAO();
                int result = dao.update(book);
                if (result > 0) {
                    JOptionPane.showMessageDialog(updateButton, "도서 정보가 수정되었습니다.");
                    loadBookTable();
                } else {
                    JOptionPane.showMessageDialog(updateButton,
                            "도서 수정에 실패했습니다. (ID를 확인하세요)");
                }
            }
        });

        //삭제 버튼
        deleteButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bookId = idTextField.getText().trim();
                if (bookId.isEmpty()) {
                    JOptionPane.showMessageDialog(deleteButton,
                            "삭제할 도서ID가 없습니다. 테이블에서 도서를 선택하세요.");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(deleteButton,
                        "도서ID: " + bookId + " 를 삭제하시겠습니까?",
                        "도서 삭제",
                        JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION) return;

                BookDAO dao = new BookDAO();
                int result = dao.delete(bookId);
                if (result > 0) {
                    JOptionPane.showMessageDialog(deleteButton, "도서가 삭제되었습니다.");
                    // 입력폼 비우기
                    idTextField.setText("");
                    nameTextField.setText("");
                    priceTextField.setText("");
                    authorTextField.setText("");
                    descTextField.setText("");
                    categoryTextField.setText("");
                    dateTextField.setText("");
                    loadBookTable();
                } else {
                    JOptionPane.showMessageDialog(deleteButton,
                            "도서 삭제에 실패했습니다. (ID를 확인하세요)");
                }
            }
        });

        //새로고침 버튼
        refreshButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBookTable();
            }
        });

        //입력 초기화 버튼
        clearButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameTextField.setText("");
                priceTextField.setText("");
                authorTextField.setText("");
                descTextField.setText("");
                categoryTextField.setText("");
                dateTextField.setText("");
            }
        });

        //테이블에서 행 선택 시 → 위 입력폼에 값 채우기
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bookTable.getSelectedRow();
                if (row < 0) return;

                String bookId   = (String) tableModel.getValueAt(row, 0);
                String name     = (String) tableModel.getValueAt(row, 1);
                int price       = (int) tableModel.getValueAt(row, 2);
                String author   = (String) tableModel.getValueAt(row, 3);
                String desc     = (String) tableModel.getValueAt(row, 4);
                String category = (String) tableModel.getValueAt(row, 5);
                String date     = (String) tableModel.getValueAt(row, 6);

                idTextField.setText(bookId);
                nameTextField.setText(name);
                priceTextField.setText(String.valueOf(price));
                authorTextField.setText(author);
                descTextField.setText(desc);
                categoryTextField.setText(category);
                dateTextField.setText(date);
            }
        });
    }

    // ================== 도서 목록을 테이블에 채우는 메서드 ==================
    private void loadBookTable() {
        BookDAO dao = new BookDAO();
        ArrayList<Book> list = dao.findAll();  // 정렬은 필요하면 파라미터로 조정

        tableModel.setRowCount(0); // 기존 행 제거

        for (Book b : list) {
            Object[] row = new Object[] {
                    b.getBookId(),
                    b.getName(),
                    b.getUnitPrice(),
                    b.getAuthor(),
                    b.getDescription(),
                    b.getCategory(),
                    b.getReleaseDate()
            };
            tableModel.addRow(row);
        }
    }
}
