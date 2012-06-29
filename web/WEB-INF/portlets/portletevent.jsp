<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<x:config >
<page name="eventportletpage">
    <com.tms.collab.calendar.ui.EventPortlet name="eventportlet"/>
</page>
</x:config>

<html>
    <body>
       <x:display name="eventportletpage.eventportlet" />

    </body>
</html>