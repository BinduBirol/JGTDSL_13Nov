
<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("billCreationHome.action?bill_parameter.isMetered_str=1");
	setTitle("Bill Creation for Metered Customer");
</script>
<link href="/JGTDSL_WEB/resources/css/page/meterReading.css" rel="stylesheet" type="text/css" />
<style>
input[type="radio"], input[type="checkbox"]
{
margin-top: -3px !important;
}
.alert{
padding-top: 4px !important;
padding-bottom: 4px !important;
}
.ui-icon, .ui-widget-content .ui-icon {
    cursor: pointer;
}
.sFont{
font-size: 12px;
}
</style>
<div class="meter-reading" style="width: 50%;height: 80%;">
	<div class="row-fluid">
		<div class="span12" id="rightSpan">
			<div class="w-box">
				<div class="w-box-header">
    				<h4 id="rightSpan_caption">Bill Creation(Metered)</h4>
				</div>
				<div class="w-box-content" style="padding: 10px;" id="content_div">
     				<form id="billProcessForm" name="billProcessForm" style="margin-bottom: 1px;">
						<div class="row-fluid">
							<div class="span12">
								
                                
							</div>
						</div>
						<div class="row-fluid">
							<div class="span12">
								<label style="width: 19.5%">Customer ID <m class='man'/></label>
								<input style="width:25.5%" type="text" onchange="getPrevReading();" name="bill_parameter.customer_id" id="customer_id"   tabindex="1"/>
								<input type="hidden" name="" id="customer_id_x" disabled="disabled" style="color: #CCC;  background: transparent; z-index: 1;border: none;width: 36.5%;margin-top: -5px;"/>
						  	</div>
						</div>
						
						
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Billing Month<m class='man'/></label>
								<select onchange="getPrevReading();" name="bill_parameter.billing_month_str" id="billing_month" style="width: 56%;margin-left: 0px;">
							       	<option value="">Select Month</option>           
							        <s:iterator  value="%{#application.MONTHS}">   
							   			<option value="<s:property value='id'/>"><s:property value="label"/></option>
									</s:iterator>
						       </select>								      
							</div>
							<div class="span6">
								<label style="width: 40%">Billing Year<m class='man'/></label>
								<select onchange="getPrevReading();" name="bill_parameter.billing_year" id="billing_year" style="width: 56%;">
							       	<option value="">Year</option>
							       	<s:iterator  value="%{#application.YEARS}" id="year">
							            <option value="<s:property/>"><s:property/></option>
									</s:iterator>
						       </select>     
							</div>  
							
							
						</div>
						
						
							<br/>
						<div class="row-fluid">	
							
							
									
								<div class="span6">									    
							<label style="width: 40%">Meter rent<m class='man'/></label>
							<input disabled type="text" style="width: 51%"  name="bill_parameter.m_rent" id="m_rent" />
						</div>
							
							<div class="span6">									    
							<label style="width: 40%">Consumption<m class='man'/></label>
							<input disabled type="text" style="width: 51%"  name="bill_parameter.consumption" id="consumption" />
						</div>		
							
						</div>
							
						<div class="row-fluid">	
							
						
						
						<div class="span6">									    
							<label style="width: 40%">Present Reading<m class='man'/></label>
							<input disabled type="text" style="width: 51%"  name="bill_parameter.p_reading" id="p_reading" />
						</div>
						
						<div class="span6">									    
								<label style="width: 40%">Reading Date<m class='man'/></label>
								<input disabled type="text" style="width: 51%"  name="" id="p_reading_date" />
						</div>
												
							
						</div>	
						
					
						<div class="row-fluid">	
							
						<div class="span6">									    
							<label style="width: 40%">Previous Reading<m class='man'/></label>
							<input disabled type="text" style="width: 51%"  name="bill_parameter.previous_reading" id="previous_reading" />
							</div>
						
						<div class="span6">									    
							<label style="width: 40%">Prev Reading Date<m class='man'/></label>
							<input disabled type="text" style="width: 51%"  name="bill_parameter.previous_reading" id="previous_reading_date" />
							</div>
						
						
												
							
						</div>
						
						
						<div class="row-fluid">	
							
						<div class="span6">									    
							<label style="width: 40%">Rate<m class='man'/></label>
							<input disabled type="text" style="width: 51%"  name="bill_parameter.rate" id="rate" />
						</div>	
						
						<div class="span6">									    
							<label style="width: 40%">HNV<m class='man'/></label>
							<input disabled type="text" style="width: 51%"  name="bill_parameter.hnv" id="hnv" />
						</div>				
							
						</div>
						
						<hr/>
						<div class="row-fluid">							
							<div class="span6">									    
								<label style="width: 40%">Issue Date<m class='man'/></label>
								<input type="text" style="width: 51%"  name="bill_parameter.issue_date" id="issue_date" />
							</div>
							
							
							<div class="span6">									    
									<label style="width: 40%">Due Date<m class='man'/></label>
									<input type="text" style="width: 51%"  name="bill_parameter.bill_due_date" id="bill_due_date" />
							</div>
								
						</div>
						
						<br/>
						<div class="row-fluid">	
							
						<div class="span6">									    
								<label style="width: 40%">Ldate with surcharge<m class='man'/></label>
								<input type="text" style="width: 51%"  name="bill_parameter.bill_due_date_w_sur" id="bill_due_date_w_sur" />
						</div>
						
						<div class="span6">									    
							<label style="width: 40%">Demand Charge<m class='man'/></label>
							<input type="text" style="width: 51%"  name="bill_parameter.demandCharge" id="d_charge" />
						</div>
												
							
						</div>
						
						
						
						<br/>
						<div class="row-fluid">
						
						
						
						<div class="span6">									    
							<label style="width: 40%">Gas Bill<m class='man'/></label>
							<input type="text" style="width: 51%"  name="bill_parameter.gasBill" id="gas_bill" />
						</div>
							
						<div class="span6">									    
							<label style="width: 40%">Surcharge<m class='man'/></label>
							<input type="text" style="width: 51%"  name="bill_parameter.surcharge" id="surcharge" />
						</div>	
							<!-- <div class="span6"></div> -->
						</div>
						<br/>
						
						<div class="row-fluid">
						
						
						
						<div class="span6">									    
							<label style="width: 40%">HNV Bill<m class='man'/></label>
							<input type="text" style="width: 51%"  name="bill_parameter.hnvBill" id="hnv_bill" />
						</div>
							
						
						</div>
						
						<br/>
						
						<div class="row-fluid">							
							<div class="span12">									    
								<label style="width: 19.5%">Remarks</label>
								<textarea rows="1" id="remarks" style="width: 76%" name="bill_parameter.remarks"></textarea>
								<input type="hidden" name="bill_parameter.isMetered_str" value="1" id="isMeter"/>
							</div>
						</div>
						<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
							<div id="aDiv" style="height: 0px;"></div>
						</div>
						
						<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">		
						   <table width="100%">
						   	<tr>
						   		<td style="width: 30%" align="left">
						   			 <button class="btn btn-primary" type="button" id="btn_parameter" onclick="reloadBillGrid()">Reload Grid</button>
						   			 <button class="btn btn-primary" type="button" id="btn_unlockDB" onclick="unlockDatabase()">Unlock</button>
						   		</td>
						   		<td style="width: 70%" align="right">
						   			     <s:if test='%{#session.user.role_name=="Super Admin"}' >
								   		<input type="checkbox" id="reprocess" name="bill_parameter.reProcess" value="Y"/> <font style="color: blue;font-size: 12px;">Re-process  Non-Approved Bills&nbsp;&nbsp;</font>
								   		</s:if>
								   		<s:else>
								   		<input type="checkbox" id="reprocess" name="bill_parameter.reProcess" value="Y" style="display: none;"/>
								   		</s:else>
						   		 
						   			 <button class="btn btn-beoro-3" type="button" id="btn_parameter"  onclick="$('#depositDetailDiv').html(jsImg.SETTING).load(validateAndProcessBilling());" >Process Billing</button>    	
									 <button class="btn btn-danger"  type="button" id="btn_cancel" onclick="callAction('blankPage.action')">Cancel</button>
						   		</td>
						   	</tr>
						   </table>								    
						   									
						</div>
					</form>																	
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 
<jsp:include page="BillCreationMeteredStat.jsp" />
  -->

<p style="clear: both;margin-top: 5px;"></p>

<div id="bill_grid_div" style="width: 99%;height: 48%;"> 
	<table id="bill_grid"></table>
	<div id="bill_grid_pager"></div>
</div>
 <script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/billCreation_for_Exception.js"></script>
