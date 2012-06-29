<%@ page import="kacang.ui.Widget,
				 kacang.stdui.Form,
				 java.util.*,
				 java.awt.*,
                 kacang.stdui.Hidden"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

		<c:set var="display" value="" />
        <c:forEach var="child" items="${form.children}">
        	<c:set var="display">
        		<x:display name="${child.absoluteName}"/>
        	</c:set>
        </c:forEach>

<c:if test="${display != ''}">
<jsp:include page="../../form_header.jsp"></jsp:include>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
      <td align="right" style="vertical-align: top; width: 153px;" height="22" class="classRowLabel">
      	<fmt:message key="general.label.contentType" />&nbsp;
      </td>
      <td style="vertical-align: top;">
      	<c:forEach var="child" items="${form.children}">
            <x:display name="${child.absoluteName}"/>
        </c:forEach>
      </td>
    </tr>
</table>
<jsp:include page="../../form_footer.jsp"></jsp:include>
</c:if>

