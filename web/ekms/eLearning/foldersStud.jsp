<%@ page import="com.tms.collab.project.WormsHandler"%>
<%@ include file="/common/header.jsp" %>

<x:config>
<page name="folder">
    <com.tms.elearning.folder.ui.FolderTableStud name="folderTableStud" width="100%">

        <listener_script>
            String id = event.getRequest().getParameter("id");

        </listener_script>
    </com.tms.elearning.folder.ui.FolderTableStud>
</page>
</x:config>
<c:if test="${!empty param.cid}">

    <x:set name="folder.folderTableStud" property="cid" value="${param.cid}" ></x:set>

</c:if>



  <c:if test="${!empty param.id}">


                     <script>

                           popUp('displayModuleIntro.jsp?id=<c:out value="${param.id}"/>');
                    function popUp(URL) {
                    day = new Date();
                    id = day.getTime();
                    eval("page" + id + " = window.open(URL, '" + id + "', 'toolbar=0,scrollbars=1,location=0,statusbar=1,menubar=0,resizable=0,width=700,height=500,left = 226,top = 182');");
                       }


                     

                    </script>



                    </c:if>





<%@include file="/ekms/includes/header.jsp"%>
<jsp:include page="includes/header.jsp"/>
     <table width="100%" border="0" cellspacing="0" cellpadding="5">
                <tr valign="middle">
                    <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.maintitle'/> > <A HREF="regCourses.jsp"><fmt:message key='eLearning.menu.label.RegisteredCourses'/></A> > <fmt:message key='eLearning.menu.label.Modules'/></font></b></td>
                    <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
                </tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
                <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

<x:display name="folder.folderTableStud"/>
       </td></tr>
                      <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
                  </table>


<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%></html>