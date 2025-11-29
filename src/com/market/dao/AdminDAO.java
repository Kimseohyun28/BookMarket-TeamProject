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
                    String name = rs.getString("name");
                    String mobile = rs.getString("mobile");

                    // Admin 생성자 형태에 맞게 수정(지금은 (이름, 전화번호 int)라고 가정)
                    int phoneInt = parseMobileInt(mobile);
                    Admin admin = new Admin(name, phoneInt);

                    return admin;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;  // 로그인 실패
    }

    private int parseMobileInt(String mobile) {
        if (mobile == null) return 0;
        String digits = mobile.replaceAll("\\D", "");
        if (digits.isEmpty()) return 0;
        try {
            return Integer.parseInt(digits.substring(Math.max(0, digits.length() - 9)));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

