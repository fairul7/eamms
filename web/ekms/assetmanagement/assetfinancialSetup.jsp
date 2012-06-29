<%@include file="/common/header.jsp"%>
<x:permission permission="com.tms.assetmanagement.manageAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">

<x:config>
	<page name="assetfinancialSetup">
		<com.tms.assetmanagement.ui.AssetFinancialSetupForm  name="form" />
	</page>	
</x:config>

<!-------- Forward Message---------->

<c:if test="${forward.name=='insert'}" >
 <script> alert("<fmt:message key="asset.message.financialInsert"/>")</script>
  
</c:if>
<c:if test="${forward.name=='update'}" >
 <script> alert("<fmt:message key="asset.message.financialUpdate"/>")</script>	
</c:if>

 <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp"/>

        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="middle">
            	<td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='asset.label.AssetManagement'/> > <fmt:message key='asset.label.financialSetup'/></font></b></td>
            	<td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        	</tr>
        	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
       	 	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
          	<tr><td>
        		<x:display name="assetfinancialSetup.form"></x:display>
        	</td></tr>
        	<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
     	</table>

</x:permission>     
    
    <jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>
