package com.market.main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import com.market.member.UserInIt;

public class GuestWindow extends JFrame {

    public GuestWindow(String title, int x, int y, int width, int height) {
        initContainer(title, x, y, width, height);
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon("./images/shop.png").getImage());
    }

    private void initContainer(String title, int x, int y, int width, int height) {
        setUndecorated(true);
        setBounds(x, y, width, height);
        setLayout(null);

        // 둥근 모서리
        setShape(new RoundRectangle2D.Double(0, 0, width, height, 40, 40));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
            }
        });

        // ============================
        // 커스텀 타이틀바
        // ============================
        JPanel titleBar = new JPanel();
        titleBar.setLayout(null);
        titleBar.setBackground(Color.WHITE);
        titleBar.setBounds(0, 0, width, 40);

        // 로고
        ImageIcon logo = new ImageIcon("./images/book.png");
        logo.setImage(logo.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH));
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setBounds(10, 5, 30, 30);
        titleBar.add(logoLabel);

        // 타이틀
        JLabel titleText = new JLabel("WSU BOOKSTORE", SwingConstants.CENTER);
        titleText.setFont(new Font("함초롬돋움", Font.BOLD, 18));
        titleText.setBounds(0, 0, width, 40);
        titleBar.add(titleText);

        // 아이콘 버튼 세팅
        String[] iconFiles = {"./images/ㅇ.png", "./images/ㅅ.png", "./images/ㄷ.png"};
        JButton[] btns = new JButton[3];
        int[] xPos = {width - 120, width - 80, width - 40};

        for (int i = 0; i < 3; i++) {
            ImageIcon ic = new ImageIcon(iconFiles[i]);
            ic.setImage(ic.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));

            btns[i] = new JButton(ic);
            btns[i].setBounds(xPos[i], 5, 30, 30);
            btns[i].setContentAreaFilled(false);
            btns[i].setBorderPainted(false);
            btns[i].setFocusPainted(false);

            titleBar.add(btns[i]);
        }

        btns[0].addActionListener(e -> setState(JFrame.ICONIFIED));  // 최소화
        btns[1].addActionListener(e -> setExtendedState(getExtendedState() ^ JFrame.MAXIMIZED_BOTH)); // 최대화
        btns[2].addActionListener(e -> dispose()); // 종료

        // 드래그 이동
        final Point mousePoint = new Point();
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mousePoint.x = e.getX();
                mousePoint.y = e.getY();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - mousePoint.x, e.getYOnScreen() - mousePoint.y);
            }
        });

        add(titleBar);

        // ============================
        // 타이틀바 아래 구분선
        // ============================
        JPanel linePanel = new JPanel();
        linePanel.setBackground(new Color(220,220,220));
        linePanel.setBounds(0, 40, width, 2);
        add(linePanel);

        // ============================
        // 전체 배경 (흰색 유지)
        // ============================
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBounds(0, 42, width, height - 42);
        contentPanel.setBackground(Color.WHITE);
        add(contentPanel);

        Font ft = new Font("함초롬돋움", Font.BOLD, 15);

        // 로고
        JPanel userPanel = new JPanel();
        userPanel.setBounds(0, 50, width, 200);
        userPanel.setOpaque(false);

        ImageIcon imageIcon = new ImageIcon("./images/wsu.png");
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH));
        JLabel userLabel = new JLabel(imageIcon);
        userPanel.add(userLabel);
        contentPanel.add(userPanel);

        // ============================
        // 고객정보 회색 카드 박스
        // ============================
        JPanel infoCard = new JPanel(null);
        infoCard.setBounds(width/2 - 200, 260, 400, 260);
        infoCard.setBackground(new Color(245,245,245)); // 연회색
        infoCard.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 2, true)); 
        contentPanel.add(infoCard);

        // 제목
        JLabel titleLabel = new JLabel("-- 고객 정보를 입력하세요 --", SwingConstants.CENTER);
        titleLabel.setFont(ft);
        titleLabel.setBounds(0, 20, 400, 30);
        infoCard.add(titleLabel);

        // 이름
        JLabel nameLabel = new JLabel("이 름 : ");
        nameLabel.setFont(ft);
        nameLabel.setBounds(60, 80, 80, 30);
        infoCard.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setFont(ft);
        nameField.setBounds(150, 80, 150, 30);
        infoCard.add(nameField);

        // 연락처
        JLabel phoneLabel = new JLabel("연락처 : ");
        phoneLabel.setFont(ft);
        phoneLabel.setBounds(60, 130, 80, 30);
        infoCard.add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setFont(ft);
        phoneField.setBounds(150, 130, 150, 30);
        infoCard.add(phoneField);

        // 로그인 버튼
        JButton enterButton = new JButton("Log In");
        enterButton.setFont(ft);
        enterButton.setForeground(Color.WHITE);
        enterButton.setBackground(new Color(0, 102, 255));
        enterButton.setBounds(140, 180, 120, 40);
        enterButton.setFocusPainted(false);
        infoCard.add(enterButton);

        // 동작
        enterButton.addActionListener(e -> {
            if (nameField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                    enterButton, "고객 정보를 입력하세요", "고객 정보", JOptionPane.ERROR_MESSAGE
                );
            } else {
                UserInIt.init(nameField.getText(), phoneField.getText());
                dispose();
                new MainWindow("온라인 서점", 0, 0, 1000, 750);
            }
        });
    }
}