<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

<script type="text/javascript">
// Check if the browser is IE4
var ie4 = false;
if(document.all) {
	ie4 = true;
}

// Get an object by ID
function getObject(id) {
	if (ie4) {
		return document.all[id];
	}
	else {
		return document.getElementById(id);
	}
}

// Toggle the visibility of a specific DIV
function togglePanelVisibility(divId) {
    var divObj = getObject(divId);

    if(divObj.style.display == 'none') {
        divObj.style.display = 'block';
    }
    
    
}

function togglePanelInvisibiliy(more){
    var moreObj = getObject(more);
	if(moreObj.style.display == 'block') {
	        moreObj.style.display = 'none';
	    }
}

var sequenceIndex = 0;
function addMore() {
	sequenceIndex++;
	togglePanelVisibility("items" + sequenceIndex );
		
	if(sequenceIndex >= 4)
	togglePanelInvisibiliy("moreLink");
}
function add() {
	window.open('popupItemSelect.jsp','select','menubar=no,toolbar=no,scrollbars=yes,resizable=yes,width=600,height=500,left=50,top=50');
}	
		

function toggleCheck() {
	var size = <c:out value="${w.size}"/>;
	for(var i=0; i<size; i++){
	document.forms['mainPg.PrePurchase'].elements['mainPg.PrePurchase.deleteBox'+i].checked=document.forms['mainPg.PrePurchase'].elements['mainPg.PrePurchase.mainDelete'].checked;
	}
}

</script>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
	<td>
		<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
	    <tr>
	        <td>
				<table width="100%" cellpadding="3" cellspacing="1">
	                <jsp:include page="../form_header.jsp" flush="true"/>
	               <tr>
	                    <td height="22" class="contentTitleFont" colspan="2">
							<fmt:message key="po.label.po"/> > <fmt:message key="po.label.prePurchase"/>
						</td>
	                </tr>
	                <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.addItem'/>(s) *
	                    </td>
	                    <td class="classRow" align="right">
	                   
	                    	<x:display name="${w.buttonPanel.absoluteName}" /> 
	                    	<p>  </p>
	                    	<span style="color:FF0000"><c:out value="${w.info}"/></span>
	                    	<c:set var="count" value="0"/>
	                    	<table width="100%" border="0" cellspacing="1" cellpadding="3">
				           		<tr>
				           			<td  bgcolor="#BBD5F2" valign="top" align="left">
				               			<b><fmt:message key='po.label.no'/></b>
				               		</td>
				           			<td  bgcolor="#BBD5F2" valign="top" width="20%" align="left">
				               			<b><fmt:message key='po.label.itemCode'/></b>
				               		</td>
				           			<td  bgcolor="#BBD5F2" valign="top"  width="40%" >
				               			<b><fmt:message key='po.label.itemDesc'/></b>
				           			</td>
				           			<td  bgcolor="#BBD5F2" valign="top" align="left">
				               			<b><fmt:message key='po.label.qty'/></b>
				               		</td>
				           			<td  bgcolor="#BBD5F2" valign="top"  width="30%">
				               			<b><fmt:message key='po.label.unitOfMeasure'/></b>
				           			</td>
				           			<td  bgcolor="#BBD5F2" valign="top" width="5%">
				               			<x:display name="${w.mainDelete.absoluteName}" /> 
				           			</td>
				           		</tr>
				           		<c:forEach items="${w.itemCode}" var="itemCode" varStatus = "status">
				                  <c:set var="itemDesc" value="${w.itemDesc[status.index]}"/>
				                  <c:set var="qty" value="${w.qty[status.index]}"/>
				                  <c:set var="unitOfMeasure" value="${w.unitOfMeasure[status.index]}"/>
				                  <c:set var="deleteBox" value="${w.deleteBox[status.index]}"/>
							    	<tr>
								   		<td class="contentBgColor" valign="top">
								   			<c:set var="count" value="${count + 1}"/>
																<c:out value="${count}" /> 
								   		</td>
										<td class="contentBgColor" valign="top">
											<x:display name="${itemCode.absoluteName}" />
											<br>
										</td>
										<td class="contentBgColor" valign="top">
								   			<x:display name="${itemDesc.absoluteName}" />
								   		</td>
										<td class="contentBgColor" valign="top">
											<x:display name="${qty.absoluteName}" />
											
										</td>
										<td class="contentBgColor" width="30%" valign="top">
								   			<x:display name="${unitOfMeasure.absoluteName}" />
								   		</td>
								   		<td class="contentBgColor" width="5%" valign="top">
								   			<x:display name="${deleteBox.absoluteName}" />
								   		</td>
									 </tr>
								</c:forEach>
				       	 	</table>
	                    </td>
	                </tr>
	                <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.reason'/> *
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.reason.absoluteName}" />   
	                    </td>
	                </tr>
	                 <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.global.priority'/> *
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.priority.absoluteName}" />   
	                    </td>
	                </tr>
	                <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.needed'/> 
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.neededBy.absoluteName}" />   
	                    </td>
	                </tr>
	                <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.label.attachment'/>
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.attachment.absoluteName}" /> 
	                    </td>
	                </tr>
	                <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.submit.absoluteName}" />
	                    	<x:display name="${w.draft.absoluteName}" />
	                    	<x:display name="${w.cancel.absoluteName}" />
	                    </td>
	                </tr>
                	<jsp:include page="../form_footer.jsp" flush="true"/>
				</table>
        	</td>
		</tr>
		</table>
	</td>
</tr>
</table>

