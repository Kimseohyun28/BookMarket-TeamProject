package com.market.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CartItemDAO {

    // 장바구니에 도서 1개 추가(또는 수량 증가)
    public int upsertCartItem(String sessionId, String bookId, int quantityDelta) {

        String sql = "INSERT INTO cart_items (session_id, book_id, quantity) "
                   + "VALUES (?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sessionId);
            pstmt.setString(2, bookId);
            pstmt.setInt(3, quantityDelta);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 한 책만 삭제
    public int removeItem(String sessionId, String bookId) {
        String sql = "DELETE FROM cart_items WHERE session_id = ? AND book_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sessionId);
            pstmt.setString(2, bookId);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 세션 전체 장바구니 비우기
    public int clearCart(String sessionId) {
        String sql = "DELETE FROM cart_items WHERE session_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sessionId);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
