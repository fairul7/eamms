<%@ page import="com.tms.portlet.taglibs.PortalServerUtil,
                 com.tms.collab.taskmanager.ui.ProgressForm"%>
<%@include file="/common/header.jsp"%>

<x:config >                             
    <page name="progressformpage" >
        <com.tms.collab.taskmanager.ui.ProgressForm name="progressform"/>
    </page>
</x:config>

<c-rt:set var="set" value="<%=ProgressForm.FORWARD_SET_SUCCESSFULLY%>"></c-rt:set>

<c:if test="${forward.name == set}" >
    <script>
        alert("<fmt:message key='taskmanager.label.progresssetsuccessfully'/>!");
        window.opener.location = "<c:url value="/ekms/calendar/calendar.jsp" ></c:url>";
        window.close();
    </script>
</c:if>

<c-rt:set var="cancel" value="<%=ProgressForm.FORWARD_CANCEL%>"/>
<c:if test="${cancel == forward.name}" >
    <script>
        window.close();
    </script>
</c:if>



<c:if test="${!empty param.taskId}" >
    <x:set name="progressformpage.progressform" property="taskId" value="${param.taskId}" />
</c:if>

<c:if test="${!empty param.userId}" >
    <x:set name="progressformpage.progressform" property="userId" value="${param.userId}" />
</c:if>



    <jsp:include page="/ekms/init.jsp"/>
    <c-rt:set var="stylesheet" value="<%= PortalServerUtil.PROPERTY_STYLESHEET %>"/>
    <modules:portalserverutil name="portal.server" property="${stylesheet}" var="ekmsCSS"/>
    <link rel="stylesheet" href="<c:out value="${ekmsCSS}"/>">


<html>
    <TITLE> <fmt:message key='taskmanager.label.taskProgress'/>
    </TITLE>
    <body>
        <x:display name="progressformpage.progressform" />
    </body>


</html>
