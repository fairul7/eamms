<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="folder">
        <com.tms.elearning.folder.ui.FolderTable name="folderTable" width="100%">


        </com.tms.elearning.folder.ui.FolderTable>
    </page>
</x:config>

<x:permission permission="com.tms.elearning.folder.View" module="com.tms.elearning.course.model.CourseModule">

    <c:if test="${!empty param.id}">
        <x:set name="folder.folderTable" property="id" value="${param.id}"></x:set>
    </c:if>

    <c:choose>
        <c:when test="${forward.name == 'add'}">
            <c:redirect url="addFolder.jsp"/>
        </c:when>
        <c:when test="${forward.name == 'deleted'}">
            <script>
                alert("<fmt:message key='eLearning.alert.label.moduledeleted'/>");
                window.location = "folders.jsp";
            </script>
        </c:when>
        <c:when test="${forward.name == 'notSelected'}">
            <script>
                alert("<fmt:message key='eLeanring.alert.label.modulenotselected'/>");
            </script>
        </c:when>
        <c:when test="${forward.name == 'activate'}">
            <script>
                alert("<fmt:message key='eLearning.general.message.activated'/>");
                /*window.location = "folders.jsp";*/
            </script>
        </c:when>
        <c:when test="${forward.name == 'deactivate'}">
            <script>
                alert("<fmt:message key='eLearning.general.message.deactivated'/>");
                /* window.location = "folders.jsp";*/
            </script>
        </c:when>
    </c:choose>

    <%@ include file="/ekms/includes/header.jsp" %>
    <jsp:include page="includes/header.jsp"/>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont">
                <fmt:message key='eLearning.menu.label.Instructor_Menu'/> > <A HREF="folders.jsp"><fmt:message
                    key='eLearning.menu.label.registermodule'/></A></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img
                src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

            <x:display name="folder.folderTable"/>

        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img
                src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>

</x:permission>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>