<%@ include file="/common/header.jsp" %>
<c:set var="form" value="${widget}"/>

<jsp:include page="../../form_header.jsp" flush="true"/>
<table align="center" width="80%" cellpadding="2" cellspacing="1" class="borderTable">
	<tr>
		<th class="tableHeader" width="25%">&nbsp;</th>
		<th class="tableHeader" width="20%">&nbsp;</th>
		<th class="tableHeader" width="25%" align="left"><b><fmt:message key='fms.tran.notSatisfied'/></b></th>
		<th class="tableHeader" width="30%" align="right"><b><fmt:message key='fms.tran.excellent'/></b>&nbsp;&nbsp;</th>
	</tr>
	<tr>
		<td class="classRowLabel">
			<b><fmt:message key='fms.tran.supportQuality'/></b>
		</td>
		<td class="classRowLabel">
			<b><x:display name="${form.quality[0].absoluteName}"/></b>
		</td>
		<td class="classRowLabel">
			<c:forEach items="${form.quality}" var="quality" varStatus="stat">
				<c:if test="${stat.index>0 && stat.index<4}">
					<b><x:display name="${form.quality[stat.index].absoluteName}"/></b> 
					&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
			</c:forEach>
		</td>
		<td class="classRowLabel">
			<c:forEach items="${form.quality}" var="quality" varStatus="stat">
				<c:if test="${stat.index>3}">
					<b><x:display name="${form.quality[stat.index].absoluteName}"/></b> 
					&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel"><b><fmt:message key='fms.tran.driverPerformance'/></b></td>
		<td class="classRowLabel">
			<b><x:display name="${form.driver[0].absoluteName}"/></b>
		</td>
		<td class="classRowLabel">
			<c:forEach items="${form.driver}" var="driver" varStatus="stat">
				<c:if test="${stat.index>0 && stat.index<4}">
					<b><x:display name="${form.driver[stat.index].absoluteName}"/></b> 
					&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
			</c:forEach>
		</td>
		<td class="classRowLabel">
			<c:forEach items="${form.driver}" var="driver" varStatus="stat">
				<c:if test="${stat.index>3}">
					<b><x:display name="${form.driver[stat.index].absoluteName}"/></b> 
					&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel"><b><fmt:message key='fms.tran.customerService'/></b></td>
		<td class="classRowLabel">
			<b><x:display name="${form.customer[0].absoluteName}"/></b>
		</td>
		<td class="classRowLabel">
			<c:forEach items="${form.customer}" var="customer" varStatus="stat">
				<c:if test="${stat.index>0 && stat.index<4}">
					<b><x:display name="${form.customer[stat.index].absoluteName}"/></b> 
					&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
			</c:forEach>
		</td>
		<td class="classRowLabel">
			<c:forEach items="${form.customer}" var="customer" varStatus="stat">
				<c:if test="${stat.index>3}">
					<b><x:display name="${form.customer[stat.index].absoluteName}"/></b> 
					&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel"><b><fmt:message key='fms.tran.availability'/></b></td>
		<td class="classRowLabel">
			<b><x:display name="${form.availability[0].absoluteName}"/></b>
		</td>
		<td class="classRowLabel">
			<c:forEach items="${form.availability}" var="availability" varStatus="stat">
				<c:if test="${stat.index>0 && stat.index<4}">
					<b><x:display name="${form.availability[stat.index].absoluteName}"/></b> 
					&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
			</c:forEach>
		</td>
		<td class="classRowLabel">
			<c:forEach items="${form.availability}" var="availability" varStatus="stat">
				<c:if test="${stat.index>3}">
					<b><x:display name="${form.availability[stat.index].absoluteName}"/></b> 
					&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td class="classRowLabel"><b><fmt:message key='fms.tran.vehicleCondition'/></b></td>
		<td class="classRowLabel">
			<b><x:display name="${form.condition[0].absoluteName}"/></b>
		</td>
		<td class="classRowLabel">
			<c:forEach items="${form.condition}" var="condition" varStatus="stat">
				<c:if test="${stat.index>0 && stat.index<4}">
					<b><x:display name="${form.condition[stat.index].absoluteName}"/></b> 
					&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
			</c:forEach>
		</td>
		<td class="classRowLabel">
			<c:forEach items="${form.condition}" var="condition" varStatus="stat">
				<c:if test="${stat.index>3}">
					<b><x:display name="${form.condition[stat.index].absoluteName}"/></b> 
					&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
			</c:forEach>
		</td>
	</tr>
</table>
<br>
<table width="80%" cellpadding="2" cellspacing="1" border="0" align="center">
	<tr>
		<td valign="top" align="right"><b><fmt:message key='fms.tran.remarks'/></b></td>
		<td><x:display name="${form.remark.absoluteName}"/></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><x:display name="${form.submit.absoluteName}"/></td>
	</tr>
</table>

<jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>