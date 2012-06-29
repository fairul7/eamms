<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="co" value="${widget.contentObject}"/>
<table cellpadding="3" cellspacing="0" width="100%">
    <tr><td class="contentBody">&nbsp;</td></tr>
    <tr><td class="contentBody"><c:out value="${co.summary}" escapeXml="false" /></td></tr>
    <tr><td class="contentBody">&nbsp;</td></tr>
</table>
