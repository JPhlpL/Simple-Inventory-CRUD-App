package com.mycompany.simpleinventorycrudapp.model;

import java.sql.Timestamp;

public class Transaction {

    private int id;
    private String partName;
    private String partNumber;
    private String supplierName;
    private int stocks;
    private Timestamp timestamp;

    public Transaction() {
    }

    public Transaction(int id, String partName, String partNumber, String supplierName, int stocks, Timestamp timestamp) {
        this.id = id;
        this.partName = partName;
        this.partNumber = partNumber;
        this.supplierName = supplierName;
        this.stocks = stocks;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getStocks() {
        return stocks;
    }

    public void setStocks(int stocks) {
        this.stocks = stocks;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
