<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="linkSelector">
         <com.tms.cms.core.ui.ContentListTable name="contentTable" customSelection="true" showPreview="true" sort="date" desc="true" />
    </page>
</x:config>

<html>
<head>
    <link rel="stylesheet" href="<c:url value='/cmsadmin/styles/style.css'/>">

<script type="text/javascript" src="<c:url value="/common/htmlarea/popups/popup.js"/>"></script>
<script language="javascript">
<!--
window.resizeTo(640, 600);
if (window.scrollbars)
    window.scrollbars.visible=true;

function select_url(url) {
  // pass data back to the calling window
  var param = new Object();
  param["f_href"] = url;
  param["f_title"] = "";
  param["f_target"] = "";
  __dlg_close(param);
  return false;
}
//-->
</script>
</head>
<body>

<c:choose>
    <c:when test="${!empty param.url}">
        <script>
        <!--
            select_url("<c:out value='${param.url}'/>");
        //-->
        </script>
    </c:when>
    <c:when test="${empty param.preview && !empty param.id}">
        <script>
        <!--
            select_url("content.jsp?id=<c:out value='${param.id}'/>");
        //-->
        </script>
    </c:when>
    <c:otherwise>
        <c:if test="${param.preview && !empty param.id}">
            <script>
            <!--
                window.open('<c:url value="/cmspreview/content.jsp?id=${param.id}"/>', 'linkPreview');
            //-->
            </script>
        </c:if>
       <form>
          <fmt:message key='general.label.url'/> <input name="url" type="text" size="50" value="http://">
          <input type="submit" class="button" value="<fmt:message key='general.label.done'/>">
       </form>
       <x:display name="linkSelector.contentTable" />




        <!---  loh requested enhancement //--->





                      <x:template name="selector" type="com.tms.cms.bookmark.ui.BookmarkSelector" body="custom">
                      <form name="bookmarkSelector">

                          <p>
                          <hr size="1">
                          <b>[<fmt:message key='general.label.formWizard'/>]</b>
                          <br><fmt:message key='general.label.linkToAFormProcess'/>
                          <select name="formSelect">
                          <c:forEach var="proc" items="${selector.formList}">
                              <option value="<c:out value='${proc.formId}'/>"><c:out value='${proc.formDisplayName}'/></option>
                          </c:forEach>
                          </select>
                          <input type="button" class="button" onclick="select_url('<c:url value="/cms/form.jsp?formId="/>' + document.forms['bookmarkSelector'].elements['formSelect'].value)" value="<fmt:message key='general.label.ok'/>">

                          <p>
                          <hr size="1">
                          <b>[<fmt:message key='general.label.forums'/>]</b>
                          <br><fmt:message key='general.label.linkToAForum'/>
                          <select name="forumSelect">
                          <c:forEach var="forum" items="${selector.forumList}">
                              <option value="<c:out value='${forum.id}'/>"><c:out value='${forum.name}'/></option>
                          </c:forEach>
                          </select>
                          <input type="button" class="button" onclick="select_url('<c:url value="/cms/forumTopicList.jsp?forumId="/>' + document.forms['bookmarkSelector'].elements['forumSelect'].value)" value="<fmt:message key='general.label.ok'/>">
                      </form>
                      </x:template>



              <!---  loh requested enhancement //--->

       








    </c:otherwise>
</c:choose>

</body>
</html>


