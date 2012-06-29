<%@include file="/common/header.jsp"%>

<x:config>
    <page name="txy_tree_page">
        <com.tms.cms.taxonomy.ui.ViewNode name="txy_tree"/>
        <com.tms.cms.taxonomy.ui.ViewNodeMapResult name="txy_result"/>
        <com.tms.cms.taxonomy.ui.ViewRelatedNode name="txy_related"/>
    </page>
</x:config>

<c:if test="${!empty param.id}">
<x:set name="txy_tree_page.txy_tree" property="selectedNodeId" value="${param.id}"/>
<x:set name="txy_tree_page.txy_result" property="taxonomyId" value="${param.id}"/>
<x:set name="txy_tree_page.txy_related" property="taxonomyId" value="${param.id}"/>
</c:if>
<c:if test="${empty param.id}">
<x:set name="txy_tree_page.txy_tree" property="selectedNodeId" value=""/>
<x:set name="txy_tree_page.txy_result" property="taxonomyId" value=""/>
<x:set name="txy_tree_page.txy_related" property="taxonomyId" value=""/>
</c:if>

<%@include file="/ekms/includes/header.jsp" %> 
<%@include file="/ekms/content/includes/header.jsp" %>
<table cellpadding="4" cellspacing="1" width="100%" border="0">
<tr>
    <td><b><fmt:message key="taxonomy.label.title"/></b></td>
</tr>
<tr>
    <td>
    <x:display name="txy_tree_page.txy_tree"/> <br>
 </td>
</tr>
<tr>
    <td>
    <x:display name="txy_tree_page.txy_result"/> <br>
 </td>
</tr>
<tr>
    <td>
    <x:display name="txy_tree_page.txy_related"/>
 </td>
</tr>
</table>

<%@include file="/ekms/content/includes/footer.jsp" %>
<%@include file="/ekms/includes/footer.jsp" %>