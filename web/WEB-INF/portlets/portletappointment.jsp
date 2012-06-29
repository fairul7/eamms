<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<x:config >
<page name="appointmentportletpage">
    <com.tms.collab.calendar.ui.AppointmentPortlet name="appointmentportlet"/>
</page>
</x:config>

<html>
    <body>
       <x:display name="appointmentportletpage.appointmentportlet" />

    </body>
</html>