<%@ page import="kacang.ui.Event,
                 com.tms.collab.taskmanager.ui.TaskView,
                 kacang.stdui.Table"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<c:set var="form" value="${widget}"/>

<c:set var="catUrl">
<%= response.encodeURL(request.getRequestURI()) %>?<%= Event.PARAMETER_KEY_WIDGET_NAME %>=<c:out value="${form.absoluteName}"/>&<%=Event.PARAMETER_KEY_EVENT_TYPE %>=<%=TaskView.PARAMETER_KEY_VIEW_CATEGORY%>
</c:set>

<script>
function openWindow(url,name,style){
    window.open(url,name,style);
    return false;
}

</script>

<%
    String cn = request.getParameter(Event.PARAMETER_KEY_WIDGET_NAME);
    if(cn!=null)
        pageContext.setAttribute("cn",cn);
%>
<c:if test="${form.catTable.absoluteName == cn}" >
    <%
        String evt = request.getParameter(Event.PARAMETER_KEY_EVENT_TYPE);
        if(Table.PARAMETER_KEY_SELECTION.equals(evt)){
            String id = request.getParameter("id");
            if(id!=null&&id.trim().length()>0){ %>
             <script>
                window.open('taskcatform.jsp?id=<%=id%>','CategoryForm','scrollbars=yes,resizable=yes,width=480,height=250');
             </script>

       <% }
        }
    %>

</c:if>



<jsp:include page="../form_header.jsp" flush="true"/>
<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
            <tr><td class="calendarHeader" colspan="7">Task Manager</td></tr>

    <tr>    <td class="calendarFooter" colspan="7" >
    <table width="100%"> <tr> <Td align="left" class="calendarFooter">
    <c:if test="${form.view!=0}" >
       Listing <x:display name="${form.pageSizeSB.absoluteName}" />
       </c:if>
        </td>
        <td align="right" colspan="6"  class="calendarFooter">
        <x:display name="${form.viewSB.absoluteName}" ></x:display>
        </td></tr></table>
         </td>
    </tr>

    <tr>
        <td colspan="7" class="calendarFooter" >
         <table width="100%"> <tr> <Td align="left">
        <%--<x:display name="${form.addCatButton.absoluteName}" />--%>
       <%-- <input
            class="button"
            type="submit"
            name="<c:out value="${form.addCatButton.absoluteName}" />"
            value="Add Category"
            onBlur=""
            onClick="window.open('taskcatform.jsp','CategoryForm','scrollbars=yes,resizable=yes,width=480,height=250');return false"
            onFocus=""
        >--%>
<%--        <x:display name="${form.catButton.absoluteName}" ></x:display>--%>
        </td>
        <td align="right">
        <c:if test="${form.view != 5}" >
                <x:display name="${form.userSB.absoluteName}" ></x:display>
        </c:if>
        </td></tr></table>
        </td>
    </tr>

    <c:choose>
        <c:when test="${form.view == 0}" >
                <tr>
                    <td  class="calendarFooter" colspan="7">
                        <b>Categories</b>
                    </td>
                </tr>
                <c:forEach items="${form.categories}" var="cat"  >
                    <Tr valign="top">
                           <td colspan="7"  class="calendarFooter" align="left">
               <a href="<c:out value="${catUrl}"/>&<%=TaskView.PARAMETER_KEY_CATEGORYID%>=<c:out value="${cat.id}"/>" >
              <img src="<c:url value="/ekms/"/>images/folder.gif" BORDER="0" ALIGN="ABSMIDDLE"><c:out value="${cat.name}" />
                         </a>
                        </td>
                    </tr>
                </c:forEach>
        </c:when>

        <c:when test="${form.view == 1 || form.view == 2||form.view == 3}" >
            <c:if test="${form.taskList.totalRows > 0}" >
                                      <x:display name="${form.taskList.absoluteName}" />
            </c:if>
            <c:if test="${form.taskList.totalRows <= 0}">
                 <tr>
                    <td align="center" class="calendarRow" colspan="7">
                    <br>
                          No task found.
                          <br>&nbsp
                    </td>
                </tr>
            </c:if>
        </c:when>

        <c:when test="${form.view == 4}" >
            <tr class="row">
                    <td colspan="7" class="calendarFooter">
                        <b>Categories</b>
                    </td>
            </tr>
            <Tr  class="row">
                <td colspan="7">
                  <a href="<c:out value="${catUrl}"/>&<%=TaskView.PARAMETER_KEY_CATEGORYID%>=<c:out value="${form.taskList.categoryId}"/>" >
                 <img src="<c:url value="/ekms/"/>images/folder.gif"  BORDER="0" ALIGN="ABSMIDDLE">   <c:out value="${form.taskList.category}" ></c:out>
                   </a>
                </td>


            </tr>

            <x:display name="${form.taskList.absoluteName}" />
            <c:forEach items="${form.categories}" var="cat"  >
                <c:if test="${cat.id != form.categoryId}" >
                    <Tr  class="row">
                        <td class="calendarFooter" colspan="7">
                         <a href="<c:out value="${catUrl}"/>&<%=TaskView.PARAMETER_KEY_CATEGORYID%>=<c:out value="${cat.id}"/>" >
                                <img src="<c:url value="/ekms/"/>images/folder.gif"  BORDER="0" ALIGN="ABSMIDDLE">    <c:out value="${cat.name}" />
                         </a>
                        </td>
                    </tr>
               </c:if>
            </c:forEach>

        </c:when>
        <c:when test="${form.view == 5}" >
              <tr  class="row">
                    <td align="center" class="calendarFooter">
                        <b>Task Categories</b>
                    </td>
            </tr>
            <tr>
                <td>
                    <x:display name="${form.catTable.absoluteName}" />
                </td>
            </tr>
        </c:when>

   </c:choose>
   <%--<tr>    <td class="calendarFooter" colspan="6" >
    <table width="100%"> <tr> <Td align="left">
    <c:if test="${form.view!=0}" >
       Listing <x:display name="${form.pageSizeSB.absoluteName}" />
       </c:if>
        </td>
        <td align="right" colspan="5">
        <x:display name="${form.viewSB.absoluteName}" ></x:display>
        </td></tr></table>
         </td>
    </tr>--%>

   <tr>
        <td colspan="7" class="calendarFooter">
        &nbsp
        <td>
    </tr>

   <tr>
        <td>
        <td>
    </tr>

   <tr>
        <td>
        <td>
    </tr>

   <tr>
        <td>
        <td>
    </tr>

</table>
<script>
function change(){
    select =  document.forms['<c:out value="${form.absoluteName}" />']['<c:out value="${form.viewSB.absoluteName}" />'];
    for(i=0;i<select.length;i++)
    {
        if(select[i].selected &&select[i].text=='Add To Do Task'){
            window.open('<c:url value="/ekms/"/>taskmanager/todotaskform.jsp ','addappointment','scrollbars=yes,resizable=yes,width=450,height=380');
            return false;
            }

    }
    document.forms['<c:out value="${form.absoluteName}" />'].submit();

}

function clearState(){
    sb = document.forms['<c:out value="${form.absoluteName}" />'].elements['<c:out value="${form.viewSB.absoluteName}"/>'];
    for(i=0;i<sb.options.length;i++){
        if(sb.options[i].selected)
        {
           sb.options.value = "-1";
           break;
        }
    }
}
</script>

<jsp:include page="../form_footer.jsp" flush="true"/>
