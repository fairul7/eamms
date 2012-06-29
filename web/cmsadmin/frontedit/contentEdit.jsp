<%@ page import="com.tms.cms.core.ui.ContentHelper"%>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:template type="com.tms.cms.core.ui.FrontEndEditor" />

<html>
<head>
<title>CMS Administration Console</title>
    <link rel="stylesheet" href="../styles/style.css">
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0">

<x:config>
<page name="frontContentEdit">
    <portlet name="editContentPortlet" text="Edit Content" width="100%" permanent="true">
        <com.tms.cms.core.ui.EditContentObjectPanel name="editContentPanel" width="100%">
            <forward name="save" url="contentClose.jsp" redirect="true"/>
            <forward name="submit" url="contentClose.jsp" redirect="true"/>
            <forward name="approve" url="contentClose.jsp" redirect="true"/>
			<forward name="undo" url="contentClose.jsp" redirect="true"/>
            <forward name="cancel_form_action" url="contentClose.jsp" redirect="true"/>
        </com.tms.cms.core.ui.EditContentObjectPanel>
    </portlet>
</page>
</x:config>

        <table cellpadding="2" cellspacing="2" border="0" style="text-align: left; width: 100%;">
          <tbody>
            <tr>
              <td style="vertical-align: top;">
                <br>

                <p>
                  <%--Edit Content--%>
                  <x:display name="frontContentEdit.editContentPortlet"/>
                </p>
              </td>
            </tr>
          </tbody>
        </table>

</body>
</html>