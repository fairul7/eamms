 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
  	<script type="text/javascript">
		onload = function() {
			showHiddenCategory('<c:out value="${form.needCategory}" />');
		}	
	</script>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	
	<tr>
		<td colspan="2" height="30">
			<fmt:message key='fms.label.editDetails'/>
		</td>
	</tr>
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.rateCardName'/><FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
			<x:display name="${form.absoluteName}.namelbl" />					
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.serviceType'/></td>
        <td class="classRow" valign="top">
			<x:display name="${form.absoluteName}.serviceType" />
        </td>
    </tr>
    <tr>
        <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.facility.rateCard.label.abwCode'/></td>
        <td class="classRow" valign="top">
			<x:display name="${form.abwCode.absoluteName}" ></x:display>
        </td>
    </tr>
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.description'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.absoluteName}.description" />
         </td>
    </tr>    
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.remarksRequestor'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.absoluteName}.remarksRequestor" />
         </td>
    </tr> 
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.tran.setup.transportRequest'/></td>
         <td class="classRow" valign="top">
			<x:display name="${form.pnType.absoluteName}" />
			<div id="category" style="padding-top:10px;<c:if test="${form.needCategory != 'Y'}">display:none;</c:if>">
				<fmt:message key="fms.tran.requestTypeOfVehicles"/>&nbsp;&nbsp;<x:display name="${form.category.absoluteName}" /><br /><br />
			</div>
         </td>
    </tr>  
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.equipment'/></td>
         <td class="classRow" valign="top">
			<input class="button" type="button" onclick="javascript:pops('mains','/ekms/sysadmin/FacilityListing.jsp?id=${param.id}',400,520)" value="<fmt:message key='fms.label.facilityAdd'/>" />
			
			<br /><br />

				<table class="borderTable" width="60%">
					<tr>
						<th width="5%"><fmt:message key='fms.label.number'/></th>
						<th width="75%"><fmt:message key='fms.label.equipment'/></th>
						<th><fmt:message key='fms.facility.table.quantity'/></th>
						<th></th>
					</tr>
					<c:forEach items="${form.facilities}" var="facility" varStatus="stat">
			        	<tr>
							<td align="center"><c:out value="${stat.index+1}"/></td>
							<td><c:out value="${facility.equipment}"/></td>						
							<td align="right" style="padding-right:5px">
								<c:forEach items="${form.equipmentQty}" var="quantity" varStatus="statQty">
									<c:if test="${stat.index == statQty.index }">
										<x:display name="${quantity.absoluteName}" />
									</c:if>
								</c:forEach>
							</td>
							<td align="center">
								<a href='?id=<c:out value="${facility.id}"/>&do=deleteEquip&idEquipment=<c:out value="${facility.idEquipment}"/>'><fmt:message key='fms.facility.delete'/></a>
							</td>
						</tr>		        
			        </c:forEach>
				</table>
			<br />		
         </td>
    </tr>    
	<tr>
         <td class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.manpower'/></td>
         <td class="classRow" valign="top">
			<input class="button" type="button" onclick="javascript:pops('mains','/ekms/sysadmin/ManpowerListing.jsp?id=${param.id}',400,520)" value="<fmt:message key='fms.label.manpowerAdd'/>" />
			
			<br /><br />
			
			<table class="borderTable" width="60%">
				<tr>
					<th width="5%"><fmt:message key='fms.label.number'/></th>
					<th width="75%"><fmt:message key='fms.label.manpower'/></th>
					<th><fmt:message key='fms.facility.table.quantity'/></th>
					<th></th>
				</tr>
				<c:forEach items="${form.manpower}" var="manpower" varStatus="statM">
		        	<tr>
						<td align="center"><c:out value="${statM.index+1}"/></td>
						<td><c:out value="${manpower.manpower}"/></td>						
						<td align="right" style="padding-right:5px">
							<c:forEach items="${form.manpowerQty}" var="manpowerQty" varStatus="statMQty">
								<c:if test="${statM.index == statMQty.index }">
									<x:display name="${manpowerQty.absoluteName}" />
								</c:if>
							</c:forEach>
						</td>
						<td align="center">
							<a href='?id=<c:out value="${manpower.id}"/>&do=deleteManpower&idManpower=<c:out value="${manpower.idManpower}"/>'><fmt:message key='fms.facility.delete'/></a>
						</td>
					</tr>		        
		        </c:forEach>
			</table>
			
			<br />
         </td>
    </tr>      
    <tr>
        <td class="classRow"></td>
        <td class="classRow">
			<x:display name="${form.absoluteName}.stat" />
            <x:display name="${form.submitButton.absoluteName}" /><x:display name="${form.cancelButton.absoluteName}" />
        </td>
    </tr>
 
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
</table>
<script>
	function showHiddenCategory(selValue){
		var tab=document.getElementById("category");

		if(selValue=='Y'){
			tab.style.display='block';
		} else {
			tab.style.display='none';
		}	
	}
</script>