<%@ page import="java.util.Collection,
                 java.util.Iterator,
                 com.tms.collab.resourcemanager.model.ResourceManager"%> <%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
 <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set value="${widget}" var="form"  />


<table BORDER="0" CELLSPACING="1" CELLPADDING="3" width="100%" valign="top">
<TR>
    <td class="calendarHeader" align="left"><fmt:message key='resourcemanager.label.resourceBooking'/>: <c:out value="${form.resourceName}"/>
    </td>
</tr>
</table>
<table width="100%" cellspacing="0" cellpadding="0">
<tr>
<td>
    <x:display name="${form.eventView.absoluteName}" />
</td></tr>
</table>
         <jsp:include page="../form_header.jsp" flush="true"/>
                      <c:set value="${widget.resource.authorities}" var="authorities"  />
                    <c:set var="userId" value="${widget.widgetManager.user.id}"/>

<table BORDER="0" CELLSPACING="1" CELLPADDING="3" width="100%" valign="top">
   <%
       Collection col =(Collection) pageContext.getAttribute("authorities");
       String userId = (String) pageContext.getAttribute("userId");
       for (Iterator iterator = col.iterator(); iterator.hasNext();)
       {
           String s = (String) iterator.next();
           if(s.equals(userId))
           {
               pageContext.setAttribute("canApprove",Boolean.TRUE);
               break;
           }
       }
                    %>

<c:if test="${canApprove}" >
             <% pageContext.setAttribute("pending",String.valueOf(ResourceManager.RESOURCE_BOOKING_PENDING));%>
             <% pageContext.setAttribute("approved",String.valueOf(ResourceManager.RESOURCE_BOOKING_APPROVED));%>
             <% pageContext.setAttribute("returned",String.valueOf(ResourceManager.RESOURCE_BOOKING_RETURNED));%>
             <% pageContext.setAttribute("rejected",String.valueOf(ResourceManager.RESOURCE_BOOKING_DECLINED));%>

<TR>
    <td class="calendarRow" align="left">
        &nbsp;
       <c:if test="${form.booking.status!=approved}" >
        <x:display name="${form.approveButton.absoluteName}" /></c:if>
        <c:if test="${form.booking.status!=rejected}" >
        <x:display name="${form.rejectButton.absoluteName}" /></c:if>
        <x:display name="${form.cancelButton.absoluteName}" />
    </td>
</tr>
 </c:if>
 <jsp:include page="../form_footer.jsp" flush="true"/>

</table>
