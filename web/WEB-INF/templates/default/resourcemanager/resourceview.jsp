<%@ page import="com.tms.collab.resourcemanager.model.ResourceManager,
                 com.tms.collab.calendar.ui.UserUtil,
                 org.w3c.tidy.Out"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<c:set var="form" value="${widget}"/>
<c:set var="resource" value="${widget.resource}"/>


<script  LANGUAGE="JavaScript">
function MM_openBrWindow(winName,features){
    var selectBox = document.forms['<c:out value="${form.absoluteName}" ></c:out>'].elements['select6'];
    //alert(selectBox.options.length);
    for(i=0; i<selectBox.options.length;i++){
        if(selectBox.options[i].selected &&selectBox.options[i].text!='To Book')
            window.open(selectBox.options[i].value,winName,features);
    }
}
</script>

   <jsp:include page="/WEB-INF/templates/default/form_header.jsp"/>

<table width="100%" cellpadding="1" cellspacing="0" class="contentBgColor">
<tr>
<td>

<table width="100%" cellpadding="0" cellspacing="1" class="classBackground">



   <tr align="center" width = "100%">
                <td colspan="2" align="center" width = "100%" class="classRowLabel">
                    <fmt:message key="resourcemanager.label.resourceDetails"/>&nbsp;
                </td>
            </tr>

            <tr width = "100%">
                <td  class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
                <fmt:message key="resourcemanager.label.ResourceName"/>&nbsp;
                </td>
                <td class="classRow"> <c:out value="${resource.name}" />
                </td>
            </tr>


            <tr>
                <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
                <fmt:message key="resourcemanager.label.Description"/>&nbsp;
                </td>
                <td class="classRow">
                <c:out value="${resource.description}" />
                </td>
            </tr>

           <c:if test="${resource.imageType != 0}" >
            <tr>
                <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
                    <fmt:message key="resourcemanager.label.Image"/>&nbsp;
                </td>
                <td class="classRow" >
                    <c:if test="${resource.imageType == 2}" >
                    <IMG src="<c:out value="${resource.image}"/>"/>
                    </c:if>
                    <c:if test="${resource.imageType == 1}" >
                    <% if (request.getServerPort() == 80){%>
                    <IMG src="http://<%=request.getServerName()%><%=request.getContextPath()%>/storage<c:out value="${resource.image}"/>"/>
                    <%}else{ %>
                    <IMG src="http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/storage<c:out value="${resource.image}"/>"/>
                    <%}%>
                    </c:if>
                </td>
            </tr>
            </c:if>

           <c:if test="${resource.category!=null}" >
            <tr>
                <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
                     <fmt:message key="resourcemanager.label.Category"/>&nbsp;
                </td>
                <td class="classRow">
                     <c:out value="${resource.category}" ></c:out>
                </td>
            </tr>
           </c:if>

            <tr >
                <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
                 <fmt:message key="resourcemanager.label.requireApproval"/>&nbsp;
                </td>
                <td class="classRow"><c:if test="${resource.requireApproval}" >
                     <fmt:message key="resourcemanager.label.Yes"/>
                    </c:if>
                    <c:if test="${!resource.requireApproval}" >
                     <fmt:message key="resourcemanager.label.No"/>
                    </c:if>
                </td>
            </tr>

     <c:if test="${resource.requireApproval}" >
        <TR>
            <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
            <fmt:message key="resourcemanager.label.approver"/>&nbsp;
            </td>
            <td class="classRow" valign="top">
                <c:forEach items="${resource.authorities}" var="member" varStatus="status" >
                <c:if test="${status.index>0}" >,</c:if>
                    <%
                        out.print(UserUtil.getUser((String)pageContext.getAttribute("member")).getName());
                    %>
                </c:forEach>
            </td>
        </tr>
     </c:if>

            <tr>
                <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
                   <fmt:message key="resourcemanager.label.Status"/>&nbsp;
                </td>
                <td class="classRow">
                    <c:choose>
                    <c:when test="${!resource.deleted}">
                        <fmt:message key="resourcemanager.label.Active"/>
                    </c:when>
                     <c:otherwise>
                        <fmt:message key="resourcemanager.label.Inactive"/>
                    </c:otherwise>
                    </c:choose>
                </td>
            </tr>

               <tr>
                <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
                   <fmt:message key="resourcemanager.label.Classification"/>&nbsp;
                </td>
                <td class="classRow">
                <%  pageContext.setAttribute("pub", String.valueOf(ResourceManager.RESOURCE_CLASSIFICATION_TYPE_PUBLIC)); %>
                <%  pageContext.setAttribute("pri", String.valueOf(ResourceManager.RESOURCE_CLASSIFICATION_TYPE_PRIVATE)); %>

                    <c:if test="${resource.classification == pub}"><fmt:message key='resourcemanager.label.Public'/></c:if>
                     <c:if test="${resource.classification == pri}"><fmt:message key='resourcemanager.label.Private'/></c:if>
                </td>
            </tr>



            <tr>
                <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top"><fmt:message key='resourcemanager.label.Createdby'/></td><td class="classRow"><c:out value="${form.creator}" /> ( <fmt:formatDate pattern="${globalDatetimeLong}"value="${resource.creationDate}" />)
                </td>
            </tr>

            <c:if test="${resource.modificationDate!=null}">
            <tr>
                <td class="classRowLabel" width="23%" height="20" valign="top" align="right"  valign="top">
                 	<fmt:message key='resourcemanager.label.Modifiedby'/>&nbsp;
                 </td><td  class="classRow">
                    <c:set value="${resource.modifiedBy}" var="modifyBy" />
                    <%
                        String modifyBY = (String)pageContext.getAttribute("modifyBy");
                        out.print(UserUtil.getUser(modifyBY).getName());
                    %>
                    (<fmt:formatDate pattern="${globalDatetimeLong}" value="${resource.modificationDate}" /> )
                </td>

            </tr>
            </c:if>
            <tr>
            <td class="classRow"> </td>
              <td  class="classRow">
                <c:if test="${form.editable}" >
                   <input
                class="button"
                type="button"
                name="editButton"
                value="<fmt:message key="resourcemanager.label.Edit"/>"
                onBlur=""
                onClick="document.location='<c:url value="/ekms/resourcemanager/"/>editresourceform.jsp?resourceId=<c:out value="${resource.id}" />';"
                onFocus=""

>
                </c:if> </td>
            </tr>



    </td>
  </tr>
   <tr width = "100%">
                <td  class="classRow" colspan="2" width="100%">
                
                    <x:display name="${form.bookingView.absoluteName}" ></x:display>
                    
                </td>

    </tr>

<%--
    <tr>
    <td  class="classRow"  colspan="2"> <x:display name="${form.viewAllButton.absoluteName}" ></x:display>  </td>
    </tr>
--%>


</table>
</td>
</tr>
</table>


<script language="JavaScript">
function newBooking(){
    document.forms['<c:out value="${form.actionForm.absoluteName}" />'].submit();

}
</script>
<jsp:include page="/WEB-INF/templates/default/form_footer.jsp"/>
