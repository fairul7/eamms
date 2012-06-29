<%@ page import="kacang.stdui.Form"%>
<%@include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="2" cellspacing="1" width="100%" class="portletBackground">
    <tr><td colspan="2" class="portletHeader"><fmt:message key='portlet.message.preferences'/></td></tr>
    <tr><td colspan="2" class="portletLabel">&nbsp;</td></tr>
    <c:choose>
        <c:when test="${empty form.update}">
            <tr><td class="portletLabel" colspan="2"><fmt:message key='portlet.message.noEditablePreferences'/></td></tr>
        </c:when>
        <c:otherwise>
            <c:forEach items="${form.childMap}" var="child">
                <c:if test="${!(child.key == 'update' || child.key == 'cancel')}">
                    <tr>
                        <td class="portletLabel"><c:out value="${child.key}"/></td>
                        <td class="portletLabel"><x:display name="${child.value.absoluteName}"/></td>
                    </tr>
                </c:if>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    <tr><td colspan="2" class="portletRow">&nbsp;</td></tr>
    <tr>
        <td colspan="2" class="portletLabel">
            <c:if test="${!(empty form.update)}">
                <x:display name="${form.update.absoluteName}"/>
            </c:if>
            <x:display name="${form.cancel.absoluteName}"/>
        </td>
    </tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
