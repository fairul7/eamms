<%@ page import="com.tms.portlet.ui.PortalServer"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="crt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:config >
  <page name="calendarPage">
    <calendarview name="calendarView" view="weekly"/>
  </page>
</x:config>

<jsp:include page="includes/header.jsp"/>
<table width="100%" border="0" cellpadding="5" cellspacing="0">
    <tr><td align="center"><x:display name="calendarPage.calendarView"/></td></tr>
</table>

<jsp:include page="includes/footer.jsp"/>
