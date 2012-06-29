<%@ page import="com.tms.collab.calendar.ui.CalendarEventView"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="view" value="${widget.child}"/>

<%--
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
   <tr  width="100%" align="left">
    <td  width="100%" align="left">
--%>
     <x:display name="${view.absoluteName}" ></x:display>
<%--
    </td>
   </tr>

</table>
--%>
