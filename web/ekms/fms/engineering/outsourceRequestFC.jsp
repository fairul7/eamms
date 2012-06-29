<%@include file="/common/header.jsp" %>

<x:config>
     <page name="outsourceRequestFC">
		<com.tms.fms.engineering.ui.OutsourceRequestFCForm name="form" width="100%"/>
		<com.tms.fms.engineering.ui.OutsourceRequestFCTable name="table" width="100%"/>
     </page>
</x:config>

<c:set var="type" value="Edit"/>
<c:choose>
  <c:when test="${not empty(param.requestId)}">
    <c:set var="requestId" value="${param.requestId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="requestId" value="${widgets['fms_requestDetailsPage.details'].requestId}"/>
	<!-- script>
		window.location='incomingRequestFCListing.jsp'
	</script-->
  </c:otherwise>
</c:choose>
<x:set name="outsourceRequestFC.table" property="requestId" value="${requestId}"/>
<x:set name="outsourceRequestFC.form" property="requestId" value="${requestId}"/>

<c:if test="${forward.name == 'delete'}">
  <script> window.location='outsourceRequestFC.jsp?requestId='<c:out value="${param.requestId}"/></script>
</c:if>

<c:if test="${forward.name == 'sendEmail'}">

  	<script> 
  		alert("<fmt:message key='fms.request.label.emailSentSuccessfully'/>");
  		window.location='outsourceRequestFC.jsp?requestId=<c:out value="${requestId}"/>'
	</script>
</c:if>

<c:if test="${forward.name == 'selectOutsourceCompany'}">
	<script>
		alert("<fmt:message key='fms.request.label.invalidCompany'/>");
	</script>
</c:if>

<script>
	function populateCompanyName(selValue){
		var outsourceCompany=document.getElementById("outsourceCompany");
		
		if(selValue=='company'){
			outsourceCompany.style.display='';
		} else if(selValue=='individual'){
			outsourceCompany.style.display='none';
		}
	}
	
	function showHideActualCost(selValue){
		var actualCost=document.getElementById("actualCost");
		var completeBtn=document.getElementById("completeButton");
		var unCompleteBtn=document.getElementById("unCompleteButton");
		
		if(selValue=='complete'){
			actualCost.style.display='';
			completeBtn.style.display='none';
			unCompleteBtn.style.display='';
			
		} else if(selValue=='uncomplete'){
			actualCost.style.display='none';
			completeBtn.style.display='';
			unCompleteBtn.style.display='none';
		}
	}

	function populateCompleteButton(selValue){
		var completeBtn=document.getElementById("completeButton");
		var unCompleteBtn=document.getElementById("unCompleteButton");
		
		if(selValue=='add'){
			completeBtn.style.display='none';
			unCompleteBtn.style.display='none';
		} else if(selValue=='edit'){
			completeBtn.style.display='';
			unCompleteBtn.style.display='none';
		}
	}

	function populateActualCost(selValue){
		var actualCost=document.getElementById("actualCost");
		var completeBtn=document.getElementById("completeButton");
		var unCompleteBtn=document.getElementById("unCompleteButton");
		
		if(selValue=='show'){
			actualCost.style.display='';
			completeBtn.style.display='none';
			unCompleteBtn.style.display='none';
			
		} else if(selValue=='hide'){
			actualCost.style.display='none';
			completeBtn.style.display='';
			unCompleteBtn.style.display='none';
		} else if(selValue=='add'){
			actualCost.style.display='none';
			completeBtn.style.display='none';
			unCompleteBtn.style.display='none';
		} else if(selValue=='edit'){
			actualCost.style.display='';
			completeBtn.style.display='none';
			unCompleteBtn.style.display='none';
		}	
	}

</script>

<%@include file="/ekms/includes/header.jsp" %>
  
<jsp:include page="includes/header.jsp" />

<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
	<tr valign="MIDDLE">
    	<td height="22" class="contentTitleFont">
      		&nbsp;<fmt:message key='fms.request.label.outsourceRequest'/>  
        </td>
    	<td align="right" class="contentTitleFont">&nbsp;</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
    		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10">
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
			<x:display name="outsourceRequestFC.form" ></x:display>
		</td>
  	</tr>
	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
			<x:display name="outsourceRequestFC.table" ></x:display>
		</td>
  	</tr>
  	<tr>
    	<td colspan="2" valign="TOP" class="contentBgColor">
      		<img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"> </td>
  	</tr>
</table>

<jsp:include page="includes/footer.jsp" />

<%@include file="/ekms/includes/footer.jsp" %>
