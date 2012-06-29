<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_reportForm">
        <com.tms.hr.claim.ui.ClaimReportForm name="reportForm"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Admin" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

    <c:if test="${!empty param.reportType}">
        <x:set name="jsp_reportForm.reportForm" property="reportType" value="${param.reportType}"></x:set>
    </c:if>
    <c:if test="${empty param.reportType}">
        <x:set name="jsp_reportForm.reportForm" property="reportType" value=""/>
    </c:if>

    <c:if test="${forward.name=='summary'}">
        <c:redirect url="reportForm.jsp?reportType=summary"/>
    </c:if>

    <c:if test="${forward.name=='history'}">
        <c:redirect url="claimHistory.jsp"/>
    </c:if>

    <c:if test="${forward.name=='generic'}">
        <c:redirect url="reportForm.jsp?reportType=generic"/>
    </c:if>
    <c:if test="${forward.name=='selectReport'}">
        <script>
            alert("Please select a report type.");
        </script>
    </c:if>

    <%@include file="/ekms/includes/header.jsp" %>
    <jsp:include page="includes/header.jsp" />

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="claims.label.claimAdmin"/> > <fmt:message key="claims.label.generateReport"/>

            </font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP"  class="contentBgColor" bgcolor="#00336F" >

            <x:display name="jsp_reportForm.reportForm"/>

        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>

</x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
