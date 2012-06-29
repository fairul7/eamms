<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
    <jsp:include page="../form_header.jsp" flush="true"/>
    <table width="100%" cellpadding="2" cellspacing="1">
        <tr>
            <td valign="top" width="10%"><font class=contentChildName>Name</td>
            <td valign="top"><x:display name="${form.childMap.name.absoluteName}"/> </td>
        </tr>

        <tr>
            <td valign="top" width="10%"><font class=contentChildName>Email </td>
            <td valign="top"><x:display name="${form.childMap.email.absoluteName}"/> </td>
        </tr>

        <tr>
            <td valign="top"><font class=contentChildName>Comments</td>
            <td valign="top"><x:display name="${form.childMap.comments.absoluteName}"/> </td>
        </tr>

        <tr>
            <td class="classRowLabel" valign="top">&nbsp;</td>
            <td class="classRow"> <x:display name="${form.childMap.save.absoluteName}"/>
            </td>
        </tr>


    </table>
  <jsp:include page="../form_footer.jsp" flush="true"/>