package com.market.dao;
/**
 * cart_items 테이블용 DAO.
 *
 * session_id 규칙:
 *   - "이름_전화번호숫자" 형태
 *   - 예: 김서현, 010-1234-5678 -> "홍길동_01012345678"
 *
 * 실제 session_id 생성/보관은 UserInIt/GuestWindow 쪽(기능 보완 담당)에서 처리.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

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
    
 // 세션 ID로 cart_items + book JOIN해서 장바구니 목록 조회
    public List<CartItemRow> findBySessionId(String sessionId) {
        String sql =
            "SELECT ci.book_id, ci.quantity, b.name, b.unit_price " +
            "FROM cart_items ci " +
            "JOIN book b ON ci.book_id = b.book_id " +
            "WHERE ci.session_id = ?";

        List<CartItemRow> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sessionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String bookId   = rs.getString("book_id");
                    int quantity    = rs.getInt("quantity");
                    String name     = rs.getString("name");
                    int unitPrice   = rs.getInt("unit_price");

                    CartItemRow row = new CartItemRow(bookId, name, unitPrice, quantity);
                    list.add(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
