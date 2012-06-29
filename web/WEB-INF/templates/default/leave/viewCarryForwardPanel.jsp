<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="panel" value="${widget}"/>
<c:if test="${!(empty requestScope.message)}">
    <script>alert("<c:out value="${requestScope.message}"/>");</script>
</c:if>
<jsp:include page="../panel_header.jsp" flush="true"/>
<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
    <tr>
        <td>
            <table width="100%" cellpadding="3" cellspacing="1">
                <tr>
                    <td class="classRowLabel" align="right" nowrap><fmt:message key='leave.label.balance'/></td>
                    <td class="classRow" aligh="left"><x:display name="${panel.balance.absoluteName}"/>
                </tr>
                <tr>
                    <td class="classRowLabel" align="right" nowrap><fmt:message key='leave.label.applyDate'/></td>
                    <td class="classRow" aligh="left"><x:display name="${panel.applyDate.absoluteName}"/>
                </tr>
                <tr>
                    <td class="classRowLabel" align="right" nowrap><fmt:message key='leave.label.appliedDays'/></td>
                    <td class="classRow" aligh="left"><x:display name="${panel.appliedDays.absoluteName}"/>
                </tr>
                <tr>
                    <td class="classRowLabel" align="right" nowrap><fmt:message key='leave.label.approvedDays'/></td>
                    <td class="classRow" aligh="left"><x:display name="${panel.approvedDays.absoluteName}"/>
                </tr>
            </table>
        </td>
    </tr>
</table>
<jsp:include page="../panel_footer.jsp" flush="true"/>
