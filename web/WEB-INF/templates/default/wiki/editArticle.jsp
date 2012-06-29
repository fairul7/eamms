<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
    <jsp:include page="../form_header.jsp" flush="true"/>
    <table width="100%" cellpadding="2" cellspacing="1">
        <tr>
            <td valign="top" width="40%"><font class="tsText">Article</td>
            <td valign="top"><x:display name="${form.childMap.article.absoluteName}"/> </td>
        </tr>

        <tr>
            <td valign="top"><font class="tsText">Edit Summary</td>
            <td valign="top"><x:display name="${form.childMap.editSummary.absoluteName}"/> </td>
        </tr>

        <tr>
            <td valign="top"><font class="tsText">Category</td>
            <td valign="top" ><b></b><x:display name="${form.childMap.category.absoluteName}"/> </td>
        </tr>

        <tr>
            <td valign="top"><font class="tsText">Tags</td>
            <td valign="top" ><b></b><x:display name="${form.childMap.tags.absoluteName}"/>(note: seperate tags with comma eg: ajax, web2.0)</td>
        </tr>

        <tr>
            <td class="classRowLabel" valign="top">&nbsp;</td>
            <td class="classRow"> <x:display name="${form.childMap.panel.absoluteName}"/>

            </td>
        </tr>


    </table>
  <jsp:include page="../form_footer.jsp" flush="true"/>