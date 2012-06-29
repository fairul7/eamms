<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="viewCategories">
          <com.tms.wiki.ui.ViewCategories name="table" width="100%"/>
    </page>
</x:config>

<c:if test="${!empty param.categoryId && param.et=='sel'}">
		<c:redirect url='viewCategories.jsp?categoryId=${param.categoryId}'/>
</c:if>
<c:choose>
    <c:when test="${!empty param.categoryId}">
        <x:set name="viewCategories.table" property="parentId" value="${param.categoryId}"/>
    </c:when>
    <c:otherwise>
        <x:set name="viewCategories.table" property="parentId" value="${widgets['viewCategories.table'].parentId}"/>
    </c:otherwise>
</c:choose>

<c:choose>
<c:when test="${!empty param.moduleId}">
        <x:set name="viewCategories.table" property="moduleId" value="${param.moduleId}"/>
    </c:when>
    <c:otherwise>
        <x:set name="viewCategories.table" property="moduleId" value="${widgets['viewCategories.table'].moduleId}"/>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${not empty widgets['viewCategories.table'].parentId}">
        <c:set var="url" value="addCategory.jsp?parentId=${widgets['viewCategories.table'].parentId}&moduleId=${widgets['viewCategories.table'].moduleId}"/>
    </c:when>
    <c:otherwise>
       <c:set var="url" value="addCategory.jsp?moduleId=${widgets['viewCategories.table'].moduleId}"/>
    </c:otherwise>
</c:choose>

<c:if test="${forward.name=='Add'}">

    <script>
        window.open('<c:out value="${url}"/>', 'mywindow', 'status=yes,width=350, height=250');
    </script>
</c:if>

<c:if test="${forward.name=='deleteError'}">
	<script>
		alert('Some of the categories have subcategories. Do you want to delete?');
	</script>
</c:if>

<%--<c:if test="${!empty param.categoryId}">
    <script>
       <c:redirect value='viewSubCategories.jsp?categoryId=${param.categoryId}'/>;
    </script>
</c:if>--%>
<jsp:include page="includes/header.jsp" flush="true"/>
<td valign=top align=left width="60%">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr><td>
        <x:display name="viewCategories.table"/>
    </td></tr>
</table>







</td>
<%@include file="./includes/rightSideWikiMenu.jsp" %>                					
<%@include file="./includes/footer.jsp" %>      

