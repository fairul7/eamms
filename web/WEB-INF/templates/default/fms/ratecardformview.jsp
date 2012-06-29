 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 <c:set var="id" value="${param.id}"/>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	
	<tr>
		<td colspan="2" height="30">
			<fmt:message key='fms.label.viewDetails'/>
		</td>
	</tr>
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"></td>
        <td class="classRow" valign="top">				
			<h2><font color="red"><x:display name="${form.childMap['errorLbl'].absoluteName}" /></font></h2>
        </td>
    </tr>
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.rateCardName'/><FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
			<x:display name="${form.nameLbl.absoluteName}" />					
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.serviceType'/></td>
        <td class="classRow" valign="top">
			<x:display name="${form.serviceLbl.absoluteName}" />
        </td>
    </tr>
    <tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.dpfEffectiveDate'/><FONT class="classRowLabel"></FONT></td>
         <td class="classRow" valign="top">
			<x:display name="${form.effectiveDateLbl.absoluteName}" />
         </td>
    </tr>
    <tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.internalRate'/></td>
         <td class="classRow" valign="top">
            RM <x:display name="${form.internalRateLbl.absoluteName}" />
         </td>
    </tr>
    <tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.externalRate'/></td>
         <td class="classRow" valign="top">
            RM <x:display name="${form.externalRateLbl.absoluteName}" />
         </td>
    </tr>
    <tr>
        <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.facility.rateCard.label.abwCode'/></td>
        <td class="classRow" valign="top">
			<x:display name="${form.abwCodeLbl.absoluteName}" />
        </td>
    </tr>
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.description'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.descriptionLbl.absoluteName}" />
         </td>
    </tr>    
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.remarksRequestor'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.remarksLbl.absoluteName}" />
         </td>
    </tr>  
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.setup.transportRequest'/></td>
         <td class="classRow" valign="top">
            <x:display name="${form.transportRequestLbl.absoluteName}" />
         </td>
    </tr>
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.status'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.statusLbl.absoluteName}" />
         </td>
    </tr>  
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.equipment'/></td>
         <td class="classRow" valign="top">
			<fmt:message key='fms.label.facilityAdded'/>
			<c:if test="${! empty form.facilities}">
				<table class="borderTable" width="60%">
					<tr>
						<th width="5%"><fmt:message key='fms.label.number'/></th>
						<th width="75%"><fmt:message key='fms.label.equipment'/></th>
						<th><fmt:message key='fms.label.status'/></th>
						<th><fmt:message key='fms.facility.table.quantity'/></th>
					</tr>
					<c:forEach items="${form.facilities}" var="facility" varStatus="stat">
			        	<tr>
							<td align="center"><c:out value="${stat.index+1}"/></td>
							<td><c:out value="${facility.equipment}"/></td>			
							<td align="center">
								<font color="red"><c:out value="${facility.status}"/></font>
								<c:if test="${empty (facility.status)}">OK</c:if>
							</td>
							<td align="right" style="padding-right:5px"><c:out value="${facility.equipmentQty}"/></td>
						</tr>		        
			        </c:forEach>
				</table>
			</c:if>
			<br />
	
         </td>
    </tr>    
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.manpower'/></td>
         <td class="classRow" valign="top">
			<fmt:message key='fms.label.manpowerAdded'/>
			<c:if test="${! empty form.manpower}">
				<table class="borderTable" width="60%">
					<tr>
						<th width="5%"><fmt:message key='fms.label.number'/></th>
						<th width="75%"><fmt:message key='fms.label.manpower'/></th>
						<th><fmt:message key='fms.label.status'/></th>
						<th><fmt:message key='fms.facility.table.quantity'/></th>
					</tr>
					<c:forEach items="${form.manpower}" var="manpower" varStatus="statM">
			        	<tr>
							<td align="center"><c:out value="${statM.index+1}"/></td>
							<td><c:out value="${manpower.manpower}"/></td>	
							<td align="center">
								<font color="red"><c:out value="${manpower.status}"/></font>
								<c:if test="${empty (manpower.status)}">OK</c:if>
							</td>
							<td align="right" style="padding-right:5px"><c:out value="${manpower.manpowerQty}"/></td>
						</tr>		        
			        </c:forEach>
				</table>
			</c:if>
			<br />
         </td>
    </tr>   
    <tr>
        <td class="classRow"></td>
        <td class="classRow">
            <x:display name="${form.absoluteName}.editButton" /> <x:display name="${form.absoluteName}.setRateButton" /> <x:display name="${form.cancelButton.absoluteName}" />
        </td>
    </tr>
 
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>