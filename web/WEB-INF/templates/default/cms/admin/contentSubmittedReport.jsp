<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="kacang.tld" prefix="x"%>

<c:set var="w" scope="request" value="${widget}" />

<jsp:include page="../../form_header.jsp" flush="true" />

<link rel="stylesheet"
	href="<%= request.getContextPath() %>/common/tree/tree.css">

<script language="javascript"
	src="<%= request.getContextPath() %>/common/tree/tree.js">
</script>

<table border=0 cellspacing=2 cellpadding=1>
	<tr>
		<td>Date</td>
		<td>From <x:display name="${w.startDate.absoluteName}" /> To <x:display
			name="${w.endDate.absoluteName}" /></td>
	</tr>
	<tr>
		<td>Group</td>
		<td><x:display name="${w.selGroup.absoluteName}" /></td>
	</tr>
	<tr>
		<td valign="top">Contents in</td>
		<td>

		<table>
			<tr>
				<td valign=top><c:choose>
					<c:when test="${!empty w.root.children}">
						<a
							style="border:solid 1px gray; margin:0px; padding: 2px; text-align:center; width:14px; font-size:12px; line-height: 8px"
							href="#"
							onclick="treeToggle('<c:out value="${w.root.id}"/>'); return false"><span
							id="icon_<c:out value="${w.root.id}"/>">-</span></a>
					</c:when>
					<c:otherwise>
						<span
							style="border:solid 1px gray; margin:0px; padding: 2px; text-align:center; width:14px; font-size:5px; line-height: 8px">-</span>
					</c:otherwise>
				</c:choose></td>
				<td><x:display name="${w.root.chkBx.absoluteName}" /><c:out
					value="${w.root.name}" /> <span id="<c:out value="${w.root.id}"/>"
					style="display: block"> <c:set var="orig"
					value="${w.root.children}" scope="request" /> <jsp:include
					page="contentTreeRecur.jsp" flush="true" /> </span></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;
		</td>
		<td>
			<x:display name="${w.submitBtn.absoluteName}" />
			<input type="button" class="button"
				onclick="location.href='systemSettings.jsp'"
				value="<fmt:message key='general.label.cancel'/>">
			<input type="button" class="button" name="exportCsv" value="Export to CSV" onclick="javascript:generateCsv()" />
		</td>
	</tr>
</table>
<br />
<table width="100%" cellpadding="4" cellspacing="1">
	<tr>
		<td class="tableHeader" rowspan="2">
			Content
		</td>
		<td class="tableHeader" rowspan="2">
			Group
		</td>
		<td class="tableHeader" align="middle" colspan="2">
			No. Of Users
		</td>
	</tr>
	<tr>
		<td class="tableHeader">
			Unique
		</td>
		<td class="tableHeader">
			Total
		</td>
	</tr>
	<c:forEach var="v" items="${w.results}">
	<tr>
		<td><c:out value="${v.sectionName}"/>
		</td>
		<td><c:out value="${v.groupName}"/>
		</td>
		<td>
			
			<a href="#" onclick="javascript:window.open('contentSubmittedReportDetails.jsp?sectionId=<c:out value='${v.contentSectionId}'/>&groupId=<c:out value='${v.groupId}'/>', 'profileWindow', 'height=300,width=250,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes');return false;">
			<c:out value="${v.uniqueCount}"/>
			</a>
		</td>
		<td><c:out value="${v.totalCount}"/>
		</td>
	</tr>
	</c:forEach>
</table>

<jsp:include page="../../form_footer.jsp" flush="true" />
