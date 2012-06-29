<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<x:config >
<page name="noteportletpage">
    <com.tms.portlet.portlets.notes.ui.NotesPortlet name="noteportlet"/>
</page>
</x:config>

<x:display name="noteportletpage.noteportlet" />
