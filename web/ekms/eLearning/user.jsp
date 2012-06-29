<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Nov 24, 2004
  Time: 11:36:59 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>

<x:config>
<page name="user">
    <com.tms.elearning.core.ui.UserTable name="userTable" width="100%">
        <forward name="add" url="addUser.jsp"/>
        <listener_script>
            String id = event.getRequest().getParameter("id");
            if (id != null) {
                return new Forward(null, "editUser.jsp?id=" + id, true);
            }
        </listener_script>
    </com.tms.elearning.core.ui.UserTable>
</page>
</x:config>

<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
<x:display name="user.userTable"/>
<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>