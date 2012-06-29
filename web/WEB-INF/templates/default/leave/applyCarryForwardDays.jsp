<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
    <tr>
        <td>
            <table width="100%" cellpadding="3" cellspacing="1">
                <jsp:include page="../form_header.jsp" flush="true"/>
                    <tr>
                        <td class="classRowsLabel" align="right" valign="top" width="30%"><fmt:message key='leave.label.currentBalance'/></td>
                        <td class="classRows"><x:display name="${form.currentBalance.absoluteName}"/></td>
                    </tr>
                    <tr>
                        <td class="classRowsLabel" align="right" valign="top" width="30%"><fmt:message key='leave.label.carryForwardMaxDays'/></td>
                        <td class="classRows"><x:display name="${form.carryForwardMaxDays.absoluteName}"/></td>
                    </tr>
                    <tr>
                        <td class="classRowsLabel" align="right" valign="top" width="30%"><fmt:message key='leave.label.carryForwardDays'/></td>
                        <td class="classRows"><x:display name="${form.carryForwardDays.absoluteName}"/></td>
                    </tr>
                    <tr>
                        <td class="classRowsLabel" align="right" valign="top" width="30%">&nbsp;</td>
                        <td class="classRows"><x:display name="${form.apply.absoluteName}"/></td>
                    </tr>
                <jsp:include page="../form_footer.jsp" flush="true"/>
            </table>
        </td>
    </tr>
</table>
