<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@include file="/common/header.jsp"%>
<x:config>
	<page name="Manpower">
		<com.tms.fms.facility.ui.RateCardAllManpowerTable name="table" width="100%"/>
	</page>
</x:config>

<script language="JavaScript">
	function pops(nama,jangkar,h,w){  
		self.name = nama;
		var nX = (screen.availWidth - w)/2;
		var nY = (screen.availHeight - h)/2;
		viewWin = window.open(jangkar , "viewWin", 'height='+ h + ', width='+ w + 
			', screenX='+ nX +',left='+ nX+ ',screenY='+nY+',top='+nY +
			', menubar=0, statusbar=0, resizeable=yes, scrollbars=yes');
		viewWin.focus();  
	}

	function closeRemote() {	
		window.opener.location.reload();
		<!--timer = setTimeout('window.close();', 10);-->		
	}

	<!--script>closeRemote();</script-->
</script>

<c:if test="${forward.name == 'select'}" >
  <script>closeRemote();</script>
</c:if>
<html>
<head>
    <title><fmt:message key='general.label.popupFormWindow.pleaseSelect'/></title>
    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">
</head>
    <body onload="focus()">

		<x:display name="Manpower.table" ></x:display>

	</body>
</html>