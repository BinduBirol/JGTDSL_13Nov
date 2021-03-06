<%@ taglib prefix="s" uri="/struts-tags"%>
<script  type="text/javascript">
	navCache("advancedCollectionHome.action");						// ** COLLECTION NON METERED
	setTitle("Non Meter Customers Advanced Bill Collection Information");
</script>
<script type="text/javascript" src="/JGTDSL_WEB/resources/thirdParty/popupModal/ns-window.js"></script>
<link href="/JGTDSL_WEB/resources/thirdParty/popupModal/css/ns-window.css" rel="stylesheet" />
<link href="/JGTDSL_WEB/resources/css/page/billCollection.css" rel="stylesheet" type="text/css" />
<style>
::placeholder {
  color: #232323;
  font-style: oblique;
  text-align: left;
}

#advanced_amount::placeholder, #surcharge_amount::placeholder {
  color: #800000;
  font-style: oblique;
  text-align: left;
}
input:focus {
    /*outline: none !important;*/
    border:3px solid red;
    box-shadow: 0 0 20px #004b9e;
}

	input[type="radio"], input[type="checkbox"]{
		margin-top: -3px !important;
	}
	.alert{
		padding-top: 4px !important;
		padding-bottom: 4px !important;
	}
	.ui-icon, .ui-widget-content .ui-icon {
    	cursor: pointer;
	}
.collection-form{
	float: left;
	width: 80%;
	height: 67%;
}

.month_help{

    position:absolute;
    top:4%;
    right:0;
	width: 20%;
	height: 50%;
	
	
}
#info_entry_div{
  float: left;
 
}
#ui-datepicker-div {z-index:99999999999 !important;}

input[type="number"]::-webkit-inner-spin-button, 
input[type="number"]::-webkit-outer-spin-button { 
  -webkit-appearance: none; 
  margin: 0; 
}
input[type="number"] {
    -moz-appearance: textfield;
}
</style>



<div class="collection-form"  >

     <form id="advancedCollectionForm" name="advancedCollectionForm">	
     	<div class="w-box">
		  <div class="w-box-header">
    				<h4 id="rightSpan_caption">Non Meter Bill Collection Information</h4>
		 </div>
		 
				<div class="w-box-content" style="padding: 10px;" id="content_div">					
						<div style="width: 80%;height: 100%;float: left;">
							<div >
								<!-- Start -->
								<div class="bank_info" id="bank_info">
								<!--<div class="row-fluid">														
								<div class="span12">
									<label style="width: 40%">Category</label>
									<select id="category_id" onclick="getAccountInfo()" style="width: 54.5%;">
										<option value="" selected="selected">Select Category</option>
										<s:iterator value="%{#application.ACTIVE_NON_CUSTOMER_CATEGORY}" >
											<option value="<s:property value="category_id" />" ><s:property value="category_name" /></option>
										</s:iterator>
									</select>      
								</div>  
							</div>-->
								<div class="row-fluid">
									<div class="span6">
										<label style="width: 41%">Bank</label>
										<select id="bank_id" name="collection.bank_id"  style="width: 54.5%;"   onchange="fetchSelectBox(branch_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(branch_sbox)">
											
											<s:iterator value="%{#session.BANK_USER_BANK_LIST_COL}">
												<option value="<s:property value="bank_id" />" ><s:property value="bank_name" /></option>
										</s:iterator>
										</select>
										<input type="hidden" name="collection.customer.connectionInfo.isMetered_str" value="" id="isMetered_str" />
									</div>
									<div class="span6">
										<label style="width: 41%">Customer ID </label>
										<input type="text" name="collection.customer_id" id="customer_id"  style="font-weight: bold;color: #3b5894; z-index: 2; background: transparent;width: 51%;margin-top: 2px;" readonly/>
										
									</div>
								</div>	
								<div class="row-fluid">
									<div class="span6">
										<label style="width: 41%">Branch</label>
										<select id="branch_id" selected name="collection.branch_id" style="width: 54.5%;"  onchange="fetchSelectBox(account_sbox)" onkeypress="selectvalue.apply(this, arguments),fetchSelectBox(account_sbox)">
											<s:iterator value="%{#session.BANK_USER_BANK_LIST_COL}">
												<option value="<s:property value="fax" />" ><s:property value="phone" /></option>
										</s:iterator>
										</select>  
									</div>
									
									<div class="span6">									    
									<label style="width: 41%">Customer Type</label>
									<input type="text" style="width: 51%;color: green;font-weight: bold;"  name="customerInfo.customerType" id="customerType" disabled="disabled" />
								</div>
									
								</div>	
								<div class="row-fluid">
									<div class="span6">
										<label style="width: 41%">Account</label>
										<select id="account_id" name="collection.account_no" onkeypress="selectvalue.apply(this, arguments)" style="width: 54.5%;">
											
										</select> 
									</div>
									
									<div class="span6">
										<label style="width: 41%">Region/Area</label>
										<select id="area_id"  style="width: 54.5%;" disabled="disabled">
											<option value="" selected="selected">Select Area</option>
											<s:iterator value="%{#session.USER_AREA}" >
												<option value="<s:property value="area_id" />" ><s:property value="area_name" /></option>
										</s:iterator>
										</select>	 
									</div>
									
								</div>
								<div class="row-fluid">
									<div class="span6">
										<label style="width: 41%">Collection Date</label>
										<input readonly type="text" name="collection.collection_date" id="collection_date" style="width: 51%"/>
									</div>
									
									<div class="span6">
										<label style="width: 41%">Customer Name</label>
										<input type="text" style="width: 51%"  name="collection.customer_name" id="customer_name"  disabled="disabled"/>
										<input type="hidden"   name="collection.bill_id" id="bill_id"  />
										<input type="hidden"   name="collection.customer.connectionInfo.isMetered_str" id="ismeter"  />
									</div>
									
								</div>
							</div>	
							
							<br/>
							
							<div style="" class="collection_info" id="collection_info">	
							<div class="row-fluid">								
							  	<div class="span6">									    
									<label style="width: 41%;font-weight: bold;">Customer Code</label>
									<input type="text" id="customer_code"  tabindex="1" style="border-style: solid;width: 51%;color: blue;font-weight: bold;" autofocus/>
									&nbsp;&nbsp;
									<!--  
									<i class="fa fa-th-list" style="cursor: pointer;border: 1px dotted green;" onclick="fetchCollectionList($('#customer_id').val());"> </i>
									-->
									<input type="hidden" id="bill_id" name="collection.bill_id"/>
									
								</div>
								
								
								
					        	<div class="span6">
					         		<label style="width: 41%">From Month Year</label>        
					         			<input placeholder="Month" onkeypress="validate(event)" type="text" maxlength="2"  name="collection.from_month" id="from_month" tabindex="1" style="text-align: right;color: black;width: 11.2%; background-color: #FFFFF;"/>
					         			<input placeholder="Year" onkeypress="validate(event)" type="text" maxlength="4"    name="collection.from_year" id="from_year" tabindex="2" style="text-align: right;color: black;width: 11.2%; background-color: #FFFFF;"/>
					        	</div> 
					        	
					        	             
					       		
									
								
							</div>
							
							
					       
								
						
						
						<div class="row-fluid">								
							
								<div class="span6">
									<label style="width: 41%;font-weight: bold;">Total Amount</label>								
									<input type="number"  id="total_amount" tabindex="3" style="text-align: right;font-weight: bold;color: black;width: 51% background-color: #FFFFF;"/>
								</div>
																			
							
					        	<div class="span6">
					         		<label style="width: 41%">To Month Year</label>        
					         			<input placeholder="Month" onkeypress="validate(event)" type="text" maxlength="2"    name="collection.to_month" id="to_month" tabindex="3" style="text-align: right;color: black;width: 11.2%; background-color: #FFFFF;"/>
					         			<input placeholder="Year" onkeypress="validate(event)" type="text"  maxlength="4"   name="collection.to_year" id="to_year" tabindex="4" style="text-align: right;color: black;width: 11.2%; background-color: #FFFFF;"/>
					        	</div>
					        
						</div>	
						
						<div class="row-fluid">	
							<div class="span6">
							
							</div>							
							
							<div class="span6" id="advanced_div" >
								
									<label style="width: 41%">Bill Amount</label>								
									<input type="number" readonly name="collection.advanced_amount" id="advanced_amount" tabindex="3" style="text-align: right;color: black;width: 55% background-color: #FFFFF;"/>
																		
							</div>
						</div>
						<div class="row-fluid"  id="surcharge_div" >						
						<div class="span6"></div>								
								<div class="span6">	
									<label style="width: 41%">Surcharge Amount</label>	
									<input type="number" readonly name="collection.surcharge_amount" id="surcharge_amount" tabindex="2" style="text-align: right;color: black;width: 51% background-color: #FFFFF;"/>
								</div>
																				
							</div>
							<div class="row-fluid" >						
						<div class="span6"></div>								
								<div class="span6">	
									<label style="width: 41%">Advanced Amount</label>	
									<input type="number" readonly name="collection.bank_advance_amount"  id="real_adv_amount" tabindex="2" style="text-align: right;color: black;width: 51% background-color: #FFFFF;"/>
								</div>
																				
							
						</div>
							
						</div>	
						
						
															
								<!-- End -->
							</div>
						<div >
							
						</div>
				</div>
							  
								<div class="formSep" style="padding-top: 2px;padding-bottom: 2px;">
									<div id="aDiv" style="height: 0px;"></div>
									
								</div>
						
								<div class="formSep sepH_b" style="padding-top: 3px;margin-bottom: 0px;padding-bottom: 2px;">										
									<table width="100%">
										<tr>
											<td width="35%" align="left">								
												<div id="msg_div" class="count_div" style="text-align: left;"></div>
												<div style="width: 100%;margin-top: 5px;">
											     <div>
														    <button class="btn btn-beoro-3" type="button" id="btn_refresh" onclick="getTotalCollectionByDateAccount();getCollectionHistoryByDate();">
																	<span class="splashy-refresh"></span>
																</button>
															<%-- 	<button class="btn btn-beoro-3" type="button" id="btn_multi_delete" onclick="multiDeleteModal()" disabled="disabled" >
																	<span class="splashy-box_remove"></span>
																	  	Multi Delete
																</button> --%>	
											    </div>
											    <!-- 
												<div class="alert alert-error" style="width: 88%;float: left;margin-top: 7px;">
													<strong>Total Deposit :</strong> <span id="total_collection" style="font-size:20px;color:blue;font-weight:bold;"></span> 
												</div>
								 				-->
									
							                  </div>	
										</td>
									    <td width="5%"></td>
									    <td width="60%" align="left">
												    		<button class='btn btn-beoro-3' type='button' id='btn_next' onclick="showShortCuts()"><span class='splashy-sprocket_dark'></span></button>
													    	<button class="btn btn-beoro-3" type="button" id="btn_save" onclick="saveAdvancedCollection()" id="btn_save"><span class="splashy-document_letter_okay"></span>Save Collection</button> 
															<!-- 
													    	<button class="btn btn-beoro-3" type="button" id="btn_parameter"><span class="splashy-help"></span> Help</button>
													    	 -->
													    	<%--  <button class="btn btn-beoro-3" type="button" id="btn_edit" onclick="editButtonPressed()" disabled="disabled">
																<span class="splashy-application_windows_edit"></span> Edit
															 </button> --%>
													    	 <button class="btn btn-beoro-3" type="button" id="btn_delete" onclick="$delete.dialog('open');" disabled="disabled">
																	<span class="splashy-gem_remove"></span> Delete
															</button>
															<button class="btn btn-beoro-3"  type="button" id="btn_close" onclick="callAction('blankPage.action')"><i class="splashy-folder_classic_remove"></i>Close</button>
										</td>
									</tr>
								</table>
												
												    									
							</div>
		</div>
			</div>
			
	 
			
			
			
	
		
  </form>
  <div class="month_help">
	<h4 style="padding-left: 100px;">	
		01 -- January 	</br>
		02 -- February 	</br>
		03 -- March 	</br></br>
		04 -- April 	</br>
		05 -- May 	</br>
		06 -- June 	</br></br>	
		07 -- July 	 </br>	
		08 -- August 	 </br>	
		09 -- September </br></br>	
		10 -- October 	</br>
		11 -- November 	</br>
		12 -- December </br>
	</h4>
 </div>
 


													
	
</div>	

<div>
<div class="span6" style="font-size:17px; width:100%;" id="applienceAndDue" ></div><br/><br/>
<textarea rows="1" style="width: 100%; color:red;" name="dueListbyString" id="dueListbyStringcoll" disabled="disabled"></textarea>  	
</div>
 
<p style="clear: both;margin-top: 5px;clear: both;"></p>
<div class="collection-list" id="collection_grid_div"> 
	<table id="collection_grid"></table>
	<div id="collection_grid_pager" ></div>
</div>

<script type="text/javascript" src="/JGTDSL_WEB/resources/js/page/bankCollNM.js"></script>
<script type="text/javascript">



 $(document).ready(function () {
 		//document.getElementById("customer_code").value="<s:property value="area_id" />";
        //fetchSelectBox(branch_sbox);
        fetchSelectBox(account_sbox);
        
        
        const date = new Date();
		const formattedDate = date.toLocaleDateString('en-GB', {
		  day: 'numeric', month: 'numeric', year: 'numeric'
		}).replace('/', '-');
       //$("#collection_date").val(new Date().toJSON().slice(0,10));
        //var today = moment().format("YYYY-MM-DD");
        //alert(roday);
 		$("#collection_date").val(formattedDate.replace('/', '-'));
    });
    

   
    
</script>


