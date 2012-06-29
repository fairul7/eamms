<%@ page import="org.apache.log4j.PropertyConfigurator,
                 java.net.URL"%>
<%
    URL url = getClass().getResource("/log4j.properties");
    String path = url.getPath();
    PropertyConfigurator.configure(url);
%>
<%= path %> reloaded