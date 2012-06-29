<%@ page import="java.util.Collection,
			java.util.Calendar,java.util.Date,
			java.util.Iterator,
			java.util.Map,
			com.tms.collab.resourcemanager.model.Resource,
			com.tms.report.model.ReportObject,
			org.apache.commons.collections.SequencedHashMap"%>
<%@include file="/common/header.jsp"%>

<c:set var="w" value="${widget}" />
<c:set var="startTime" value="${w.startTime}" />
<c:set var="endTime" value="${w.endTime}" />
<c:set var="reportMap" value="${w.reportMap}" />
<c:set var="resourceCol" value="${w.resourceCol}" />

<%
	Map reportMap = (SequencedHashMap) pageContext.getAttribute("reportMap");
	int startTime = ((Integer)pageContext.getAttribute("startTime")).intValue();
	int endTime = ((Integer)pageContext.getAttribute("endTime")).intValue();
	Collection resourceCol = (Collection) pageContext.getAttribute("resourceCol");
%>

<jsp:include page="/WEB-INF/templates/ekms/form_header.jsp" />

<script type="text/javascript">
	function validateCsv(count) {
		if (count > 0)
			generateCsv();
		else
			alert("<fmt:message key='resourcemanager.message.noResourceFound'/>");
	}
</script>

<table border="0" cellpadding="4" cellspacing="4"
	style="border:0px solid gray" width="100%">
	<tbody>
		<tr>
			<td><fmt:message key="taskmanager.label.Date"/></td>
			<td><fmt:message key="calendar.label.from"/>  <x:display name="${w.startDate.absoluteName}" /> <fmt:message key="calendar.label.to"/> <x:display name="${w.endDate.absoluteName}" /></td>
		</tr>
		<tr>
			<td><fmt:message key="resourcemanager.label.Category"/></td>
			<td><x:display name="${w.categoryList.absoluteName}" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<x:display name="${w.submitBtn.absoluteName }"/>
				<input type="button" class="button"
					onclick="location.href='<c:url value="/ekms/resourcemanager/"/>'"
					value="<fmt:message key='general.label.cancel'/>">
				<input type="button" class="button" name="exportCsv" value="Export to CSV" onclick="javascript:validateCsv('<%=resourceCol.size() %>')" />
			</td>
		</tr>
	</tbody>
</table>

<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td>

		<table width="100%" cellpadding="5" cellspacing="1" align=center>

			<thead>
				<c:set var="colspan" value="0" />
				<tr class="tableHeader">
					<td height="25">
						<b><fmt:message key="resourcemanager.label.ResourceName"/></b>
					</td>
					<c:forEach var="i" begin="${startTime}" end="${endTime-1}">
						<td height="25">
							<c:set var="j" value="${i%12}"/>
							<c:set var="j2" value="${j+1}"/>
							<c:if test="${j == 0 }">
								<c:set var="j" value="${12}"/>
							</c:if>
							<c:if test="${j2 == 0 }">
								<c:set var="j2" value="${12}"/>
							</c:if>
							<c:out value="${j}"/>-<c:out value="${j2}"/>
							<c:choose>
								<c:when test="${i+1 < 12 }">
									am
								</c:when>
								<c:otherwise>
									pm
								</c:otherwise>
							</c:choose>
						</td>
					</c:forEach>
				</tr>
			</thead>
			
			<%
				for(Iterator i=resourceCol.iterator(); i.hasNext();){
					Resource re = (Resource) i.next();
					
			%>
			
			<tr>
				<td align="left" class="contentBgColor"><%=re.getName()%></td>
				
				<%
					StringBuffer key = new StringBuffer();
					for(int j=startTime; j<endTime; j++){
						key = new StringBuffer();
						key.append(re.getId()+"_"+j);
						
				%>
					
					<td height="25">
						<%=(reportMap.get(key.toString())!=null)?reportMap.get(key.toString()):"-" %>
						&nbsp;
					</td>
				<%
					}
				%>
				
			</tr>
			
			<%
				}
			%>
			
		</table>
		</td>
	</tr>
</table>

<jsp:include page="/WEB-INF/templates/ekms/form_footer.jsp" />
