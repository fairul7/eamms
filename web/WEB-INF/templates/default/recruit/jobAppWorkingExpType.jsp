<%@ include file="../recruit/fileHeader.jsp" %>

<table width="100%" cellpadding="1" cellspacing="1" class="contentBgColor">
<tr>
<td>
<table width="100%" cellpadding="0" cellspacing="1" class="contenctBackground">
<tr>
	<td>
		
		<table width="100%" cellpadding="3" cellspacing="1">
		<jsp:include page="../form_header.jsp" flush="true" />		
		
		<tr>
        	<td class="classRowLabel" valign="top" align="right"><fmt:message key="recruit.general.label.workingExp" /></td>
        	<td class="classRow"></td>
    	</tr>
    	
    	<tr>
        	<td class="classRowLabel" valign="top" align="right"></td>
        	<td class="classRow"><x:display name="${form.rsFresh.absoluteName}" /><fmt:message key="recruit.general.label.freshMsg" /></td>
    	</tr>
		
       <tr>
        	<td class="classRowLabel" valign="top" align="right"></td>
        	<td class="classRow"><x:display name="${form.rsNonFresh.absoluteName}" /><fmt:message key="recruit.general.label.nonFreshMsg" />
        	<x:display name="${form.txtYear.absoluteName}" /><fmt:message key="recruit.general.label.years" />
        	<x:display name="${form.txtMonth.absoluteName}" /><fmt:message key="recruit.general.label.months" />
        	</td>
    	</tr>
   		
   		<tr>
        	<td class="classRowLabel" valign="top" align="right"></td>
        	<td class="classRow"><x:display name="${form.btnSave.absoluteName}" /><x:display name="${form.btnReset.absoluteName}" /></td>
    	</tr>
   		
		<jsp:include page="../form_footer.jsp" flush="true"/>	
		</table>
	
	</td>
</tr>
</table>		

</td>
</tr>
</table>

