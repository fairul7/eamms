<%@ page import="java.util.Collection, 
				com.tms.assetmanagement.model.AssetModule,
				java.util.Iterator, 
				java.util.StringTokenizer,         
                kacang.Application "%>
                
<%@include file="/common/header.jsp"%>

<x:permission permission="com.tms.assetmanagement.manageAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">

<x:config>
    <page name="monthlyDepReport">
     	<com.tms.assetmanagement.ui.MonthlyDepreciationReport name="form" width="100%" />
     	<com.tms.assetmanagement.ui.SavedMonthlyDepReportTable name="table" width="100%" />     	
    </page>
</x:config>

<x:set name="monthlyDepReport.form" property="showPrintViewDisposal" value="false"/>	
<c:if test="${!empty param.year}">
	<c:set var="year" value="${param.year}"/>
	<x:set name="monthlyDepReport.form" property="year" value="${param.year}"/>
</c:if>

<c:if test="${!empty param.category}">
	<c:set var="category" value="${param.category}"/>
	<x:set name="monthlyDepReport.form" property="category" value="${param.category}"/>
</c:if>

<c:if test="${!empty param.saveMonthlyId}">
	<script>  
   		myWin = open("<c:url value="savedMonthlyDepReport.jsp?id=${param.saveMonthlyId}"/>","Report","width=900,height=500,toolbar=no,menubar=yes,resizable=yes,scrollbars=yes");
    </script>
</c:if>

<!-------- Forward Message -------->

<c:if test="${forward.name=='Insert'}" >
 <script> alert("<fmt:message key="asset.message.insert"/>")</script>
</c:if>

<c:if test="${forward.name=='Duplicated'}" >
	<script> alert("<fmt:message key="asset.message.dataDuplicated"/>")</script> 
</c:if>


<%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp"/>   
	
    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='asset.label.AssetManagement'/> > <fmt:message key='asset.label.generateReport'/> > <fmt:message key='asset.label.monthlyDepReport'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
         
            <x:display name="monthlyDepReport.form" />
        
        </td></tr>
   
    <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>
    <BR>
    
    <table width="100%" border="0" cellspacing="0" cellpadding="5">
    <tr valign="middle">
  	<td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='asset.label.AssetManagement'/> > <fmt:message key='asset.label.generateReport'/></font></b></td>
    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
    </tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
    <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
         
   		<x:display name="monthlyDepReport.table" />
        
   </td></tr>
   <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
   <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
   </table>    
	
   </x:permission>  
    
    <jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>