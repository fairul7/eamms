<%@ page import="kacang.ui.Event,
                 kacang.stdui.Table"%>
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<HTML>
<HEAD>
    <link rel="stylesheet" href="<c:url value='/cmsadmin/styles/style.css'/>">
</HEAD>
<BODY>

<script language="Javascript">
<!--
    function vSectionContentObjectPopupForm_select() {
        if (window.opener.document.forms['contentEdit.editContentPortlet.editContentPanel.containerForm'])
            targetBox = window.opener.document.forms['contentEdit.editContentPortlet.editContentPanel.containerForm'].elements['contentEdit.editContentPortlet.editContentPanel.containerForm.contentObjectForm.selectBox'];
        else if (window.opener.document.forms['contentAdd.addContentPortlet.addContentPanel.containerForm'])
            targetBox = window.opener.document.forms['contentAdd.addContentPortlet.addContentPanel.containerForm'].elements['contentAdd.addContentPortlet.addContentPanel.containerForm.contentObjectForm.selectBox'];
        else if (window.opener.document.forms['frontContentEdit.editContentPortlet.editContentPanel.containerForm'])
            targetBox = window.opener.document.forms['frontContentEdit.editContentPortlet.editContentPanel.containerForm'].elements['frontContentEdit.editContentPortlet.editContentPanel.containerForm.contentObjectForm.selectBox'];
        sourceBox = document.forms['vSectionContentObjectPopupForm'].elements['vSectionContentObjectPopupForm.contentListTable.selectBox'];

        // remove target options
        for(c=targetBox.length-1; c>=0; c--) {
            targetBox.options[c] = null;
        }

        // copy source to target
        var values='';
        var names='';
        for(c=0; c<sourceBox.length; c++) {
            if (sourceBox.options[c].value == '')
                break;
            values += sourceBox.options[c].value + "|";
            names += sourceBox.options[c].text + "|";
            //targetBox.options[targetBox.length] = new Option(sourceBox.options[c].text, sourceBox.options[c].value, false, true);
        }
        window.opener.document.getElementById('selectedValue').value=values;
        window.opener.document.getElementById('selectedName').value=names;
        window.opener.document.getElementById('resetSortOrder').focus();
        self.close();
    }

    function vSectionContentObjectPopupForm_remove() {
        sourceBox = document.forms['vSectionContentObjectPopupForm'].elements['vSectionContentObjectPopupForm.contentListTable.selectBox'];

        // remove selected options
        for(c=sourceBox.length-1; c>=0; c--) {
            if (sourceBox.options[c].selected)
                sourceBox.options[c] = null;
        }

        // submit form
        vSectionContentObjectPopupForm_contentListTable_selectBoxsubmit()
        document.forms['vSectionContentObjectPopupForm'].submit();
    }


//-->
</script>

<input type="button" class="button" value="<fmt:message key='general.label.okDone'/>" onclick="vSectionContentObjectPopupForm_select()">
<input type="button" class="button" value="<fmt:message key='general.label.removeSelected'/>" onclick="vSectionContentObjectPopupForm_remove()">
<input type="button" class="button" value="<fmt:message key='general.label.cancel'/>" onclick="window.close()">

<c:if test="${!empty param.id}">
    <script>
    <!--
        window.open('<c:url value="/cmspreview/content.jsp"/>?id=<c:out value="${param.id}"/>', 'vSectionPreview');
    //-->
    </script>
</c:if>

<x:display name="vSectionContentObjectPopupForm" />

</BODY>
</HTML>
