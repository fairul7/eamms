<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>



<c:set var="form" value="${widget}"/>
<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">

<tr>
  <td class="calendarRow"><fmt:message key='calendar.label.reasonforRejec'/>:<br>
    <x:display name="${form.reasonTB.absoluteName}" />
  </td>
</tr>

<tr>
  <td class="calendarRow" align="left">
    <x:display name="${form.rejectButton.absoluteName}" />
<%--
     <x:display name="${form.cancelButton.absoluteName}" />
--%>
    <input type="button" class="button" value="Cancel" onClick="window.close();"/>    
  </td>
</tr>



</table>
<jsp:include page="../form_footer.jsp" flush="true"/>
