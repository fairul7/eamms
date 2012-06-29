<%@ page import="kacang.runtime.*, kacang.ui.*,
                 kacang.stdui.Portlet" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="portlet" value="${widget}"/>
<table cellpadding="3" cellspacing="0" width="<c:out value="${portlet.width}"/>">
    <tr>
        <td class="classHeader2" width="98%"><b><c:out value="${portlet.text}"/></b>&nbsp;<hr size="1"></td>
        <td class="classHeader2" align="right">
            <span>
                <c:choose>
                    <c:when test="${portlet.minimized}">
                        <x:event name="${portlet.absoluteName}" type="<%= Portlet.PARAMETER_KEY_MAXIMIZE %>"><span class="classHeader2">^</span></x:event>
                    </c:when>
                    <c:otherwise>
                        <x:event name="${portlet.absoluteName}" type="<%= Portlet.PARAMETER_KEY_MINIMIZE %>"><span class="classHeader2">_</span></x:event>
                    </c:otherwise>
                </c:choose>
            </span>
            <c:choose>
                <c:when test="${!portlet.permanent}">
                    <span>
                        <x:event name="${portlet.absoluteName}" type="<%= Portlet.PARAMETER_KEY_CLOSE %>"><span style="background: #003366; color: white; font-weight: bold">X</span></x:event>
                    </span>
                </c:when>
                <c:otherwise>
                    <%--<span style="background: #003366; color: white; font-weight: bold">X</span>--%>
                </c:otherwise>
            </c:choose>
            &nbsp;
        </td>
    </tr>
    <tr>
        <td colspan="2" class="classRow">



