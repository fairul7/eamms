 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
    <tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.form.vehicleNumber'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.vehicleNum.absoluteName}" />
         </td>
    </tr>  
	<tr>
        <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.assignmentDateFrom'/></td>
        <td class="classRow" valign="top">
			<x:display name="${form.dtStart.absoluteName}" />&nbsp;
			<b><fmt:message key='fms.label.assignmentDateTo'/></b>&nbsp;
			<x:display name="${form.dtEnd.absoluteName}" />
        </td>
    </tr>    
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.request.label.requestorDepartment'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.department.absoluteName}" />
         </td>
    </tr>  
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.program'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.program.absoluteName}" />		
         </td>
    </tr>      
    <tr>
        <td class="classRow"></td>
        <td class="classRow">
            <x:display name="${form.buttonPanel.absoluteName}" />
        </td>
    </tr>
 
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
<tr>
	<td>
		<c:if test="${not empty form.assignments}">
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
								<td height="25" width="15%" align="center"><b><fmt:message key='fms.request.label.requestTitle'/></b></td>
								<td width="15%" align="center"><b><fmt:message key='fms.label.requestId'/></b></td>
								<td width="15%" align="center"><b><fmt:message key='fms.facility.form.requestor'/></b></td>
								<td width="15%" align="center"><b><fmt:message key='fms.facility.table.department'/></b></td>
								<td width="15%" align="center"><b><fmt:message key='fms.request.label.requestedItem'/></b></td>
								<td width="15%" align="center"><b><fmt:message key='fms.facility.label.requiredTime'/></b></td>
								<td width="15%" align="center"><b><fmt:message key='fms.ratecard.table.label.status'/></b></td>
								<td width="15%" align="center"><b><fmt:message key='fms.ratecard.table.label.status'/></b></td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${form.assignments}" var="assignment" varStatus="stat">
								<tr>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">	
										<c:out value="${stat.index+1}" />.
									</td>
									<td class="classRow" valign="top" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.title}"/><br />			
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.requestId}"/><br />					
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.firstName}"/><br />					
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.department}"/><br />					
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.items}"/><br />					
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.requiredTime}"/><br />					
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<c:out value="${assignment.status}"/><br />					
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<x:display name="${form.absoluteName}.completionDate${stat.index}"/><br />					
							        </td>							
							    </tr>				
							</c:forEach>
						</tbody>
						</table>
					</td>
				</tr>
			</table>
		</c:if>
	</td>
</tr>
</table>