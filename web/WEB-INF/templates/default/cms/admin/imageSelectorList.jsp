<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="list" value="${widget}"/>
<script language="JavaScript">
<!--
function preview(name) {
   if (document.all) {
       parent.ImagePreview.pic.src = "http://<%= request.getServerName() %>:<%= request.getServerPort() %>" + name;
   }
   else {
       top.document.getElementById('ImagePreview').contentDocument.getElementById('pic').src = "http://<%= request.getServerName() %>:<%= request.getServerPort() %>" + name;
   }
   return true;
}
//-->
</script>

<form>
<table>
    <c:forEach items="${list.imageList}" var="co">
    <c:set var="image" value="${co.contentObject}"/>
    <tr>
        <td>
            <input TYPE="radio" onclick='return preview("<c:out value="${pageContext.request.contextPath}/cms/storage${image.filePath}"/>")' value="<%=request.getContextPath()%>/cms/storage<c:out value="${image.filePath}"/>" name="choice">
            <c:out value="${image.fileName}"/>
        </td>
    </tr>
    </c:forEach>
</table>
</form>
