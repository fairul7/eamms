<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="list" value="${widget}"/>


<table border="0" align="center" cellspacing="1" cellpadding="4" width="100%">
    <tr><td colspan="4" class="forumHeader"><b>Form Wizard</b></td></td>

    <c:choose>
        <c:when test="${empty list.publicFormList}">
            <tr><td colspan="4"><br><br>No Forms Found<br><br></td></tr>
        </c:when>
        <c:otherwise>
            <c:forEach items="${list.publicFormList}" var="form" varStatus="cnt">
                 <tr>
                    <td height="20" valign="top"><a href="frmViewForm.jsp?formID=<c:out value="${form.formsID}"/>"><c:out value="${form.formDisplayName}" /></a></td>
                 </tr>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</table>
