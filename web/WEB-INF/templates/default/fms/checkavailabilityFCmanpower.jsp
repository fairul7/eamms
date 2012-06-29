 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.request.label.checkType'/><FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
			<x:display name="${form.pnType.absoluteName}" />					
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.request.label.startDate'/></td>
        <td class="classRow" valign="top">
			<x:display name="${form.dtStart.absoluteName}" />
        </td>
    </tr>
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.request.label.endDate'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.dtEnd.absoluteName}" />
         </td>
    </tr>    
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.request.label.startTime'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.tmStart.absoluteName}" />
         </td>
    </tr>  
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.request.label.endTime'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.tmEnd.absoluteName}" />		
         </td>
    </tr>    
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.facility.form.selectManpower'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.mpsbManpowerType.absoluteName}" />		
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
		<c:if test="${not empty form.manpowers}">
			<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
				<tr valign="MIDDLE">
			    	<td height="22" class="contentTitleFont">
			      		&nbsp;<fmt:message key='fms.request.label.manpowersAvailability'/>  
			        </td>
			    	<td align="right" class="contentTitleFont">&nbsp;</td>
			  	</tr>
				<tr>
					<td height="20" colspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<table width="95%">
						<thead>
							<tr class="tableHeader">
								<td height="40" width="15%" align="center"><fmt:message key='fms.table.label.manpower'/></td>
								<td width="15%" align="center"><fmt:message key='fms.table.label.totalInPool'/></td>
								<c:forEach items="${form.dates}" var="dates" varStatus="dateIdx">
									<td align="center">
										<c:forEach items="${form.dateSelectedMap[dates]}" var="dateItem">
											<c:out value="${dateItem.dateChecked}" /><br />
											<c:out value="${dateItem.timeFrom}" />-<c:out value="${dateItem.timeTo}" /> 
										</c:forEach>
									</td>
								</c:forEach>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${form.manpowers}" var="manpowers" varStatus="stat">
								<tr>
									<td class="classRow" valign="top" style="border-bottom:1px solid #e9eccd">
										<c:out value="${manpowers.competencyName}"/><br />					
							        </td>
									<td class="classRow" valign="top" align="center" style="border-bottom:1px solid #e9eccd">
										<c:out value="${manpowers.totalUser}"/><br />					
							        </td>
									<c:forEach items="${form.dates}" var="dates" varStatus="dateIdx">
										<td class="classRow" valign="top" align="left" style="border-bottom:1px solid #e9eccd">
											<c:forEach items="${form.dateSelectedMap[dates]}" var="dateItem">
												<c:set var="found" value="0" />
												<c:forEach items="${dateItem[dates]}" var="detail">
													
															<c:choose>
																<c:when test="${found == 0}">
																	<c:if test="${manpowers.competencyId == detail}">
																		<c:set var="found" value="1" />													
																	</c:if>
																</c:when>
																<c:otherwise>
																	<c:out value="${detail}" escapeXml="false"/>
																	<c:set var="found" value="0" />
																</c:otherwise>
															</c:choose>
	
												</c:forEach>
												<br />
												
											</c:forEach>
										</td>
									</c:forEach>
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