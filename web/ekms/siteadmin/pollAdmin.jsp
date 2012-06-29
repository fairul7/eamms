<%@ include file="/common/header.jsp" %>

<x:permission permission="com.tms.collab.vote.ManageVotes" module="com.tms.collab.vote.model.PollModule" url="noPermission.jsp" />

<x:config>
   <page name="PollAdmin">
      <%-- ortlet name="pollportlet" text="<fmt:message key='vote.label.votes'/>" width="100%" permanent="true" --%>
       <com.tms.collab.vote.ui.PollAdminView name="PollAdminView"/>
      <%-- /portlet --%>
   </page>
</x:config>
<%
    String param = request.getParameter("event");
    Application app = Application.getInstance();
    if(param==null)
    	param="";
    if("view".equals(param)){
        String title = app.getMessage("vote.label.voteListing","Vote Listing");
        pageContext.setAttribute("title",title);
    }else{
        String title = app.getMessage("vote.label.addNewVote","Add New Vote");
        pageContext.setAttribute("title",title);
    }
%>

<%@include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true"/>

<c:set var="bodyTitle" scope="request"><fmt:message key="vote.label.votes"/> > <c:out value="${title}"/></c:set>
<jsp:include page="/ekms/includes/bodyHeader.jsp" flush="true"/>

    <%-- x:display name="PollAdmin.pollportlet" / --%>
    <x:display name="PollAdmin.PollAdminView" />

<jsp:include page="/ekms/includes/bodyFooter.jsp" flush="true"/>
<jsp:include page="includes/footer.jsp" flush="true"/>
<%@include file="/ekms/includes/footer.jsp" %>