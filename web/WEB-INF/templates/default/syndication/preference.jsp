<%@ page import="com.tms.cms.syndication.ui.SyndicationPreference"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<c:set var="form" value="${widget}"/>
<c-rt:set var="forward_exist" value="<%= SyndicationPreference.LINK_EXIST %>" />

<c:if test="${forward.name == forward_exist}">
    <script>
        alert("RSS Link Exist.");
    </script>

</c:if>
<jsp:include page="../form_header.jsp" flush="true"/>

<table width="100%">

    <tr><td class="portletHeader" colspan="2"><fmt:message key='syndication.label.subheader.register.link'/></td></tr>


    <c:choose>
        <c:when test="${empty form.linkList}">
            <tr><td colspan="2"><fmt:message key='syndication.label.no.item' /></td></tr>
        </c:when>
        <c:otherwise>
                <c:forEach var="link" items="${form.linkList}">
                    <tr>
                        <td colspan="2">[<a href="?cn=<c:out value="${form.absoluteName}"/>&et=deleteLink&id=<c:out value="${link.id}" />"><fmt:message key='syndication.label.delete'/></a>] <a href="?cn=<c:out value="${form.absoluteName}"/>&et=editLink&id=<c:out value="${link.id}"/>"><c:out value="${link.title}"/></a></td>
                    </tr>
                </c:forEach>
        </c:otherwise>
    </c:choose>

    <tr><td colspan="2">&nbsp;</td></tr>
    <tr><td colspan="2">&nbsp;</td></tr>

    <tr><td class="portletHeader" colspan="2"><fmt:message key='syndication.label.subheader.new.link'/></td></tr>

    <tr>
        <td><b><fmt:message key='syndication.label.title'/></b></td>
        <td><x:display name="${form.tfTitle.absoluteName}"/></td>
    </tr>

    <tr>
        <td><b><fmt:message key='syndication.label.link'/></b></td>
        <td><x:display name="${form.tfLink.absoluteName}"/></td>
    </tr>

    <tr>
        <td><b><fmt:message key='syndication.label.refresh.rate'/></b></td>
        <td><x:display name="${form.tfRefreshRate.absoluteName}"/>(<fmt:message key='syndication.label.refresh.rate.minute'/>)</td>
    </tr>

    <tr>
        <td>&nbsp;</td>
        <td>
            <x:display name="${form.btAdd.absoluteName}"/>
            <c:if test="${!empty form.id}">
                <x:display name="${form.btUpdate.absoluteName}"/>
            </c:if>
        </td>
    </tr>



    <tr><td colspan="2">&nbsp;</td></tr>
    <tr><td colspan="2">&nbsp;</td></tr>

    <tr><td class="portletHeader" colspan="2"><fmt:message key='syndication.label.subheader.subscribe'/></td></tr>

    <c:choose>
        <c:when test="${empty form.predefinedList}">
            <tr><td colspan="2"><fmt:message key='syndication.label.no.item' /></td></tr>
        </c:when>
        <c:otherwise>
            <c:set var="categoryId" value="" />
            <c:forEach var="predefinedChannel" items="${form.predefinedList}">

                <c:if test="${categoryId != predefinedChannel.categoryId}">
                    <c:set var="labelName" value="lbCategoryName${predefinedChannel.categoryId}" />
                    <tr><td><b><x:display name="${form.childMap[labelName].absoluteName}" /></b></td></tr>
                </c:if>

                <c:set var="checkBoxName" value="cbChannel${predefinedChannel.id}" />
                <tr><td><x:display name="${form.bgChannelList.childMap[checkBoxName].absoluteName}" /></td></tr>
                <c:set var="categoryId" value="${predefinedChannel.categoryId}" />
            </c:forEach>
                <tr><td colspan="2"><x:display name="${form.btSubscribe.absoluteName}" /></td></tr>
        </c:otherwise>
    </c:choose>
   
</table>

<jsp:include page="../form_footer.jsp" flush="true"/>