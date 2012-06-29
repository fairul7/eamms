<%@ include file="/common/header.jsp" %>
<x:permission permission="com.tms.wiki.ManageWiki" module="com.tms.wiki.model.WikiModule" url="noPermission.jsp" />
<x:config>
    <page name="lockedArticles">
          <com.tms.wiki.ui.LockedArticles name="table" width="100%"/>
    </page>
</x:config>

<c:if test="${forward.name=='lock'}">
    <script>
        alert('article locked successfully');
    </script>
</c:if>
<c:if test="${forward.name=='unlock'}">
    <script>
        alert('article unlocked successfully');
    </script>
</c:if>
<c:if test="${forward.name=='delete'}">
    <script>
        alert('Wwant to delet article?');
    </script>
</c:if>

<jsp:include page="includes/header.jsp" flush="true"/>
<td valign=top align=left width="60%">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr><td>
        <x:display name="lockedArticles.table"/>
    </td></tr>
</table>
</td>                					
<%@include file="./includes/rightSideWikiMenu.jsp" %>    
<jsp:include page="includes/footer.jsp" flush="true"/>    

