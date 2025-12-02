package com.market.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.market.page.GuestInfoPage;
import com.market.cart.Cart;
import com.market.bookitem.BookInIt;
import com.market.member.UserInIt;
import com.market.dao.CartItemDAO;
import com.market.dao.CartItemRow;
import com.market.dao.BookDAO;
import com.market.bookitem.Book;

import com.market.page.CartAddItemPage;
import com.market.page.CartItemListPage;
import com.market.page.CartShippingPage;
import com.market.page.AdminLoginDialog;
import com.market.page.AdminPage;
import com.market.page.OrderHistoryPage;

public class MainWindow extends JFrame {

    static Cart mCart;
    static JPanel mMenuPanel, mPagePanel;

    private JPanel titleBar;
    private Point initialClick;

    public MainWindow(String title, int x, int y, int width, int height) {

        initContainer(title, x, y, width, height);
        initMenu();

        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon("./images/shop.png").getImage());
    }

    private void initContainer(String title, int x, int y, int width, int height) {
        setTitle(title);
        setBounds(x, y, width, height);
        setLayout(null);

        // 커스텀 UI 추가 부분
        setUndecorated(true);
        applyRoundedCorners(this, 20);
        initCustomTitleBar(width);
        add(titleBar);
        // 끝

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 1000) / 2, (screenSize.height - 750) / 2);

        mMenuPanel = new JPanel();
        mMenuPanel.setBounds(0, 40, width, 130);  // 타이틀바 때문에 Y=40으로 내림
        menuIntroduction();
        add(mMenuPanel);

        mPagePanel = new JPanel();
        mPagePanel.setBounds(0, 170, width, height);
        add(mPagePanel);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setVisible(false);
                new GuestWindow("고객 정보 입력", 0, 0, 1000, 750);
            }
        });
    }

    private void menuIntroduction() {
        mCart = new Cart();
        restoreCartFromDB();

        Font ft = new Font("함초롬돋움", Font.BOLD, 15);

        JButton bt1 = new JButton("고객 정보 확인하기", new ImageIcon("./images/1.png"));
        bt1.setFont(ft);
        mMenuPanel.add(bt1);

        bt1.addActionListener(e -> {
            mPagePanel.removeAll();
            mPagePanel.add("고객 정보 확인", new GuestInfoPage(mPagePanel));
            mPagePanel.revalidate();
            mPagePanel.repaint();
        });

        JButton bt2 = new JButton("장바구니 상품목록보기", new ImageIcon("./images/2.png"));
        bt2.setFont(ft);
        mMenuPanel.add(bt2);

        bt2.addActionListener(e -> {
            if (mCart.mCartCount == 0)
                JOptionPane.showMessageDialog(bt2, "장바구니에 항목이 없습니다", "오류", JOptionPane.ERROR_MESSAGE);
            else {
                mPagePanel.removeAll();
                mPagePanel.add("장바구니 상품 목록 보기", new CartItemListPage(mPagePanel, mCart));
                mPagePanel.revalidate();
                mPagePanel.repaint();
            }
        });

        JButton bt3 = new JButton("장바구니 비우기", new ImageIcon("./images/3.png"));
        bt3.setFont(ft);
        mMenuPanel.add(bt3);

        bt3.addActionListener(e -> {
            if (mCart.mCartCount == 0)
                JOptionPane.showMessageDialog(bt3, "장바구니가 비어 있습니다");
            else {
                mPagePanel.removeAll();
                menuCartClear(bt3);
                mPagePanel.add("장바구니 비우기", new CartItemListPage(mPagePanel, mCart));
                mPagePanel.revalidate();
                mPagePanel.repaint();
            }
        });

        JButton bt4 = new JButton("장바구니 항목추가", new ImageIcon("./images/4.png"));
        bt4.setFont(ft);
        mMenuPanel.add(bt4);

        bt4.addActionListener(e -> {
            mPagePanel.removeAll();
            BookInIt.init();
            mPagePanel.add("항목 추가", new CartAddItemPage(mPagePanel, mCart));
            mPagePanel.revalidate();
            mPagePanel.repaint();
        });

        JButton bt6 = new JButton("장바구니 항목삭제", new ImageIcon("./images/5.png"));
        bt6.setFont(ft);
        mMenuPanel.add(bt6);

        bt6.addActionListener(e -> {
            if (mCart.mCartCount == 0)
                JOptionPane.showMessageDialog(bt6, "장바구니가 비어 있습니다");
            else {
                mPagePanel.removeAll();
                CartItemListPage cartList = new CartItemListPage(mPagePanel, mCart);
                if (mCart.mCartCount == 0)
                    JOptionPane.showMessageDialog(bt6, "장바구니가 비었습니다");
                else if (cartList.mSelectRow == -1)
                    JOptionPane.showMessageDialog(bt6, "삭제할 항목을 선택하세요");
                else {
                    mCart.removeCart(cartList.mSelectRow);
                    cartList.mSelectRow = -1;
                }
            }
            mPagePanel.add("항목 삭제", new CartItemListPage(mPagePanel, mCart));
            mPagePanel.revalidate();
            mPagePanel.repaint();
        });

        JButton bt7 = new JButton("주문하기", new ImageIcon("./images/6.png"));
        bt7.setFont(ft);
        mMenuPanel.add(bt7);

        bt7.addActionListener(e -> {
            if (mCart.mCartCount == 0)
                JOptionPane.showMessageDialog(bt7, "장바구니에 항목이 없습니다");
            else {
                mPagePanel.removeAll();
                mPagePanel.add("주문 배송지", new CartShippingPage(mPagePanel, mCart));
                mPagePanel.revalidate();
                mPagePanel.repaint();
            }
        });

        JButton bt8 = new JButton("종료", new ImageIcon("./images/7.png"));
        bt8.setFont(ft);
        mMenuPanel.add(bt8);

        bt8.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(bt8, "종료하시겠습니까? ") == 0)
                System.exit(0);
        });

        JButton bt9 = new JButton("관리자", new ImageIcon("./images/8.png"));
        bt9.setFont(ft);
        mMenuPanel.add(bt9);

        bt9.addActionListener(e -> {
            AdminLoginDialog adminDialog;
            JFrame frame = new JFrame();
            adminDialog = new AdminLoginDialog(frame, "관리자 로그인");
            adminDialog.setVisible(true);

            if (adminDialog.isLogin) {
                mPagePanel.removeAll();
                mPagePanel.add("관리자", new AdminPage(mPagePanel));
                mPagePanel.revalidate();
                mPagePanel.repaint();
            }
        });

        JButton bt10 = new JButton("주문 내역", new ImageIcon("./images/9.png"));
        bt10.setFont(ft);
        mMenuPanel.add(bt10);

        bt10.addActionListener(e -> {
            mPagePanel.removeAll();
            mPagePanel.add("주문 내역 보기", new OrderHistoryPage(mPagePanel));
            mPagePanel.revalidate();
            mPagePanel.repaint();
        });
    }

    private void initMenu() {
        Font ft = new Font("함초롬돋움", Font.BOLD, 15);

        JMenuBar menuBar = new JMenuBar();

        JMenu menu01 = new JMenu("고객");
        menu01.setFont(ft);
        JMenuItem item01 = new JMenuItem("고객 정보");
        JMenuItem item11 = new JMenuItem("종료");
        menu01.add(item01);
        menu01.add(item11);
        menuBar.add(menu01);

        JMenu menu02 = new JMenu("상품");
        menu02.setFont(ft);
        JMenuItem item02 = new JMenuItem("상품 목록");
        menu02.add(item02);
        menuBar.add(menu02);

        JMenu menu03 = new JMenu("장바구니");
        menu03.setFont(ft);
        JMenuItem item03 = new JMenuItem("항목 추가");
        JMenuItem item04 = new JMenuItem("항목 수량 줄이기");
        JMenuItem item05 = new JMenuItem("항목 삭제");
        JMenuItem item06 = new JMenuItem("비우기");
        menu03.add(item03);
        menu03.add(item04);
        menu03.add(item05);
        menu03.add(item06);
        menuBar.add(menu03);

        JMenu menu04 = new JMenu("주문");
        menu04.setFont(ft);
        JMenuItem item07 = new JMenuItem("영수증 표시");
        JMenuItem item08 = new JMenuItem("주문 내역 보기");
        menu04.add(item07);
        menu04.add(item08);

        setJMenuBar(menuBar);

        item01.addActionListener(e -> {
            mPagePanel.removeAll();
            mPagePanel.add("고객 정보 확인", new GuestInfoPage(mPagePanel));
            mPagePanel.revalidate();
        });

        item02.addActionListener(e -> {
            mPagePanel.removeAll();
            BookInIt.init();
            mPagePanel.add("항목 추가", new CartAddItemPage(mPagePanel, mCart));
            mPagePanel.revalidate();
        });

        item11.addActionListener(e -> {
            mPagePanel.removeAll();
            setVisible(false);
            new GuestWindow("고객 정보 입력", 0, 0, 1000, 750);
            mPagePanel.revalidate();
        });

        item08.addActionListener(e -> {
            mPagePanel.removeAll();
            mPagePanel.add("주문 내역 보기", new OrderHistoryPage(mPagePanel));
            mPagePanel.revalidate();
            mPagePanel.repaint();
        });
    }

    private void menuCartClear(JButton button) {
        if (mCart.mCartCount == 0)
            JOptionPane.showMessageDialog(button, "장바구니에 항목이 없습니다");
        else {
            int select = JOptionPane.showConfirmDialog(button, "모든 항목을 삭제하시겠습니까? ");
            if (select == 0) {
                mCart.deleteBook();
                JOptionPane.showMessageDialog(button, "모든 항목을 삭제했습니다");
            }
        }
    }

    private void restoreCartFromDB() {
        String sessionId = UserInIt.getSessionId();
        if (sessionId == null || sessionId.isEmpty())
            return;

        CartItemDAO cartDao = new CartItemDAO();
        BookDAO bookDao = new BookDAO();

        java.util.List<CartItemRow> rows = cartDao.findBySessionId(sessionId);

        for (CartItemRow row : rows) {
            Book book = bookDao.findById(row.getBookId());
            if (book == null) continue;

            for (int i = 0; i < row.getQuantity(); i++) {
                mCart.insertBook(book);
            }
        }
    }

    //UI 커스텀 타이틀바
    private void initCustomTitleBar(int width) {

        titleBar = new JPanel();
        titleBar.setLayout(null);
        titleBar.setBackground(new Color(245, 245, 245));
        titleBar.setBounds(0, 0, width, 40);

        // ===== 로고 이미지 =====
        ImageIcon logo = new ImageIcon("./images/book.png");  // 원하는 이미지 파일
        logo.setImage(logo.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH));

        JLabel logoLabel = new JLabel(logo);
        logoLabel.setBounds(10, 6, 30, 30);
        titleBar.add(logoLabel);

        // ===== 가운데 텍스트 =====
        JLabel titleText = new JLabel("WSU BOOKSTORE", SwingConstants.CENTER);
        titleText.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        titleText.setBounds(0, 0, width, 40);  // 전체 너비 가운데
        titleBar.add(titleText);

        // ===== 종료 버튼 =====
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

        // ===== 창 드래그 이동 =====
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });

        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - initialClick.x, y - initialClick.y);
            }
        });
    }

    //둥근 모서리 적용 함수
    private void applyRoundedCorners(JFrame frame, int radius) {
        frame.setShape(new java.awt.geom.RoundRectangle2D.Float(
                0, 0, frame.getWidth(), frame.getHeight(), radius, radius));
    }
}