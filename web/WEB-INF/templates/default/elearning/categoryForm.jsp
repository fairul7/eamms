
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
    <table width="100%" cellpadding="0" cellspacing="0" class="classBackground">
        <tr><td colspan="2"><table width="100%" cellpadding="3" cellspacing="1"></td></tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right"><fmt:message key="eLearning.category.label.name"/>*</td>
            <td class="classRowLabel" valign="top" align="left"><x:display name="${widget.childMap.category.absoluteName}"/></td>
        </tr>
        <tr><td class="classRowLabel" valign="top" align="right">&nbsp;</td><td class="classRowLabel" valign="top" align="right"></td></tr>
        <tr>
            <td class="classRowLabel" valign="top" align="right">&nbsp;</td>
            <td class="classRow"><x:display name="${widget.childMap.submit.absoluteName}"/> <input type="button" class="button" value="Cancel" onclick="self.location='category.jsp'"></td>
        </tr>
    </table>
<jsp:include page="../form_footer.jsp" flush="true"/>
