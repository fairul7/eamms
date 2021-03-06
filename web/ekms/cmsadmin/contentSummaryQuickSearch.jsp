<%@ page import="com.tms.cms.core.model.ContentManager,
                 kacang.Application"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

    <table border="0" cellpadding="4" cellspacing="0" style="border:0px solid gray" width="100%">
    <form name="quickSearchForm"
          action="<%= request.getContextPath() %>/ekms/cmsadmin/contentList.jsp"
          method="POST">

    <input type="hidden" name="cn" value="cms.contentListPortlet.contentListTable" />
    <input type="hidden" name="et" value="action" />
    <input type="hidden" name="cms.contentListPortlet.contentListTable.filterForm.pageSizeSelectBox" value="20" />

        <tr>
          <td align="right">
            <div style="border:0 solid silver; width:" class="contentFont">

                <fmt:message key='general.label.quickSearch'/>

                <input
                   type="text" name="cms.contentListPortlet.contentListTable.filterForm.name"
                   size="20">

                <select
                    name="cms.contentListPortlet.contentListTable.filterForm.contentType"
                    size="0">

                    <option value=""><fmt:message key='general.label.any'/></option>
                    <%
                    Application app = Application.getInstance();
                    ContentManager cm = (ContentManager)app.getModule(ContentManager.class);
                    Class[] classes = cm.getAllowedClasses(null);
                    for (int i=0; i<classes.length; i++) {
                    %>
                    <option value="<%= classes[i].getName() %>"><%= app.getMessage("cms.label_" + classes[i].getName()) %></option>
                    <%
                    }
                    %>
                </select>

                <select
                    name="cms.contentListPortlet.contentListTable.filterForm.checkOutSelectBox"
                    size="0">

                <option value="any">
                    <fmt:message key='general.label.any'/></option>

                <option value="checkedIn">
                    <fmt:message key='general.label.notCheckedOut'/></option>

                <option value="checkedOut">
                    <fmt:message key='general.label.checkedOut'/></option>

                <option value="submitted">
                    <fmt:message key='general.label.submitted'/></option>

                <option value="approved">
                    <fmt:message key='general.label.approved'/></option>

            </select>

            <select
                name="cms.contentListPortlet.contentListTable.filterForm.publishedSelectBox"
                size="0">

                <option value="any">
                    <fmt:message key='general.label.any'/></option>

                <option value="true">
                    <fmt:message key='general.label.published'/></option>

                <option value="false">
                    <fmt:message key='general.label.notPublished'/></option>

            </select>

            <select
                name="cms.contentListPortlet.contentListTable.filterForm.archivedSelectBox"
                size="0">

                <option value="any">
                    <fmt:message key='general.label.any'/></option>

                <option value="true">
                    <fmt:message key='general.label.archived'/></option>

                <option value="false" selected>
                    <fmt:message key='general.label.notArchived'/></option>

            </select>

            <input
                class="button"
                type="submit"
                name="button*cms.contentListPortlet.contentListTable.filterForm.filter"
                value="<fmt:message key='general.label.show'/>">
            </div>

          </td>
        </tr>
    </form>
    </table>
