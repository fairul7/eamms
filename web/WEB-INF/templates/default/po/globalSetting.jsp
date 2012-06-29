<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />
<jsp:include page="../form_header.jsp" flush="true"/>
<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
	<td>
		<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
   			<tr>
        		<td>
					<table width="100%" cellpadding="3" cellspacing="1">
                		
                		<tr valign="top">
						  	<td class="contentTitleFont" style="padding:5px;" colspan="2">
						  		<fmt:message key="po.menu.setups"/> > <fmt:message key="po.global.setup"/>
						  	</td>
						 </tr><%--
						 <tr>
						     <td width="20%" class="classRowLabel" width="20%" valign="top" align="right">
						         <fmt:message key='po.global.fileSize'/> *
						     </td>
						     <td class="classRow" width="80%" valign="top">
						         <x:display name="${w.fileSizeUpload.absoluteName}" />
						     </td>
						 </tr> --%>
						 <tr>
							<td class="classRowLabel" width="20%" valign="top" align="right">
							    <fmt:message key='po.global.priority'/> *
							</td>
							<td class="classRow" width="80%" valign="top">
							    <fmt:message key='po.label.priorityInstruction'/>
							</td>
						 </tr>
						 <tr>
							<td class="classRowLabel" width="20%" valign="top" align="right">
							    
							</td>
							<td class="classRow" width="80%" valign="top">
							    <x:display name="${w.priority.absoluteName}" />
							</td>
						 </tr><%--
						 <tr>
							 <td class="classRowLabel" width="15%" valign="top" align="right"><b>
							 	<fmt:message key='po.global.attachFileExt'/> *
							 </b></td>
							 <td class="classRow" width="80%" valign="top"> 
							    <fmt:message key='po.label.allowedFileExtensionsInstruction'/>
							 </td>
						</tr>
						<tr>
							 <td class="classRowLabel" width="15%" valign="top" align="right"><b>
							 	
							 </b></td>
							 <td class="classRow" width="80%" valign="top">
							    <x:display name="${w.attachmentFileExt.absoluteName}" />
							 </td>
						</tr>--%>
                		
					</table>
        		</td>
			</tr>
		</table>
	</td>
</tr>
</table>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
	<td>
		<table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
		    <tr>
		        <td>
				<table width="100%" cellpadding="3" cellspacing="1">
	               
	                <tr>
	                    <td height="22" class="contentTitleFont" colspan="2">
							<fmt:message key="po.global.companyProfile"/> *
						</td>
	                </tr>
		  			 <tr>
	                    <td  width="20%" class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.global.companyName'/> *
	                    </td>
	                    <td class="classRow">
	                    	 <x:display name="${w.companyName.absoluteName}" />
	                    </td>
	                </tr>
	                 <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.global.companyAddr'/> *
	                    </td>
	                    <td class="classRow">
	                    	  <x:display name="${w.companyAddr.absoluteName}" />
	                    </td>
	                </tr>
	                 <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.global.companyTel'/> *
	                    </td>
	                    <td class="classRow">
	                    	 <x:display name="${w.companyTel.absoluteName}" />
	                    </td>
	                </tr>
	                		  			 <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    	<fmt:message key='po.global.companyFax'/>
	                    </td>
	                    <td class="classRow">
	                    	 <x:display name="${w.companyFax.absoluteName}" />
	                    </td>
	                </tr>
	                 <tr>
	                    <td class="classRowLabel" valign="top" align="right">
	                    </td>
	                    <td class="classRow">
	                    	  <x:display name="${w.btnPanel.absoluteName}" /> 
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
