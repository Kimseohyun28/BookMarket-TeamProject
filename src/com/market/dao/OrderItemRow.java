package com.market.dao;

public class OrderItemRow {
    private String bookId;
    private String bookName;
    private int quantity;
    private int unitPrice;
    private int totalPrice;

    public OrderItemRow(String bookId,
                        String bookName,
                        int quantity,
                        int unitPrice,
                        int totalPrice) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
