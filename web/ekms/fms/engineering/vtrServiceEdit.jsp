<%@ include file="/common/header.jsp" %>


<x:config>
  <page name="fms_vtrEditPage">
    	<com.tms.fms.engineering.ui.VtrForm name="edit" width="100%"/>
  </page>
</x:config>

<c:set var="type" value="Edit"/>
<c:choose>
  <c:when test="${not empty(param.id)}">
    <c:set var="id" value="${param.id}"/>
  </c:when>
  <c:otherwise>
    <c:set var="id" value="${widgets['fms_vtrEditPage.edit'].id}"/>
  </c:otherwise>
</c:choose>
<x:set name="fms_vtrEditPage.edit" property="id" value="${id}"/>
<x:set name="fms_vtrEditPage.edit" property="type" value="${type}"/>

<c:choose>
  <c:when test="${forward.name == 'ADDED'}">
    <script>alert('<fmt:message key="fms.facility.msg.bookingUpdated"/>'); 
    	window.opener.location="requestDetails.jsp";
    	window.close();
    </script>
  </c:when>
  <c:when test="${forward.name == 'EXISTS'}">
    <script>alert('<fmt:message key="fms.facility.msg.bookingExists"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'FAILED'}">
    <script>alert('<fmt:message key="fms.facility.msg.bookingNotUpdate"/>');</script>
  </c:when>
  <c:when test="${forward.name == 'MODIFIED'}">
    <script>
		alert('<fmt:message key="fms.facility.msg.bookingAdded"/>'); 
	    window.opener.location="requestServiceModify.jsp";
    	window.close();
    </script>
  </c:when>
</c:choose>

<script>
var h1 = 0;
var h2 = 0;
var m1 = 0;
var m2 = 0;
var hdiff = 0;
var mdiff = 0;

function setTime(){
	h1 = parseInt(document.getElementById('hourfrom').options[document.getElementById('hourfrom').selectedIndex].value);
	m1 = parseInt(document.getElementById('minutefrom').options[document.getElementById('minutefrom').selectedIndex].value);
	h2 = parseInt(document.getElementById('hourto').options[document.getElementById('hourto').selectedIndex].value);
	m2 = parseInt(document.getElementById('minuteto').options[document.getElementById('minuteto').selectedIndex].value);
}
	
function calculate(){
	setTime();	
	
	var convertedH1 = (h1 * 60) + m1; 
	var convertedH2 = (h2 * 60) + m2; 

	var diff = convertedH2 - convertedH1; 
	
	hdiff = Math.floor(diff / 60);
	if (hdiff < 1){
		hdiff = 0;
	}
	
	mdiff = diff % 60;
	
	var strHour = hdiff + ' hours ';
	var strMinute = mdiff + ' minutes';
	
	
	if (hdiff <= 0) {
		strHour = '';
	} else if (hdiff <= 1 && hdiff > 0) {
		strHour = hdiff + ' hour ';
	}
	
	if (mdiff <= 0) {
		strMinute = '';
	} else if (mdiff <= 1 && mdiff > 0) {
		strMinute = mdiff + ' minute';
	}	

	var result = '';
	if ((strHour == '') && (strMinute == '')){
		result = '0 hour';
	} else if (strHour != '' && strMinute == '') {
		result = strHour;
	} else if (strHour == '' && strMinute != '') {
		result = strMinute;
	} else {
		result = strHour + strMinute;
	}
	document.getElementsByName('fms_vtrEditPage.edit.duration')[0].value = result;

}

function isValidTime(){
	setTime();
	
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

	calculate();	
}

function populateClientName(){
	var selValue = document.getElementById('fms_vtrEditPage.edit.serviceParticulars').options[document.getElementById('fms_vtrEditPage.edit.serviceParticulars').selectedIndex].value;
	var formatIngest=document.getElementById("formatIngest");
	var format=document.getElementById("format");
	
	if((selValue=='3') || (selValue=='4') || (selValue=='5')){
		formatIngest.style.display='';
		format.style.display='none';
	}else {
		formatIngest.style.display='none';
		format.style.display='';
	}	
}
</script>

<%@include file="/ekms/includes/linkCSS.jsp" %>
<jsp:include page="includes/header.jsp" />
<body onload="isValidTime()">
<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.facility.msg.vtrBooking"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="fms_vtrEditPage.edit"/>
    
    </td></tr>
</table>

<jsp:include page="includes/footer.jsp" />
