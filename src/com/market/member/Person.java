package com.market.member;

public class Person {
    private String name;
    private String phone;   
    private String address;

    public Person(String name, String phone) {   
        this.name = name;
        this.phone = phone;
    }

    public Person(String name, String phone, String address) {   
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {      // 반환 타입 String
        return this.phone;
    }

    public void setPhone(String phone) {   //파라미터 타입 String
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
