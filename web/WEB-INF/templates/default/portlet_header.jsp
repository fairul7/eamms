<%@ page import="kacang.runtime.*, kacang.ui.*,
                 kacang.stdui.Portlet" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="portlet" value="${widget}"/>

<table style="border: 1px ridge silver" cellpadding="4" cellspacing="0" width="<c:out value="${portlet.width}"/>">
<tr>
<td style="background: #003366; color: white; font-weight: bold"><c:out value="${portlet.text}"/>&nbsp;</td>
<td style="background: #003366; color: white; font-weight: bold" align="right">
<span style="border:1px ridge silver; padding:2;">
<c:choose>
    <c:when test="${portlet.minimized}">
        <x:event name="${portlet.absoluteName}" type="<%= Portlet.PARAMETER_KEY_MAXIMIZE %>"><span style="background: #003366; color: white; font-weight: bold">^</span></x:event>
    </c:when>
    <c:otherwise>
        <x:event name="${portlet.absoluteName}" type="<%= Portlet.PARAMETER_KEY_MINIMIZE %>"><span style="background: #003366; color: white; font-weight: bold">_</span></x:event>
    </c:otherwise>
</c:choose>
</span>
<c:choose>
    <c:when test="${!portlet.permanent}">
        <span style="border:1px ridge silver; padding:2;">
            <x:event name="${portlet.absoluteName}" type="<%= Portlet.PARAMETER_KEY_CLOSE %>"><span style="background: #003366; color: white; font-weight: bold">X</span></x:event>
        </span>
    </c:when>
    <c:otherwise>
<%--        <span style="background: #003366; color: white; font-weight: bold">X</span>--%>
    </c:otherwise>
</c:choose>

</span>
</td>
</tr>
<tr>
<td colspan="2" style="background: white">



