 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>

</td>
</tr>	
<tr>
	<td>
		
			<table width="100%" cellpadding="0" cellspacing="0" class="contentBgColor">
				<tr valign="MIDDLE">
			    	<td height="22" class="contentTitleFont">
			      		&nbsp;<fmt:message key='fms.request.label.facilitiesConflict'/>  
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
								<td height="40" width="15%" align="center"><fmt:message key='fms.table.label.facilities'/></td>
								<td width="15%" align="center"><fmt:message key='fms.table.label.conflictedBooking'/></td>
								
							</tr>
						</thead>
						<c:if test="${not empty form.facilities}">
						<tbody>
							<c:forEach items="${form.facilities}" var="facility" varStatus="stat">
								<tr>
									<td class="classRow" valign="top" style="border-bottom:1px solid #e9eccd">
										<!-- a href="#"
onclick="javascript:window.open('scpServiceEdit.jsp?id=<c:out value="${facility.id}" />', 'ca', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=450')"-->
											<c:out value="${facility.facility}"/>		
										<!-- /a -->
							        </td>
									<td class="classRow" valign="top" align="left" style="border-bottom:1px solid #e9eccd">
										<c:out value="${facility.additionalInfo}" escapeXml="false"/>
							        </td>
									
							    </tr>				
							</c:forEach>
						</tbody>
						</c:if>

						<c:if test="${not empty form.postService}">
						<tbody>
							<c:forEach items="${form.postService}" var="postService" varStatus="stat">
								<tr>
									<td class="classRow" valign="top" style="border-bottom:1px solid #e9eccd">
										<!-- a href='#' 
onclick="javascript:window.open('postProductionEdit.jsp?id=<c:out value="${postService.id}"/>', 'ca', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=450')"-->
											<c:out value="${postService.facility}"/>		
										<!-- /a-->
							        </td>
									<td class="classRow" valign="top" align="left" style="border-bottom:1px solid #e9eccd">
										<c:out value="${postService.additionalInfo}" escapeXml="false"/>
							        </td>
									
							    </tr>				
							</c:forEach>
						</tbody>
						</c:if>

						<c:if test="${not empty form.vtrService}">
						<tbody>
							<c:forEach items="${form.vtrService}" var="vtrService" varStatus="stat">
								<tr>
									<td class="classRow" valign="top" style="border-bottom:1px solid #e9eccd">
										<!-- a href='#' 
onclick="javascript:window.open('vtrServiceEdit.jsp?id=<c:out value="${vtrService.id}"/>', 'ca', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=450')"-->
											<c:out value="${vtrService.facility}"/>		
										<!-- /a-->
							        </td>
									<td class="classRow" valign="top" align="left" style="border-bottom:1px solid #e9eccd">
										<c:out value="${vtrService.additionalInfo}" escapeXml="false"/>
							        </td>
									
							    </tr>				
							</c:forEach>
						</tbody>
						</c:if>

						<c:if test="${not empty form.manPowService}">
						<tbody>
							<c:forEach items="${form.manPowService}" var="manPowService" varStatus="stat">
								<tr>
									<td class="classRow" valign="top" style="border-bottom:1px solid #e9eccd">
										<!-- a href='#'
onclick="javascript:window.open('manpowerServiceEdit.jsp?id=<c:out value="${manPowService.id}"/>', 'ca', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=450')"-->
											<c:out value="${manPowService.competencyName}"/>		
										<!-- /a-->
							        </td>
									<td class="classRow" valign="top" align="left" style="border-bottom:1px solid #e9eccd">
										<c:out value="${manPowService.additionalInfo}" escapeXml="false"/>
							        </td>
									
							    </tr>				
							</c:forEach>
						</tbody>
						</c:if>

						<c:if test="${not empty form.studioService}">
						<tbody>
							<c:forEach items="${form.studioService}" var="studioService" varStatus="stat">
								<tr>
									<td class="classRow" valign="top" style="border-bottom:1px solid #e9eccd">
										<!-- a href='#'
onclick="javascript:window.open('studioServiceEdit.jsp?id=<c:out value="${studioService.id}"/>', 'ca', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=450')"-->
											<c:out value="${studioService.facility}"/>		
										<!-- /a-->
							        </td>
									<td class="classRow" valign="top" align="left" style="border-bottom:1px solid #e9eccd">
										<c:out value="${studioService.additionalInfo}" escapeXml="false"/>
							        </td>
									
							    </tr>				
							</c:forEach>
						</tbody>
						</c:if>

						<c:if test="${not empty form.otherService}">
						<tbody>
							<c:forEach items="${form.otherService}" var="otherService" varStatus="stat">
								<tr>
									<td class="classRow" valign="top" style="border-bottom:1px solid #e9eccd">
										<!-- a href='#'
onclick="javascript:window.open('otherServiceEdit.jsp?id=<c:out value="${otherService.id}"/>', 'ca', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=450')"-->
											<c:out value="${otherService.facility}"/>		
										<!-- /a-->
							        </td>
									<td class="classRow" valign="top" align="left" style="border-bottom:1px solid #e9eccd">
										<c:out value="${otherService.additionalInfo}" escapeXml="false"/>
							        </td>
									
							    </tr>				
							</c:forEach>
						</tbody>
						</c:if>

						<c:if test="${not empty form.tvroService}">
						<tbody>
							<c:forEach items="${form.tvroService}" var="tvroService" varStatus="stat">
								<tr>
									<td class="classRow" valign="top" style="border-bottom:1px solid #e9eccd">
										<!-- a href='#' 
onclick="javascript:window.open('tvroServiceEdit.jsp?iid=<c:out value="${tvroService.id}"/>', 'ca', 'resizable=1, scrollbars=yes, statusbar=1, menubar=1, width=500, height=450')"-->
											<c:out value="${tvroService.feedTitle}"/>		
										<!-- /a-->
							        </td>
									<td class="classRow" valign="top" align="left" style="border-bottom:1px solid #e9eccd">
										<c:out value="${tvroService.additionalInfo}" escapeXml="false"/>
							        </td>
									
							    </tr>				
							</c:forEach>
						</tbody>
						</c:if>
	
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<x:display name="${form.btnContinue.absoluteName}" />
						<x:display name="${form.btnCancel.absoluteName}" />
					</td>
				</tr>
			</table>

	</td>
</tr>
</table>