<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}"/>

<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" cellpadding="4" cellspacing="2">
<tr>
	<td class="calendarHeader">
	<fmt:message key="taxonomy.title.editForm"/>
	</td>
</tr>
<tr>
 	<td class="contentBgColor">
 		<table width="100%" cellpadding="0" cellspacing="0">
 		<tr>
 		<td bgcolor="#FFFFFF">
 	
 		<table width="100%" cellpadding="4" cellspacing="1">
 		<c:if test="${! empty w.parentId }">
 		<tr>
 			<td class="contentBgColor" align="right">
 			<b><fmt:message key="taxonomy.label.parentNodeName"/></b>
 			</td>
 			<td class="contentBgColor">
 			<c:if test="${empty w.parentNode.taxonomyName || w.parentNode.taxonomyName==''}">
 			-
 			</c:if>
 			<c:out value="${w.parentNode.taxonomyName }"/>	
 			</td>
 		</tr>
 		</c:if>
 		<tr>
 			<td class="contentBgColor" align="right">
 			<b><fmt:message key="taxonomy.label.nodeName"/></b> <font color="#ff0000">*</font>
 			</td>
 			<td class="contentBgColor">
 			<x:display name="${w.node.absoluteName }"/>
 			</td>
 		</tr>
 		<tr>
 			<td class="contentBgColor" align="right" valign="top">
 			<b><fmt:message key="taxonomy.label.nodeDescription"/></b>	<font color="#ff0000">*</font>
 			</td>
 			<td class="contentBgColor">
 			<x:display name="${w.nodeDescription.absoluteName }"/>
 			</td>
 		</tr>
 		<tr>
 			<td class="contentBgColor" align="right" valign="top">
 			<b><fmt:message key="taxonomy.label.nodeSynonym"/></b>
 			</td>  
 			<td class="contentBgColor">
 			<x:display name="${w.nodeSynonym.absoluteName }"/>
 			</td>
 	 	</tr>
 	 	<tr>
 	 		<td class="contentBgColor" align="right">
 	 		<b><fmt:message key="taxonomy.label.nodeShown"/></b> 
 	 		</td>
 	 		<td class="contentBgColor">
 	 		<x:display name="${w.cbShown.absoluteName }"/><b><fmt:message key="taxonomy.label.nodeShown"/></b>
 	 		</td>
 	 	</tr>
 	 	<tr>
 	 	 	<td class="contentBgColor" align="right">
 	 	 	
 	 	 	</td>
 	 	 	<td class="contentBgColor">
 	 	 	<x:display name="${w.btnSubmit.absoluteName }"/>
 	 	 	<x:display name="${w.btnDelete.absoluteName }"/>
 	 	 	<x:display name="${w.btnDeleteChild.absoluteName }"/>
 	 	 	<x:display name="${w.btnCancel.absoluteName }"/>
 	 	 	</td>
 	 	</tr>
 	 	</table>
 	 	
 	 	</td>
 	 	</tr>
 	 	</table>
 	</td>
</tr>
</table>
<jsp:include page="../form_footer.jsp" flush="true"/>