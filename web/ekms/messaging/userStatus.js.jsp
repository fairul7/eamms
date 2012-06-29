<%@ page import="java.io.Writer,
                 com.tms.collab.messaging.model.Util"%>

<%@include file="includes/userStatusInclude.jsp" %>

var c = "<%= Util.escapeJavaScript(pageContext.getAttribute("bodyContents").toString())%>";
var us = parent.document.getElementById("userStatus");
var bod = us.contentWindow.document.getElementById("bod");
bod.innerHTML = c;
setTimer();