<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Nov 25, 2004
  Time: 4:09:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>
<x:config>
<page name="user">
    <com.tms.elearning.core.ui.UserAddForm name="userAddForm" width="100%">
        <forward name="added" url="user.jsp" redirect="true"/>
    </com.tms.elearning.core.ui.UserAddForm>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
<x:display name="user.userAddForm" />
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>