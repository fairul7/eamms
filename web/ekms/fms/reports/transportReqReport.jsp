<%@ include file="/common/header.jsp" %>
<head>
<link rel="stylesheet" href="/contentSyndicationToMiti/images/style.css">
</head>
<x:config>
    <page name="reporttable">         
            <com.tms.fms.reports.ui.TransportReqReportTable name="transport" width="100%"/>        
    </page>
</x:config>


<c:if test="${forward.name == 'export'}">
	<c:redirect url="exportTransport.jsp"/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table border="0" cellspacing="0" cellpadding="5" bgcolor="white" width="100%">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
        	<fmt:message key="fms.report.message.label.transportrequestlisting"/> <fmt:message key="fms.report.message.label.report"/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="reporttable.transport" />
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>



<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>