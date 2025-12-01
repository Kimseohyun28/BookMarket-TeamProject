package com.market.dao;

import java.sql.Timestamp;

public class OrderSummaryRow {

    private long orderId;
    private Timestamp orderDate;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private int totalPrice;
    private int itemCount;

    public OrderSummaryRow(long orderId,
                           Timestamp orderDate,
                           String receiverName,
                           String receiverPhone,
                           String receiverAddress,
                           int totalPrice,
                           int itemCount) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.totalPrice = totalPrice;
        this.itemCount = itemCount;
    }

    public long getOrderId() {
        return orderId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getItemCount() {
        return itemCount;
    }
}
