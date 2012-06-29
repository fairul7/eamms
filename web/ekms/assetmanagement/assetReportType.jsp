 <%@include file="/common/header.jsp"%>
 <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp"/>
<x:permission permission="com.tms.assetmanagement.manageAssetPermission" module="com.tms.assetmanagement.model.AssetModule" url="noPermission.jsp">
 
    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='asset.label.AssetManagement'/> > <fmt:message key='asset.label.generateReport'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" class="classRowLabel" valign="top" align="left">
            <fmt:message key='asset.report.reportType'/>: <BR>
            </td></tr>
            <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
              <ul>
              		<li><a href="assetReportform.jsp?reportType=depreciationReport"><fmt:message key='asset.report.monthlyDepreciationReport'/></li>
              		<li><a href="assetReportform.jsp?reportType=fixedAssetReport"><fmt:message key='asset.report.fixedAssetReport'/></li>
              </ul>                 	      	               
        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>
    </x:permission>  
    
    <jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>