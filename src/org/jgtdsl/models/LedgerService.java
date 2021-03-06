package org.jgtdsl.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleCallableStatement;

import org.jgtdsl.dto.CollectionDTO;
import org.jgtdsl.dto.CollectionLedgerDTO;
import org.jgtdsl.dto.ConnectionLedgerDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerLedgerDTO;
import org.jgtdsl.dto.DepositLedgerDTO;
import org.jgtdsl.dto.ResponseDTO;
import org.jgtdsl.enums.BankAccountTransactionType;
import org.jgtdsl.utils.connection.ConnectionManager;

public class LedgerService {

	public ArrayList<CustomerLedgerDTO> getCustomerLedger(String customer_id) {
		CustomerService customerService = new CustomerService();

		CustomerDTO customer = customerService.getCustomerInfo(customer_id);
		String sql = "";
		CustomerLedgerDTO entry = null;
		ArrayList<CustomerLedgerDTO> ledger = new ArrayList<CustomerLedgerDTO>();
		Connection conn = ConnectionManager.getConnection();

		if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Metered")) {

			sql = "SELECT *  " +
					"    FROM (SELECT bm.BILL_ID,  " +
					"                 bm.CUSTOMER_ID,  " +				
					"                 getBankBranch(BRANCH_ID) BANK_NAME,  " +
					"                 TO_CHAR (bm.COLLECTION_DATE) COLLECTION_DATE,  " +
					"                 MON || ', ' || BILL_YEAR DESCRIPTION,  " +
					"                 decode(nvl(BILLED_CONSUMPTION,0),0,ROUND (ACTUAL_GAS_CONSUMPTION, 2),ROUND (BILLED_CONSUMPTION, 2)) BILLED_CONSUMPTION, " +
					"                 BILLED_AMOUNT,  " +
					"                 bm.METER_RENT,  " +
					"                 CMS_RENT,  " +
					"                 SURCHARGE_AMOUNT,  " +
					"                 bm.PAYABLE_AMOUNT,  " +
					"                 COLLECTED_SURCHARGE,  " +
					"                 COLLECTED_AMOUNT,  " +
					"                 TO_CHAR (LAST_PAY_DATE_WO_SC, 'dd-mm-rrrr') DUE_DATE  " +
					"            FROM bill_metered bm, " +
					"                 MST_MONTH mm  " +
					"           WHERE   BM.BILL_MONTH = MM.M_ID  " +
					"                 AND bm.CUSTOMER_ID = ?)  " +
					"     ORDER BY BILL_ID " ;

			// "SELECT BILL_ID,TO_CHAR(COLLECTION_DATE) COLLECTION_DATE,MON||', '||BILL_YEAR DESCRIPTION,BILLED_CONSUMPTION,BILLED_AMOUNT,METER_RENT,CMS_RENT,SURCHARGE_AMOUNT,PAYABLE_AMOUNT,COLLECTED_SURCHARGE,"+
			// "COLLECTED_AMOUNT,to_char(LAST_PAY_DATE_WO_SC,'dd-mm-rrrr') DUE_DATE FROM BILL_METERED BM,MST_MONTH mm where BM.BILL_MONTH=MM.M_ID AND CUSTOMER_ID=? order by BILL_ID";

		} else {
			/*
			 * sql= " SELECT * " + "    FROM (SELECT bnm.BILL_ID, " +
			 * "                 bnm.CUSTOMER_ID, " +
			 * "                 bcnm.BANK_ID," +
			 * "             (select BANK_NAME from MST_BANK_INFO where BANK_ID=bcnm.BANK_ID) BANK_NAME, "
			 * +
			 * "                 TO_CHAR (bnm.COLLECTION_DATE) COLLECTION_DATE, "
			 * + "                 MON || ', ' || BILL_YEAR DESCRIPTION, " +
			 * "                 TOTAL_CONSUMPTION BILLED_CONSUMPTION, " +
			 * "                 BILLED_AMOUNT, " +
			 * "                 NULL METER_RENT, " +
			 * "                 NULL CMS_RENT, " +
			 * "                 ACTUAL_SURCHARGE SURCHARGE_AMOUNT, " +
			 * "                 ACTUAL_PAYABLE_AMOUNT PAYABLE_AMOUNT, " +
			 * "                 COLLECTED_SURCHARGE_AMOUNT COLLECTED_SURCHARGE, COLLECTED_BILL_AMOUNT COLLECTED_AMOUNT, "
			 * + "                 TO_CHAR (DUE_DATE, 'dd-mm-rrrr') DUE_DATE " +
			 * "            FROM bill_non_metered bnm, BILL_COLLECTION_NON_METERED bcnm, MST_MONTH mm "
			 * +
			 * "           WHERE bnm.BILL_ID = bcnm.BILL_ID(+)   and BNM.BILL_MONTH = MM.M_ID AND bnm.CUSTOMER_ID = ? "
			 * + "          UNION ALL " + "          SELECT NULL BILL_ID, " +
			 * "                 CUSTOMER_ID, " +
			 * "                 BANK_ID, NULL BANK_NAME, " +
			 * "                 TO_CHAR (TRANS_DATE) COLLECTION_DATE, " +
			 * "                 'Advanced' DESCRIPTION, " +
			 * "                 NULL BILLED_CONSUMPTION, " +
			 * "                 NULL BILLED_AMOUNT, " +
			 * "                 NULL METER_RENT, " +
			 * "                 NULL CMS_RENT, " +
			 * "                 NULL SURCHARGE_AMOUNT, " +
			 * "                 NULL PAYABLE_AMOUNT, " +
			 * "                 NULL COLLECTED_SURCHARGE, " +
			 * "                 ADVANCED_AMOUNT COLLECTED_AMOUNT, " +
			 * "                 NULL DUE_DATE " +
			 * "            FROM bill_coll_advanced " +
			 * "           WHERE status = 1 AND CUSTOMER_ID = ?) " +
			 * "			ORDER BY BILL_ID " ;
			 */

			sql = "SELECT *  "
					+ "    FROM (SELECT bnm.BILL_ID,  "
					+ "                 bnm.CUSTOMER_ID,                   "
					+ "                getBankBranch(BRANCH_ID) BANK_NAME,   "
					+ "                 TO_CHAR (bnm.COLLECTION_DATE) COLLECTION_DATE,  "
					+ "                 MON || ', ' || BILL_YEAR DESCRIPTION,  "
					+ "                 round(TOTAL_CONSUMPTION,3) BILLED_CONSUMPTION,  "
					+ "                 BILLED_AMOUNT,  "
					+ "                 NULL METER_RENT,  "
					+ "                 NULL CMS_RENT,  "
					+ "                 ACTUAL_SURCHARGE SURCHARGE_AMOUNT,  "
					+ "                 ACTUAL_PAYABLE_AMOUNT PAYABLE_AMOUNT,  "
					+ "                 COLLECTED_SURCHARGE, COLLECTED_PAYABLE_AMOUNT COLLECTED_AMOUNT,  "
					+ "                 TO_CHAR (DUE_DATE, 'dd-mm-rrrr') DUE_DATE  "
					+ "            FROM bill_non_metered bnm, MST_MONTH mm  "
					+ "           WHERE BNM.BILL_MONTH = MM.M_ID AND bnm.CUSTOMER_ID = ? "
					+ "          UNION ALL  "
					+ "          SELECT NULL BILL_ID,  "
					+ "                 CUSTOMER_ID,                 "
					+ "                 getBankBranch(BRANCH_ID) BANK_NAME,   "
					+ "                 TO_CHAR (TRANS_DATE) COLLECTION_DATE,  "
					+ "                 'Advanced' DESCRIPTION,  "
					+ "                 NULL BILLED_CONSUMPTION,  "
					+ "                 NULL BILLED_AMOUNT,  "
					+ "                 NULL METER_RENT,  "
					+ "                 NULL CMS_RENT,  "
					+ "                 NULL SURCHARGE_AMOUNT,  "
					+ "                 NULL PAYABLE_AMOUNT,  "
					+ "                 NULL COLLECTED_SURCHARGE,  "
					+ "                 ADVANCED_AMOUNT COLLECTED_AMOUNT,  "
					+ "                 NULL DUE_DATE  "
					+ "            FROM bill_coll_advanced  "
					+ "           WHERE status = 1 AND CUSTOMER_ID = ?)  "
					+ " ORDER BY BILL_ID ";
		}

		/*
		 * " Select TRANS_ID,to_char(TRANS_DATE,'DD-MON-RRRR') TRANS_DATE_F1,PARTICULARS,DEBIT,CREDIT,BALANCE,STATUS"
		 * +
		 * " FROM CUSTOMER_LEDGER Where Customer_Id = ? And STATUS=1 Order By TRANS_DATE,TRANS_ID, INSERTED_ON Asc "
		 * ;
		 */

		PreparedStatement stmt = null;
		ResultSet r = null;

		try {
			stmt = conn.prepareStatement(sql);
			if (customer.getConnectionInfo().getIsMetered_name()
					.equalsIgnoreCase("Metered")) {
				stmt.setString(1, customer_id);
			} else {
				stmt.setString(1, customer_id);
				stmt.setString(2, customer_id);
			}

			r = stmt.executeQuery();

			while (r.next()) {
				entry = new CustomerLedgerDTO();
				entry.setEntry_type(r.getString("BILL_ID"));
				// entry.setBank_id(r.getString("BANK_ID"));
				entry.setBank_name(r.getString("BANK_NAME"));
				entry.setIssue_paid_date(r.getString("COLLECTION_DATE"));
				entry.setParticulars(r.getString("DESCRIPTION"));
				entry.setGas_sold(r.getString("BILLED_CONSUMPTION"));
				entry.setSales_amount(r.getString("BILLED_AMOUNT"));
				entry.setSurcharge(r.getString("SURCHARGE_AMOUNT"));
				entry.setDebit_amount(r.getString("PAYABLE_AMOUNT"));
				entry.setCredit_surcharge(r.getString("COLLECTED_SURCHARGE"));
				entry.setCredit_amount(r.getString("COLLECTED_AMOUNT"));
				// entry.setBalance_amount(r.getDouble("BALANCE"));
				entry.setDue_date(r.getString("DUE_DATE"));
				// entry.setStatus(r.getString("STATUS"));
				ledger.add(entry);
			}

			double balance = 0d;
			for (int i = 0; i < ledger.size(); i++) {
				if (i == 0)
					balance = Double.valueOf(ledger.get(i).getBalance_amount());

				// System.out.println("Balance : "+balance+", Debit : "+ledger.get(i).getDebit_amount()+", Credit : "+ledger.get(i).getCredit_amount());
				balance = balance
						+ Double.valueOf(ledger.get(i).getDebit_amount() == null ? "0"
								: ledger.get(i).getDebit_amount())
						- Double.valueOf(ledger.get(i).getCredit_amount() == null ? "0"
								: ledger.get(i).getCredit_amount());

				// System.out.print("\n ===>> New Balance : "+balance);
				/*
				 * + Double.valueOf(ledger.get(i).getSurcharge() == null ? "0" :
				 * ledger.get(i).getSurcharge()) -
				 * Double.valueOf(ledger.get(i).getCredit_surcharge() == null ?
				 * "0" : ledger.get(i).getCredit_surcharge());
				 */
				ledger.get(i).setBalance_amount(balance);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return ledger;
	}
	
//For editing surcharge ~ Prince ~ april 25, 2018
	
	public ArrayList<CustomerLedgerDTO> getNMLedgerByMonthYear(String customer_id, int month, int year) {
		
		String sql = "";
		CustomerLedgerDTO entry = null;
		ArrayList<CustomerLedgerDTO> ledger = new ArrayList<CustomerLedgerDTO>();
		
		CustomerService customerService = new CustomerService();
		CustomerDTO customer = customerService.getCustomerInfo(customer_id);
		if(customer == null){
			return ledger;
		}
		
		Connection conn = ConnectionManager.getConnection();
				
		if (customer.getConnectionInfo().getIsMetered_name()
				.equalsIgnoreCase("Metered")) {
			System.out.println("This function works for non-metered customers only.");
		}else{
			sql = "SELECT bnm.CUSTOMER_ID, " +
					"       bnm.BILL_ID, " +
					"       MON || ', ' || BILL_YEAR DESCRIPTION, " +
					"       BILLED_AMOUNT, " +
					"       ACTUAL_SURCHARGE SURCHARGE_AMOUNT, " +
					"       COLLECTED_BILLED_AMOUNT, " +
					"       NVL (COLLECTED_SURCHARGE, 0) COLLECTED_SURCHARGE, " +
					"       TO_CHAR (DUE_DATE, 'dd-mm-rrrr') DUE_DATE, " +
					"       getBankBranch (BRANCH_ID) BANK_NAME, " +
					"       TO_CHAR (bnm.COLLECTION_DATE) COLLECTION_DATE " +
					"  FROM bill_non_metered bnm, MST_MONTH mm " +
					" WHERE     BNM.BILL_MONTH = MM.M_ID " +
					"       AND bnm.CUSTOMER_ID = ? " +
					"       AND BILL_MONTH = ? " +
					"       AND BILL_YEAR = ? "  ;
		}
			
		

	
		PreparedStatement stmt = null;
		ResultSet r = null;

		try {
			stmt = conn.prepareStatement(sql);
		
				stmt.setString(1, customer_id);
				stmt.setInt(2, month);
				stmt.setInt(3, year);
			

			r = stmt.executeQuery();

			while (r.next()) {
				entry = new CustomerLedgerDTO();
				entry.setCustomer_id(r.getString("CUSTOMER_ID"));
				entry.setEntry_type(r.getString("BILL_ID"));
				entry.setParticulars(r.getString("DESCRIPTION"));
				entry.setSales_amount(r.getString("BILLED_AMOUNT"));
				entry.setSurcharge(r.getString("SURCHARGE_AMOUNT"));
				entry.setCredit_amount(r.getString("COLLECTED_BILLED_AMOUNT"));
				entry.setCredit_surcharge(r.getString("COLLECTED_SURCHARGE"));
				entry.setDue_date(r.getString("DUE_DATE"));
				entry.setBank_name(r.getString("BANK_NAME"));
				entry.setIssue_paid_date(r.getString("COLLECTION_DATE"));
				ledger.add(entry);
			}


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return ledger;
	}

	//end of - editing surcharge
	
	
	//For updating surcharge ~ Prince ~ april 28, 2018
	
		public String updateNMSurcharge (CustomerLedgerDTO cl) {
			
			String sql = "";
			String msg = "";
			
			Connection conn = ConnectionManager.getConnection();
				sql = "UPDATE bill_non_metered " +
						"SET ACTUAL_SURCHARGE = ?, " +
						"    COLLECTED_SURCHARGE = ?, " +
						"    ACTUAL_PAYABLE_AMOUNT = BILLED_AMOUNT+ NVL(?,0), " +
						"    COLLECTED_PAYABLE_AMOUNT = COLLECTED_BILLED_AMOUNT + NVL(?,0) " +
						"WHERE BILL_ID= ? ";
				
			PreparedStatement stmt = null;
			//ResultSet r = null;
			int affectedRows =0;

			try {
				stmt = conn.prepareStatement(sql);
			
					stmt.setInt(1, Integer.parseInt(cl.getSurcharge()));
					stmt.setInt(2, Integer.parseInt(cl.getCredit_surcharge()));
					stmt.setInt(3, Integer.parseInt(cl.getSurcharge()));
					stmt.setInt(4, Integer.parseInt(cl.getCredit_surcharge()));
					stmt.setString(5, cl.getEntry_type());
					
					affectedRows = stmt.executeUpdate();
					msg = "Rows affected : " +affectedRows ;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					stmt.close();
					ConnectionManager.closeConnection(conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
				stmt = null;
				conn = null;
			}

			return  msg;
		}

		//end of - updating surcharge
	
	

	public ArrayList<DepositLedgerDTO> getDepositLedger(String customer_id) {
		DepositLedgerDTO entry = null;
		ArrayList<DepositLedgerDTO> ledger = new ArrayList<DepositLedgerDTO>();
		Connection conn = ConnectionManager.getConnection();
		String sql = "select DEPOSIT_ID,to_char(DEPOSIT_DATE,'dd-mm-rrrr') DEPOSIT_DATE,TOTAL_DEPOSIT,DECODE (DEPOSIT_PURPOSE,  2, 'Less',  1, 'Add')||', ' || getDepositComment(DEPOSIT_ID) as particular, "
				+ "to_char(VALID_TO,'dd-mm-rrrr') VALID_TO,decode(DEPOSIT_TYPE,0,'CASH BANK',1,'BANK GUARANTEE') deposit_type, getBankBranch(BRANCH_ID) bank_name from mst_deposit "
				+ "where customer_id=? "
				+ "and DEPOSIT_PURPOSE in (1,2,3) "
				+ "order by DEPOSIT_DATE";

		/*
		 * "Select TRANS_ID,to_char(TRANS_DATE,'DD-MON-RRRR') TRANS_DATE_VIEW,TRANS_DATE,DESCRIPTION,DEBIT,CREDIT,BALANCE,STATUS,to_char(VALID_TO,'dd-mm-rrrr') expire_date,decode(nvl(Credit,0),0,'DEPOSIT','WITHDRAW') deposit "
		 * +
		 * " From CUSTOMER_SECURITY_LEDGER Where Customer_Id = ? Order By TRANS_DATE,INSERTED_ON Asc "
		 * ;
		 */

		float balance = 0;
		PreparedStatement stmt = null;
		ResultSet r = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, customer_id);
			r = stmt.executeQuery();
			while (r.next()) {
				entry = new DepositLedgerDTO();
				entry.setTrans_id(r.getString("DEPOSIT_ID"));
				entry.setDeposit_date(r.getString("DEPOSIT_DATE"));
				entry.setDescription(r.getString("particular"));
				entry.setDeposit_type(r.getString("deposit_type"));
				entry.setDebit_amount(r.getString("TOTAL_DEPOSIT"));
				entry.setExpire_date(r.getString("VALID_TO"));
				entry.setBank_name(r.getString("bank_name"));
				// entry.setDeposit(r.getString("deposit"));
				// entry.setCredit_amount(r.getString("CREDIT"));
				// balance=balance+(r.getString("DEBIT")==null?Float.valueOf("0"):Float.valueOf(r.getString("DEBIT")))-(r.getString("CREDIT")==null?Float.valueOf("0"):Float.valueOf(r.getString("CREDIT")));
				// entry.setBalance_amount(String.valueOf(balance));
				// entry.setStatus(r.getString("STATUS"));
				ledger.add(entry);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return ledger;
	}

	public ArrayList<ConnectionLedgerDTO> getConnectionLedger(String customer_id) {
		ArrayList<ConnectionLedgerDTO> ledger = new ArrayList<ConnectionLedgerDTO>();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		ConnectionLedgerDTO connection;
		try {

			
			stmt = (OracleCallableStatement) conn
					.prepareCall("{ call GET_CONNECTION_LEDGER(?,?)  }");
			

			stmt.setString(1, customer_id);
			stmt.registerOutParameter(2, OracleTypes.CURSOR);
			stmt.execute();
			ResultSet rs = ((OracleCallableStatement) stmt).getCursor(2);

			while (rs.next()) {
				connection = new ConnectionLedgerDTO();
				connection.setEvent_date(rs.getString("Event_Date_View"));
				connection.setDescription(rs.getString("Description"));
				try {
					connection.setMin_load(rs.getString("Min_Load"));
					connection.setMax_load(rs.getString("Max_Load"));
				} catch (Exception ex) {
					connection.setSingle_burner(rs
							.getString("Single_Burner_Qnt"));
					connection.setDouble_burner(rs
							.getString("Double_Burner_Qnt"));
				}
				ledger.add(connection);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return ledger;

	}

	public ResponseDTO processCustomerLedgerBalance(String customer_id) {
		ResponseDTO response = new ResponseDTO();
		Connection conn = ConnectionManager.getConnection();
		OracleCallableStatement stmt = null;
		int response_code = 0;
		try {

			// System.out.println("===>>Procedure : [PROCESS_BALANCE_CUST_ACCOUNT] START");
			stmt = (OracleCallableStatement) conn
					.prepareCall("{ call PROCESS_BALANCE_CUST_ACCOUNT(?,?,?,?)  }");
			// System.out.println("==>>Procedure : END");

			stmt.setString(1, customer_id);
			stmt.setString(2, "");
			stmt.registerOutParameter(3, java.sql.Types.INTEGER);
			stmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			stmt.executeUpdate();
			response_code = stmt.getInt(3);
			response.setMessasge(stmt.getString(4));

			if (response_code == 1) {
				response.setResponse(true);
			} else {
				response.setResponse(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				ConnectionManager.closeConnection(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stmt = null;
			conn = null;
		}

		return response;

	}
	
	///collection ledger starts
	
	public ArrayList<CollectionLedgerDTO> getCollectionLedger(String customer_id){
		
		CollectionLedgerDTO collDto= null;
		ArrayList<CollectionLedgerDTO> collectionLedger= new ArrayList<CollectionLedgerDTO>();
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pst= null;
		ResultSet rs= null;
		
		String sql= "select bal.*, GETBANKBRANCH(bal.BRANCH_ID) bank_branch, to_char(bal.TRANS_DATE) trans_date_to_char "
				+ "from BANK_ACCOUNT_LEDGER bal "
				+ "where bal.CUSTOMER_ID = ? "
				+ "order by bal.TRANS_DATE desc";
		try{
			pst= conn.prepareStatement(sql);
			pst.setString(1, customer_id);
			
			rs=pst.executeQuery();
			
			while(rs.next()){
				collDto= new CollectionLedgerDTO();
				collDto.setTrans_date(rs.getString("trans_date_to_char"));
				collDto.setDebit_amount(rs.getInt("DEBIT"));
				collDto.setSurcharge(rs.getInt("SURCHARGE"));
				collDto.setBank_branch(rs.getString("bank_branch"));
				collDto.setInsert_date(rs.getString("INSERTED_ON"));
				collDto.setRemarks(rs.getString("PARTICULARS"));
				
				String trans_type= rs.getString("TRANS_TYPE");
				BankAccountTransactionType t= BankAccountTransactionType.values()[Integer.parseInt(trans_type)];
				
				collDto.setTrans_type(t.toString());
				
				collectionLedger.add(collDto);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		finally {
			try {
				pst.close();
				rs.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return collectionLedger;
	}

}
