package org.jgtdsl.dto;

import com.google.gson.Gson;

public class CustomerListDTO {

	private String customerName;
	private String customerId;
	private String customerAddress;
	private String fatherName;
	private String status;
	private double ledgerBalance;
	private String category;
	private String categoryName;
	private double maxLoad;
	private float burnerQty;
	private float billBurner;
	private double numberOfCustomer;
	private String customerType;
	private String areaName;
	private int count;

	
	//for mixed customer report 
	
	private String burner_or_minmaxload;
	
	private String parent_connection;
	
	
	
	
	public String getParent_connection() {
		return parent_connection;
	}
	public void setParent_connection(String parent_connection) {
		this.parent_connection = parent_connection;
	}
	
	public String getBurner_or_minmaxload() {
		return burner_or_minmaxload;
	}
	public void setBurner_or_minmaxload(String burner_or_minmaxload) {
		this.burner_or_minmaxload = burner_or_minmaxload;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getAreaName() {
		return areaName;s
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public double getNumberOfCustomer() {
		return numberOfCustomer;
	}
	public void setNumberOfCustomer(double numberOfCustomer) {
		this.numberOfCustomer = numberOfCustomer;
	}
	public float getBurnerQty() {
		return burnerQty;
	}
	public void setBurnerQty(float burnerQty) {
		this.burnerQty = burnerQty;
	}
	public float getBillBurner() {
		return billBurner;
	}
	public void setBillBurner(float billBurner) {
		this.billBurner = billBurner;
	}
	public double getMaxLoad() {
		return maxLoad;
	}
	public void setMaxLoad(double maxLoad) {
		this.maxLoad = maxLoad;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getLedgerBalance() {
		return ledgerBalance;
	}
	public void setLedgerBalance(double ledgerBalance) {
		this.ledgerBalance = ledgerBalance;
	}
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }		
	
	
}
