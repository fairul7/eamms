<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="co" value="${widget.contentObject}"/>
<table cellpadding="0" cellspacing="1" width="100%">
    <tr><td class="contentHeader"><span class="contentName"><c:out value="${co.name}"/></span></td></tr>
    <tr><td><span class="contentDate"><fmt:formatDate pattern="${globalDatetimeLong}" value="${co.date}"/></span></td></tr>
    <tr><td><span class="contentAuthor"><c:out value="${co.author}"/></td></tr>
    <tr><td><hr size="1"></td></tr>
    <tr><td><span class="contentBody"><c:out value="${co.summary}"/></span></td></tr>
    <tr>
        <td>
            <x:template name="DisplayContentChildren" type="com.tms.cms.tdk.DisplayContentChildren" properties="id=${co.id}&page=${param.page}&types=${widget.types}" />
        </td>
    </tr>
</table>
