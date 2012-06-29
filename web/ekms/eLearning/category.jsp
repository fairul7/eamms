<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="category">
        <com.tms.elearning.coursecategory.ui.CategoryTable name="categoryTable" width="100%">
            <forward name="add" url="addCategory.jsp"/>
        </com.tms.elearning.coursecategory.ui.CategoryTable>
    </page>
</x:config>

<x:permission permission="com.tms.elearning.coursecategory.View" module="com.tms.elearning.coursecategory.model.CategoryModule">

    <c:choose>
        <c:when test="${forward.name == 'notSelected'}" >
            <script>

                alert("<fmt:message key='eLearning.alert.label.categorynotselected' />");

            </script>
        </c:when>
        <c:when test="${forward.name == 'deleted'}" >
            <script>
                window.location="category.jsp";
            </script>
        </c:when>
    </c:choose>

    <c:if test="${!empty param.id}">
        <script>
            window.open("popCourseDetail.jsp?id=<c:out value="${param.id}"/>","same_frame",
                    "height=400,width=400,status=yes,toolbar=no,menubar=no,location=no, scrollbars=1, resizable=1");
        </script>

    </c:if>

    <%@include file="/ekms/includes/header.jsp"%>
    <jsp:include page="includes/header.jsp"/>
    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63" class="contentTitleFont"><fmt:message key='eLearning.menu.label.Administrator_Menu'/> > <a href="category.jsp"><fmt:message key='eLearning.course.label.category'/></a>  </font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">

            <x:display name="category.categoryTable"/>

        </td></tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
    </table>
</x:permission>

<jsp:include page="includes/footer.jsp"/>
<%@include file="/ekms/includes/footer.jsp"%>