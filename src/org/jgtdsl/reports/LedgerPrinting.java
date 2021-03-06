package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.swing.table.TableColumn;

import org.apache.struts2.util.ServletContextAware;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.actions.Customer;
import org.jgtdsl.dto.BurnerQntChangeDTO;
import org.jgtdsl.dto.CustomerApplianceDTO;
import org.jgtdsl.dto.CustomerConnectionDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.dto.CustomerInfoDTO4Bill;
import org.jgtdsl.dto.CustomerLedgerDTO;
import org.jgtdsl.dto.CustomerMeterDTO;
import org.jgtdsl.dto.DepositLedgerDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.ConnectionStatus;
import org.jgtdsl.enums.MeterStatus;
import org.jgtdsl.models.BurnerQntChangeService;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.LedgerService;
import org.jgtdsl.models.MeterService;
import org.jgtdsl.utils.Utils;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class LedgerPrinting extends BaseAction implements ServletContextAware {
	private static final long serialVersionUID = 8854240739341830184L;
	private ServletContext servlet;
	private String bill_id;
	private String customer_category;
	private String area_id;
	private String customer_id;
	private String category_name;
	private String bill_month;
	private String bill_year;
	private String download_type;
	private String bill_for;
	private String from_date;
	private String to_date;
	private int serial = 0;
	public static int textDiff = 597;
	private boolean water_mark = false;
	
	ArrayList<CustomerLedgerDTO> mainLedger= new ArrayList<CustomerLedgerDTO>();
	ConnectionStatus connenum;
	MeterService ms=new MeterService();
	ArrayList<CustomerApplianceDTO> applianceList=new ArrayList<CustomerApplianceDTO>();
	private ArrayList<CustomerMeterDTO> meterList=new ArrayList<CustomerMeterDTO>();
	BurnerQntChangeService BQCS= new BurnerQntChangeService();
	ArrayList<BurnerQntChangeDTO> connledger= new ArrayList<BurnerQntChangeDTO>();
	LedgerService ls= new LedgerService();
	CustomerDTO customerInfo = new CustomerDTO();
	CustomerService cs= new CustomerService();
	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.00");

	public String downloadLedger() throws Exception {
		Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		Font font2 = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
		Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		Font font4 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		UserDTO loggedInUser = (UserDTO) session.get("user");
		if ((area_id == null || area_id.equalsIgnoreCase(""))
				&& loggedInUser != null)
			area_id = loggedInUser.getArea_id();

		String fileName = "LEDGER-" + customer_id + ".pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		try {
			ReportFormat Event = new ReportFormat(getServletContext());
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			writer.setPageEvent(Event);
			PdfPCell pcell = null;

			document.open();
			String realPath = servlet
					.getRealPath("/resources/images/logo/JG.png"); // image path
			Image img = Image.getInstance(realPath);

			// img.scaleToFit(10f, 200f);
			// img.scalePercent(200f);
			img.scaleAbsolute(30f, 34f);
			// img.setAbsolutePosition(145f, 780f);
			img.setAbsolutePosition(260f, 500f); // rotate

			document.add(img);
			
			CustomerDTO customer = cs.getCustomerInfo(customer_id);

			PdfPTable mTable = new PdfPTable(1);
			mTable.setWidthPercentage(100);
			
			
			
			pcell = new PdfPCell(new Paragraph(
					"JALALABAD GAS T & D SYSTEM LIMITED", ReportUtil.f11B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
			

			pcell = new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)",
					ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);	
			

			Chunk chunk1 = new Chunk("Regional Distribution: ", font2);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer
					.valueOf(getArea_id()) - 1]), ReportUtil.f8B);
			Paragraph p = new Paragraph();
			p.add(chunk1);
			p.add(chunk2);
			pcell = new PdfPCell(p);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(
					"ledger for: "+customer.getConnectionInfo().getIsMetered_name()+" Customer", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(Rectangle.NO_BORDER);
			mTable.addCell(pcell);
			
			
			///////////////////////////////////////
			PdfPTable ledger1 = new PdfPTable(3);
			ledger1.setWidthPercentage(98);
			
			pcell=new PdfPCell(new Paragraph(" ",font3));
			pcell.setPadding(5);
			pcell.setColspan(3);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			ledger1.addCell(pcell);
			///////////customerTable//////////
			
			PdfPTable customerinfo = new PdfPTable(2);
			customerinfo.setWidths(new float[]{35,65});
			
			DefaulterCCertificate dcc= new DefaulterCCertificate();
			customerInfo= dcc.getCustomerInfo(this.customer_id);
			
			pcell=new PdfPCell(new Paragraph("Customer Code: ",font3));
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			customerinfo.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(this.customer_id,font2));
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			customerinfo.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Customer Name: ",font3));
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			customerinfo.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(customerInfo.getCustomer_name(),font3));
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			customerinfo.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Customer Address: ",font3));
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			customerinfo.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(customerInfo.getAddress(),font3));
			pcell.setPadding(5);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			customerinfo.addCell(pcell);	
			
			//////////customerTable ends/////////
			
			
			///////////////burnerTable/////////////
			PdfPTable burnerTable= new PdfPTable(2);			
			pcell=new PdfPCell(new Paragraph("Burner Info.",font4));
			pcell.setColspan(2);
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			burnerTable.addCell(pcell);
			
			
			applianceList=ms.getCustomerApplianceList(customer_id);	
			for(CustomerApplianceDTO x: applianceList){
				
				pcell=new PdfPCell(new Paragraph(x.getApplianc_name(),font4));
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				burnerTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getApplianc_qnt(),font4));
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				burnerTable.addCell(pcell);			
								
			}
			
			/////////////burnertable ends/////////////
			
			/////////////////////meter table/////////////////
			
			MeterService ms=new MeterService();
			meterList=ms.getCustomerMeterList(customer_id, Utils.EMPTY_STRING,Utils.EMPTY_STRING);
			
			PdfPTable meterTable= new PdfPTable(5);			
			pcell=new PdfPCell(new Paragraph("Meter SL",font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			meterTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Rent",font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			meterTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Min load",font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			meterTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Max load",font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			meterTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Status",font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			meterTable.addCell(pcell);
			
			for(CustomerMeterDTO x: meterList){
				
				pcell=new PdfPCell(new Paragraph(x.getMeter_sl_no(),font3));
				pcell.setPadding(3);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				meterTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getMeter_rent(),font3));
				pcell.setPadding(3);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				meterTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getMin_load(),font3));
				pcell.setPadding(3);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				meterTable.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getMax_load(),font3));
				pcell.setPadding(3);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				meterTable.addCell(pcell);
				
				
				pcell=new PdfPCell(new Paragraph(String.valueOf(x.getStatus()),font3));
				pcell.setPadding(3);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				meterTable.addCell(pcell);
			}
			
			/////////////////////meter table ends////////////////
			
			/////////////Connection Ledger non meter///////////
			PdfPTable connLdgr= new PdfPTable(3);			
			
			pcell=new PdfPCell(new Paragraph("Old(Double)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			connLdgr.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("New(Double)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			connLdgr.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Date",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			connLdgr.addCell(pcell);
			
			this.connledger= BQCS.getBurnerQntChangeListGrid(this.customer_id);	
			
			for(BurnerQntChangeDTO x: connledger){
				pcell=new PdfPCell(new Paragraph(x.getOld_double_burner_qnt(),font3));
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				connLdgr.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getNew_double_burner_qnt(),font3));
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				connLdgr.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getEffective_date(),font3));
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				connLdgr.addCell(pcell);
			}
			
			/////////////connection ledger non meter ends///////////
			
			////////////Security ledger meter/////////////////
			
			PdfPTable secLedMetered= new PdfPTable(2);			
			
			
			
			ArrayList<DepositLedgerDTO> depositAmount= ls.getDepositLedger(this.customer_id);
			int totalBG=0;
			int totalCB=0;
			
			pcell=new PdfPCell(new Paragraph("Deposit Total (Tk.)",font4));
			pcell.setPadding(3);
			pcell.setColspan(2);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			secLedMetered.addCell(pcell);
			
			
			pcell=new PdfPCell(new Paragraph("Cash Bank",font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			secLedMetered.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("BG",font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			secLedMetered.addCell(pcell);
			
			for(int i=0; i<depositAmount.size();i++){
				if(depositAmount.get(i).getDeposit_type().equalsIgnoreCase("BANK GUARANTEE")){
					totalBG = totalBG + Integer.parseInt( depositAmount.get(i).getDebit_amount());
				}
					
			}
			
			for(int i=0; i<depositAmount.size();i++){
				if(depositAmount.get(i).getDeposit_type().equalsIgnoreCase("CASH BANK")){
					totalCB = totalCB + Integer.parseInt( depositAmount.get(i).getDebit_amount());
				}
			}
			
			pcell=new PdfPCell(new Paragraph(String.valueOf(totalCB),font4));
			pcell.setPadding(3);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			secLedMetered.addCell(pcell);			
			
			pcell=new PdfPCell(new Paragraph(String.valueOf(totalBG),font4));
			pcell.setPadding(3);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			secLedMetered.addCell(pcell);
			
			////////////security ledger meter ends/////////////////
			
			
			
			
			
			///////////////main ledger///////////////////
			PdfPTable mainledger= new PdfPTable(11);
			mainledger.setWidthPercentage(98);
			
			
			

			pcell=new PdfPCell(mTable);
			pcell.setPadding(3);
			pcell.setColspan(3);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);				
			ledger1.addCell(pcell);			
			
		
			
			ledger1.addCell(customerinfo);
			
				if (customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered")) {
					ledger1.addCell(meterTable);
					ledger1.addCell(secLedMetered);
				}else{				
					ledger1.addCell(burnerTable);
					//ledger1.addCell(connLdgr);
					ledger1.addCell(secLedMetered);
				}				
							
				pcell=new PdfPCell(ledger1);
				pcell.setPadding(3);
				pcell.setColspan(11);
				pcell.setBorder(0);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);	
				mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph(" ",font4));
			pcell.setPadding(5);
			pcell.setColspan(11);
			pcell.setBorder(0);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Payment Date",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Description",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Bank",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Volum of Gas Sold(m+"+Math.pow(0, 3)+")",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Sales(Tk.)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Debit Surcharge(Tk.)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Debit(Tk.)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Credit Surcharge(Tk.)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Credit(Tk.)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Balance(Tk.)",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("Bill Due Date",font4));
			pcell.setPadding(5);
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			mainledger.addCell(pcell);
			
			
			
			this.mainLedger= getCustomerLedger(this.customer_id);
			
			for(CustomerLedgerDTO x: mainLedger){
				
				pcell=new PdfPCell(new Paragraph(x.getIssue_paid_date(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getParticulars(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getBank_name(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getGas_sold(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getSales_amount(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getSurcharge(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getDebit_amount(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getCredit_surcharge(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getCredit_amount(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(taka_format.format(x.getBalance_amount()),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				pcell=new PdfPCell(new Paragraph(x.getDue_date(),font3));
				pcell.setPadding(3);
				pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				mainledger.addCell(pcell);
				
				mainledger.setHeaderRows(3);
				
			}
			
			///////////////main ledger ends////////////////
			
			
			
			document.add(mainledger);
			
			

			
			
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(), fileName);
			document = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<CustomerLedgerDTO> getCustomerLedger(String customer_id) {
		String fromYearMonth="";
		String toYearMonth="";
		String from_date_to_date_clause="";
		
		if(from_date!=null&&to_date!=null){
			String [] from_date_split = this.from_date.split("-");
			String [] to_date_split = this.to_date.split("-");	
			
			if(from_date_split[1].length()<2){
				from_date_split[1]="0"+from_date_split[1];
			}			
			if(to_date_split[1].length()<2){
				to_date_split[1]="0"+to_date_split[1];
			}
			fromYearMonth= from_date_split[2]+from_date_split[1];
			toYearMonth= to_date_split[2]+to_date_split[1];
			
			from_date_to_date_clause= " and bill_year||LPAD(bill_month, 2, '0') Between "+fromYearMonth+" and "+toYearMonth;
		}		
		
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
					"                 AND bm.CUSTOMER_ID = ?   " +from_date_to_date_clause+
					" )    ORDER BY BILL_ID " ;

			
		} else {
			

			sql = "SELECT *  "
					+ "    FROM (SELECT bnm.BILL_ID,  "
					+ "                 bnm.CUSTOMER_ID,                   "
					+ "                getBankBranch(BRANCH_ID) BANK_NAME,   "
					+ "                 TO_CHAR (bnm.COLLECTION_DATE) COLLECTION_DATE,  "
					+ "                 MON || ', ' || BILL_YEAR DESCRIPTION,  "
					+ "                 TOTAL_CONSUMPTION BILLED_CONSUMPTION,  "
					+ "                 BILLED_AMOUNT,  "
					+ "                 NULL METER_RENT,  "
					+ "                 NULL CMS_RENT,  "
					+ "                 ACTUAL_SURCHARGE SURCHARGE_AMOUNT,  "
					+ "                 ACTUAL_PAYABLE_AMOUNT PAYABLE_AMOUNT,  "
					+ "                 COLLECTED_SURCHARGE, COLLECTED_PAYABLE_AMOUNT COLLECTED_AMOUNT,  "
					+ "                 TO_CHAR (DUE_DATE, 'dd-mm-rrrr') DUE_DATE  "
					+ "            FROM bill_non_metered bnm, MST_MONTH mm  "
					+ "           WHERE BNM.BILL_MONTH = MM.M_ID AND bnm.CUSTOMER_ID = ? "+from_date_to_date_clause
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
	public ServletContext getServlet() {
		return servlet;
	}

	public void setServlet(ServletContext servlet) {
		this.servlet = servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}

	public String getBill_id() {
		return bill_id;
	}

	public void setBill_id(String bill_id) {
		this.bill_id = bill_id;
	}

	public String getCustomer_category() {
		return customer_category;
	}

	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getBill_month() {
		return bill_month;
	}

	public void setBill_month(String bill_month) {
		this.bill_month = bill_month;
	}

	public String getBill_year() {
		return bill_year;
	}

	public void setBill_year(String bill_year) {
		this.bill_year = bill_year;
	}

	public String getDownload_type() {
		return download_type;
	}

	public void setDownload_type(String download_type) {
		this.download_type = download_type;
	}

	public String getBill_for() {
		return bill_for;
	}

	public void setBill_for(String bill_for) {
		this.bill_for = bill_for;
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public static int getTextDiff() {
		return textDiff;
	}

	public static void setTextDiff(int textDiff) {
		LedgerPrinting.textDiff = textDiff;
	}

	public boolean isWater_mark() {
		return water_mark;
	}

	public void setWater_mark(boolean water_mark) {
		this.water_mark = water_mark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
	
}
