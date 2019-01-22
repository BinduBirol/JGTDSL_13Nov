<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript"
	src="./resources/js/template/jquery-latest.js"></script>


<!-- Added by Nasir for styling transaction success/failure page -->
<!-- 
<link rel="stylesheet" href="./resources/css/ipg/basic.css"
	type="text/css" media="screen" />
 -->

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
	integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm"
	crossorigin="anonymous">


	<h1 style="text-align: center;">Transaction Confirmation</h1>
	<br />
	<hr />
	<form style="padding-left:5%; font-weight: bold;font-size: 20px;">
		<div class="form-group row">
			<label for="txnStatus" class=" col-form-label " style="color:#008080;">Transaction
				Status:</label>

			<div class="">
				<input id="txnStatus" type="text" readonly
					class="form-control-plaintext"
					value="<s:property value='txnStatus' />">
			</div>
		</div>
		<div class="form-group row">
			<label for="transID" class=" col-form-label" style="color:#008080;">Transaction
				ID from software:</label>

			<div class="">
				<input id="transID" type="text" readonly
					class="form-control-plaintext"
					value="<s:property value='transID' />">
			</div>
		</div>
		<div class="form-group row">
			<label for="ipgTrxID" class=" col-form-label" style="color:#008080;">Transaction
				ID from Bank:</label>

			<div class="">
				<input id="ipgTrxID" type="text" readonly
					class="form-control-plaintext"
					value="<s:property value='ipgTrxID' />">
			</div>
		</div>
		<div class="form-group row">
			<label for="error_msg" class=" col-form-label" style="color:#008080;">Message:</label>

			<div class="">
				<input id="error_msg" type="text" readonly
					class="form-control-plaintext"
					value="<s:property value='error_msg' />">
			</div>
		</div>
		<div class="form-group row">
			<label for="card_no" class=" col-form-label" style="color:#008080;">Card No:</label>

			<div class="">
				<input id="card_no" type="text" readonly
					class="form-control-plaintext"
					value="<s:property value='card_no' />">
			</div>
		</div>
		<div class="form-group row">
			<label for="card_name" class=" col-form-label" style="color:#008080;">Card
				Name:</label>

			<div class="">
				<input id="card_name" type="text" readonly
					class="form-control-plaintext"
					value="<s:property value='card_name'/>">
			</div>
		</div>


		<!-- 

		Transaction Status: <input type="text"
			value="<s:property value='txnStatus' />" style="width: 56%;" /><br>
		Transaction ID from software: <input type="text"
			value="<s:property value='transID' />" style="width: 56%;" /><br>
		Transaction ID from Bank: <input type="text"
			value="<s:property value='ipgTrxID' />" style="width: 56%;" /><br>
		Error Msg: <input type="text" value="<s:property value='error_msg' />"
			style="width: 66%;" /><br> Card No: <input type="text"
			value="<s:property value='card_no' />" style="width: 56%;" /><br>
		Card Holder Name: <input type="text"
			value="<s:property value='card_name'/>" style="width: 56%;" /><br>
		 -->
	</form>

	<!-- 
<c:choose>
    <c:when test="${txnStatus == 'SUCCESS'}">
        <h3>jstl success txnStatus</h3>  
        <br />
    </c:when>    
    <c:otherwise>
      <h3>jstl failed txnStatus</h3>
        <br />
    </c:otherwise>
</c:choose>
 -->

	<c:choose>
		<c:when test="${ipgResponse.txnStatus == 'SUCCESS'}">
			<h1
				style="color: #009F72;font-size: 40px;text-align: center;margin-top: 0px">
				Payment Success <img src="./resources/images/ipg/success.png"
					style="width: 50px;" />
			</h1>
			<br />
		</c:when>
		<c:otherwise>
			<h1
				style="color: #f04251;font-size: 40px;text-align: center;margin-top: 0px">
				Payment Failed <img src="./resources/images/ipg/failed.png"
					style="width: 50px;" />
			</h1>
			<br />
		</c:otherwise>
	</c:choose>



<!--  

    <s:if test="{txnStatus=='SUCCESS'}">
        success**
        <br />
    </s:if>    
    <s:else>
       failed**
        <br />
    </s:else>



<s:if test="{ipgResponse.txnStatus == 'SUCCESS'}">
Success
</s:if>
<s:else>
Failed
</s:else>

-->


