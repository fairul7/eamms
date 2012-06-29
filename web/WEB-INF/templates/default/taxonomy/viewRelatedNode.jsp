<%@ include file="/common/header.jsp" %>
<c:if test="${!empty widget.nodeList}">
	<table width="100%" cellpadding="0" cellspacing="0">
		<tr><td class="midddleasiahd"><fmt:message key="taxonomy.title.seeAlso"/></td></tr>
		<tr>
			<td class="tsArticleMore">
				<table cellpadding="2" cellspacing="1" width="100%" align="center">
					<% int i=0; %>
					<% String s=""+i; %>
					<c:forEach var="related" items="${widget.taxonomyNodes}">
						<c-rt:set var="i" value="<%=s%>"/>
						<tr>
							<td class="tsArticleMore">
                            <li>
                            <span class="indication">
								<c:forEach var="node" items="${widget.nodeList[i]}">
									<a href="/cmsadmin/content/taxonomyTree.jsp?id=<c:out value="${node.taxonomyId}"/>"><c:out value="${node.name}"/></a>
									<c:if test="${node.taxonomyId!=related.taxonomyId}">
										&nbsp;&gt;&nbsp;
									</c:if>
								</c:forEach>
							</span>
                            </li>
							</td>
						</tr>
					<% i++; s=""+i; %>
					</c:forEach>
				</table>
			</td>
		</tr>
	</table>
</c:if>
