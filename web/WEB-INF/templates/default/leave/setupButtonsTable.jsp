<%@ page import="kacang.runtime.*,kacang.ui.*,
                 kacang.stdui.Table" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="table" value="${widget}"/>
<c:set var="model" value="${table.model}" scope="page" />
<c:set var="showCheckbox" value="${!empty model.tableRowKey}"/>
<c:choose>
  <c:when test="${table.multipleSelect}">
    <c:set scope="page" var="selectType" value="checkbox"/>
  </c:when>
  <c:otherwise>
    <c:set scope="page" var="selectType" value="radio"/>
  </c:otherwise>
</c:choose>
<c:set var="showIndex" value="${table.numbering}"/>

<table border="0" cellpadding="4" cellspacing="4"  width="<c:out value="${table.width}"/>">

<%-- Show Form Header --%>
<c:if test="${table == table.rootForm}">
<form name="<c:out value="${table.absoluteName}"/>"
      action="<%= response.encodeURL(request.getRequestURI()) %>"
      method="POST"
      target="<c:out value="${table.target}"/>"
      <c:if test="${!empty table.enctype}">
          enctype="<c:out value="${table.enctype}"/>"
      </c:if>
      onSubmit="<c:out value="${table.attributeMap['onSubmit']}"/>"
      onReset="<c:out value="${table.attributeMap['onReset']}"/>"
>
<input type="hidden" name="<%= Event.PARAMETER_KEY_WIDGET_NAME %>" value="<c:out value="${table.absoluteName}"/>" />
</c:if>
<input type="hidden" name="<%= Event.PARAMETER_KEY_EVENT_TYPE %>" value="<%= Table.PARAMETER_KEY_ACTION %>" />
<tr>
<td>

<%-- Show Action Buttons --%>
<c:if test="${!empty model.actionList[0]}">
<table border="0" cellpadding="4" cellspacing="4" style="border:0px solid gray" width="100%">
  <tbody>
    <tr>
      <td align="right">
        <c:forEach items="${model.actionList}" var="action" varStatus="hdrStatus">
            <c:if test="${!empty action.message}">
                <c:set var="onclick">return confirm('<c:out value="${action.message}"/>')</c:set>
            </c:if>
            <input type="submit" class="button" name="<c:out value="${table.tableActionPrefix}"/><c:out value="${table.absoluteName}"/>.<c:out value="${action.action}"/>" value="<c:out value="${action.label}"/>" onclick="<c:out value="${onclick}"/>" />
        </c:forEach>
      </td>
    </tr>
  </tbody>
</table>
</c:if>

</td>
</tr>
</form>
</table>
