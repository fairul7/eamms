<%@include file="/common/header.jsp"  %>

<SCRIPT LANGUAGE="JAVASCRIPT">
  function resourceCheckAll()
  {
    /* will now be transformed into a toggle */
    if(document.forms['<c:out value="${widget.absoluteName}" />'].resourceCalID == null)
      return;

    if (document.forms['<c:out value="${widget.absoluteName}" />'].resourceCalID[1] != null)
      for (var i=0; i < document.forms['<c:out value="${widget.absoluteName}" />'].resourceCalID.length; i++)
        document.forms['<c:out value="${widget.absoluteName}" />'].resourceCalID[i].checked =
         !document.forms['<c:out value="${widget.absoluteName}" />'].resourceCalID[i].checked;
    else
      document.forms['<c:out value="${widget.absoluteName}" />'].resourceCalID.checked =
        !document.forms['<c:out value="${widget.absoluteName}" />'].resourceCalID.checked;
  }

  function approveConfirm()
  {
    if(document.forms['<c:out value="${widget.absoluteName}" />'].resourceCalID == null)
      return;

    var ok = 0;

    if (document.forms['<c:out value="${widget.absoluteName}" />'].resourceCalID[1] != null)
    {
      for(var c=0; c < document.forms['<c:out value="${widget.absoluteName}" />'].resourceCalID.length; c++)
        if(document.forms['<c:out value="${widget.absoluteName}" />'].resourceCalID[c].checked)
        {
          ok = 1;
          break;
        }
    }
    else if (document.forms['<c:out value="${widget.absoluteName}" />'].resourceCalID.checked)
      ok = 1;

    if(ok == 0)
    {
      alert("You must select at least one resource");
		  return;
    }


    if (confirm("Click on OK to confirm"))
      document.forms['<c:out value="${widget.absoluteName}" />'].submit();
  }
</SCRIPT>


<c:set var="form" value="${widget}"/>


<jsp:include page="../form_header.jsp"/>



<table cellpadding="4" cellspacing="1" class="forumBackground" width="100%">
    <Tr>
        <td class="calendarSubheader" align="center">
           <B><fmt:message key='resourcemanager.label.ApproveResourceBooking'/></B>
        </td>
    </tr>
<%--
    <c:set var="resources" value="${form.resources}"/>
--%>
    <c:forEach items="${form.resources}" var="resource" >
        <tr>
            <td class="calendarRow">
            &nbsp;&nbsp;<INPUT TYPE="checkbox" NAME="resourceCalID" VALUE="<c:out value="${resource.id}" />">
                <c:out value="${resource.name}" />
            </td>
        </tr>
    </c:forEach>

    <Tr>
        <td class="calendarRow">
            &nbsp;
        </td>
    </tr>

    <Tr>
        <td class="calendarRow" align="left">
            &nbsp;&nbsp;
            <input type="button" class="button" value="Select All" onClick="resourceCheckAll();return false;" />
            <x:display name="${form.approveButton.absoluteName}" />
            <input type="button" class="button" value="Cancel" onClick="window.close();" />

        </td>
    </tr>


</table>
<jsp:include page="../form_footer.jsp"/>
