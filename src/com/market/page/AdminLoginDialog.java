package com.market.page;
import com.market.dao.AdminDAO;
import com.market.member.Admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class AdminLoginDialog extends JDialog {

	JTextField pwField, idField;
	public boolean isLogin = false;

	public AdminLoginDialog(JFrame frame, String str) {
		super(frame, "관리자 로그인", true);

		Font ft;
		ft = new Font("함초롬돋움", Font.BOLD, 15);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - 400) / 2, (screenSize.height - 300) / 2);
		setSize(400, 300);
		setLayout(null);

		JPanel titlePanel = new JPanel();
		titlePanel.setBounds(0, 20, 400, 50);
		add(titlePanel);
		JLabel titleLabel = new JLabel("관리자 로그인");
		titleLabel.setFont(new Font("함초롬돋움", Font.BOLD, 20));
		titlePanel.add(titleLabel);

		JPanel idPanel = new JPanel();
		idPanel.setBounds(0, 70, 400, 50);
		add(idPanel);
		JLabel idLabel = new JLabel("아 이 디 : ");
		idLabel.setFont(ft);
		idField = new JTextField(10);
		idField.setFont(ft);
		idPanel.add(idLabel);
		idPanel.add(idField);

		JPanel pwPanel = new JPanel();
		pwPanel.setBounds(0, 120, 400, 50);
		add(pwPanel);
		JLabel pwLabel = new JLabel("비밀번호 : ");
		pwLabel.setFont(ft);
		pwField = new JTextField(10);
		pwField.setFont(ft);
		pwPanel.add(pwLabel);
		pwPanel.add(pwField);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(0, 170, 400, 50);
		add(buttonPanel);
		JLabel okLabel = new JLabel("확인");
		okLabel.setFont(ft);
		JButton okButton = new JButton();
		okButton.add(okLabel);
		buttonPanel.add(okButton);

		JLabel cancelLabel = new JLabel("취소");
		cancelLabel.setFont(ft);
		JButton cancelBtn = new JButton();
		cancelBtn.add(cancelLabel);
		buttonPanel.add(cancelBtn);

		okButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {

		        String adminId = idField.getText().trim();
		        String adminPw = pwField.getText().trim();

		        // 입력 안 했을 때 체크
		        if (adminId.isEmpty() || adminPw.isEmpty()) {
		            JOptionPane.showMessageDialog(okButton, "아이디와 비밀번호를 모두 입력하세요");
		            return;
		        }

		        // DB 기반 로그인 체크
		        AdminDAO adminDao = new AdminDAO();
		        Admin admin = adminDao.login(adminId, adminPw);

		        if (admin != null) {
		            // 로그인 성공
		            isLogin = true;
		            // 원하면 여기서 admin 정보 출력도 가능
		            // System.out.println("관리자 로그인: " + admin.getName());
		            dispose();
		        } else {
		            // 로그인 실패
		            JOptionPane.showMessageDialog(okButton, "관리자 정보가 일치하지 않습니다");
		        }
		    }
		});

		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isLogin = false;
				dispose();
			}
		});
	}
}