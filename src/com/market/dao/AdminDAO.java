package com.market.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.market.member.Admin;

public class AdminDAO {

    // admin_id + password로 로그인 체크
    public Admin login(String adminId, String password) {

        String sql = "SELECT admin_id, password, name, mobile "
                   + "FROM admins WHERE admin_id = ? AND password = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, adminId);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name   = rs.getString("name");
                    String mobile = rs.getString("mobile");   // 010 포함된 문자열

                    // 전화번호를 그대로 String으로 사용
                    Admin admin = new Admin(name, mobile);
                    admin.setId(adminId);
                    admin.setPassword(password);

                    return admin;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;  // 로그인 실패
    }
}

