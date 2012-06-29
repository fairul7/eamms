<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

<x:permission permission="com.tms.cms.maillist.ManageMailList" module="com.tms.cms.maillist.model.MailListModule" url="noPermission.jsp" />

<x:config>
    <page name="page1">
        <com.tms.cms.vsection.ui.VSectionContentObjectPopupForm name="popup" />
    </page>
</x:config>
<html>
<head>
<title><fmt:message key='general.label.selectContent'/></title>
<script language="Javascript">
<!--
    function vSectionContentObjectPopupForm_select() {
        f = 'page1.mailListUi1.contentMailListForm';
        sb = 'page1.mailListUi1.contentMailListForm.ffContentIds';
        targetBox = window.opener.document.forms[f].elements[sb];
        sourceBox = document.forms['page1.popup'].elements['page1.popup.contentListTable.selectBox'];

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
        sourceBox = document.forms['page1.popup'].elements['page1.popup.contentListTable.selectBox'];

        // remove selected options
        for(c=sourceBox.length-1; c>=0; c--) {
            if (sourceBox.options[c].selected)
                sourceBox.options[c] = null;
        }

        // submit form
        vSectionContentObjectPopupForm_contentListTable_selectBoxsubmit();
        document.forms['page1.popup'].submit();
    }


//-->
</script>
</head>
<body onload="focus()">

<input type="button" class="button" value="<fmt:message key='general.label.okDone'/>" onclick="vSectionContentObjectPopupForm_select()">
<input type="button" class="button" value="<fmt:message key='general.label.removeSelected'/>" onclick="vSectionContentObjectPopupForm_remove()">
<input type="button" class="button" value="<fmt:message key='general.label.cancel'/>" onclick="window.close()">

<x:display name="page1.popup" />

</body>
</html>