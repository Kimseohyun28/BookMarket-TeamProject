package com.market.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * 이름 + 전화번호로 users 테이블에서 유저를 찾고,
     * 없으면 새로 INSERT 한 뒤 user_id를 리턴하는 메서드.
     */
    public int ensureUser(String name, String mobile) {
        // 1) 이미 존재하는지 확인
        String selectSql = "SELECT user_id FROM users WHERE mobile = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSql)) {

            pstmt.setString(1, mobile);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");   // 이미 존재하면 그 id 사용
                }
            }

            // 2) 없으면 새로 INSERT
            String insertSql = "INSERT INTO users (name, mobile) VALUES (?, ?)";
            try (PreparedStatement pstmt2 = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt2.setString(1, name);
                pstmt2.setString(2, mobile);
                int affected = pstmt2.executeUpdate();

                if (affected > 0) {
                    try (ResultSet rs2 = pstmt2.getGeneratedKeys()) {
                        if (rs2.next()) {
                            return rs2.getInt(1);  // 새 user_id
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;  // 실패
    }
}
