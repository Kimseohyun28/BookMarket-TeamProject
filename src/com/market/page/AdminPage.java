package com.market.page;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import com.market.bookitem.Book;
import com.market.dao.BookDAO;

public class AdminPage extends JPanel {

	public AdminPage(JPanel panel) {

		Font ft;
		ft = new Font("함초롬돋움", Font.BOLD, 15);

		setLayout(null);

		Rectangle rect = panel.getBounds();
		System.out.println(rect);
		setPreferredSize(rect.getSize());

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddhhmmss");
		String strDate = formatter.format(date);

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

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(100, 350, 700, 50);
		add(buttonPanel);
		JLabel okLabel = new JLabel("추가");
		okLabel.setFont(ft);
		JButton okButton = new JButton();
		okButton.add(okLabel);
		buttonPanel.add(okButton);

		okButton.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		        String bookId   = idTextField.getText().trim();
		        String name     = nameTextField.getText().trim();
		        String priceStr = priceTextField.getText().trim();
		        String author   = authorTextField.getText().trim();
		        String desc     = descTextField.getText().trim();
		        String category = categoryTextField.getText().trim();
		        String date     = dateTextField.getText().trim();

		        // 간단한 입력값 체크
		        if (bookId.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
		            JOptionPane.showMessageDialog(okButton, "도서ID, 도서명, 가격은 반드시 입력해야 합니다.");
		            return;
		        }

		        int price = 0;
		        try {
		            price = Integer.parseInt(priceStr);
		        } catch (NumberFormatException ex) {
		            JOptionPane.showMessageDialog(okButton, "가격은 숫자로 입력해야 합니다.");
		            return;
		        }

		        // Book 객체 만들기
		        Book newBook = new Book(
		                bookId,
		                name,
		                price,
		                author,
		                desc,
		                category,
		                date
		        );

		        // DB에 INSERT
		        BookDAO dao = new BookDAO();
		        int result = dao.insert(newBook);

		        if (result > 0) {
		            JOptionPane.showMessageDialog(okButton, "새 도서 정보가 DB에 저장되었습니다.");

		            // 다음 책 입력을 위해 필드 초기화 + 새로운 ID 생성
		            java.util.Date now = new java.util.Date();
		            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyMMddhhmmss");
		            String strDate = formatter.format(now);

		            idTextField.setText("ISBN" + strDate);
		            nameTextField.setText("");
		            priceTextField.setText("");
		            authorTextField.setText("");
		            descTextField.setText("");
		            categoryTextField.setText("");
		            dateTextField.setText("");

		        } else {
		            JOptionPane.showMessageDialog(okButton, "도서 저장에 실패했습니다. (이미 존재하는 ID일 수 있습니다.)");
		        }
		    }
		});

		JLabel noLabel = new JLabel("취소");
		noLabel.setFont(ft);
		JButton noButton = new JButton();
		noButton.add(noLabel);
		buttonPanel.add(noButton);

		noButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {

				nameTextField.setText("");
				priceTextField.setText("");
				authorTextField.setText("");
				descTextField.setText("");
				categoryTextField.setText("");
				dateTextField.setText("");
			}
		});
	}

	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setBounds(0, 0, 1000, 750);
		frame.setLayout(null);

		JPanel mPagePanel = new JPanel();
		mPagePanel.setBounds(0, 150, 1000, 750);

		frame.add(mPagePanel);
		mPagePanel.add("주문하기", new AdminPage(mPagePanel));
		frame.setVisible(true);
	}
}