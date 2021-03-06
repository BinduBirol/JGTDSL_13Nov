package org.jgtdsl.actions.bank;

import java.util.ArrayList;

import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.BankDepositWithdrawDTO;
import org.jgtdsl.dto.BillingParamDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.dto.TransactionDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.models.BankTransactionService;
import org.jgtdsl.models.BillingService;

public class BankTransaction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private BankDepositWithdrawDTO bankDepositWithdraw;
	private ArrayList<TransactionDTO> transactionList;
	private ArrayList<TransactionDTO> mpg_dashboard;
	private ArrayList<TransactionDTO> mpg_dashboard_date_wise;
	private ArrayList<TransactionDTO> mpg_dashboard_area_wise;
	private String transaction_id;
	private String whereClause;
	
	private String from_date;
	private String to_date;
	private String bank_name;
	private double total_gas_bill;
	private double total_surcharge;
	private double grand_total;

	public String saveBankTransaction(){
		
		BankTransactionService bankTransaction=new BankTransactionService();
		bankDepositWithdraw.setInserted_by(((UserDTO)session.get("user")).getUserId());		
		ResponseDTO response=null;
		response=bankTransaction.saveBankTransaction(bankDepositWithdraw);
			
		setJsonResponse(response);		
		return null;
	}
	public String transactionAuthorization(){
		UserDTO loggedInUser=(UserDTO)session.get("user");
		BankTransactionService bankTransaction=new BankTransactionService();
		transactionList=bankTransaction.getUnAuthCount(loggedInUser.getArea_id());
        return SUCCESS;
	}
	
	public String mpgDashboardHome(){		
        return SUCCESS;
	}
	
	
	public String deleteBankTransaction(){
		BankTransactionService bankTransaction=new BankTransactionService();
		ResponseDTO response=bankTransaction.deleteBankTransaction(transaction_id);
		setJsonResponse(response);
		return null;
	}
	public String getTotalDebitCredit(){
		BankTransactionService bankTransaction=new BankTransactionService();		
		setTextResponse(bankTransaction.getTotalDebitCredit(whereClause));
		return null;
	}
	public BankDepositWithdrawDTO getBankDepositWithdraw() {
		return bankDepositWithdraw;
	}
	
	public String MPGdashboard(){
		BankTransactionService bankTransaction=new BankTransactionService();
		mpg_dashboard= bankTransaction.getMPGTransactionList(from_date,to_date, bank_name);
		mpg_dashboard_date_wise=bankTransaction.getMPGTransactionDateWise(from_date, to_date, bank_name);
		mpg_dashboard_area_wise=bankTransaction.getMPGTransactionAreaWise(from_date, to_date, bank_name);
		for(TransactionDTO x:mpg_dashboard){
			total_gas_bill+= x.getGas_bill();
			total_surcharge+=x.getSurcharge();
			grand_total+= x.getDebit();
		}		
		return "success";
	}

	public void setBankDepositWithdraw(BankDepositWithdrawDTO bankDepositWithdraw) {
		this.bankDepositWithdraw = bankDepositWithdraw;
	}
		
	public ArrayList<TransactionDTO> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(ArrayList<TransactionDTO> transactionList) {
		this.transactionList = transactionList;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transactionId) {
		transaction_id = transactionId;
	}
	public String getWhereClause() {
		return whereClause;
	}
	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}
	public ArrayList<TransactionDTO> getMpg_dashboard() {
		return mpg_dashboard;
	}
	public void setMpg_dashboard(ArrayList<TransactionDTO> mpg_dashboard) {
		this.mpg_dashboard = mpg_dashboard;
	}
	public String getFrom_date() {
		return from_date;
	}
	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}
	public String getTo_date() {
		return to_date;
	}
	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}
	public double getTotal_gas_bill() {
		return total_gas_bill;
	}
	public void setTotal_gas_bill(double total_gas_bill) {
		this.total_gas_bill = total_gas_bill;
	}
	public double getTotal_surcharge() {
		return total_surcharge;
	}
	public void setTotal_surcharge(double total_surcharge) {
		this.total_surcharge = total_surcharge;
	}
	public double getGrand_total() {
		return grand_total;
	}
	public void setGrand_total(double grand_total) {
		this.grand_total = grand_total;
	}
	public ArrayList<TransactionDTO> getMpg_dashboard_date_wise() {
		return mpg_dashboard_date_wise;
	}
	public void setMpg_dashboard_date_wise(
			ArrayList<TransactionDTO> mpg_dashboard_date_wise) {
		this.mpg_dashboard_date_wise = mpg_dashboard_date_wise;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public ArrayList<TransactionDTO> getMpg_dashboard_area_wise() {
		return mpg_dashboard_area_wise;
	}
	public void setMpg_dashboard_area_wise(
			ArrayList<TransactionDTO> mpg_dashboard_area_wise) {
		this.mpg_dashboard_area_wise = mpg_dashboard_area_wise;
	}
	
	
}
