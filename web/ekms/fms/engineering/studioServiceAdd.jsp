<%@ include file="/common/header.jsp" %>
<x:config>
	<page name="fms_studioPage">
    	<com.tms.fms.engineering.ui.StudioForm name="add" width="100%"/>
	</page>
</x:config>
<c:set var="type" value="Add"/>
<x:set name="fms_studioPage.add" property="type" value="${type}"/>

<c:set var="requestId" value="${widgets['fms_requestDetailsPage.details'].requestId}"/>
<c:set var="dtRequiredFrom" value="${widgets['fms_requestDetailsPage.details'].request.requiredFrom}"/>
<c:set var="dtRequiredTo" value="${widgets['fms_requestDetailsPage.details'].request.requiredTo}"/>

<x:set name="fms_studioPage.add" property="requestId" value="${requestId}"/>
<x:set name="fms_studioPage.add" property="dtRequiredFrom" value="${dtRequiredFrom}" />
<x:set name="fms_studioPage.add" property="dtRequiredTo" value="${dtRequiredTo}" />

<c:choose>
  <c:when test="${not empty(param.serviceId)}">
    <c:set var="serviceId" value="${param.serviceId}"/>
  </c:when>
  <c:otherwise>
    <c:set var="serviceId" value="${widgets['fms_studioPage.add'].serviceId}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_studioPage.add" property="serviceId" value="${serviceId}"/>

<c:choose>
  <c:when test="${forward.name == 'ADDED'}">
	<script>
		alert('<fmt:message key="fms.facility.msg.bookingAdded"/>'); 
	    window.opener.location="requestDetails.jsp";
    	window.close();
    </script>
  </c:when>
  <c:when test="${forward.name == 'EXISTS'}">
    <script>alert('<fmt:message key="fms.facility.msg.bookingExists"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'FAILED'}">
    <script>alert('<fmt:message key="fms.facility.msg.bookingNotAdded"/>');</script>
  </c:when>
   <c:when test="${forward.name == 'MODIFIED'}">
    <script>
		alert('<fmt:message key="fms.facility.msg.bookingAdded"/>'); 
	    window.opener.location="requestServiceModify.jsp";
    	window.close();
    </script>
  </c:when>
	
	<c:when test="${forward.name=='checkTimeFrom'}">
		<script>
		alert('<fmt:message key="fms.facility.invalid.endTime"/>');
		</script>
	</c:when>
</c:choose>
<%@include file="/ekms/includes/linkCSS.jsp" %>
<jsp:include page="includes/header.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.msg.studioBooking"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        <x:display name="fms_studioPage.add"/>
    </td></tr>
</table>

<script>
var dateFrom=document.forms["fms_studioPage.add"].elements["fms_studioPage.add.bookingDate"].value;
var dateTo=document.forms["fms_studioPage.add"].elements["fms_studioPage.add.bookingDateTo"].value;
var h1 = 0;
var h2 = 0;
var m1 = 0;
var m2 = 0;

function setTime(){
	h1 = parseInt(document.getElementById('hourfrom').options[document.getElementById('hourfrom').selectedIndex].value);
	m1 = parseInt(document.getElementById('minutefrom').options[document.getElementById('minutefrom').selectedIndex].value);
	h2 = parseInt(document.getElementById('hourto').options[document.getElementById('hourto').selectedIndex].value);
	m2 = parseInt(document.getElementById('minuteto').options[document.getElementById('minuteto').selectedIndex].value);
}

function isValidTime(){
	setTime();
	
	<%--
	if (h2 < h1) {
		document.getElementById('hourfrom').options[document.getElementById('hourfrom').selectedIndex].selected=true;
		document.getElementById('hourto').options[document.getElementById('hourfrom').selectedIndex].selected=true;
		document.getElementById('minutefrom').options[document.getElementById('minutefrom').selectedIndex].selected=true;
		document.getElementById('minuteto').options[document.getElementById('minutefrom').selectedIndex].selected=true;
		alert('Your end time is not valid');
	} else if ((h2 == h1) && (m2 < m1)){
		document.getElementById('minutefrom').options[document.getElementById('minutefrom').selectedIndex].selected=true;
		document.getElementById('minuteto').options[document.getElementById('minutefrom').selectedIndex].selected=true;
		alert('Your end time is not valid');
	} 
	--%>
	if(dateFrom == dateTo){
		if (h2 < h1 || h2==0 && m2==0) {
			document.getElementById('hourfrom').options[document.getElementById('hourfrom').selectedIndex].selected=true;
			document.getElementById('minutefrom').options[document.getElementById('minutefrom').selectedIndex].selected=true;
			alert('Your end time is not valid');
		}
	}

	
	if ((h2 == h1) && (m2 < m1)){
		document.getElementById('minutefrom').options[document.getElementById('minutefrom').selectedIndex].selected=true;
		document.getElementById('minuteto').options[document.getElementById('minutefrom').selectedIndex].selected=true;
		alert('Your end time is not valid');
	}
}
	
function saveIt(){
	setTime();
	if(dateFrom != dateTo){
		if ((h2 == 0 && (m2 == 0))){
			if(confirm("<fmt:message key='fms.facility.save0000'/>")) {
				return true;
			}
			else {
				return false;
			}
		} 		
	}
}

</script>
<jsp:include page="includes/footer.jsp" />
