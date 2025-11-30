package com.market.member;

public class Admin extends Person {

    private String id;        // 관리자 로그인 ID
    private String password;  // 관리자 비밀번호


    public Admin(String name, String phone) {
        super(name, phone);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {    // <- 이게 있어야 admin.setId(...)가 됨
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {  // <- 이것도
        this.password = password;
    }
}
