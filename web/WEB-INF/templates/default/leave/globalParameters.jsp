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
                    <td class="classRowLabel" valign="top" align="right" nowrap width="30%"><fmt:message key='leave.label.notificationMethod'/></td>
                    <td class="classRow">
                        <x:display name="${form.memoAlert.absoluteName}"/><fmt:message key='leave.label.memo'/><br>
                        <x:display name="${form.emailAlert.absoluteName}"/><fmt:message key='leave.label.email'/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right" nowrap>&nbsp;</td>
                    <td class="classRow"><x:display name="${form.autoIncBalance.absoluteName}"/><fmt:message key='leave.label.autoIncrementLeaveBalance'/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right" nowrap>&nbsp;</td>
                    <td class="classRow">
                        <x:display name="${form.add.absoluteName}"/>
                        <x:display name="${form.reset.absoluteName}"/>
                    </td>
                </tr>
                <jsp:include page="../form_footer.jsp" flush="true"/>
            </table>
        </td>
    </tr>
</table>
