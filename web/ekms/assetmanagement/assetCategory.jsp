<%@include file="/common/header.jsp" %>
<x:permission permission="com.tms.assetmanagement.accessAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">

<x:config>
    <page name="assetCategory">
     	<com.tms.assetmanagement.ui.AssetCategoryTable name="table" template="table" width="100%" />

     	<com.tms.assetmanagement.ui.AssetCategoryForm name="form"  width="100%" />
    </page>
</x:config>


<c:if test="${!empty param.categoryId}" >
	<script>
 		window.location = "assetCategory.jsp?id=<c:out value="${param.categoryId}"/>";
  	</script>     
 </c:if>
 
 <c:if test="${!empty param.id}">
	<x:set name="assetCategory.form" property="strCategoryId" value="${param.id}"/>
</c:if>

<c:if test="${forward.name=='Duplicated'}" >
	<script> alert("<fmt:message key="asset.message.duplicatedCategory"/> ")</script>	
</c:if>

<c:if test="${forward.name=='inValidDep'}" >
	<script> alert("<fmt:message key="asset.message.invalidDepRate"/>")</script>	
</c:if>

<c:if test="${forward.name=='categoryInUsed'}" >
	<script> alert("<fmt:message key="asset.message.categoryInUsed"/>")</script>	
</c:if>
 
 <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp" />
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
            <tr valign="middle">
            	<td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='asset.label.AssetManagement'/> > <fmt:message key='asset.label.addcategory'/></font></b></td>
            	<td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        	</tr>
        	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
       	 	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
          	<tr><td>
        		<x:display name="assetCategory.form"></x:display>
        	</td></tr>
        	<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
     	</table>

	<br>
        <table cellpadding="4" cellspacing="1" class="sfaBackground" width="100%">
                <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='asset.label.AssetManagement'/> > <fmt:message key='asset.label.viewCategory'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        	</tr>
        	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        	<tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
        
            <tr valign="top">
                <td align="left" valign="top" >       
					<x:display name="assetCategory.table"></x:display>
 				</td></tr> 
 			<tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>  
    	</table>
    	
</x:permission>

<jsp:include page="includes/footer.jsp" />
<%@include file="/ekms/includes/footer.jsp"%>

