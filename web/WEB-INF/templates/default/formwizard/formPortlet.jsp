<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="list" value="${widget}"/>

<table border="0" align="center" cellspacing="0" cellpadding="0" width="100%">
    <c:if test="${list.pendingForm > 0}">




        <tr><td>Approval Required: <a href="<c:out value="${list.workflowLink}" />"><font color="red"><c:out value="${list.pendingForm}" /></font></a></td></tr>

        <tr><td><hr></td></tr>
    </c:if>

    <c:choose>
        <c:when test="${empty list.formList}">
            <tr><td><br><br>No Forms Found<br><br></td></tr>
        </c:when>
        <c:otherwise>
            <ul>
            <c:forEach items="${list.formList}" var="form" varStatus="cnt">
                 <tr>
                    <td valign="top"><li><a href="<c:out value="${list.link}"/>?formId=<c:out value="${form.formId}"/>&reset=true"><c:out value="${form.formDisplayName}" /></a></li></td>
                 </tr>
            </c:forEach>
            </ul>
        </c:otherwise>
    </c:choose>
    <tr><td>&nbsp;</td></tr>
    <tr class="portletFooter"><td>&nbsp;</td></tr>

</table>

