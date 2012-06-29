<%
    if (request.getAttribute("exportedData") != null) {
        //response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/download");
        response.setHeader("Content-Disposition", "attachment; filename=claimReport.csv" );
        out.print(request.getAttribute("exportedData"));
        out.flush();
        return;
    }
%>
<%@ include file="/common/header.jsp" %>



<x:config>
    <page name="generateReport">
          <com.tms.hr.claim.ui.ReportForm name="reportForm" />
    </page>
</x:config>


<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">


<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">

			Claims Report


			</font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">


            <x:display name="generateReport.reportForm" />


	</td></tr>


    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>

    </x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
