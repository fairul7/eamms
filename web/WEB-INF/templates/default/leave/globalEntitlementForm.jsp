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
                <tr><td class="classRow" align="center" colspan="4"><font color='#FF0000'><x:display name="${form.errorMsg.absoluteName}"/> </font></td></tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='leave.label.serviceClass'/></td>
                    <td class="classRow" align="left" colspan="3"><x:display name="${form.serviceClass.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='leave.label.experienceFrom'/></td>
                    <td class="classRow" valign="top" align="left" width="7%"><x:display name="${form.yearFrom.absoluteName}"/> </td>
                    <td class="classRowLabel" valign="top" align="right" width="5%"><fmt:message key='leave.label.to'/></td>
                    <td class="classRow" valign="top" align="left"> <x:display name="${form.yearTo.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right"><fmt:message key='leave.label.entitlement'/></td>
                    <td class="classRow" align="left" colspan="3"><x:display name="${form.entitlement.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
                    <td class="classRow" valign="top" align="center" colspan ="3">
                        <x:display name="${form.add.absoluteName}"/>
                        <x:display name="${form.reset.absoluteName}"/>
                        <x:display name="${form.cancel.absoluteName}"/>
                    </td>
                </tr>
                <jsp:include page="../form_footer.jsp" flush="true"/>
            </table>
        </td>
    </tr>
</table>
