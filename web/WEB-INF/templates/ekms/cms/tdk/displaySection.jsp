<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="co" value="${widget.contentObject}"/>
<table cellpadding="3" cellspacing="0" width="100%">
    <c:if test="${!widget.noHeader}">
        <tr><td class="contentHeader"><span class="contentName"><c:out value="${co.name}" escapeXml="false" /></span></td></tr>
        <tr><td class="contentBody">&nbsp;</td></tr>
    </c:if>
    <c:if test="${!(empty co.summary)}">
        <tr><td class="contentBody"><span class="contentBody"><c:out value="${co.summary}" escapeXml="false" /></span></td></tr>
        <tr><td class="contentBody"><hr size="1"></td></tr>
    </c:if>
    <tr>
        <td class="contentBody" align="center">
            <table cellpadding="0" cellspacing="0" width="95%">
                <tr>
                    <td>
                        <x:template name="DisplayContentChildren" type="com.tms.cms.tdk.DisplayContentChildren" properties="id=${co.id}&types=${widget.types}" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr><td class="contentBody">&nbsp;</td></tr>
</table>
