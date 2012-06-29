<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%@ page import="kacang.Application,com.tms.fms.engineering.model.EngineeringModule"%>

<c:set var="widget" value="${widget}"/>

<script type="text/javascript">
	
	onload = function() {
		var radio = document.getElementsByName('fms_submitRequest.submit.requestType');
		var radioValue = getCheckedValue(radio);
		
		if(radioValue == "fms_submitRequest.submit.submitType_N"){
			populateSubmit('<%=EngineeringModule.SUBMIT_NEW_REQUEST%>');
		}else{
			populateSubmit('<%=EngineeringModule.COPY_NEW_REQUEST%>');
		}
	}	
	
	function getCheckedValue(radioObj) {
		if(!radioObj)
			return "";
		var radioLength = radioObj.length;
		if(radioLength == undefined)
			if(radioObj.checked)
				return radioObj.value;
			else
				return "";
		for(var i = 0; i < radioLength; i++) {
			if(radioObj[i].checked) {
				return radioObj[i].value;
			}
		}
		return "";
	}
</script>

<jsp:include page="../../form_header.jsp" flush="true"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%" border="0">
	<c:forEach items="${widget.radioRequest}" var="submitType" varStatus="subNo">
		<c:if test="${subNo.index==0}">
			<tr>
				<td width="30%" nowrap class="profileRow" align="right">Do you want to: &nbsp; </td>
				<td width="70%" class="profileRow"><x:display name="${submitType.absoluteName}"/></td>
			</tr>
		</c:if>
		<c:if test="${subNo.index==1}">
			<tr>
				<td width="30%" nowrap class="profileRow">&nbsp;</td>
				<td width="70%" class="profileRow"><x:display name="${submitType.absoluteName}"/></td>
			</tr>
		</c:if>
	</c:forEach>

	<tr >
		<td width="30%" nowrap class="profileRow">&nbsp;</td>
		<td width="70%" class="profileRow" id="request">&nbsp; &nbsp; &nbsp;<x:display name="${widget.request.absoluteName}"/> </td>
	</tr>
	<tr>
		<td width="30%" nowrap class="profileRow">&nbsp;</td>
		<td width="70%" class="profileRow">&nbsp; &nbsp; &nbsp;<x:display name="${widget.btnContinue.absoluteName}"/></td>
	</tr>
	<script>
	function populateSubmit(selValue){
		var requestType=document.getElementById("request");
		if(selValue=='<%=EngineeringModule.SUBMIT_NEW_REQUEST%>'){
			requestType.style.display='none';
			removeOptions(document.getElementsByName('fms_submitRequest.submit.request')[0]);
		}else if(selValue=='<%=EngineeringModule.COPY_NEW_REQUEST%>'){
			requestType.style.display='block';
		}else{
		}	
	}

	function removeOptions(selectbox)
	{
		var i;
		for(i=selectbox.options.length-1;i>=0;i--)
		{
			if(selectbox.options[i].selected)
				selectbox.remove(i);
		}
	}
</script>
</table>
<jsp:include page="../../form_footer.jsp" flush="true"/>
