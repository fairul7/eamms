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
                <tr><td class="classRow" colspan=2 align='center'><font color='#FF0000'><x:display name="${form.childMap.errorMsg.absoluteName}"/></font></td></tr>
                <tr>
                    <td class="classRowLabel" valign="top" width="30%" align="right"><fmt:message key='employee.label.user'/></td>
                    <td class="classRow" align="left"><c:out value="${form.employee.name}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='employee.label.gender'/></td>
                    <td class="classRow" align="left" >
                        <x:display name="${form.childMap.male.absoluteName}"/>&nbsp;&nbsp;
                        <x:display name="${form.childMap.female.absoluteName}"/>
                    </td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='employee.label.dateJoined'/></td>
                    <td class="classRow" align="left" ><x:display name="${form.childMap.joinDate.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='employee.label.dateResigned'/></td>
                    <td class="classRow" align="left" ><x:display name="${form.childMap.resignDate.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='employee.label.department'/></td>
                    <td class="classRow" align="left"><x:display name="${form.childMap.department.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='employee.label.serviceClass'/></td>
                    <td class="classRow" align="left"><x:display name="${form.childMap.serviceClass.absoluteName}"/></td>
                </tr>
<%--
                <tr>
                    <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
                    <td class="classRow" align="left"><x:display name="${form.childMap.recommender.absoluteName}"/></td>
                </tr>
--%>
                <c:if test="${!form.childMap.shiftWorker.hidden}">
                    <tr>
                        <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
                        <td class="classRow" align="left"><x:display name="${form.childMap.shiftWorker.absoluteName}"/></td>
                    </tr>
                </c:if>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='employee.label.reportTo'/></td>
                    <td class="classRow" align="left"><x:display name="${form.childMap.reportToUsersList.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
                    <td class="classRow">
                        <x:display name="${form.childMap.add.absoluteName}"/>
                        <x:display name="${form.childMap.cancel_form_action.absoluteName}"/>
                    </td>
                </tr>
                <jsp:include page="../form_footer.jsp" flush="true"/>
            </table>
        </td>
    </tr>
</table>

