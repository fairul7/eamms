<%@include file="/common/header.jsp"%>

<x:permission permission="com.tms.assetmanagement.accessAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">

<x:config>
    <page name="assetItem">
     	<com.tms.assetmanagement.ui.AssetItemForm name="form" width="100%" />
    </page>
</x:config>

<x:set name="assetItem.form" property="isEditMode" value="false"/>

<c:if test="${forward.name=='Insert'}" >
 <script> 
 	alert("<fmt:message key="asset.message.insert"/>")
 	window.location ="assetItemTableView.jsp"
 </script>
</c:if>

<c:if test="${forward.name=='errorUnit'}" >
<%-- refresh jsp page --%>
</c:if>

<c:if test="${forward.name=='setupNotification'}" >
  <script> 
  	alert("<fmt:message key="asset.message.insert"/>")
 	window.open('assetNotification.jsp?notification=true&strNumb=1','','resizable=yes,width=550,height=550,menubar=no,toolbar=no,scrollbars=yes');
  	window.location ="assetItemTableView.jsp";   
</script>	  
</c:if>

<c:if test="${forward.name=='Duplicated'}" >
  <script> alert("<fmt:message key="asset.message.duplicatedAsset"/> " )</script>	
</c:if>

<c:if test="${forward.name=='UnselectCategory'}" >
 <script> alert("<fmt:message key="asset.message.unselectedCategory"/>")</script>	
</c:if>

<c:if test="${forward.name=='cancel_form_action'}" >
    <c:redirect url="/ekms/assetmanagement/assetItemTableView.jsp" ></c:redirect>
</c:if>

<c:if test="${forward.name=='ErrorInGenerateMonthlyDep'}" >
    <script> alert("<fmt:message key="asset.message.errorInMonthlyDep"/>")</script>	
</c:if>

<%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp"/>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='asset.label.AssetManagement'/> > <fmt:message key='asset.label.items'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
         
            <x:display name="assetItem.form" />
        
        </td></tr>
          <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>
 </x:permission>   
    
    <jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>