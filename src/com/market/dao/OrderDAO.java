package com.market.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAO {

    /**
     * 주문 1건 저장하고 생성된 order_id를 리턴
     */
    public long insertOrder(int userId,
                            String receiverName,
                            String receiverPhone,
                            String address,
                            int totalPrice) {

        String sql = "INSERT INTO orders "
                   + "(user_id, receiver_name, receiver_phone, receiver_address, total_price) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, receiverName);
            pstmt.setString(3, receiverPhone);
            pstmt.setString(4, address);
            pstmt.setInt(5, totalPrice);

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);   // 생성된 order_id
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 주문 상세 1건 저장 (order_items)
     */
    public int insertOrderItem(long orderId,
                               String bookId,
                               int quantity,
                               int unitPrice,
                               int totalPrice) {

        String sql = "INSERT INTO order_items "
                   + "(order_id, book_id, quantity, unit_price, total_price) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, orderId);
            pstmt.setString(2, bookId);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, unitPrice);
            pstmt.setInt(5, totalPrice);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}