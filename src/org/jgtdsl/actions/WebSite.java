package org.jgtdsl.actions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.struts2.ServletActionContext;
import org.jgtdsl.dto.ClearnessDTO;
import org.jgtdsl.dto.CollectionDTO;
import org.jgtdsl.dto.CustomerApplianceDTO;
import org.jgtdsl.dto.CustomerDTO;
import org.jgtdsl.enums.Month;
import org.jgtdsl.models.BillingService;
import org.jgtdsl.models.CustomerService;
import org.jgtdsl.models.MeterService;
import org.jgtdsl.reports.DefaulterCertificate;


public class WebSite extends BaseAction{
	
	private static final long serialVersionUID = 1793004616091761918L;
	private String customer_id;
	private String bill_month;
	private String bill_year;
	private String area_name;
	

	
	
	
	static DecimalFormat taka_format = new DecimalFormat("#,##,##,##,##,##0.0");
	
	public String getCustomerBillInfo()
	{

		CustomerService customerService=new CustomerService();
		CustomerDTO customer=customerService.getCustomerInfo(customer_id);
		MeterService ms= new MeterService();
		ArrayList<CustomerApplianceDTO> appliance= ms.getCustomerApplianceList(customer_id);
		
		BillingService billingService=new BillingService();
		DefaulterCertificate defaulterCertificate=new DefaulterCertificate();
		 
		String responseHtml="<style type=text/css>" +
			"body {"+
			    //"background-image: url('./resources/images/logo/JG.png');"+
			   // "background-repeat: x repeat;" +
			    //"background-size: 20px;"+
			"}"+
			    
			"#rounded-corner"+
			"{"+
				"font-family: 'Lucida Sans Unicode', 'Lucida Grande', Sans-Serif;"+
				"font-size: 18px;"+
				"margin-top: 10px;margin-right: 10px;margin-bottom: 0px;margin-left: 0px;"+
				"text-align: left;"+
				"border-collapse: collapse;"+
			"}"+
	
			"#rounded-corner th"+
			"{"+
				"padding: 8px;"+
				"font-weight: normal;"+
				"font-size: 13px;"+
				"color: #039;"+
				//"background: #b9c9fe;"+
			"}"+
				
			"#rounded-corner td"+
			"{"+
				"padding: 8px;"+
				//"background: #e8edff;"+
				"border: 1px solid #d9f2d9;"+
				"color: #000;"+
			"}"+

			
			"#rounded-corner tbody tr:nth-child(odd){background-color: #ddd;}"+
			"#rounded-corner tbody tr:hover {background-color: #ddd;}"+
			
			".peymentMethods, #flip {"+
			    "text-align: center;"+
			"}"+

			".peymentMethods " +
			"{"+
			    "display: none;"+
			"}" +
			    
			"#allInfo" +
			"{" +
				"background: #fff;" +
				"width: 1020px;" +
				"margin-left: 150px;" +
				"margin-top: 50px;" +
			"}" +
			"#div__1" +
			"{" +
				"display:none;" +
				"padding-left: 10px;" +
			"}" +
			"#div__2" +
			"{" +
				"display:none;" +
				"padding-left: 10px;" +
			"}" +
			"#div__3" +
			"{" +
				"padding-left: 10px;" +
			"}" +
			"#inputradio" +
			"{" +
				"padding-left: 210px;"+
				"padding-top: 10px;" +
				"font-size: 23px;" +
			"}" +
			
			
		"</style>";
		
		if(customer==null ){
			responseHtml+="<body>" +
							"<div id='allInfo'>"+
								"<table width='1020px' id='rounded-corner'>"+
									"<tr>"+
									   "<td align=center style='color:#ff5c33;height=100px;font-size:35px'>Customer ID is not valid</td>"+
									"</tr>"+
								"</table>" +
							"</div>"+
						"</body>";
		}
		else{
//			CollectionDTO billInfo=billingService.getBillInfo(bill_month,bill_year,customer_id);
			ArrayList<CollectionDTO> paidBill=new ArrayList<CollectionDTO>();
			ArrayList<ClearnessDTO> dueInfo= new ArrayList<ClearnessDTO>();
			paidBill=billingService.getPaidBillInfo(customer_id);
			dueInfo=defaulterCertificate.getDueMonthWeb(customer_id);
			int paidSize=paidBill.size();
			int dueSize=dueInfo.size();

							
				responseHtml +="<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>"+
						"<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css'>"+
						"<script type='text/javascript'> " +
						" function showHideDivs(divId){ " +
						"  document.getElementById('div__1').style.display='none'; " +
						"  document.getElementById('div__2').style.display='none'; " +
						" document.getElementById('div__3').style.display='none'; " +
						"  document.getElementById('div__'+divId).style.display='block';} " +
						" </script>";
				
				
				
				
			
	    responseHtml+="<body>" +
	    				"<div id='allInfo'>" +
	    				"<div id='inputradio'>" +
		    				"<input type='radio'  style='' name='selectionType' value='gbStatus'  onclick='showHideDivs(1)' />Customer Information &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" +
		    				"<input type='radio' name='selectionType' value='gbInfo' onclick='showHideDivs(2)' /> Paid Info &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp" +
		    				"<input type='radio' name='selectionType' value='pPatra' checked='checked' onclick='showHideDivs(3)' /> Outstanding Bills " +
		    			"</div>";
	    		
			
		responseHtml+="<div id='div__1' style=''><table width='1000px' id='rounded-corner'>"+
						"<tbody>"+
						   
								"<tr>"+
								   "<td align=center colspan=7 style='color:black;font-weight:bold;font-size: 24px'>Customer Information</td>"+
								"</tr>"+	
								"<tr>"+
								   "<td width='300px;' align=left colspan=3>Customer Code : </td>"+
								   "<td width='400px;' style='color:blue' align=left colspan=4><b>"+customer.getCustomer_id()+"</b></td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=left colspan=3>Customer Name : </td>"+
								   "<td align=left colspan=4>"+customer.getPersonalInfo().getFull_name()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=left colspan=3>Address : </td>"+
								   "<td align=left colspan=4>"+customer.getAddress()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=left colspan=3>Telephone Number : </td>"+
								   "<td align=left colspan=4>"+customer.getPersonalInfo().getPhone()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=left colspan=3>Category : </td>"+
								// "<td colspan=4>"+customer.getCustomer_category_name().substring(0,customer.getCustomer_category_name().length()-6)+"</td>"+
								   "<td align=left colspan=4>"+customer.getCustomer_category_name()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=left colspan=3>Customer Type : </td>"+
								// "<td colspan=4>"+customer.getCustomer_category_name().substring(customer.getCustomer_category_name().length()-6)+"</td>"+
								   "<td align=left colspan=4>"+customer.getCustomer_category_name()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=left colspan=3>Connection Date : </td>"+
								   "<td align=left colspan=4>"+customer.getConnectionInfo().getConnection_date()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=left colspan=3>Minimum Monthy Load : </td>"+
								   "<td align=left colspan=4 style='color:green;'>"+customer.getConnectionInfo().getMin_load()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=left colspan=3>Maximum Monthy Load : </td>"+
								   "<td align=left colspan=4 style='color:green;'>"+customer.getConnectionInfo().getMax_load()+"</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=left colspan=3>Metered Status : </td>"+
								   "<td align=left colspan=4>"+customer.getConnectionInfo().getIsMetered_name()+"</td>"+
								"</tr>";
		
		if(!customer.getConnectionInfo().getIsMetered_name().equalsIgnoreCase("Metered")){				
				
				
				for(CustomerApplianceDTO x:appliance){
					responseHtml+="<tr>"+
							   "<td align=left colspan=3>Number of Burners : </td>"+
							   "<td align=left colspan=4>"+x.getApplianc_name()+" : "+x.getApplianc_qnt()+"</td>"+
							"</tr>";
				}
				
		}
		
				responseHtml+="<tr>"+
								   "<td align=left colspan=3>Regional Office : </td>"+
								   "<td align=left colspan=4>"+customer.getArea_name()+"</td>"+
								"</tr>"+
							"</tbody>" +
						"</table>" +
					"</div>";
								
		responseHtml+="<div id='div__2' style=''><table width='1000px'  id='rounded-corner'>"+
							"<tbody>"+								   
								"<tr>"+
								   "<td align=center colspan=7 style='color:black;font-weight:bold;font-size: 24px'>Paid Info</td>"+
								"</tr>"+								
								  
								"<tr>"+
								   "<td align=left colspan=7 style='color:black;font-weight:bold;'>Last three month's payment details</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=center rowspan=2>Billing Month</td>"+
								   "<td align=center colspan=3>Paid Amount</td>"+
								   "<td align=center rowspan=2 colspan=2>Bank</td>"+
								   "<td align=center rowspan=2>Date of Payment</td>"+
								"</tr>"+
								"<tr>"+
								   "<td align=center>Bill</td>"+
								   "<td align=center>Surcharge</td>"+
								   "<td align=center>Total</td>"+
								"</tr>";				
		
								for (int i = 0; i < paidSize; i++) {
									
									responseHtml+="<tr>"+
									   "<td align=center style='color:blue;font-weight:bold;'>"+Month.values()[Integer.valueOf(paidBill.get(i).getBill_month())-1]+" "+paidBill.get(i).getBill_year()+"</td>"+
									   "<td align=center style='color:blue;font-weight:bold;'>"+taka_format.format(paidBill.get(i).getCollected_amount())+"</td>"+
									   "<td align=center style='color:blue;font-weight:bold;'>"+taka_format.format(paidBill.get(i).getSurcharge_amount())+"</td>"+
									   "<td align=center style='color:blue;font-weight:bold;'>"+taka_format.format(paidBill.get(i).getCollected_amount()+paidBill.get(i).getSurcharge_amount())+"</td>"+
									   "<td align=center colspan=2 style='color:blue;'>"+paidBill.get(i).getBank_id()+", "+paidBill.get(i).getBranch_id()+"</td>"+
									   "<td align=center style='color:blue;font-weight:bold;'>"+paidBill.get(i).getCollection_date()+"</td>"+
									"</tr>";
								}
								
								if(paidSize==0){
									
									responseHtml+="<tr>"+
											   		"<td align=center colspan=7 style='color:green;height=20px;font-size: 35px;'>Paid Information Not Found</td>"+
											   	"</tr>";
								}
								
			   
								responseHtml+="<tr>"+
								   "<td align=center colspan=7 style='color:#ff5c33;height=20px;'>If any anomaly is found, Please contact to your own Regional Office</td>"+
								"</tr>"+
								
								"</tbody></table></div>";			
			
		responseHtml +="<script type='text/javascript'> " +
				
			"var total = 0;"+
				 "function sum(item){"+											
				 "if(item.checked){"+
				 "total+= parseInt(item.value);"+
	        		"}else{"+
	           "total-= parseInt(item.value);"+
	        "}"+
	        //alert(total);
	        "document.getElementById('TotalBill').innerHTML = total + ' /-';"+
	    "}"+
				
		
//	
//		
//		responseHtml +="<script type='text/javascript'> " +
//				
//
//																	// select checkbox and sum value
//				
//				//"var total = 0;"+
//				//"var max=0;"+
//				"var billList = [];"+
//				//"document.getElementsByName('Bill').innerHTML = billList;"+
//				
//				"function test(item){"+											// test function
//					"if(item.checked){"+
//						//"total+= parseInt(item.value);"+
//					
//						"billList.push(item.value);"+
//				    	//"document.getElementById('bill').innerHTML = billList;"+
//						
//					
//					"}"+
//						
//					"else{"+
//						//"total-= parseInt(item.value);"+
//					
//						"billList.pop(item.value);"+
//				    	//"document.getElementById('bill').innerHTML = billList;"+
//					"}"+
//					 "document.getElementById('checkBillId').innerHTML = billList;"+
//				//alert(total);
//					//"document.getElementById('TotalBill').innerHTML = total + ' /-';"+
//				"}"+
//
//				
//																						// select all checkbox		
//				
				"function toggle(item) {"+
					"checkboxes = document.getElementsByName('selectedBills');"+
					"if(item.checked){"+
		
						//"total=0;"+
		
						"for(var i=0, n=checkboxes.length;i<n;i++) {"+
							"checkboxes[i].checked = item.checked;"+
							//total+= parseInt(item.value);
							//"sum(checkboxes[i]);"+
						"}"+ 
					"}"+
					
					"else{"+
						"for(var i=0, n=checkboxes.length;i<n;i++) {"+
							"checkboxes[i].checked = item.checked;"+
							"checkboxes[i].checked = item.checked;"+
							//"sum(checkboxes[i]);"+
							//"total=0;"+
						"}"+
		
			 		"}"+
			 		
					//"document.getElementById('TotalBill').innerHTML = total + ' /-';"+
				"}"+
	
//	// toggle payment method icon 1
//	
////		"$(document).ready(function(){"+
////		    "$('#tog').click(function(){"+
////		        "$('im').toggle(1000);"+
////		    "});"+
////		"});"+
		    

				//toggle payment method icon 2
		
				"$(document).ready(function(){"+
				    "$('#flip').click(function(){"+
				        "$('.peymentMethods').slideToggle('slow');"+
				    "});"+
				"});"+
		
	// selest img as radio 
	
//		"$('.img-ms-form').click(function(){"+
//			  "$(this).next('div.radio').find('input[type='radio']').prop('checked', true);"+
//			"});"+


			// function validate
			
			"function validate(){"+	
			
				"var payment=document.getElementsByName(paymentMethodId);"+
				 
				"if(payment.value==''){"+
				 	"alert('Please Select a Payment Method');"+
				 	"return false;"+
		   		"}"+
			"}"+
/*		   		
			
" function downloadpdf(){ "+
		  	"var d = new Date(),"+
		     " n = d.getMonth(),"+
		      "y = d.getFullYear();"+

		  	"$pdfurl='http://103.48.17.216:8080/JGTDSL_WEB/clearnessCertificateInfo.action?area=19&download_type=individual_wise&customer_id='"+
		  	"+$cust_id+'&report_type=DC&collection_month='+n+'&calender_year='+y+'&officer_name=Mobile App&officer_desig=JGTDSL';"+
		  	"window.open($pdfurl);"+

		  "}"+*/

		    
" </script>";
		
		
		
			
responseHtml+="<div id='div__3' style=''><table width='1000px'  id='rounded-corner'>"+
					"<form method='post' id='frm_confirm_payment' action='getIpgConfirmationPage.action'>"+
			    	"<input type='hidden' name='customerId' value='"+customer_id+"'>"+
					
					"<tbody>"+
					"<tr>"+
					   "<td align=center colspan=7 style='color:black;font-weight:bold;font-size: 24px'>Dues List</td>"+
					"</tr>";
					
					
					
					if(dueSize==0){
						
								/* No Dues bill
								 * */
								
								responseHtml+="<tr>"+
										   			"<td align=center colspan=7 style='color:green;height=20px;font-size: 35px;'>You have no dues.</td>"+
										   	  "</tr>"+
										   	"<tr>"+
								   				"<td align=center colspan=7 style='color:green;height=20px;font-size: 15px;'></td>"+
								   			"</tr>"+
										   "</tbody>" +								   			
									   "</table>";
						
					}else{
						
								
								responseHtml+="<tr>"+
										   		"<td align=center colspan=7  colspan=7 style='color:black;font-weight:bold;'>Due Month's Gas Bill</td>"+
											 "</tr>"+
											"<tr>"+
											   "<td align=center colspan=2 style='color:green;font-weight:bold;'><b>Billing Month</b></td>"+
											   "<td align=center colspan=2 style='color:green;font-weight:bold;'><b>Due Amount</b></td>"+
											   "<td align=center colspan=1 style='color:green;font-weight:bold;'><b>Surcharge</b></td>"+
											   //"<td align=center colspan=2 style='color:green;font-weight:bold;'>Select bill<input type='checkbox' onClick='toggle(this)'/></td>"+
											"</tr> ";

									double totalDueAmount=0.0;
									
									for (int i = 0; i < dueSize; i++) {
										
										String bill_amount=taka_format.format(dueInfo.get(i).getDueAmount());
										
										responseHtml+="<tr> "+
												"<td align=center colspan=2 style='color:blue;font-weight:bold;'>"+dueInfo.get(i).getDueMonth()+", "+dueInfo.get(i).getBillYear()+"</td>"+
												   
												"<td align=center colspan=2 style='color:blue;font-weight:bold;'>"+bill_amount+"</td>"+
												"<td align=center colspan=1 style='color:blue;font-weight:bold;'>"+dueInfo.get(i).getDueSurcharge()+"</td>"+
												//"<td align=center colspan=2 style='color:blue;font-weight:bold;'><input type='checkbox' name='Bill' value='"+bill_amount+"' onClick='test(this);'/></td>"+
												//"<td align=center colspan=2 style='color:blue;font-weight:bold;'><input type='checkbox' name='selectedBills' value='"+dueInfo.get(i).getBillId()+"' onClick='test(this);'/></td>"+
												"</tr>";
										totalDueAmount=totalDueAmount+dueInfo.get(i).getDueAmount()+dueInfo.get(i).getDueSurcharge();				
									}
								
								
									responseHtml+="<tr>"+
													"<td align=right colspan=2 style='color:blue;font-weight:bold;'>Total = </td>"+
												    "<td align=left colspan=2 style='color:blue;font-weight:bold;'>"+taka_format.format(totalDueAmount)+" Tk.</td>"+
												    "<td align=left colspan=3 style='color:green;font-weight:bold;'></td>"+
												   //Selected Amount =<span id='TotalBill'> </span>
												  "</tr>"+
												"</tbody>" +
											"</table>";
						
						
									/* payment methods
									 * */
						/*
								responseHtml+="<div id='flip'>"+
										"<table width='1000px' border='0' id='rounded-corner'>"+
											"<tbody>"+
												"<tr>"+
												   "<td align=center colspan=7>Select Payment Method</td>"+
												"</tr>"+			
											"</tbody>"+
										"</table>"+
									"</div>";*/
						
								responseHtml+="<div class='peymentMethods'>"+
												"<table width='1000px' border='0' id='rounded-corner'>"+
													"<tbody>"+
														"<tr>"+
														   "<td align=center colspan=1></td>"+
														   "<td align=center colspan=1><label class='radio'><input type='radio' value='BRACBANK' name='paymentMethodId'><img src='./resources/images/ipg/brac.png' class='img-ms-form'></td>"+
														   "<td align=center colspan=1><label class='radio'><input type='radio' value='DBBL' name='paymentMethodId'><img src='./resources/images/ipg/nexus.png' class='img-ms-form'></td>"+
														   "<td align=center colspan=1><label class='radio'><input type='radio' value='DBBL_MASTER' name='paymentMethodId'><img src='./resources/images/ipg/master.png' class='img-ms-form'></td>"+
														   "<td align=center colspan=1><label class='radio'><input type='radio' value='DBBL_MOBILE' name='paymentMethodId'><img src='./resources/images/ipg/rocket.png' class='img-ms-form'></td>"+
														   "<td align=center colspan=1><label class='radio'><input type='radio' value='DBBL_VISA' name='paymentMethodId'><img src='./resources/images/ipg/visa.png' class='img-ms-form'></td>"+
														   "<td align=center colspan=1></td>"+
														 "</tr>"+
														 "<tr>"+
															"<td align=center colspan=7><input style='font-size: 25px;' type='submit' value='Confirm Payment'></td>"+		//onClick='validate();' //onsubmit='return validate()'
														"</tr>"+
													"</tbody>"+
												"</table>"+
											"</div>"+
										"</form>"+
								"</div>";
					}
					
					
					
					Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016
					Calendar cal = Calendar.getInstance();
					cal.setTime(today);
					
				//	int month = cal.get(Calendar.MONTH); // 5
				//	int year = cal.get(Calendar.YEAR); download_type report_type month year officer_name officer_desig
					int year;

					
					responseHtml+="<table width='1000px' border='0'  id='rounded-corner'>"+
							"<tbody>"+
					
						"<form action='http://103.48.17.216:8080/JGTDSL_WEB/clearnessCertificateInfo.action'>"+//?area=area_name&download_type=download_type&customer_id="+customer_id+"&report_type=report_type&collection_month=month&calender_year=year&officer_name=officer_name&officer_desig=officer_desig'>"+
						" <input type='hidden' id='area' name='area' value=19>"+
						" <input type='hidden' id='download_type' name='download_type' value='individual_wise'>"+
						" <input type='hidden' id='customer_id' name='customer_id' value="+customer_id+">"+
						" <input type='hidden' id='report_type' name='report_type' value='DC'>"+
						
						
						"<tr>"+
						"<td border='0'> <label style='width: 40%'>To Month<m class='man'/></label> </td>"+
						"<td><select name='collection_month' id='collection_month'>"+
						" <option value=''>Select Month</option>"+
						"</select></td><br>"+
						
						"</tr>"+
					
						"<tr>"+
					
							"<td><label style='width: 40%'>To Year<m class='man'/></label></td>"+
					
							"<td><select name='calender_year' id='calender_year'>"+
							"</select></td><br>"+
						"</tr>"+	
							" <input type='hidden' id='officer_name' name='officer_name' value='This is computer genereted'>"+
							" <input type='hidden' id='officer_desig' name='officer_desig' value='Signature is not required'>"+
							
							"<tr>"+
							"<td align=center colspan=2><input type='submit'align='center'  class='btn btn-primary' value='Download Clearance Certificate'></td>"+
							"</tr>"+
							"</tbody>"+
							"</table>"+
							
						"</form> ";
							
							
							
					
					
/*responseHtml+=
				"<select id='date-dropdown'>"+
		
				"</select>";*/
					
					responseHtml +="<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>"+
							"<script type='text/javascript'> " +
							"var d = new Date();"+
							"var monthArray = new Array();"+
							"monthArray[0] = 'January';"+
							"monthArray[1] = 'February';"+
							"monthArray[2] = 'March';"+
							"monthArray[3] = 'April';"+
							"monthArray[4] = 'May';"+
							"monthArray[5] = 'June';"+
							"monthArray[6] = 'July';"+
							"monthArray[7] = 'August';"+
							"monthArray[8] = 'September';"+
							"monthArray[9] = 'October';"+
							"monthArray[10] = 'November';"+
							"monthArray[11] = 'December';"+
							"for(m = 0; m <= 11; m++) {"+
							"var optn = document.createElement('OPTION');"+
							" optn.text = monthArray[m];"+
							"optn.value = (m+1);"+
							"if ( m == optn.text) {"+
							"optn.selected = true;"+
							"}"+
							"document.getElementById('collection_month').options.add(optn);"+
							"}"+
					
					" </script>";
				
responseHtml +="<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>"+
		"<script type='text/javascript'> " +
			"let dateDropdown = document.getElementById('calender_year');"+	
		"let currentYear = new Date().getFullYear();"+
			"let earliestYear = 1970;"+
		"while (currentYear >= earliestYear) {"+
			"let dateOption = document.createElement('option');"+
		"dateOption.text = currentYear;"+
			"dateOption.value = currentYear;"+
		"dateDropdown.add(dateOption);"+
			"currentYear -= 1;"+
		"}"+
		" </script>";


					
					
					//bindu clear
	/*				responseHtml+="<div id=''>"+
							"<table width='1000px' border='0' id=''>"+
								"<tbody>"+
									"<tr>"+
									   "<td align=center colspan=7><br/><a class='btn btn-primary' href='http://103.48.17.216:8080/JGTDSL_WEB/clearnessCertificateInfo.action?area=19&download_type=individual_wise&customer_id="+customer_id+"&report_type=DC&collection_month=12&calender_year=year&officer_name=Website&officer_desig=JGTDSL'>Download Clearance Certificate</a></td>"+
									//	"<td align=center colspan=7><br/><input type='button' class='btn btn-sm btn-success'  onclick=saveDate_month() value='Download Clearance Certificate'></td>"+
									//"<div style='float: center; colspan=7'><input type='button' class='btn btn-sm btn-success'  onclick='saveBooking()' value='Download Clearance Certificate'></div>"+
									"</tr>"+			
								"</tbody>"+
							"</table>"+
						"</div>";
	
					responseHtml+="</div>" +
							"</body>";	*/		
					
		
			
					
		}
		
	//	setJsonResponse("jsonPresponse({\"response\":\""+responseHtml+"\"})");
		setJsonResponse(responseHtml);
		return null;
	}


	public String getCustomer_id() {
		return customer_id;	
	}


	public void setCustomer_id(String customerId) {
		customer_id = customerId;
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


	

}
