<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="widget" value="${widget}"/>
<jsp:include page="../../form_header.jsp" flush="true"/>
<table class="profileTable" cellpadding="3" cellspacing="1" width="100%">
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.requestTitle"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.request.title}"/></td>
    </tr>
	<tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.facility.label.requiredDate"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><fmt:formatDate value="${widget.request.requiredFrom}"  pattern="${globalDateLong}"/> - <fmt:formatDate value="${widget.request.requiredTo}"  pattern="${globalDateLong}"/></td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.requestType"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.requestTypeLabel}"/></td>
    </tr>
	<c:if test="${widget.request.requestType eq 'I'}">
	    <tr>
	        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.program"/></b>&nbsp;</td>
	        <td width="70%" class="profileRow"><c:out value="${widget.request.programName}"/></td>
	    </tr>
	</c:if>
	<c:if test="${widget.request.requestType eq 'E'}">
	    <tr>
	        <td width="30%" nowrap class="profileRow" align="right"><b><fmt:message key="fms.request.label.clientName"/></b>&nbsp;</td>
	        <td width="70%" class="profileRow"><c:out value="${widget.request.clientName}"/></td>
	    </tr>
	</c:if>
    <tr>
        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.request.label.remarks"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.request.description}" escapeXml="false"/></td>
    </tr>
    <c:if test="${widget.viewMode eq 'true'}" >
    <tr>
        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.facility.label.submittedBy"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.request.createdUserName}"/> [<fmt:formatDate value="${widget.request.submittedDate}"  pattern="${globalDatetimeLong}"/>]</td>
    </tr>
    <tr>
        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.facility.table.status"/></b>&nbsp;</td>
        <td width="70%" class="profileRow"><c:out value="${widget.request.statusLabel}"/></td>
    </tr>
   	 	<c:if test="${ not empty(widget.request.approvedBy)}" >
			<tr>
		        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.facility.label.remarksDepartmentApprover"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow">
							<c:out value="${widget.request.approverRemarks}"/>
				</td>
		    </tr>
			<tr>
		        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.facility.label.approvedBy"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow">
							<c:out value="${widget.request.approvedBy}"/> [<fmt:formatDate value="${widget.request.approvedDate}"  pattern="${globalDatetimeLong}"/>]
				</td>
		    </tr>
		</c:if>
    
		<!-- c:if test="${(widget.isHod eq 'true') || (widget.isFC eq 'true') || (widget.isFCHead eq 'true')|| (widget.isHOU eq 'true')}" -->
			<tr>
		        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.facility.table.totalAmountRate"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow">
		        	<fmt:message key="fms.request.label.currency"/>&nbsp;
					<c:choose>
						<c:when test="${widget.request.requestType eq 'E'}">
							<fmt:formatNumber value="${widget.totalExternalRate}" maxFractionDigits="2" pattern="#,##0.00"/>
						</c:when>
						<c:otherwise>
							<fmt:formatNumber value="${widget.totalInternalRate}" maxFractionDigits="2" pattern="#,##0.00"/>
						</c:otherwise>						
					</c:choose>
				</td>
		    </tr>
		<!-- /c:if-->

		<c:if test="${ not empty(widget.request.cancellationCharges) }">
			<tr>
		        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.label.cancellationCharges"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow">
		        	<fmt:message key="fms.request.label.currency"/>&nbsp;
					<fmt:formatNumber value="${widget.request.cancellationCharges}" maxFractionDigits="2" pattern="#,##0.00"/>					
				</td>
		    </tr>
		</c:if>

		<c:if test="${ not empty(widget.request.lateCharges) }">
			<tr>
		        <td width="30%" nowrap class="profileRow"  valign="top" align="right"><b><fmt:message key="fms.label.lateCharges"/></b>&nbsp;</td>
		        <td width="70%" class="profileRow">
		        	<fmt:message key="fms.request.label.currency"/>&nbsp;
					<fmt:formatNumber value="${widget.request.lateCharges}" maxFractionDigits="2" pattern="#,##0.00"/>					
				</td>
		    </tr>
		</c:if>
    </c:if>

	<tr>
	<td colspan="2" align="center">
	
	<c:if test="${ not empty(widget.lbSCPQuestions) }">
		<table width="98%" border="0" cellspacing="0" cellpadding="5">
		    <tr valign="middle">
		        <td height="17" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.feedback.label"/>&nbsp;:&nbsp;<fmt:message key="fms.facility.msg.service.1"/></font></b></td>
		        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
		    </tr>
			<tr>
				<td colspan="2">
					<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
					<tr>
						<th width="250">Please rate the following</th>
						<th colspan="4">&lt;&lt;Not Satisfactory</th>
						<th colspan="4">Excellent&gt;&gt;</th>
					</tr>
						<c:forEach items="${widget.lbSCPQuestions}" var="check" varStatus="checkStatus">
							<tr>
								<td class="profileRow" align="left">&nbsp;
									<x:display name="${check.absoluteName}"/>
								</td>
								<td><x:display name="${widget.scpAnswer0[checkStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.scpAnswer1[checkStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.scpAnswer2[checkStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.scpAnswer3[checkStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.scpAnswer4[checkStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.scpAnswer5[checkStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.scpAnswer6[checkStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.scpAnswer7[checkStatus.index].absoluteName}"/></td>
			
							</tr>
						</c:forEach>
					</table>
					<br>
				</td>
			</tr>
		</table>
		
		
	</c:if>

	<c:if test="${ not empty(widget.lbPOSTQuestions) }">
		<table width="98%" border="0" cellspacing="0" cellpadding="5">
		    <tr valign="middle">
		        <td height="17" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.feedback.label"/>&nbsp;:&nbsp;<fmt:message key="fms.facility.msg.service.2"/></font></b></td>
		        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
		    </tr>
			<tr>
				<td colspan="2">
					<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
					<tr>
						<th width="250">Please rate the following</th>
						<th colspan="4">&lt;&lt;Not Satisfactory</th>
						<th colspan="4">Excellent&gt;&gt;</th>
					</tr>
						<c:forEach items="${widget.lbPOSTQuestions}" var="post" varStatus="postStatus">
							<tr>
								<td class="profileRow" align="left">&nbsp;
									<x:display name="${post.absoluteName}"/>
								</td>
								<td><x:display name="${widget.postAnswer0[postStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.postAnswer1[postStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.postAnswer2[postStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.postAnswer3[postStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.postAnswer4[postStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.postAnswer5[postStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.postAnswer6[postStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.postAnswer7[postStatus.index].absoluteName}"/></td>
			
							</tr>
						</c:forEach>
					</table>
					<br>
				</td>
			</tr>
		</table>
	</c:if>

	<c:if test="${ not empty(widget.lbVTRQuestions) }">
		<table width="98%" border="0" cellspacing="0" cellpadding="5">
		    <tr valign="middle">
		        <td height="17" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.feedback.label"/>&nbsp;:&nbsp;<fmt:message key="fms.facility.msg.service.3"/></font></b></td>
		        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
		    </tr>
			<tr>
				<td colspan="2">
					<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
					<tr>
						<th width="250">Please rate the following</th>
						<th colspan="4">&lt;&lt;Not Satisfactory</th>
						<th colspan="4">Excellent&gt;&gt;</th>
					</tr>
						<c:forEach items="${widget.lbVTRQuestions}" var="vtr" varStatus="vtrStatus">
							<tr>
								<td class="profileRow" align="left">&nbsp;
									<x:display name="${vtr.absoluteName}"/>
								</td>
								<td><x:display name="${widget.vtrAnswer0[vtrStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.vtrAnswer1[vtrStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.vtrAnswer2[vtrStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.vtrAnswer3[vtrStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.vtrAnswer4[vtrStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.vtrAnswer5[vtrStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.vtrAnswer6[vtrStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.vtrAnswer7[vtrStatus.index].absoluteName}"/></td>
			
							</tr>
						</c:forEach>
					</table>
					<br>
				</td>
			</tr>
		</table>
	</c:if>

	<c:if test="${ not empty(widget.lbMANQuestions) }">
		<table width="98%" border="0" cellspacing="0" cellpadding="5">
		    <tr valign="middle">
		        <td height="17" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.feedback.label"/>&nbsp;:&nbsp;<fmt:message key="fms.facility.msg.service.4"/></font></b></td>
		        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
		    </tr>
			<tr>
				<td colspan="2">
					<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
					<tr>
						<th width="250">Please rate the following</th>
						<th colspan="4">&lt;&lt;Not Satisfactory</th>
						<th colspan="4">Excellent&gt;&gt;</th>
					</tr>
						<c:forEach items="${widget.lbMANQuestions}" var="man" varStatus="manStatus">
							<tr>
								<td class="profileRow" align="left">&nbsp;
									<x:display name="${man.absoluteName}"/>
								</td>
								<td><x:display name="${widget.manAnswer0[manStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.manAnswer1[manStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.manAnswer2[manStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.manAnswer3[manStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.manAnswer4[manStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.manAnswer5[manStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.manAnswer6[manStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.manAnswer7[manStatus.index].absoluteName}"/></td>
			
							</tr>
						</c:forEach>
					</table>
					<br>
				</td>
			</tr>
		</table>
	</c:if>

	<c:if test="${ not empty(widget.lbSTDQuestions) }">
		<table width="98%" border="0" cellspacing="0" cellpadding="5">
		    <tr valign="middle">
		        <td height="17" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key="fms.feedback.label"/>&nbsp;:&nbsp;<fmt:message key="fms.facility.msg.service.5"/></font></b></td>
		        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
		    </tr>
			<tr>
				<td colspan="2">
					<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
					<tr>
						<th width="250">Please rate the following</th>
						<th colspan="4">&lt;&lt;Not Satisfactory</th>
						<th colspan="4">Excellent&gt;&gt;</th>
					</tr>
						<c:forEach items="${widget.lbSTDQuestions}" var="std" varStatus="stdStatus">
							<tr>
								<td class="profileRow" align="left">&nbsp;
									<x:display name="${std.absoluteName}"/>
								</td>
								<td><x:display name="${widget.stdAnswer0[stdStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.stdAnswer1[stdStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.stdAnswer2[stdStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.stdAnswer3[stdStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.stdAnswer4[stdStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.stdAnswer5[stdStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.stdAnswer6[stdStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.stdAnswer7[stdStatus.index].absoluteName}"/></td>
			
							</tr>
						</c:forEach>
					</table>
					<br>
				</td>
			</tr>
		</table>
	</c:if>

	<c:if test="${ not empty(widget.lbOTHQuestions) }">
		<table width="98%" border="0" cellspacing="0" cellpadding="5">
		    <tr valign="middle">
		        <td height="17" bgcolor="#003366" class="contentTitleFont">
					<b>
						<font color="#FFCF63" class="contentTitleFont">
		        			<fmt:message key="fms.feedback.label"/>&nbsp;:&nbsp;
		        			<fmt:message key="fms.facility.msg.service.6"/>
						</font>
					</b>
				</td>
		        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
		    </tr>
			<tr>
				<td colspan="2">
					<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
					<tr>
						<th width="250">Please rate the following</th>
						<th colspan="4">&lt;&lt;Not Satisfactory</th>
						<th colspan="4">Excellent&gt;&gt;</th>
					</tr>
						<c:forEach items="${widget.lbOTHQuestions}" var="oth" varStatus="othStatus">
							<tr>
								<td class="profileRow" align="left">&nbsp;
									<x:display name="${oth.absoluteName}"/>
								</td>
								<td><x:display name="${widget.othAnswer0[othStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.othAnswer1[othStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.othAnswer2[othStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.othAnswer3[othStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.othAnswer4[othStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.othAnswer5[othStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.othAnswer6[othStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.othAnswer7[othStatus.index].absoluteName}"/></td>
			
							</tr>
						</c:forEach>
					</table>
					<br>
				</td>
			</tr>
		</table>
	</c:if>

	<c:if test="${ not empty(widget.lbTVROQuestions) }">
		<table width="98%" border="0" cellspacing="0" cellpadding="5">
		    <tr valign="middle">
		        <td height="17" bgcolor="#003366" class="contentTitleFont">
					<b>
						<font color="#FFCF63" class="contentTitleFont">
							<fmt:message key="fms.feedback.label"/>&nbsp;:&nbsp;
							<fmt:message key="fms.facility.msg.service.7"/>
						</font>
					</b>
				</td>
		        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
		    </tr>
			<tr>
				<td colspan="2">
					<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">
					<tr>
						<th width="250">Please rate the following</th>
						<th colspan="4">&lt;&lt;Not Satisfactory</th>
						<th colspan="4">Excellent&gt;&gt;</th>
					</tr>
						<c:forEach items="${widget.lbTVROQuestions}" var="tvro" varStatus="tvroStatus">
							<tr>
								<td class="profileRow" align="left">&nbsp;
									<x:display name="${tvro.absoluteName}"/>
								</td>
								<td><x:display name="${widget.tvroAnswer0[tvroStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.tvroAnswer1[tvroStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.tvroAnswer2[tvroStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.tvroAnswer3[tvroStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.tvroAnswer4[tvroStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.tvroAnswer5[tvroStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.tvroAnswer6[tvroStatus.index].absoluteName}"/></td>
								<td><x:display name="${widget.tvroAnswer7[tvroStatus.index].absoluteName}"/></td>
			
							</tr>
						</c:forEach>
					</table>
					<br>
				</td>
			</tr>
		</table>
	</c:if>
	
		<table width="98%" border="0" cellspacing="0" cellpadding="5">
			<tr>
				<td>
					<table align="center" class="borderTable" cellpadding="3" cellspacing="1" width="95%">					
						<tr>
							<td class="profileRow" align="left" width="245" valign="top">
								<b><fmt:message key="fms.request.label.feedBackSuggestion"/></b>							
							</td>
							<td><x:display name="${widget.remarks.absoluteName}"/></td>		
						</tr>
					</table>
					<br>
				</td>
			</tr>
		</table>

	</td></tr>
    <tr>
    	<td class="contentTitleFont" colspan="2" align="center"></td>
    </tr>
    <tr>
    	<td class="profileFooter" colspan="2" align="center">
            <x:display name="${widget.submit.absoluteName}"/>
            <x:display name="${widget.rsButton.absoluteName}"/>
    	</td>
    </tr>   
</table>
<jsp:include page="../../form_footer.jsp" flush="true"/>
