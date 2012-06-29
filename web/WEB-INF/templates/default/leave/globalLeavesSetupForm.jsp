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
                    <td class="classRowLabel" valign="top" align="right" nowrap width="30%"><fmt:message key='leave.label.medicalLeave'/></td>
                    <td class="classRow" align="left"><x:display name="${form.medical.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right" nowrap width="30%"><fmt:message key='leave.label.maternityLeave'/></td>
                    <td class="classRow" align="left"><x:display name="${form.maternity.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right" nowrap><fmt:message key='leave.label.paternityLeave'/></td>
                    <td class="classRow" align="left"><x:display name="${form.paternity.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right" nowrap><fmt:message key='leave.label.compassionateLeave'/></td>
                    <td class="classRow" align="left" ><x:display name="${form.compassionate.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
                    <td class="classRow" align="left">
                        <x:display name="${form.add.absoluteName}"/>
<%--                        <x:display name="${form.reset.absoluteName}"/>--%>
                        <x:display name="${form.cancel.absoluteName}"/>
                    </td>
                </tr>
                <jsp:include page="../form_footer.jsp" flush="true"/>
            </table>
        </td>
    </tr>
</table>
