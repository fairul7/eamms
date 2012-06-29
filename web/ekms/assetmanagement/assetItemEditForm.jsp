<%@include file="/common/header.jsp"%>
<x:permission permission="com.tms.assetmanagement.accessAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">

<x:config>
    <page name="assetEdit">
     	<com.tms.assetmanagement.ui.AssetItemForm name="form1" width="100%" />
     	<com.tms.assetmanagement.ui.AssetNotificationTable name="table" width="100%"/>
    </page>
</x:config>

 <c:if test="${!empty param.id}">
	<x:set name="assetEdit.form1" property="strItemid" value="${param.id}"/>
	<x:set name="assetEdit.form1" property="isEditMode" value="true"/>
</c:if>

<!-------- Forward Message---------->

<c:if test="${forward.name=='Update'}" >
  <script> alert("<fmt:message key="asset.message.update"/>")</script>	 
  <c:redirect url="/ekms/assetmanagement/assetItemTableView.jsp" ></c:redirect>  
</c:if>

<c:if test="${forward.name=='errorUnit'}" >
  <script> 
  	alert("<fmt:message key="asset.massage.errorUnit"/>") 
</script>	  
</c:if>

<c:if test="${forward.name=='Duplicated'}" >
  <script> alert("<fmt:message key="asset.message.duplicatedAsset"/> <fmt:message key="asset.label.asset"/> <fmt:message key="asset.message.samePurchasedDate"/> ")</script>	
</c:if>

<c:if test="${forward.name=='ErrorInGenerateMonthlyDep'}" >
    <script> alert("<fmt:message key="asset.message.errorInMonthlyDep"/>")</script>	
</c:if>

<c:if test="${forward.name=='cancel_form_action'}" >
    <c:redirect url="/ekms/assetmanagement/assetItemTableView.jsp" ></c:redirect>
</c:if>

<c:if test="${forward.name=='UnselectCategory'}" >
 <script> alert("<fmt:message key="asset.message.unselectedCategory"/>")</script>	
</c:if>

<%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp"/>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='asset.label.AssetManagement'/> > <fmt:message key='asset.label.editAsset'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
         
            <x:display name="assetEdit.form1" />
        
        </td></tr>
  
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>
    	<br>
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
                <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='asset.label.AssetManagement'/> > <fmt:message key='asset.label.viewNotification'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        	</tr>
        	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        
            <tr valign="top">
                <td align="left" valign="top" >       
					<x:display name="assetEdit.table"></x:display>
 				</td></tr>
 				 
 			<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>  
    	</table>
     </x:permission> 
    
    <jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>