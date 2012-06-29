<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="AddComment">
          <com.tms.wiki.ui.AddComment name="form" width="100%"/>
    </page>
</x:config>

<%--<c:if test="${forward.name=='Save' || forward.name=='cancel_form_action'}">
    <c:redirect url="latestArticles.jsp"/>
</c:if>--%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">

        <p>
        <x:display name="AddComment.form"/>
    </td></tr>
</table>


