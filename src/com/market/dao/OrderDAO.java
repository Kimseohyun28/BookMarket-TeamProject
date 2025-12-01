package com.market.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

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
    
    public List<OrderSummaryRow> findOrdersByUser(int userId) {
        String sql =
            "SELECT o.order_id, o.order_date, " +
            "       o.receiver_name, o.receiver_phone, o.receiver_address, " +
            "       o.total_price, " +
            "       COUNT(oi.order_item_id) AS item_count " +
            "FROM orders o " +
            "LEFT JOIN order_items oi ON o.order_id = oi.order_id " +
            "WHERE o.user_id = ? " +
            "GROUP BY o.order_id, o.order_date, " +
            "         o.receiver_name, o.receiver_phone, o.receiver_address, o.total_price " +
            "ORDER BY o.order_date DESC";

        List<OrderSummaryRow> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    long orderId = rs.getLong("order_id");
                    Timestamp orderDate = rs.getTimestamp("order_date");
                    String receiverName = rs.getString("receiver_name");
                    String receiverPhone = rs.getString("receiver_phone");
                    String receiverAddress = rs.getString("receiver_address");
                    int totalPrice = rs.getInt("total_price");
                    int itemCount = rs.getInt("item_count");

                    OrderSummaryRow row = new OrderSummaryRow(
                        orderId, orderDate,
                        receiverName, receiverPhone, receiverAddress,
                        totalPrice, itemCount
                    );
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public List<OrderItemRow> findOrderItems(long orderId) {
        String sql =
            "SELECT oi.book_id, b.name AS book_name, " +
            "       oi.quantity, oi.unit_price, oi.total_price " +
            "FROM order_items oi " +
            "JOIN book b ON oi.book_id = b.book_id " +
            "WHERE oi.order_id = ?";

        List<OrderItemRow> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, orderId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String bookId   = rs.getString("book_id");
                    String bookName = rs.getString("book_name");
                    int quantity    = rs.getInt("quantity");
                    int unitPrice   = rs.getInt("unit_price");
                    int totalPrice  = rs.getInt("total_price");

                    OrderItemRow row = new OrderItemRow(
                        bookId, bookName,
                        quantity, unitPrice, totalPrice
                    );
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}