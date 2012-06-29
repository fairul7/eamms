<%@ include file="/common/header.jsp" %>

<c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
	<td><jsp:include page="../../form_header.jsp"/></td>
</tr>
<tr>
	<td>
		<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
			<tr>
				<td height="20" colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<table width="95%">
						<thead>
							<tr class="tableHeader">
								<td rowspan="2" align="center"><b><fmt:message key='claims.label.no'/></b></td>																
								<td rowspan="2" align="center"><b><fmt:message key='fms.label.transport.assgId'/></b></td>
								<td rowspan="2" align="center"><b><fmt:message key='fms.facility.form.requestor'/></b></td>
								<td rowspan="2" align="center"><b><fmt:message key='fms.tran.requestProgram'/></b></td>
								<td rowspan="2" align="center"><b><fmt:message key='fms.facility.table.department'/></b></td>
								<td rowspan="2" align="center"><b><fmt:message key='fms.tran.form.vehicleNumber'/></b></td>													
								<td colspan="2" align="center"><b><fmt:message key='fms.tran.checkout'/></b></td>
								<td colspan="2" align="center"><b><fmt:message key='fms.tran.checkin'/></b></td>
								<td rowspan="2" align="center"><b><fmt:message key='fms.tran.petrolCard'/></b></td>
								<td rowspan="2" align="center"><b><fmt:message key='fms.tran.meterStart'/></b></td>
								<td rowspan="2" align="center"><b><fmt:message key='fms.tran.meterEnd'/></b></td>								
								<td rowspan="2" align="center"><b><fmt:message key='fms.tran.remarks'/></b></td>							
							</tr>
							<tr class="tableHeader">
								<td align="center"><b><fmt:message key='fms.facility.form.date'/></b></td>
								<td align="center"><b><fmt:message key='fms.tran.time'/></b></td>
								<td align="center"><b><fmt:message key='fms.facility.form.date'/></b></td>
								<td align="center"><b><fmt:message key='fms.tran.time'/></b></td>
							</tr>
						</thead>
						
						<c:if test="${not empty form.assignments}">
							<tbody>
								<c:forEach items="${form.assignments}" var="assignment" varStatus="stat">
									<tr>
										<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">	
											<c:out value="${stat.index+1}" />
										</td>									
										<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
											<c:out value="${assignment.id}"/><br />					
								        </td>
								        <td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
											<c:out value="${assignment.name}"/><br />					
								        </td>
								        <td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
											<c:out value="${assignment.programName}"/><br />					
								        </td>
								        <td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
											<c:out value="${assignment.department}"/><br />					
								        </td>
								         <td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
											<c:out value="${assignment.vehicle_num}"/><br />					
								        </td>
								        <td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
											<c:choose>
												<c:when test="${(assignment.status eq 'E')}">
													<x:display name="${form.absoluteName}.lbCheckOutDate${stat.index}"/>
												</c:when>
												<c:when test="${(assignment.status eq 'U')}">
													-
												</c:when>
												<c:otherwise>
													<x:display name="${form.absoluteName}.checkOutDate${stat.index}"/>
												</c:otherwise>
											</c:choose>									
												
											<br />					
								        </td>
								        <td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
											<c:choose>
											 	<c:when test="${(assignment.status eq 'E')}">
													<x:display name="${form.absoluteName}.lbCheckOutTime${stat.index}"/>
												</c:when>
												<c:when test="${(assignment.status eq 'U')}">
													-
												</c:when>
												<c:otherwise>
													<x:display name="${form.absoluteName}.checkOutTime${stat.index}"/>
												</c:otherwise>
											</c:choose>
											<br />					
								        </td>									        
								        <td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
											<c:choose>
												<c:when test="${(assignment.status eq 'E')}">
													<x:display name="${form.absoluteName}.lbCompletionDate${stat.index}"/>
												</c:when>
												<c:when test="${(assignment.status eq 'U')}">
													-
												</c:when>
												<c:otherwise>
													<x:display name="${form.absoluteName}.checkInDate${stat.index}"/>
												</c:otherwise>
											</c:choose>									
												
											<br />					
								        </td>							        
								        <td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
											<c:choose>
												<c:when test="${(assignment.status eq 'E')}">
													<x:display name="${form.absoluteName}.lbCheckInTime${stat.index}"/>
												</c:when>
												<c:when test="${(assignment.status eq 'U')}">
													-
												</c:when>
												<c:otherwise>
													<x:display name="${form.absoluteName}.checkInTime${stat.index}"/>
												</c:otherwise>
											</c:choose>
											<br />					
								        </td>									        
								        <td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
											<c:choose>
												<c:when test="${(assignment.status eq 'E')}">
													<c:out value="${assignment.petrolCard}"/>
												</c:when>
												<c:when test="${(assignment.status eq 'U')}">
													-
												</c:when>
												<c:otherwise>
													<x:display name="${form.absoluteName}.petrolCard${stat.index}"/>
												</c:otherwise>
											</c:choose>
											<br />					
								        </td>										
										<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">																		
											<c:choose>
												<c:when test="${(assignment.status eq 'E') || (assignment.status eq 'U')}">
													<c:out value="${assignment.meterStart}"/>
												</c:when>
												<c:otherwise>
													<x:display name="${form.absoluteName}.meterStart${stat.index}"/>
												</c:otherwise>
											</c:choose>
											<br />		
								        </td>	
								        
								        <td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
											<c:choose>
												<c:when test="${(assignment.status eq 'E') || (assignment.status eq 'U')}">
													<c:out value="${assignment.meterEnd}"/>
												</c:when>
												<c:otherwise>
													<x:display name="${form.absoluteName}.meterEnd${stat.index}"/>
												</c:otherwise>
											</c:choose>
											<br />					
								        </td>	
										<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
											<c:choose>
												<c:when test="${(assignment.status eq 'E') || (assignment.status eq 'U')}">
													<c:out value="${assignment.remarks}"/>
												</c:when>
												<c:otherwise>
													<x:display name="${form.absoluteName}.remarks${stat.index}"/>
												</c:otherwise>
											</c:choose>
											<br />	
								        </td>								       							       	
								    </tr>				
								</c:forEach>
							
								<tr>
									<td colspan="13">
										<div align="center">
											<x:display name="${form.buttonPanel.absoluteName}" />
										</div>
									</td>
								</tr>
							</tbody>
						</c:if>

					</table>
				</td>
			</tr>
		</table>		
	</td>
</tr>
</table>