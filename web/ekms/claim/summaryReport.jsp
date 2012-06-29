<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_summaryReport">
          <com.tms.hr.claim.ui.ClaimSummaryReport name="summaryReport"/>
    </page>
</x:config>


<x:permission permission="com.tms.hr.claim.model.Admin" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">
<c:if test="${!empty param.month}">
<x:set name="jsp_summaryReport.summaryReport" property="month" value="${param.month}"/>
</c:if>
<c:if test="${!empty param.employee}">
<x:set name="jsp_summaryReport.summaryReport" property="selectedEmployee" value="${param.employee}"/>
</c:if>
<c:if test="${empty param.employee}">
<x:set name="jsp_summaryReport.summaryReport" property="selectedEmployee" value=""/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">

			Summary


			</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP"  class="contentBgColor" bgcolor="#00336F" >

<x:display name="jsp_summaryReport.summaryReport"/>

	</td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
    <script>
    function doPrint() {
       myWin = open("<c:url value="summaryReportPrint.jsp?month=${param.month}&employee=${param.employee}"/>","Report","width=800,height=500,toolbar=no,menubar=yes,resizable=yes,scrollbars=yes");
    }
    </script>
<input class=button type=button name="print" value="Print" onClick="doPrint()">
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

    </x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
