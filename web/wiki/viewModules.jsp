<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.wiki.ManageWiki" module="com.tms.wiki.model.WikiModule" url="noPermission.jsp" />
<x:config>
    <page name="viewModules">
          <com.tms.wiki.ui.ViewModules name="table" width="100%"/>
    </page>
</x:config>

<c:if test="${forward.name=='Add'}">
    <script>
         window.open('addModule.jsp','mywindow', 'status=yes,width=350, height=250');
    </script>
</c:if>
<c:if test="${forward.name=='deleteError'}">
	<script>
		alert('Some of the modules selected have categories. Please make sure you have deleted the categories.');
	</script>
</c:if>

<c:if test="${not empty param.moduleId}">
    <c:redirect url="viewCategories.jsp?moduleId=${param.moduleId}" />
</c:if>

<jsp:include page="includes/header.jsp" flush="true"/>
<td valign=top align=left width="60%">
<c:if test="${!empty widgets['viewCategories.table'].categoryName}">
<table  cellSpacing=4 cellPadding=4  width="100%" >
    <tr>								
	<td colspan="2" class="discusson_td"> 
	<table class="discusson1" cellSpacing=4 cellPadding=4  width="100%" border=1>
	     
     <td class="discusson_td" valign="top" align="left" ><i> <font class ="font_date"> current category :</i> <c:out value="${widgets['viewCategories.table'].categoryName}"/> </font><td>
     <td class="discusson_td" valign="top" align="left" ><i> <font class ="font_date"> &nbsp; &nbsp; &nbsp; created on :</i> <c:out value="${widgets['viewCategories.table'].categoryCreatedOn}"/> </font><td>	
     </tr>       
    </table>
    </td></tr>
</table>
</c:if>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr><td>
        <x:display name="viewModules.table"/>
    </td></tr>
</table>
</td>
<%@include file="./includes/rightSideWikiMenu.jsp" %>    
<jsp:include page="includes/footer.jsp" flush="true"/>



