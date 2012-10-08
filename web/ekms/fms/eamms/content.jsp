<%@ page import="com.tms.cms.core.model.ContentUtil,
                 com.tms.cms.core.model.ContentManager"%>
<%@ include file="/common/header.jsp" %>
<x:template type="TemplateProcessVote" />
<x:template type="TemplateProcessBookmark" />
<%@ include file="/ekms/includes/header.jsp" %>
<jsp:include page="includes/header.jsp" flush="true" />
<table cellpadding="0" cellspacing="0" width="100%">
    <tr>
        <td valign="top">
            <table cellpadding="3" cellspacing="0" width="100%">
                <tr><td class="contentPath"><x:template type="TemplateDisplayPath" properties="rootId=com.tms.cms.section.Section_Sections" /></td></tr>
            </table>
        </td>
    </tr>
    <tr>
        <td valign="top" class="contentBody">
            <br>
            <table cellpadding="0" cellspacing="0" width="95%" align="center">
                <tr><td><x:template type="TemplateDisplaySubsections" properties="id=${param.id}&orphans=true&showCount=true"/></td></tr>
            </table>
        </td>
    </tr>
    <tr>
        <td valign="top" class="contentBody">
            <br>
            <table cellpadding="0" cellspacing="0" width="95%" align="center">
                <tr><td valign="top"><x:template type="TemplateDisplayContent" properties="noHeader=true&hideSummary=true&types=com.tms.cms.article.Article,com.tms.cms.document.Document,com.tms.cms.page.Page,com.tms.cms.bookmark.Bookmark" /></td></tr>
                <tr><td><x:template type="TemplateDisplayRelated" /></td></tr>
            </table>
        </td>
    </tr>
    <tr>
        <td class="contentStrapColor">
            <table cellpadding="0" cellspacing="0" width="95%" align="center">
                <tr>
                    <td>
                        <x:template name="widget" type="TemplateDisplayContentSubscriptionOption" properties="id=${param.id}" body="custom">
                            <c:if test="${!widget.hidden}">
                                <c:set var="subscription" value="${widget.subscription}"/>
                                <c:choose>
                                <c:when test="${!empty subscription}">
                                    <input type="button" class="button" onclick="location.href='?action=unsubscribe&id=<c:out value="${param.id}"/>'" value="<fmt:message key="security.label.unsubscribe"/>">
                                </c:when>
                                <c:otherwise>
                                    <input type="button" class="button" onclick="location.href='?action=subscribe&id=<c:out value="${param.id}"/>'" value="<fmt:message key="security.label.subscribe"/>">
                                </c:otherwise>
                                </c:choose>
                            </c:if>
                        </x:template>

                        <x:template name="fronteditor" type="TemplateDisplayFrontEndEdit" properties="editMode=true" body="custom">
                            <c:if test="${fronteditor.permissionMap['CheckOut']}">
                            <script>
                            <!--
                                function editContent(id){
                                    window.open('<c:url value="/ekms/content/popup/editContent.jsp"/>?action=edit&id=' + id,'newContentPopup','scrollbars=yes,resizable=yes,width=700,height=500')
                                }
                            //-->
                            </script>
                            <input class="button" value="<fmt:message key='general.label.editContent'/>" type="button" onClick="editContent('<c:out value="${param.id}"/>')"/>
                            </c:if>
                        </x:template>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<jsp:include page="includes/footer.jsp" flush="true" />
<%@ include file="/ekms/includes/footer.jsp" %>
