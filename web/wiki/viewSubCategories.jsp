<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="viewSubCategories">
          <com.tms.wiki.ui.ViewSubCategories name="table" width="100%"/>
    </page>
</x:config>

<c:if test="${forward.name=='Add'}">
    <script>
        window.open('addSubCategory.jsp', 'mywindow', 'status=yes,width=350,height=250');
    </script>
</c:if>


<c:if test="${!empty param.categoryId}">
    <x:set name="viewSubCategories.table" property="categoryId" value="${param.categoryId}"/>
</c:if>



<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr><td>
        <x:display name="viewSubCategories.table"/>
    </td></tr>
</table>


