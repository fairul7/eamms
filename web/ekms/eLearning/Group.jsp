<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Dec 3, 2004
  Time: 6:22:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>
<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>

<table align="center">

<tr>&nbsp;&nbsp;</tr>
<tr><input type="button" class="button" value="    MODULES    " onclick="self.location='folders.jsp'"></tr>
<tr>&nbsp;&nbsp;</tr>
<tr><input type="button" class="button" value="    LESSONS     " onclick="self.location='lessons.jsp'"></tr>
<tr>&nbsp;&nbsp;</tr>
<tr><input type="button" class="button" value="ASSESSMENTS" onclick="self.location='assessment.jsp'"></tr>

</table>

<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>