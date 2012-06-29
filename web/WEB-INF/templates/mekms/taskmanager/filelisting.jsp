<%@ page import="kacang.ui.Event,
                 com.tms.collab.taskmanager.ui.TaskListing,
                 com.tms.collab.taskmanager.ui.FileListing"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="files" value="${widget.files}" />



<%--<table cellpadding="0" border="0" cellspacing = "0">

<tr valign="top">
   <td align="left">--%>
<% int i=0;%>
<c:forEach items="${widget.files}" var="file">
       <c:if test="${widget.downloadable}">
      <a href="<c:out value='${pageContext.request.contextPath}'/>/storage/<c:out value="${widget.folderId}" />/<c:out value="${file.name}" />" target="_blank">
        </c:if>
         <c:out value="${file.name}" />
        <c:if test="${widget.downloadable}"></a> </c:if>
        <c:if test="${widget.deleteable}" > (
        <a href="<%=response.encodeURL(request.getRequestURI())%>?<%=Event.PARAMETER_KEY_WIDGET_NAME%>=<c:out value="${widget.absoluteName}"/>&<%=Event.PARAMETER_KEY_EVENT_TYPE%>=<%=FileListing.PARAMETER_EVENT_DELETE%>&<%=FileListing.PARAMETER_DELETE_FILENAME%>=<c:out value="${file.name}"/>">
  <fmt:message key='taskmanager.label.Delete'/></a> )</c:if><br>
    <%i++;%>
</c:forEach>
     <% if(i==0){%><fmt:message key='taskmanager.label.None'/><%}%>
<%--
    </td>
   </tr>
</table>
--%>
