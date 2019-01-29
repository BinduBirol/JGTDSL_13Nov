package org.jgtdsl.reports;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.actions.BaseAction;
import org.jgtdsl.dto.CustomerListDTO;
import org.jgtdsl.dto.TransactionDTO;
import org.jgtdsl.dto.UserDTO;
import org.jgtdsl.enums.Area;
import org.jgtdsl.enums.Month;
import org.jgtdsl.utils.connection.ConnectionManager;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class MixedCustomerInfoReport extends BaseAction {
	private static final long serialVersionUID = 1L;
	public  ServletContext servlet;
	private  String report_for; 
	
	public String getReport_for() {
		return report_for;
	}


	public void setReport_for(String report_for) {
		this.report_for = report_for;
	}

	Connection conn = ConnectionManager.getConnection();
	CustomerListDTO customerListDTO = null;
	ArrayList<CustomerListDTO> MixedCustomerListInfo=new ArrayList<CustomerListDTO>();


    
	    static Font fonth = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
		static Font font1 = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
		static Font font1nb = new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL);
		static Font font3 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD);
		static Font font2 = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
		static DecimalFormat  taka_format = new DecimalFormat("#,##,##,##,##,##0.00");
		static DecimalFormat consumption_format = new DecimalFormat("##########0.000");
		UserDTO loggedInUser=(UserDTO) ServletActionContext.getRequest().getSession().getAttribute("user");
		
	public String execute() throws Exception
	{
				
		String fileName="MixedCustomerList.pdf";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		//document.setMargins(20,20,50,80);
		PdfPCell pcell=null;
		
		
		try{
			
			ReportFormat eEvent = new ReportFormat(getServletContext());
			
			PdfWriter.getInstance(document, baos).setPageEvent(eEvent);
			
			document.open();
			
			PdfPTable headerTable = new PdfPTable(3);
		   
				
			headerTable.setWidths(new float[] {
				5,190,5
			});

			
			pcell= new PdfPCell(new Paragraph(""));
			pcell.setBorder(0);
			headerTable.addCell(pcell);
			
			
			String realPath = servlet.getRealPath("/resources/images/logo/JG.png"); 	// image path
			Image img = Image.getInstance(realPath);
						
            	//img.scaleToFit(10f, 200f);
            	//img.scalePercent(200f);
            img.scaleAbsolute(28f, 31f);
            img.setAbsolutePosition(120f, 770f);		
            	//img.setAbsolutePosition(290f, 540f);		// rotate
            
	        document.add(img);
			
			
			PdfPTable mTable=new PdfPTable(1);
			//mTable.setWidthPercentage(90);
			//mTable.setWidths(new float[]{100});
			pcell=new PdfPCell(new Paragraph("JALALABAD GAS T & D SYSTEM LIMITED",fonth));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);	
			mTable.addCell(pcell);
			
			pcell=new PdfPCell(new Paragraph("(A COMPANY OF PETROBANGLA)", font3));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setBorder(0);
			mTable.addCell(pcell);

			Chunk chunk1 = new Chunk("REGIONAL OFFICE : ",font2);
			Chunk chunk2 = new Chunk(String.valueOf(Area.values()[Integer.valueOf(loggedInUser.getArea_id())-1]),font3);
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
			

			generatePdfMixedustomer(document);
	
			document.close();		
			document.close();
			ReportUtil rptUtil = new ReportUtil();
			rptUtil.downloadPdf(baos, getResponse(),fileName);
			document=null;
			
		    
		}catch(Exception e){e.printStackTrace();}
		
		return null;		
	}
	
	private void generatePdfMixedustomer(Document document) throws DocumentException
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
		
		String reportSign="Mixed customer Info ";
		
		if (report_for.equalsIgnoreCase("conn")){
			reportSign+="for Connected Customers ";
		}else if(report_for.equalsIgnoreCase("disconn")){
			reportSign+="for Disonnected Customers";
		}
		
		pcell=new PdfPCell(new Paragraph(reportSign,ReportUtil.f11B));
		pcell.setMinimumHeight(18f);
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph(""));
		pcell.setBorder(0);
		headLinetable.addCell(pcell);
		
		document.add(headLinetable);
		
		
		
		PdfPTable pdfPTable = new PdfPTable(6);
		
		pdfPTable.setWidthPercentage(100);
		
		pdfPTable.setWidths(new float[] {10,15,30,30,30,15});
		
		pcell = new PdfPCell(new Paragraph(" ",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setPadding(3);
		pcell.setBorder(0);
		pcell.setColspan(6);
		pdfPTable.addCell(pcell);
		
		
		pcell = new PdfPCell(new Paragraph("Sl.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Customer.ID ",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setPadding(3);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Full Name ",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Address ",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setPadding(3);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Meter/Burner ",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setPadding(3);
		pdfPTable.addCell(pcell);
		
		pcell = new PdfPCell(new Paragraph("Parent Cust.",ReportUtil.f8B));
		pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pcell.setPadding(3);
		pdfPTable.addCell(pcell);

		MixedCustomerListInfo = getMixedCustomerList();
		int listSize=MixedCustomerListInfo.size();

		for (int i = 0; i < listSize; i++) {
			
			
			pcell = new PdfPCell(new Paragraph(String.valueOf(i+1),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setPadding(3);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(MixedCustomerListInfo.get(i).getCustomerId(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setPadding(3);
			pdfPTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(MixedCustomerListInfo.get(i).getCustomerName(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setPadding(3);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(MixedCustomerListInfo.get(i).getCustomerAddress(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setPadding(3);
			pdfPTable.addCell(pcell);
			
			
			pcell = new PdfPCell(new Paragraph(MixedCustomerListInfo.get(i).getBurner_or_minmaxload(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pcell);
			
			pcell = new PdfPCell(new Paragraph(MixedCustomerListInfo.get(i).getParent_connection(),ReportUtil.f8));
			pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
			pcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pcell.setPadding(3);
			pdfPTable.addCell(pcell);
			
	}
		
	document.add(pdfPTable);
		
	}
	
	
	
	private ArrayList<CustomerListDTO> getMixedCustomerList(){
		String area=loggedInUser.getArea_id();
		
		ArrayList<CustomerListDTO> MixedCustomerList= new ArrayList<CustomerListDTO>();
		try {
			
			String queryExtn="";
			
			if (report_for.equalsIgnoreCase("conn")){
				queryExtn=" and mci.status=1 ";
			}else if(report_for.equalsIgnoreCase("disconn")){
				queryExtn=" and mci.status=0 ";
			}
			
			String MixedCustomer_info_sql=" SELECT mci.*, getburner(mci.customer_id) burner "+
											" FROM MVIEW_CUSTOMER_INFO mci"+
											" WHERE PARENT_CONNECTION is not null AND" +
											" AREA_ID = '"+area+"' "+queryExtn + " ORDER BY mci.CUSTOMER_ID";
											
											
											
			PreparedStatement stmt=conn.prepareStatement(MixedCustomer_info_sql);
			ResultSet r=stmt.executeQuery();
			while(r.next()){
				customerListDTO=new CustomerListDTO();
				customerListDTO.setCustomerId(r.getString("CUSTOMER_ID"));
				customerListDTO.setCustomerName(r.getString("FULL_NAME"));
				customerListDTO.setCustomerAddress(r.getString("ADDRESS"));
				
				String min_max_load= "Min: "+r.getString("MIN_LOAD")+"\nMax: "+r.getString("MAX_LOAD");
				
				String[] burner_split= r.getString("BURNER").split("#");
				
				String burner_info= "Single: "+burner_split[0]+"\nDouble:"+burner_split[1];
				
				if(r.getInt("ISMETERED")==0){
					customerListDTO.setBurner_or_minmaxload(burner_info);
				}else{
					customerListDTO.setBurner_or_minmaxload(min_max_load);
				}				
				customerListDTO.setParent_connection(r.getString("PARENT_CONNECTION"));
        		MixedCustomerList.add(customerListDTO);
				
			}
	
		}catch(SQLException e){
			e.printStackTrace();
			
		}
		
		return MixedCustomerList;
	}
	
	

	
	public ServletContext getServlet() {
		return servlet;
	}

	public void setServletContext(ServletContext servlet) {
		this.servlet = servlet;
	}
}
