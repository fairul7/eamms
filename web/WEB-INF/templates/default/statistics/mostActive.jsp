<%@ include file="/common/header.jsp" %>
<jsp:include page="../form_header.jsp" flush="true"/>
<jsp:include page="../form.jsp" flush="true"/>
<jsp:include page="../form_footer.jsp" flush="true"/>
<c:if test="${!empty widget.mostActive}">
	<table cellpadding="5" cellspacing="1" border="0" align="center">
		<tr>
			<td nowrap class="classRowLabel">Most Active User</td>
			<td nowrap class="classRowLabel">Score</td>
		</tr>
		<c:forEach var="item" items="${widget.mostActive}">
			<tr>
				<td nowrap class="classRow"><c:out value="${item.key.name}"/></td>
				<td nowrap class="classRow"><c:out value="${item.value}"/></td>
			</tr>
		</c:forEach>
	</table>
</c:if>
