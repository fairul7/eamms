<%--
  Created by IntelliJ IDEA.
  User: tirupati
  Date: Oct 18, 2004
  Time: 3:41:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ include file="/common/header.jsp" %>
<x:config>
    <page name="category">

        <com.tms.elearning.coursecategory.ui.CategoryAddForm name="categoryAddForm" width="100%">

        </com.tms.elearning.coursecategory.ui.CategoryAddForm>
    </page>
</x:config>

<x:permission permission="com.tms.elearning.coursecategory.Add"
              module="com.tms.elearning.coursecategory.model.CategoryModule" url="noPermission.jsp">

    <c:choose>
        <c:when test="${forward.name == 'updated'}">
            <script>
                alert("<fmt:message key='eLearning.alert.label.categoryadded'/>");
                window.location = "category.jsp";
            </script>
        </c:when>
        <c:when test="${forward.name == 'added'}">
            <script>
                alert("<fmt:message key='eLearning.alert.label.categoryadded'/>");
                window.location = "category.jsp";
            </script>
        </c:when>
    </c:choose>

    <%@ include file="/ekms/includes/header.jsp" %>
    <jsp:include page="includes/header.jsp"/>

    <table width="100%" border="0" cellspacing="0" cellpadding="5">
        <tr valign="middle">
            <td height="22" bgcolor="#003366" class="contentTitleFont"><b><font color="#FFCF63"
                                                                                class="contentTitleFont"><fmt:message key='eLearning.menu.label.Administrator_Menu'/> > <fmt:message key='com.tms.elearning.coursecategory.Add'/></font></b></td>
            <td align="right" bgcolor="#003366" class="contentTitleFont">&nbsp;</td>
        </tr>
        <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor"><img
                src="<c:url value="/ekms/" />images/blank.gif" width="5" height="10"></td></tr>
            <%--
                            <tr><td colspan="2" valign="TOP" bgcolor="#EFEFEF" class="contentBgColor">
            --%>
        <x:display name="category.categoryAddForm"/>
            <%--
                       </td></tr>
                                <tr><td colspan="2" valign="TOP" bgcolor="#CCCCCC" class="contentStrapColor"><img src="<c:url value="/ekms/" />images/blank.gif" width="5" height="15"></td></tr>
            --%>
    </table>

</x:permission>
<jsp:include page="includes/footer.jsp"/>
<%@ include file="/ekms/includes/footer.jsp" %>