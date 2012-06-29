<%@ include file="/common/header.jsp" %>

<x:config >
<page name="timecardPortlet">
    <com.tms.timecard.ui.TimeCardPortlet name="timecardPorlet"/>
</page>
</x:config>

<html>
    <body>
        <tr><td align="left"><x:display name="timecardPortlet.timecardPorlet" /></</td></tr>

    </body>
</html>