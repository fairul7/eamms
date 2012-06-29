<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>

<table align="left" width="100%">
<tr>
	<td><jsp:include page="../../form_header.jsp"/></td>
</tr>
	<tr>
		<td align="left">
			<table border="0" width="100%" cellspacing="0" cellpadding="2">
				<c:if test="${!empty form.childMap['pageSizeSelectBox'].absoluteName}">
				<tr>
					<td></td>
					<td >
						<x:display name="${form.childMap['pageSizeSelectBox'].absoluteName}" />
					</td>
					
					<td></td>
					<td></td>
				</tr>
				</c:if>
				
				<tr>
					<td align="right"><b><fmt:message key='fms.report.message.label.bookingdate'/> <fmt:message key="general.from" /></b></td>
					<td><x:display name="${form.childMap['bookingDateFrom'].absoluteName}" /></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td align="right"><b><fmt:message key='fms.report.message.label.bookingdate'/> <fmt:message key="general.to" /></b></td>
					<td><x:display name="${form.childMap['bookingDateTo'].absoluteName}" /></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td align="right"><b><fmt:message key='fms.request.label.requestType'/></b></td>
					<td>
						<x:display name="${form.childMap['internalChbx'].absoluteName}" /><fmt:message key="fms.facility.requestType.I"/>
						<x:display name="${form.childMap['externalChbx'].absoluteName}" /><fmt:message key="fms.facility.requestType.E"/>
						<x:display name="${form.childMap['nonprogramChbx'].absoluteName}" /><fmt:message key="fms.facility.requestType.N"/>
					</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td align="right"><b><fmt:message key="fms.report.message.label.status"/></b></td>
					<td><x:display name="${form.childMap['sbStatus'].absoluteName}" /></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td align="right"><b><fmt:message key="fms.report.message.label.program"/></b></td>
					<td><x:display name="${form.childMap['sbProgram'].absoluteName}" /></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td><b></b></td>
					<td>
						<x:display name="${form.childMap['filter'].absoluteName}" />
						
						<input type="submit" onclick="" value="<fmt:message key='fms.report.message.export'/>" name="button*reqRep.table.export" class="tableButton">
					</td>
					<td></td>
					<td></td>
				</tr>
				
			</table>
		</td>
		
	</tr>
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>
