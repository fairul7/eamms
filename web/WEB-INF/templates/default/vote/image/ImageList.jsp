<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="il" value="${widget}"/>
<script language="JavaScript">

function preview(name)
{
   parent.ImagePreview.pic.src = "http://<%= request.getServerName() %>:<%= request.getServerPort() %>"+name;
   return false;
}

</script>

<jsp:include page="/WEB-INF/templates/default/form_header.jsp"/>
<table>

    <c:forEach items="${il.storageFiles}" var="storageFile">
    <tr>
        <td>
            <input TYPE="radio" value="<%=request.getContextPath()%>/storage<c:out value="${storageFile.parentDirectoryPath}"/>/<c:out value="${storageFile.name}"/>" name="choice">
            <a href="" onClick="javascript:return preview('<%=request.getContextPath()%>/storage<c:out value="${storageFile.parentDirectoryPath}"/>/<c:out value="${storageFile.name}"/>')"> <c:out value="${storageFile.name}"/></a>
        </td>
    </tr>
    </c:forEach>
    <tr>
    <td> <br><br><x:display name="${il.deleteButton.absoluteName}"/>
    </td>
    </tr>
</table>
<jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>

