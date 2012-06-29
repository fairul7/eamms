<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>



<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">

<tr>
  <td class="calendarRow"><font color="red">Information :<br></font>
    <x:display name="${form.info.absoluteName}" />
  </td>
</tr>

<tr>
  <td class="calendarRow" align="left">
    <x:display name="${form.submitButton.absoluteName}" />     
  </td>
</tr>



</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
