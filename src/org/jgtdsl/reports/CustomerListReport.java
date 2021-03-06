package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CustomerListDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.reports.ReportFormat;
import org.jgtdsl.reports.ReportUtil;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;




public class CustomerListReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	ArrayList<CustomerListDTO> customerListInfo=new ArrayList<CustomerListDTO>();
	CustomerListDTO customerListDTO = new CustomerListDTO();
	public  ServletContext servlet;
	Connection conn = ConnectionManager.getConnection();
	
	    private  String from_date;
	    private  String report_for; 
	    private  String area;
	    private  String customer_type;
	    private  String customer_category;
	    private  String customer_status;
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		static Font font3 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");	
	public String execute() throws Exception
	{
				
		String fileName="Customer_List.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4.rotate());
		document.setMargins(20,20,30,72);
		PdfPCell pcell=null;
		
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			//MeterReadingDTO meterReadingDTO = new MeterReadingDTO();
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
		   
				
			headerTable.setWidths(new float[] {
				5,190,5
			});
			
			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			
			String realPath = servlet.getRealPath("/resources/images/logo/JG.png");  // image path
			   Image img = Image.getInstance(realPath);
			      
			             //img.scaleToFit(10f, 200f);
			             //img.scalePercent(200f);
			            img.scaleAbsolute(32f, 35f);
			            //img.setAbsolutePosition(145f, 780f);  
			            img.setAbsolutePosition(155f, 530f);  // rotate
			            
			         document.add(img);
			
			
			PdfPTable mTable=new PdfPTable(1);
			mTable.setWidths(new float[]{100});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS TRANSMISSION AND DISTRIBUTION SYSTEM LIMITED"));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A company of PetroBangla)", ReportUtil.f8B));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			
				Chunk chunk1 = new Chunk("Regional Office :",ReportUtil.f8B);
				Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),ReportUtil.f8B);
				Paragraph p = new Paragraph(); 
				p.add(chunk1);
				p.add(chunk2);
				pcell=new PdfPCell(p);
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setBorder(0);
				mTable.addCell(pcell);
						
				pcell=new PdfPCell(mTable);
				pcell.setBorder(0);
				headerTable.addCell(pcell);
						
				pcell = new PdfPCell(new Paragraph(""));
				pcell.setBorder(0);
				headerTable.addCell(pcell);
				document.add(headerTable);
			
		
			
			
			if(this.report_for.equalsIgnoreCase("category_wise")&&!this.customer_category.equalsIgnoreCase("01")&&!this.customer_category.equalsIgnoreCase("09")){
				generatePdfMeterArea_wise(document);
			}
			else if(this.report_for.equalsIgnoreCase("area_wise")&&this.customer_type.equalsIgnoreCase("1")){
				generatePdfMeterArea_wise(document);
			}else if(this.report_for.equalsIgnoreCase("zone")){
				if(customer_type.equalsIgnoreCase("1")){generatePdfMeterZone_wise(document);}
				else{generatePdfNonMeterZone_wise(document);};
				
			}
			
			
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;
		
	}

	
	
	
	
	
	private void generatePdfMeterArea_wise(Document document) throws DocumentException
	{
		document.setMargins(20,20,30,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		String connection_satus="for connected Customers";
		
		if(report_for.equalsIgnoreCase("disconnected"))
			connection_satus= " for disconnected Customers"; 
		
		
		pcell=new PdfPCell(new Paragraph("Customer List "+connection_satus ,ReportUtil.f8B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
	
		
		document.add(headLinetable);
		
		
		
		PdfPTable pdfPTable = new PdfPTable(12);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{8,20,23,25,12,12,10,8,15,15,15,15});
		
		
		
		
		///////Balance Forward///////////////////
		customerListInfo = getMeterCustomerList();
		int listSize=customerListInfo.size();
		
		double subBalance=0.0;
		double totalBalance=0.0;		
		String previousCategory="";
		String currentCategory="";
		int serial=1;
		
		for (int i = 0; i < listSize; i++) {
			currentCategory= customerListInfo.get(i).getCategory();
			if(!currentCategory.equals(previousCategory)){
				serial=1;
				
				pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCategoryName(),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setPadding(10);
				pcell.setBorder(0);
				pcell.setColspan(12);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Sr. No.",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cus. ID",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Address",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Min Load",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Max Load",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Meter Type",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("PF",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Meter Number",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Meter Installation Date",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Mobile no.",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Remarks",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
			
				
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(serial),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerId(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerName(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerAddress(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf( customerListInfo.get(i).getMin_load()),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf (customerListInfo.get(i).getMaxLoad()),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getMeter_info(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf( customerListInfo.get(i).getPressure()),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getM_no(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getM_inst_date(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getMobile(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			previousCategory= customerListInfo.get(i).getCategory();
			serial++;

		}
		//pdfPTable.setHeaderRows(2);
		document.add(pdfPTable);
		
		
	}
	
	
	private void generatePdfMeterZone_wise(Document document) throws DocumentException
	{
		document.setMargins(20,20,30,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		String connection_satus="for connected Customers";
		
		if(report_for.equalsIgnoreCase("disconnected"))
			connection_satus= " for disconnected Customers"; 
		
		
		pcell=new PdfPCell(new Paragraph("Customer List "+connection_satus ,ReportUtil.f8B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
	
		
		document.add(headLinetable);
		
		
		
		PdfPTable pdfPTable = new PdfPTable(12);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{8,20,23,25,12,12,10,8,15,15,15,15});
		
		
		
		
		///////Balance Forward///////////////////
		customerListInfo = getMeterCustomerListZone();
		int listSize=customerListInfo.size();
		
		double subBalance=0.0;
		double totalBalance=0.0;		
		String previousCategory="";
		String currentCategory="";
		int serial=1;
		
		for (int i = 0; i < listSize; i++) {
			currentCategory= customerListInfo.get(i).getCategory();
			if(!currentCategory.equals(previousCategory)){
				serial=1;
				
				pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCategoryName(),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setPadding(10);
				pcell.setBorder(0);
				pcell.setColspan(12);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Sr. No.",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cus. ID",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Address",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Min Load",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Max Load",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Meter Type",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("PF",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Meter Number",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Meter Installation Date",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Mobile no.",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Remarks",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
			
				
			}
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(serial),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerId(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerName(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerAddress(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf( customerListInfo.get(i).getMin_load()),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf (customerListInfo.get(i).getMaxLoad()),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getMeter_info(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(String.valueOf( customerListInfo.get(i).getPressure()),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getM_no(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getM_inst_date(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getMobile(),ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph("",ReportUtil.f7));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setPadding(5);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			previousCategory= customerListInfo.get(i).getCategory();
			serial++;

		}
		//pdfPTable.setHeaderRows(2);
		document.add(pdfPTable);
		
		
	}
	
	private void generatePdfNonMeterZone_wise(Document document) throws DocumentException
	{
		document.setMargins(20,20,30,72);
		PdfPTable headLinetable = null;
		PdfPCell pcell=null;
		headLinetable = new PdfPTable(3);
		headLinetable.setWidthPercentage(100);
		headLinetable.setWidths(new float[]{10,80,10});
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		String connection_satus="for connected Customers";
		int total_s_burner=0;
		int total_d_burner=0;
		String previousCategory="";
		String currentCategory="";
		int serial=1;
		
		if(report_for.equalsIgnoreCase("disconnected"))
			connection_satus= " for disconnected Customers"; 
		
		
		pcell=new PdfPCell(new Paragraph("Customer List "+connection_satus ,ReportUtil.f8B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("",ReportUtil.f8));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
	
		
		document.add(headLinetable);
		
		
		
		PdfPTable pdfPTable = new PdfPTable(8);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{8,20,23,25,12,12,15,15});
		
		
		
		
		///////Balance Forward///////////////////
		customerListInfo = getNonMeterCustomerListZone();
		int listSize=customerListInfo.size();
		int f=0;
		
		for (int i = 0; i < listSize; i++) {
			currentCategory= customerListInfo.get(i).getCategory();
			
			if(!currentCategory.equals(previousCategory)){
				serial=1;
				if(f!=0){
					pcell = new PdfPCell(new Paragraph(String.valueOf("Total double burner: "+total_d_burner+" & Total single burner: "+total_s_burner),ReportUtil.f8B));
					pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);				
					pcell.setColspan(8);
					pdfPTable.addCell(pcell);
				}
				f++;
				
				pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCategoryName(),ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setPadding(10);
				pcell.setBorder(0);
				pcell.setColspan(8);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Sr. No.",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Cus. ID",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Name",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Customer Address",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Single B.",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Double B",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				
				
				pcell = new PdfPCell(new Paragraph("Mobile no.",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("Remarks",ReportUtil.f8B));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pdfPTable.addCell(pcell);
				
				total_s_burner=0;
				total_d_burner=0;
			}				
			
				pcell = new PdfPCell(new Paragraph(String.valueOf(serial),ReportUtil.f7));
				pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerId(),ReportUtil.f7));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerName(),ReportUtil.f7));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pdfPTable.addCell(pcell);
				
				
				pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getCustomerAddress(),ReportUtil.f7));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(String.valueOf( customerListInfo.get(i).getSingle_burners()),ReportUtil.f7));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph(String.valueOf (customerListInfo.get(i).getDouble_burners()),ReportUtil.f7));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pdfPTable.addCell(pcell);
				
				
				
				pcell = new PdfPCell(new Paragraph(customerListInfo.get(i).getMobile(),ReportUtil.f7));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pdfPTable.addCell(pcell);
				
				pcell = new PdfPCell(new Paragraph("",ReportUtil.f7));
				pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
				pcell.setPadding(5);
				pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				pdfPTable.addCell(pcell);				
				
				serial++;
				previousCategory= customerListInfo.get(i).getCategory();
				
				total_s_burner+=customerListInfo.get(i).getSingle_burners();
				total_d_burner+=customerListInfo.get(i).getDouble_burners();
		}
		pcell = new PdfPCell(new Paragraph(String.valueOf("Total double burner: "+total_d_burner+" & Total single burner: "+total_s_burner),ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_RIGHT);				
		pcell.setColspan(8);
		pdfPTable.addCell(pcell);
		//pdfPTable.setHeaderRows(2);
		document.add(pdfPTable);
		
		
	}
	
	
	private ArrayList<CustomerListDTO> getMeterCustomerList()
	{
		ArrayList<CustomerListDTO> customerList= new ArrayList<CustomerListDTO>();
		
		String whereClause= " AND CM.STATUS = 1 AND MCI.STATUS = 1 ";		
		String area=loggedInUser.getArea_id();
		
		
		if(report_for.equalsIgnoreCase("disconnected")) whereClause= " AND MCI.STATUS = 0 ";
		if(report_for.equalsIgnoreCase("category_wise")) whereClause= "AND MCI.STATUS = 1 AND MCI.CATEGORY_ID= "+this.customer_category;
		try {
		
			String customer_info_sql=" SELECT MCI.CUSTOMER_ID, " 
					+ "       MCI.CATEGORY_ID, "
					+ "       MCI.CATEGORY_NAME, "
					+ "       MCI.FULL_NAME, "
					+ "       MCI.ADDRESS, "
					+ "       MCI.MIN_LOAD, "
					+ "       MCI.MAX_LOAD, "
					+ "       CM.METER_SL_NO, "
					+ "       MT.TYPE_NAME, "
					+ "       to_char(cm.INSTALLED_DATE) INSTALLED_DATE, "
					+ "       MCI.MOBILE, "
					+ "       CM.PRESSURE "
					+ "  FROM mview_customer_info mci, CUSTOMER_METER cm, MST_METER_TYPE mt "
					+ " WHERE     MCI.CUSTOMER_ID = CM.CUSTOMER_ID "
					+ "       AND MT.TYPE_ID = CM.METER_TYPE "
					+ "       AND area_id = '"+ area +"' "					
					+ "       AND MCI.ISMETERED = 1 "
					+ whereClause
					+ "       order by customer_id " ;




			
			PreparedStatement ps1=conn.prepareStatement(customer_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		customerListDTO=new CustomerListDTO();
        		
        		customerListDTO.setCustomerId(resultSet.getString("CUSTOMER_ID"));
        		customerListDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		customerListDTO.setCustomerAddress(resultSet.getString("ADDRESS"));
        		customerListDTO.setMin_load(resultSet.getDouble("MIN_LOAD"));
        		customerListDTO.setMaxLoad(resultSet.getDouble("MAX_LOAD"));
        		customerListDTO.setMeter_info(resultSet.getString("TYPE_NAME"));
        		customerListDTO.setPressure(resultSet.getDouble("PRESSURE"));
        		customerListDTO.setM_no(resultSet.getString("METER_SL_NO"));
        		customerListDTO.setM_inst_date(resultSet.getString("INSTALLED_DATE"));
        		customerListDTO.setMobile(resultSet.getString("MOBILE"));
        		customerListDTO.setCategory(resultSet.getString("CATEGORY_ID"));
        		customerListDTO.setCategoryName(resultSet.getString("CATEGORY_NAME"));
        		
        		

        		
        		
        		customerList.add(customerListDTO);
        		
        		
        	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return customerList;
	}
	
	
	private ArrayList<CustomerListDTO> getMeterCustomerListZone()
	{
		ArrayList<CustomerListDTO> customerList= new ArrayList<CustomerListDTO>();
		
		String whereClause= " AND CM.STATUS = 1 AND MCI.STATUS = 1 ";		
		String area=loggedInUser.getArea_id();
		
		
		if(customer_type.equalsIgnoreCase("01")) whereClause= " AND MCI.STATUS = 0 ";
		
		try {
		
			String customer_info_sql= 
					
					"SELECT MCI.CUSTOMER_ID, "
					+ "         MCI.CATEGORY_ID, "
					+ "         MCI.CATEGORY_NAME, "
					+ "         MCI.FULL_NAME, "
					+ "         MCI.ADDRESS, "
					+ "         MCI.MIN_LOAD, "
					+ "         MCI.MAX_LOAD, "
					+ "         CM.METER_SL_NO, "
					+ "         MT.TYPE_NAME, "
					+ "         TO_CHAR (cm.INSTALLED_DATE) INSTALLED_DATE, "
					+ "         MCI.MOBILE, "
					+ "         CM.PRESSURE, "
					+ "         MZ.ZONE_NAME, MZ.ZONE_ID "
					+ "    FROM mview_customer_info mci, "
					+ "         CUSTOMER_METER cm, "
					+ "         MST_METER_TYPE mt, "
					+ "         MST_ZONE mz "
					+ "   WHERE     MCI.CUSTOMER_ID = CM.CUSTOMER_ID "
					+ "         AND MCI.AREA_ID = MZ.AREA_ID "
					+ "         AND MCI.ZONE = MZ.ZONE_ID "
					+ "         AND MT.TYPE_ID = CM.METER_TYPE "
					+ "         AND mci.area_id = '"+area+"' "
					+ "         AND MCI.ISMETERED =  "+customer_type
					+ "         AND CM.STATUS = 1 "
					+ "         AND MCI.STATUS = 1 "
					+ "ORDER BY ZONE_ID";




			
			PreparedStatement ps1=conn.prepareStatement(customer_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		customerListDTO=new CustomerListDTO();
        		
        		customerListDTO.setCustomerId(resultSet.getString("CUSTOMER_ID"));
        		customerListDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		customerListDTO.setCustomerAddress(resultSet.getString("ADDRESS"));
        		customerListDTO.setMin_load(resultSet.getDouble("MIN_LOAD"));
        		customerListDTO.setMaxLoad(resultSet.getDouble("MAX_LOAD"));
        		customerListDTO.setMeter_info(resultSet.getString("TYPE_NAME"));
        		customerListDTO.setPressure(resultSet.getDouble("PRESSURE"));
        		customerListDTO.setM_no(resultSet.getString("METER_SL_NO"));
        		customerListDTO.setM_inst_date(resultSet.getString("INSTALLED_DATE"));
        		customerListDTO.setMobile(resultSet.getString("MOBILE"));
        		customerListDTO.setCategory(resultSet.getString("ZONE_ID"));
        		customerListDTO.setCategoryName(resultSet.getString("ZONE_NAME"));
        		
        		

        		
        		
        		customerList.add(customerListDTO);
        		
        		
        	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return customerList;
	}
	
	
	private ArrayList<CustomerListDTO> getNonMeterCustomerListZone()
	{
		ArrayList<CustomerListDTO> customerList= new ArrayList<CustomerListDTO>();
		
		String whereClause= " AND CM.STATUS = 1 AND MCI.STATUS = 1 ";		
		String area=loggedInUser.getArea_id();
		
		
		if(customer_type.equalsIgnoreCase("01")) whereClause= " AND MCI.STATUS = 0 ";
		
		try {
		
			String customer_info_sql= 
					
					"SELECT MCI.CUSTOMER_ID, "
							+ "       MCI.CATEGORY_ID, "
							+ "       MCI.CATEGORY_NAME, "
							+ "       MCI.FULL_NAME, "
							+ "       MCI.ADDRESS, "
							+ "       MCI.MOBILE, "
							+ "       MZ.ZONE_NAME, "
							+ "       MZ.ZONE_ID, "
							+ "       getBurner(MCI.CUSTOMER_ID) BURNER "
							+ "  FROM mview_customer_info mci, MST_ZONE mz "
							+ " WHERE     MCI.ISMETERED = 0 "
							+ " and MCI.AREA_ID='"+area+"' "
							+ "        "
							+ "       AND MCI.AREA_ID = MZ.AREA_ID "
							+ "       AND MCI.ZONE = MZ.ZONE_ID "
							+ " order by ZONE ";




			
			PreparedStatement ps1=conn.prepareStatement(customer_info_sql);
			
        	ResultSet resultSet=ps1.executeQuery();
        	
        	
        	while(resultSet.next())
        	{
        		 
        		customerListDTO=new CustomerListDTO();
        		
        		customerListDTO.setCustomerId(resultSet.getString("CUSTOMER_ID"));
        		customerListDTO.setCustomerName(resultSet.getString("FULL_NAME"));
        		customerListDTO.setCustomerAddress(resultSet.getString("ADDRESS"));        		
        		customerListDTO.setMobile(resultSet.getString("MOBILE"));
        		customerListDTO.setCategory(resultSet.getString("ZONE_ID"));
        		customerListDTO.setCategoryName(resultSet.getString("ZONE_NAME"));
        		
        		String burner = resultSet.getString("BURNER");
				String[] brnrArray = burner.split("#");
				customerListDTO.setSingle_burners(Integer.parseInt(brnrArray[0]));				
				customerListDTO.setDouble_burners(Integer.parseInt(brnrArray[1]));       		
        		
        		customerList.add(customerListDTO);
        		
        		
        	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return customerList;
	}
	

	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}

	

	
	



	public String getCustomer_category() {
		return customer_category;
	}




	public void setCustomer_category(String customer_category) {
		this.customer_category = customer_category;
	}




	public String getFrom_date() {
		return from_date;
	}




	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}




	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}




	public String getCustomer_type() {
		return customer_type;
	}




	public void setCustomer_type(String customer_type) {
		this.customer_type = customer_type;
	}




	public String getCustomer_status() {
		return customer_status;
	}




	public void setCustomer_status(String customer_status) {
		this.customer_status = customer_status;
	}

	public ServletContext getServlet() {
		return servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}
	
  }


