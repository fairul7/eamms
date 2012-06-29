<%@ page import="kacang.ui.Event"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="root" scope="request" value="${widget.root}"/>
<script language="javascript" src="<%= request.getContextPath() %>/common/tree/tree.js">
</script>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
<tr>
    <td height="28">
        <c:if test="${!empty root.childNodes && !empty root.childNodes[0]}">
            <span id="<c:out value="${widget.name}"/><c:out value="${root.taxonomyId}"/>" style="display: block">
                <script>
                <!--
                    //treeLoad('<c:out value="${tree.name}"/><c:out value="${root.id}"/>');
                //-->
                </script>
                    <c:set var="orig" scope="page" value="${root}"/>
                    <c:set var="level" scope="request" value="1"/>
                    <c:set var="origLevel" scope="request" value="1"/>
                    <c:set var="maxLevel" scope="request" value="${widget.levels}"/>
                    <c:catch var="ie">
                        <jsp:include page="displayTaxonomyTreeRecursion.jsp" flush="true"/>
                    </c:catch>
                    <c:set var="root" scope="request" value="${orig}"/>
            </span>
        </c:if>
    </td>
</tr>
</table>


