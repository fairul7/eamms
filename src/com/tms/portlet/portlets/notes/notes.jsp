<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<!-- Declare Widgets -->
<x:config>
<page name="notesPage">
	<portlet name="portletNotesAdd" text="Add Notes">
		<addNotesForm name="addNotesForm" />
	</portlet>	

	<notesTable name="notesTable" />
	
	<portlet name="portletNotesEdit" text="Edit Notes">
		<editNotesForm name="editNotesForm" />
	</portlet>	
	
</page>
</x:config>

<!-- Handle Events -->
<c:if test="${!empty param.notesId}">
	<x:set name="notesPage.portletNotesEdit.editNotesForm" property="notesId" value="${param.notesId}"/>
</c:if>


<!-- Display -->
<html>
<head>

<title>Notes</title>
</head>

<body>
	<table width="100%">
		<tr><td><x:display name="notesPage.portletNotesAdd"/></td></tr>
		
		<tr><td><x:display name="notesPage.notesTable"/></td></tr>
		
		<tr><td><x:display name="notesPage.portletNotesEdit"/></td></tr>
    </table>
</body>

</html>
