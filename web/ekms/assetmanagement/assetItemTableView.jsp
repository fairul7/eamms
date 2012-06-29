<%@ page import="java.util.Collection, 
				com.tms.assetmanagement.model.AssetModule,
				java.util.Iterator, 
				kacang.services.security.SecurityService,         
                kacang.Application "%>

<%@include file="/common/header.jsp"%>

<x:config>
    <page name="assetTable">
     	<com.tms.assetmanagement.ui.AssetItemTable name="table" width="100%" />
    </page>
</x:config>

<%
	Application app = Application.getInstance();
	SecurityService securityService = (SecurityService) app.getService(SecurityService.class);
	String userId = securityService.getCurrentUser(request).getId();
	
	boolean isAdmin = securityService.hasPermission(userId, "com.tms.assetmanagement.manageAssetPermission", null, null);
    boolean isUser = securityService.hasPermission(userId, "com.tms.assetmanagement.accessAssetPermission", null, null);
    
      if(isUser || isAdmin) {  
    
  %>    	
 
<c:if test="${!empty param.itemId}" >
 	<c:redirect url= "assetItemEditForm.jsp?id=${param.itemId}"/>;   
 </c:if>

<c:if test="${forward.name=='Add'}" >
 <c:redirect url= "assetItemForm.jsp"/>;  
</c:if>


<%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp"/>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='asset.label.AssetManagement'/> > <fmt:message key='asset.label.viewItem'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
         
            <x:display name="assetTable.table" />
        
        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>

<%
}
%>
    
    <jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>