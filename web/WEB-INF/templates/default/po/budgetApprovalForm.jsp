<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
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
							<fmt:message key='userRequest.label.remark'/>
						</td>
					</tr>	                
	                <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='userRequest.label.addRemark'/>
	                    </td>
	                    <td class="classRow">
	                    	<x:display name="${w.txtRemark.absoluteName}" />   
	                    </td>
	                </tr>
	                <tr>
	                   <td class="classRowLabel" valign="top" align="right">
	                   </td>
	                   <td class="classRow">
	                    	 <x:display name="${w.radioGroup.absoluteName}" />
	                   </td>
	                </tr>
	                <tr>
	                   <td class="classRowLabel" valign="top" align="right">
	                   </td>
	                   <td class="classRow">
	                    	<x:display name="${w.buttonPanel.absoluteName}" />
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

