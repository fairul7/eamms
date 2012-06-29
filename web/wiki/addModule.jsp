<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="addModule">
          <com.tms.wiki.ui.AddModule name="form" width="100%"/>
    </page>
</x:config>

<%--<c:choose>
    <c:when test="${!empty param.moduleId}">
        <x:set name="AddModule.form" property="parentId" value="${param.parentId}"/>
    </c:when>
    <c:otherwise>
        <x:set name="AddCategory.form" property="parentId" value="${widgets['viewCategories.table'].parentId}"/>
    </c:otherwise>
</c:choose>--%>

<c:if test="${forward.name=='Add'}">
    <script>
     	 alert('Module successfully added');
         opener.location ='viewModules.jsp'
         window.close();
    </script>
</c:if>

<c:if test="${forward.name=='cancel_form_action'}">
    <script>
         window.close();
    </script>
</c:if>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML><HEAD><TITLE>TMS-ekp</TITLE>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<LINK href="./images/default.css" rel=stylesheet>
<META content="MSHTML 6.00.5346.5" name=GENERATOR></HEAD>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginwidth="0" 
marginheight="0"><LINK href="./images/default.css" rel=stylesheet>
<table width="100%" border="0" cellspacing="0" cellpadding="0">

        <p>
        <x:display name="addModule.form"/>
    </td></tr>
</table>
</body>
</html>

