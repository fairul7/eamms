<%@ include file="/common/header.jsp" %>

<c:set var="form" value="${widget}"/>

<ul id="TickerVertical">
<c:choose>

<c:when test="${not empty form.col}">
	
			<c:forEach items="${form.col}" var="facility" varStatus="stat">
				<li>
					<span class="Request bg-white">
						<strong><c:out value="${facility.title}"/></strong><br />
						    <em>
								<fmt:message key='fms.setup.form.producer'/>:&nbsp;
								<c:choose>
									<c:when test="${not empty facility.producer}">
										<c:out value="${facility.producer}"/><br />
									</c:when>
									<c:otherwise>
										-<br />
									</c:otherwise>
								</c:choose>
								<fmt:message key='fms.facility.label.requiredDate'/>:&nbsp; 
									<fmt:formatDate pattern="dd MMM yyyy" value="${facility.requiredFrom}"/> - 
									<fmt:formatDate pattern="dd MMM yyyy" value="${facility.requiredTo}"/><br />
						    	<fmt:message key='fms.request.label.requestId'/>:&nbsp;<c:out value="${facility.requestId}"/></em>	
					</span>	
					<span class="AssignmentDetails bg-white">
						<c:choose>
							<c:when test="${not empty facility.assignments}">
								<table width="100%" border="0" cellspacing="1" cellpadding="8">
								<c:forEach items="${facility.assignments}" var ="asg">				
							      <tr>
							        <td width="30%" valign="top" class="details-bg">
							        	<fmt:formatDate pattern="dd MMM yyyy" value="${asg.requiredFrom}"/> - <fmt:formatDate pattern="dd MMM yyyy" value="${asg.requiredTo}"/><br />
										<c:out value="${asg.fromTime}"/> - <c:out value="${asg.toTime}"/>
							        </td>
							        <td width="20%" valign="top" class="details-bg">
							        	<c:out value="${asg.competencyName}"/>
									</td>
							        <!-- td width="15%" valign="top" class="details-bg">Location</td-->
							        <td valign="top" class="details-bg">
										<c:choose>
											<c:when test="${not empty asg.assignee && asg.assignee != ''}">
												<c:out value="${asg.assignee}" />
											</c:when>
											<c:otherwise>
												<span class="notask"><fmt:message key="fms.facility.msg.notAssignedYet"/></span>
											</c:otherwise>
										</c:choose>
									</td>
							      </tr>
								</c:forEach>
							    </table>
							</c:when>
							<c:otherwise>
								<span class="notask"><fmt:message key="fms.facility.msg.assignmentNotCreated"/></span>
							</c:otherwise>
						</c:choose>	
					</span>
				</li>
			</c:forEach>	
</c:when>
<c:otherwise>
	<li>
		<span><div style="height:50px;padding:20px;text-align:center;font-size: 14px;font-weight: bold;">No assignment information available</div></span>
	</li>
</c:otherwise>
</c:choose>
</ul>