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
                <tr><td class="classRow" colspan=2 align='center'><font color='#FF0000'><x:display name="${form.errorMsg.absoluteName}"/></font></td></tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='leave.label.leaveTypes'/></td>
                    <td class="classRow"><x:display name="${form.leaveType.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='leave.label.startDate'/></td>
                    <td class="classRow"><x:display name="${form.startDate.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='leave.label.endDate'/></td>
                    <td class="classRow"><x:display name="${form.endDate.absoluteName}"/>
<%--                    &nbsp;&nbsp; <x:display name="${form.halfDay.absoluteName}"/>--%>
                    </td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='leave.label.reason'/></td>
                    <td class="classRow"><x:display name="${form.reason.absoluteName}"/></td>
                </tr>
<%--
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='leave.label.contactNo'/></td>
                    <td class="classRow"><x:display name="${form.contactNo.absoluteName}"/></td>
                </tr>
--%>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
                    <td class="classRow">
                        <x:display name="${form.submitButton.absoluteName}"/>
                        <x:display name="${form.resetButton.absoluteName}"/>
                    </td>
                </tr>
                <jsp:include page="../form_footer.jsp" flush="true"/>
            </table>
        </td>
    </tr>
</table>



