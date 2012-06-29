<%@include file="/common/header.jsp"%>

<x:permission permission="com.tms.assetmanagement.accessAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">
  
<x:config>
    <page name="disposalEdit">
     	<com.tms.assetmanagement.ui.AssetDisposalEditForm name="form1" width="100%" />
    </page>
</x:config>

<c:if test="${forward.name=='InvalidDate'}" >
 <script> alert("<fmt:message key="asset.message.invalidDate"/>")</script>	
</c:if>

 <c:if test="${!empty param.id}">
	<x:set name="disposalEdit.form1" property="strDisposalid" value="${param.id}"/>
</c:if>


<!-------- Forward Message---------->

<c:if test="${forward.name=='Update'}" >
  <script> alert("<fmt:message key="asset.message.update"/>")</script>  
   <c:redirect url="/ekms/assetmanagement/assetDisposaltableView.jsp" ></c:redirect>
   
</c:if>

<c:if test="${forward.name=='cancel_form_action'}" >
    <c:redirect url="/ekms/assetmanagement/assetDisposaltableView.jsp" ></c:redirect>
</c:if>


<%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp"/>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='asset.label.AssetManagement'/> > <fmt:message key='asset.label.editDisposal'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
         
            <x:display name="disposalEdit.form1" />
        
        </td></tr>
       
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>
    
</x:permission>
    <jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>