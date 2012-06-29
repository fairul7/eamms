<%@ page import="com.tms.collab.directory.model.DirectoryModule,
                 com.tms.collab.directory.model.Folder,
                 com.tms.collab.directory.model.Contact,
                 kacang.services.security.Group"%>
<%@include file="/common/header.jsp" %>

<c:set var="w" value="${widget}" />

<c:if test="${w.personal}">
    <form action="<c:url value="/ekms/addressbook/abContactList.jsp" />">
    <b><fmt:message key='addressbook.label.personalContacts'/></b>
    <br>
    <input name="query" class="input" size="15">
    <select name="folderId" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8.5pt; font-weight:normal">
        <option value=""><fmt:message key='addressbook.label.personal.allFolders'/></option>
        <c:forEach items="${w.personalFolderList}" var="item">
            <%
                Folder folder = (Folder)pageContext.findAttribute("item");
                String value = folder.getName();
                if (value.length() > 20){
                    value = value.substring(0, 20) + "..";
                }
                pageContext.setAttribute("value", value);
            %>
            <option value="<c:out value="${item.id}"/>"><c:out value="${item.name}"/></option>
        </c:forEach>
    </select>
    <input type="submit" class="button" value="<fmt:message key='addressbook.label.personal.searchButton'/>">
    </form>
</c:if>

<c:if test="${w.intranet}">
    <p>
    <form action="<c:url value="/ekms/addressbook/udContactList.jsp" />">
    <b><fmt:message key='addressbook.label.intranetUsers'/></b>
    <br>
    <input name="query" class="input" size="15">
    <select name="groupId" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8.5pt; font-weight:normal">
        <option value=""><fmt:message key='addressbook.label.intranet.allGroups'/></option>
        <c:forEach items="${w.groupList}" var="item">
            <%
                Group group = (Group)pageContext.findAttribute("item");
                String value = group.getName();
                if (value.length() > 20){
                    value = value.substring(0, 20) + "..";
                }
                pageContext.setAttribute("value", value);
            %>
            <option value="<c:out value="${item.id}"/>"><c:out value="${item.name}"/></option>
        </c:forEach>
    </select>
    <input type="submit" class="button" value="<fmt:message key='addressbook.label.intranetUserssearchButton'/>">

    </form>
</c:if>

<c:if test="${w.business}">
    <p>
    <form action="<c:url value="/ekms/addressbook/bdContactList.jsp" />">
    <b><fmt:message key='addressbook.label.businessDirectory'/></b>
    <br>
    <input name="query" class="input" size="15">
    <select name="folderId" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8.5pt; font-weight:normal">
        <option value=""><fmt:message key='addressbook.label.business.allFolders'/></option>
        <c:forEach items="${w.directoryFolderList}" var="item">
            <%
                Folder folder = (Folder)pageContext.findAttribute("item");
                String value = folder.getName();
                if (value.length() > 20){
                    value = value.substring(0, 20) + "..";
                }
                pageContext.setAttribute("value", value);
            %>
            <option value="<c:out value="${item.id}"/>"><c:out value="${value}"/></option>
        </c:forEach>
    </select>
    <select name="companyId" style="border-width:1.5pt; background-color:#FFFFFF; text-decoration:none; font-family:Verdana, Arial, Helvetica, sans-serif;font-size:8.5pt; font-weight:normal">
        <option value=""><fmt:message key='addressbook.label.allCompanies'/></option>
        <c:forEach items="${w.companyList}" var="item">
            <%
                Contact company = (Contact)pageContext.findAttribute("item");
                String value = company.getCompany();
                if (value.length() > 20){
                    value = value.substring(0, 20) + "..";
                }
                pageContext.setAttribute("value", value);
            %>
            <option value="<c:out value="${item.id}"/>"><c:out value="${value}"/></option>
        </c:forEach>
    </select>
    <input type="submit" class="button" value="<fmt:message key='addressbook.label.businessDirectory.searchButton'/>">
    </form>

    <x:permission var="approveContacts" permission="<%= DirectoryModule.PERMISSION_MANAGE_CONTACTS %>" module="<%= DirectoryModule.class.getName() %>"/>
    <x:permission var="approveCompanies" permission="<%= DirectoryModule.PERMISSION_MANAGE_COMPANIES %>" module="<%= DirectoryModule.class.getName() %>"/>
    <c:if test="${ (approveContacts && w.pendingContactCount > 0) || (approveCompanies && w.pendingCompanyCount > 0) }">
        <p>
        <b><fmt:message key='addressbook.label.approval'/></b>
        <hr size="1">
        <c:if test="${approveContacts && w.pendingContactCount > 0}">
            <li><fmt:message key='addressbook.label.pendingContacts'/>:
            <a href="<c:url value="/ekms/addressbook/bdContactApprovalList.jsp" />"><c:out value="${w.pendingContactCount}"/></a>
        </c:if>
        <c:if test="${approveCompanies && w.pendingCompanyCount > 0}">
            <li><fmt:message key='addressbook.label.pendingCompanies'/>:
            <a href="<c:url value="/ekms/addressbook/bdCompanyApprovalList.jsp" />"><c:out value="${w.pendingCompanyCount}"/></a>
        </c:if>
    </c:if>
</c:if>
