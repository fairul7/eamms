 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../../form_header.jsp"/>
</td>
</tr>
<tr>
	<td>
		
			<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
				<!-- tr valign="MIDDLE">
			    	<td height="22" class="contentTitleFont">
			      		&nbsp;<fmt:message key='fms.request.label.facilitiesAvailability'/>  
			        </td>
			    	<td align="right" class="contentTitleFont">&nbsp;</td>
			  	</tr-->
				<tr>
					<td height="20" colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<table width="95%">
						<thead>
							<tr class="tableHeader">
								<td width="5%" align="center"><b><fmt:message key='claims.label.no'/></b></td>
								<td height="25" width="10%" align="center"><b><fmt:message key='fms.request.label.requestTitle'/></b></td>
								<td width="6%" align="center"><b><fmt:message key='fms.request.label.requestId'/></b></td>
								<td width="10%" align="center"><b><fmt:message key='fms.label.assignmentCode'/></b></td>
								<td width="10%" align="center"><b><fmt:message key='fms.facility.table.assignee'/></b></td>
								<td width="20%" align="center"><b><fmt:message key='fms.facility.form.requestor'/></b></td>
								<td width="10%" align="center"><b><fmt:message key='fms.facility.label.requiredTime'/></b></td>
								<td width="5%" align="center"><b><fmt:message key='fms.ratecard.table.label.status'/></b></td>
								<td width="13%" align="center"><b><fmt:message key='fms.request.label.completionDate'/></b></td>
								<td width="17%" align="center"><b><fmt:message key='fms.request.label.completionTime'/></b></td>
							</tr>
						</thead>
					<c:if test="${not empty form.assignments}">
						<tbody>
							<c:forEach items="${form.assignments}" var="assignment" varStatus="stat">
								<tr>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">	
										<c:out value="${stat.index+1}" />.
									</td>
									<td class="classRow" valign="top" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.title}"/><br />			
							        </td>
									<td class="classRow" valign="top" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.requestId}"/><br />
									</td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.code}"/><br />					
							        </td>
									<td class="classRow" valign="top" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.pic}"/><br />			
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.firstName}"/><br />	
										(<c:out value="${assignment.department}"/>)				
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.requiredTime}"/><br />					
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.statusLabel}"/><br />					
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<x:display name="${form.absoluteName}.completionDate${stat.index}"/>
										<x:display name="${form.absoluteName}.lbCompletionDate${stat.index}"/>
										<br />					
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<c:if test="${!(assignment.status eq 'C')}">
										<x:display name="${form.absoluteName}.completionTime${stat.index}"/>
										</c:if>
										<x:display name="${form.absoluteName}.lbCompletionTime${stat.index}"/>
										<br />					
							        </td>							
							    </tr>				
							</c:forEach>
						
						<tr>
							<td colspan="9">
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