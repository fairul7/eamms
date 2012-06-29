<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="jsp_summary">
        <com.tms.hr.claim.ui.ClaimSummary name="summary"/>
    </page>
</x:config>

<x:permission permission="com.tms.hr.claim.model.Access" module="com.tms.hr.claim.model.ClaimConfigModule" url="noPermission.jsp">

    <c:if test="${!empty param.year}">
        <x:set name="jsp_summary.summary" property="year" value="${param.year}"/>
    </c:if>
    <x:set name="jsp_summary.summary" property="act" value="summary"/>

    <%@include file="/ekms/includes/header.jsp" %>
    <jsp:include page="includes/header.jsp" />

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key="claims.label.claimListing"/> > <fmt:message key="claims.label.summary"/>

            </font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>

        <tr><td colspan="2" valign="TOP"  class="contentBgColor" bgcolor="#00336F" >

            <x:display name="jsp_summary.summary"/>

        </td></tr>

        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor">
            <script>
                function doPrint() {
                    myWin = open("<c:url value="summaryPrint.jsp?year=${param.year}&act=summary"/>","Report","width=800,height=500,toolbar=no,menubar=yes,resizable=yes,scrollbars=yes");
                }
            </script>
            <input type="button" class="button" name="print" value="Print" onClick="doPrint()">
        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>

</x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>
