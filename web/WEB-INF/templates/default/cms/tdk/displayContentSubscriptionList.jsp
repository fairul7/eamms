<%@ include file="/common/header.jsp" %>

<c:set var="contentMap" value="${widget.contentMap}"/>

<table class="profileTable" width="100%" border="0" cellpadding="2" cellspacing="1">
<form>
<input type="hidden" name="action" value="unsubscribe">
<tr>
    <td>&nbsp;</td>
    <td align="right"><input class="button" type="submit" value="<fmt:message key="security.label.unsubscribe"/>"><br></td>
</tr>
<c:forEach items="${contentMap}" var="item">
    <c:set var="className" value="${item.key}"/>
    <c:set var="contentList" value="${item.value}"/>
    <tr>
        <td colspan="2" class="profileHeader"><b><fmt:message key="cms.label_${className}"/></b></td>
    </tr>
    <c:if test="${empty contentList[0]}">
    <tr>
        <td class="profileRow" colspan="2"><fmt:message key="general.label.none"/></td>
    </tr>
    </c:if>
    <c:forEach items="${contentList}" var="co">
    <tr>
        <td class="profileRow" width="90%"><li><a href="<c:url value='content.jsp?id=${co.id}'/>"><c:out value="${co.name}"/></a></li></td>
        <td valign="top" class="profileRow" width="10%" align="right"><input type="checkbox" name="id" value="<c:out value="${co.id}"/>"></td>
    </tr>
    </c:forEach>
    <tr>
        <td colspan="2" class="profileRow">&nbsp;</td>
    </tr>
</c:forEach>
</form>
</table>