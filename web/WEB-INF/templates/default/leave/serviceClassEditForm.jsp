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
                    <td class="classRowLabel" valign="top" align="right" nowrap><fmt:message key='leave.label.classificationCode'/></td>
                    <td class="classRow" align="left"><x:display name="${form.codeLabel.absoluteName}"/><x:display name="${form.serviceCode.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right" nowrap><fmt:message key='leave.label.classificationDesc'/></td>
                    <td class="classRow" align="left"><x:display name="${form.description.absoluteName}"/></td>
                </tr>
                <tr>
                    <td class="classRowLabel" valign="top" align="right" nowrap>&nbsp;</td>
                    <td class="classRow" align="left">
                        <x:display name="${form.save.absoluteName}"/>
                        <x:display name="${form.cancel.absoluteName}"/>
                    </td>
                </tr>
                <jsp:include page="../form_footer.jsp" flush="true"/>
            </table>
        </td>
    </tr>
</table>