<%@ page import="com.tms.collab.resourcemanager.model.ResourceManager,
                 java.util.Collection,
                 java.util.Iterator"%>
 <%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
 <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>
<c:set var="bookings" value="${widget.bookings}"/>

<%--
<style>
.button {border-width:1pt; background-color:#E1E1E1; border-style:solid; border-color:#AAAAAA; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:7pt; font-weight:normal}
.header {background-color: #003366; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 7.5pt; color: #FFFFFF; font-weight:bold}
.background {background-color: #EEEEDD; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt}
.row {background-color: #E6E6CA; font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8.5pt; font-weight:normal}
</style>
--%>

<jsp:include page="/WEB-INF/templates/default/form_header.jsp"/>
<table width ="100%" class="contentBgColor">
<tr width ="100%">
    <td align="center" width ="100%">
        <b></b>
    </td>
</tr>
<tr>
    <td align="right" >
    	<b><fmt:message key='general.from'/></b>
        <x:display name="${form.startDate.absoluteName}" />
        <b><fmt:message key="general.to" /></b>
        <x:display name="${form.endDate.absoluteName}" />
        <x:display name="${form.lookButton.absoluteName}" />

    </td>
</tr>


<tr>
    <td>
        <table border="0" cellpadding="2" cellspacing="1" width="100%">
            <tr class="tableHeader" height="20">
                <td> <%--<span style="color:white">--%><fmt:message key='resourcemanager.label.BookedBy'/><%--</span>--%></td>
                <td><%--<span style="color:white">--%><fmt:message key='resourcemanager.label.BookingDate'/><%--</span>--%></td>
                <td><%-- <span style="color:white">--%><fmt:message key='resourcemanager.label.Status'/><%--</span>--%></td>
            </tr>
             <% int i=1;%>
             <% pageContext.setAttribute("pending",String.valueOf(ResourceManager.RESOURCE_BOOKING_PENDING));%>
             <% pageContext.setAttribute("approved",String.valueOf(ResourceManager.RESOURCE_BOOKING_APPROVED));%>
             <% pageContext.setAttribute("returned",String.valueOf(ResourceManager.RESOURCE_BOOKING_RETURNED));%>
             <% pageContext.setAttribute("rejected",String.valueOf(ResourceManager.RESOURCE_BOOKING_DECLINED));%>


            <c:forEach items="${bookings}" var="booking" >
            <% if(i%2 == 0)
            { %>
                 <c:set var="style" value="background: #dddddd"/>
            <%} else
                    {%>
             <c:set var="style" value="background: white"/>
             <%}%>

             <tr class="tableRow">
                <td valign="top">
                    <c:out value="${booking.userName}" />
                </td>
                <td><table border="0" cellspacing="0" cellpadding="0"><tr><td><fmt:message key='resourcemanager.label.Booked'/></td><Td>&nbsp; <fmt:formatDate value="${booking.startDate}" pattern="${globalDatetimeLong}"/>
                </td></tr>
                <tr><Td><fmt:message key='resourcemanager.label.Due'/></td><Td>&nbsp; <fmt:formatDate value="${booking.endDate}" pattern="${globalDatetimeLong}"  />
                </td></tr></table>

                </td>
                <td valign="top">
                <%--    <c:set value="${widget.resource.authorities}" var="authorities"  />
                    <c:set var="userId" value="${widget.widgetManager.user.id}"/>
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
                    %>--%>
                    
                    <c:set var="link" value="${true}"/>
                    <c:if test="${(booking.event.classification == 'pri' && !booking.loginUserInvolved)}">
                        <c:set var="link" value="${false}"/>
                    </c:if>
                    

<%--

                    
                    <c:choose>
                    <c:when test="${canApprove}" >
--%>                
                    
                    <c:if test="${booking.status == pending}" >
                        
                        <c:choose>
                        <c:when test="${link}">
                            <%--<x:event name="${widget.absoluteName}" type="approve" param="resourceId=${booking.resourceId}&eventId=${booking.eventId}&instanceId=${booking.instanceId}" >
                            <fmt:message key='resourcemanager.label.Pendingapproval'/>
                            </x:event>--%>
                            <a href="" onClick="window.open('bookingdetail.jsp?instanceId=<c:out value="${booking.instanceId}"/>&resourceId=<c:out value="${booking.resourceId}"/>&eventId=<c:out value="${booking.eventId}"/>','','height=350,width=500,left=50,top=50,screenx=50,screeny=50,scrollbars=yes,resizable=yes'); return false;"><fmt:message key='resourcemanager.label.Pendingapproval'/></a>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key='resourcemanager.label.Pendingapproval'/>
                        </c:otherwise>
                        </c:choose>
                        
                    <%--</c:when>
                    <c:otherwise><fmt:message key='resourcemanager.label.Pending'/></c:otherwise>
                    </c:choose>
            --%>    </c:if>
                   <c:if test="${booking.status == approved}" >
<%--
                    <c:choose>
                    <c:when test="${canApprove}" >
--%>
                        <c:choose>
                        <c:when test="${link}">
                            <x:event name="${widget.absoluteName}" type="approve" param="resourceId=${booking.resourceId}&eventId=${booking.eventId}&instanceId=${booking.instanceId}" >
                            <fmt:message key='resourcemanager.label.Approved'/>
                            </x:event>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key='resourcemanager.label.Approved'/>
                        </c:otherwise>
                        </c:choose>
<%--
                    </c:when>
                    <c:otherwise><fmt:message key='resourcemanager.label.Approved'/></c:otherwise>
                    </c:choose>
--%>

                    </c:if>
                    <c:if test="${booking.status == returned}" >

<%--
                    <c:choose>
                    <c:when test="${canApprove}" >
--%>
                        <c:choose>
                        <c:when test="${link}">
                            <x:event name="${widget.absoluteName}" type="approve" param="resourceId=${booking.resourceId}&eventId=${booking.eventId}&instanceId=${booking.instanceId}" ><fmt:message key='resourcemanager.label.Returned'/></x:event>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key='resourcemanager.label.Returned'/>
                        </c:otherwise>
                        </c:choose>
<%--
                    </c:when>
                    <c:otherwise><fmt:message key='resourcemanager.label.Returned'/></c:otherwise>
                    </c:choose>
--%>

                    </c:if>
                    <c:if test="${booking.status == rejected}" >
                        <c:choose>
                        <c:when test="${link}">
                            <x:event name="${widget.absoluteName}" type="approve" param="resourceId=${booking.resourceId}&eventId=${booking.eventId}&instanceId=${booking.instanceId}" ><fmt:message key='resourcemanager.label.Declined'/></x:event>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key='resourcemanager.label.Declined'/>
                        </c:otherwise>
                        </c:choose>
                    </c:if>


                </td>
             </tr>
              <% i++;%>
            </c:forEach>
        </table>

    </td>
</tr>
<tr>
    <td>
    </td>
</tr>



</table>
<jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
