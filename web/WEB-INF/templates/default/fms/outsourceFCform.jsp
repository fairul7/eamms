 <%@ include file="/common/header.jsp" %>

 <c:set var="form" value="${widget}"/>
 <script type="text/javascript">
 	onload = function() {
		populateCompanyName('<c:out value="${widget.outsourceType}" />');	
		populateCompleteButton('<c:out value="${widget.formMode}" />');
		populateActualCost('<c:out value="${widget.acDisplay}" />');
	}	
</script>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor" >
<tr>
<td>
<jsp:include page="../form_header.jsp"/>
<table width="100%" cellpadding="2" cellspacing="1" class="classBackground" >
	
	<tr>
		<td colspan="2" height="30">
		</td>
	</tr>
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.requestTitle'/> <FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
            <x:display name="${form.nameLbl.absoluteName}" ></x:display>
        </td>
    </tr>
  	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.program'/> <FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
            <x:display name="${form.programLbl.absoluteName}" ></x:display>
        </td>
    </tr>
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.submittedBy'/> <FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
            <x:display name="${form.submittedLbl.absoluteName}" ></x:display>
        </td>
    </tr>
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.outsourceType'/> <FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
            <x:display name="${form.pnType.absoluteName}" ></x:display>
        </td>
    </tr>
	<tr id="outsourceCompany" style="display:none;">
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.outsourceCompany'/> <FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
            <x:display name="${form.sbOutsourceCompany.absoluteName}" ></x:display>
        </td>
    </tr>
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.relatedFiles'/> <FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
			<c:if test="${! empty form.files}">
				<table class="borderTable" width="40%">
					<c:forEach items="${form.files}" var="file" varStatus="stat">
			        	<tr>
							<!-- td align="center"><c:out value="${stat.index+1}"/></td-->
							<td><c:out value="${file.fileName}"/></td>						
							<td align="right" style="padding-right:5px">
								<a href="outsourceRequestFC.jsp?requestId=<c:out value="${form.requestId}" />&amp;do=delete&amp;idfile=<c:out value="${file.fileId}"/>">Remove</a>
							</td>
						</tr>		        
			        </c:forEach>
				</table>
				<br />
			</c:if>
            <x:display name="${form.attachment.absoluteName}" ></x:display>
        </td>
    </tr>
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.estimatedCost'/> <FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
            <x:display name="${form.estimatedCost.absoluteName}" ></x:display>
        </td>
    </tr>
	<tr id="actualCost" style="display:none">
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top">
        	<fmt:message key='fms.facility.table.label.actualCost'/>
			(<fmt:message key='fms.request.label.currency'/>) 
		</td>
        <td class="classRow" valign="top">
            <x:display name="${form.actualCost.absoluteName}" ></x:display>
        </td>
    </tr>
	<tr>
        <td  class="classRowLabel" width="23%" height="30" valign="top" align="right"  valign="top"><fmt:message key='fms.label.outsourceRemarks'/>&nbsp;* <FONT class="classRowLabel"></FONT></td>
        <td class="classRow" valign="top">
            <x:display name="${form.outsourceRemarks.absoluteName}" ></x:display>
        </td>
    </tr>
    <tr>
        <td class="classRow"></td>
        <td class="classRow">
            <x:display name="${form.submitButton.absoluteName}" />
            <input class="button" type="button" id="completeButton" name="complete" value="<fmt:message key='fms.label.button.completeActualCost'/>" onclick="showHideActualCost('complete');" style="display:none"/>
			<input class="button" type="button" id="unCompleteButton" name="uncomplete" value="<fmt:message key='fms.label.button.cancelCompleteActualCost'/>" onclick="showHideActualCost('uncomplete');" style="display:none"/>
            <x:display name="${form.cancelButton.absoluteName}" />
        </td>
    </tr>
 	
    <jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
</table>
</td>
</tr>	
	<tr valign="MIDDLE">
    	<td height="22" class="contentTitleFont">
      		&nbsp;<fmt:message key='fms.request.label.outsourceRequestListing'/>  
        </td>
  	</tr>
	<tr>
		<td height="30">
			<fmt:message key='fms.request.label.totalEstimatedCost'/> : 
			<fmt:message key='fms.request.label.currency'/>
			<x:display name="${form.totalCostLbl.absoluteName}" />
		</td>
	</tr>
</table>

<script>
	
</script>