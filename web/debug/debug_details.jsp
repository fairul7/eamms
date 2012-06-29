<%@ page import="kacang.Application,
                 kacang.ui.WidgetManager,
                 kacang.ui.Widget,
                 java.beans.BeanInfo,
                 java.beans.Introspector,
                 java.beans.PropertyDescriptor,
                 java.lang.reflect.Method,
                 java.lang.reflect.Array"%>

<html>
<head>
    <title>debug_details</title>
</head>
<style>
<!--
td {
    font-family: Verdana;
    font-size: 8pt;
    border: 1px solid silver;
}
//-->
</style>
<body onload="focus()">

<%
    WidgetManager widgetManager = WidgetManager.getWidgetManager(request);
%>

<!-- Widget Details -->
<%
    String name = request.getParameter("name");
    if (name != null) {
        Widget comp = widgetManager.getWidget(name);
        if (comp != null) {
%>
            <h3>
                <%= comp.getAbsoluteName() %>
                [<%= comp.getClass().getName() %>]
            </h3>

            <table cellspacing="0" cellpadding="5">

<%
            BeanInfo beanInfo = Introspector.getBeanInfo(comp.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for(int x=0; x<pds.length; x++) {
                PropertyDescriptor pd = pds[x];
                Method method = pd.getReadMethod();
                try {
%>
                <tr>
                    <td valign="top">
                        <%= pd.getName() %>
                    </td>
                    <td valign="top">
                        <%= method.invoke(comp, null) %>
                    </td>
                </tr>
<%
                }
                catch(Exception e) {
                    ;
                }
            }
%>
            </table>
<%
        }
    }
%>

</body>
</html>