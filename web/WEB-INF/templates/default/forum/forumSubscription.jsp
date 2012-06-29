<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="widget" value="${widget}"/>
<link ref="stylesheet" href="images/style.css">
<jsp:include page="../form_header.jsp" flush="true"/>
<tr>
    <td width="30%" nowrap class="forumRowLabel" valign="top" align="right">Subscriptions&nbsp;</td>
    <td width="70%" class="forumRow"><x:display name="${widget.forums.absoluteName}"/></td>
</tr>
<tr>
    <td width="30%" nowrap class="forumRowLabel" valign="top" align="right">Forum Type&nbsp;</td>
    <td width="70%" class="forumRow"><x:display name="${widget.forumSetting.absoluteName}"/></td>
</tr>
<jsp:include page="../form_footer.jsp" flush="true"/>
