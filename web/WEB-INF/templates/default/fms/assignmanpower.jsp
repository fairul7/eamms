 <%@ include file="/common/header.jsp" %>
<%@ page import="kacang.services.security.SecurityService, 
				kacang.services.security.SecurityException,
				kacang.ui.WidgetManager,
				kacang.util.Log,
				kacang.Application" %>
<c:set var="form" value="${widget}"/>
 
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
    <tr>
        <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.facility.label.requiredDate'/>
		</td>
        <td class="classRow" valign="top">
			<x:display name="${form.requiredDate.absoluteName}" />
        </td>
    </tr>
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.facility.label.requiredTime'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.requiredTime.absoluteName}" />
         </td>
    </tr>    
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.tran.selectManpower'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.userSelectBox.absoluteName}" />		
         </td>
    </tr>   
	 <tr>
        <td class="classRow"></td>
        <td class="classRow">
            <x:display name="${form.buttonPanel.absoluteName}" />
        </td>
    </tr> 
<%
	Application app = Application.getInstance();
	SecurityService service = (SecurityService) app.getService(SecurityService.class);
	String userId = service.getCurrentUser(request).getId();
	boolean assignDiffMan = false;

	try {
		assignDiffMan = service.hasPermission(userId, "com.tms.fms.facility.permission.assignDiffManpower", null, null);
	} catch (SecurityException e) {
		Log.getLog(getClass()).error(e.getMessage(), e);
	}
	if(assignDiffMan){
%>  
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top">
         	<fmt:message key='fms.tran.assignDiffManpower'/>
		</td>
         <td class="classRow" valign="top">
			<x:display name="${form.altUserSelectBox.absoluteName}" />		
         </td>
    </tr>      
     <tr>
        <td class="classRow"></td>
        <td class="classRow">
            <x:display name="${form.buttonPanelDiff.absoluteName}" />
        </td>
    </tr>
<%
	}
%>
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
<tr>
	<td>
		<c:if test="${not empty form.manpowers}">
			<table width="100%" cellpadding="0" cellspacing="0" >
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
						<table width="95%" class="borderTable">
						<thead>
							<tr class="tableHeader">
								<th height="40" width="15%" align="center"><fmt:message key='fms.table.label.manpower'/></th>
								<th width="10%" align="center"><fmt:message key='fms.table.label.workingProfile'/></th>
								<th width="20%" align="center"><fmt:message key='fms.facility.label.competencyInPool'/></th>
								<th width="35%" align="center"><fmt:message key='fms.table.label.assignment'/></th>
								<th align="center" width="10%">&nbsp;
								</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${form.manpowers}" var="manpowers" varStatus="stat">
								<tr>
									<td valign="top" height="30">
										<c:out value="${manpowers.competencyName}" escapeXml="false"/><br />					
							        </td>
									<td valign="top" align="center">
										<c:out value="${manpowers.workingProfile}"/>
									</td>
									<td valign="top" align="center">
										<c:out value="${manpowers.competency}"/>
									</td>
									<td  valign="top" align="left">
										<c:out value="${manpowers.totalUser}" escapeXml="false"/><br />					
							        </td>
									<td valign="top" align="center">
										<a href="javascript:pops('mains','assign.jsp?id=${manpowers.asgId}&userId=${manpowers.competencyId}&requestId=${form.requestId}&act=assign',210,470)">[ Assign ]</a>
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