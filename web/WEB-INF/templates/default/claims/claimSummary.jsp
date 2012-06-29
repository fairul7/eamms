<%@ include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%" cellpadding="0" cellspacing="0">
<tr>
<td><b><fmt:message key="claims.label.expensesSummary"/> <c:out value="${w.year}"/></b></td>
</tr>
<tr>
<td class="tableBackground">

<table width="100%" cellpadding="4" cellspacing="1">
<%-- header --%>
<tr>
    <td class="contentTitleFont">&nbsp;</td>
    <c:forEach var="m" items="${w.monthName}">
    <td class="contentTitleFont"><b><c:out value="${m}"/></b></td>
    </c:forEach>
    <td class="contentTitleFont"><fmt:message key='claims.label.total'/></td>
</tr>
<%
    int i=0;
%>
<c:forEach var="c" items="${w.categoryName}">
    <%
        String iStr=""+i;
    %>
    <c-rt:set var="i" value="<%=iStr%>"/>
    <tr>
        <td class="tableRow"><b><c:out value="${c}"/></b></td>
        <c:forEach var="t" items="${w.monthListStr[i]}">
            <td bgcolor="#EFEFEF" class="tableRow" align="right">
                <c:if test="${t=='0.00'}">-</c:if>
                <c:if test="${t!='0.00'}"><c:out value="${t}"/></c:if>
            </td>
        </c:forEach>
        <td bgcolor="#EFEFEF" class="tableRow" align="right"><c:out value="${w.categoryTotalStr[i]}"/></td>
    </tr>
    <%
        i++;
    %>
</c:forEach>
<tr>
    <td class="tableRow">&nbsp;</td>
    <c:forEach var="total" items="${w.monthTotalStr}">
        <td bgcolor="#EFEFEF" class="tableRow" align="right"><c:out value="${total}"/></td>
    </c:forEach>
    <td bgcolor="#EFEFEF" class="tableRow" align="right"><c:out value="${w.grandTotalStr}"/></td>
</tr>
</table>

</td>
</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>