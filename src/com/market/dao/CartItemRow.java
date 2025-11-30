
package com.market.dao;

/**
 * cart_items + book JOIN 결과를 담는 DTO.
 * (화면/Cart에서는 이걸 받아서 CartItem으로 변환해서 쓸 예정)
 */

public class CartItemRow {
    private String bookId;
    private String name;
    private int unitPrice;
    private int quantity;

    public CartItemRow(String bookId, String name, int unitPrice, int quantity) {
        this.bookId = bookId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getBookId() {
        return bookId;
    }

    public String getName() {
        return name;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return unitPrice * quantity;
    }
}


