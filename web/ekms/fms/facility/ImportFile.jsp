<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.fms.facility.manager" module="com.tms.fms.facility.model.FacilityModule" url="/ekms/home.jsp"/>

<x:config>
	<page name="Import">
		<com.tms.fms.facility.ui.ExcelImportForm name="excelImport" />
	</page>
</x:config>

<c:if test="${forward.name == 'Empty'}" >
  <script>
  	alert('Please select a file to upload');
  </script>
</c:if>

<c:if test="${forward.name == 'Fail'}" >
  <script>
  	alert('Failed to upload the file! Please check the file"s format');
  </script>
</c:if>

<c:if test="${forward.name == 'import'}" >
  <script>
  	alert('Successfully import excel file');  	
	window.open('<c:url value="/ekms/fms/read.jsp"/>','newContentPopup','scrollbars=yes,resizable=yes,width=700,height=500')                    
  </script>
</c:if>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" />

<table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
        <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='fms.label.ImportCsvFile'/></font></b></td>
        <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

        <x:display name="Import.excelImport" ></x:display>
    
    </td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
</table>
<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp" %>