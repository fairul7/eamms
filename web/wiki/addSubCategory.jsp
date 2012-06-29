<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="addSubCategories">
          <com.tms.wiki.ui.AddSubCategory name="table" width="100%"/>
    </page>
</x:config>

<c:if test="${forward.name=='Add' || forward.name=='cancel_form_action'}">
    <script>
         close();
    </script>

</c:if>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr><td>
        <x:display name="addSubCategories.table"/>
    </td></tr>
</table>


