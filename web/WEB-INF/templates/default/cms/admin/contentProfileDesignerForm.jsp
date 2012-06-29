<%@ page import="com.tms.cms.profile.ui.ContentProfileDesignerPanel"%>
<%@ include file="/common/header.jsp" %>

<c:set var="panel" scope="request" value="${widget}"/>
<c:set var="form" scope="request" value="${widget.childMap.fieldForm}"/>
<c:set var="profileForm" scope="request" value="${widget.childMap.profileForm}"/>
<c:set var="optionsForm" scope="request" value="${widget.childMap.optionsForm}"/>

<c-rt:set var="eventEdit" value="<%= ContentProfileDesignerPanel.EVENT_EDIT %>"/>
<c-rt:set var="eventDelete" value="<%= ContentProfileDesignerPanel.EVENT_DELETE %>"/>
<c-rt:set var="eventUp" value="<%= ContentProfileDesignerPanel.EVENT_UP %>"/>
<c-rt:set var="eventDown" value="<%= ContentProfileDesignerPanel.EVENT_DOWN %>"/>

<c:set var="width" value="${panel.width}"/>

<script>
<!--
    function jump() {
        window.location.hash="field";
        document.forms['<c:out value="${form.rootForm.absoluteName}"/>'].elements['<c:out value="${form.childMap.tfName.absoluteName}"/>'].focus();
    }
//-->
</script>

<%--<c:if test="${!empty panel.profile.fields[0]}">--%>
<c:if test="${true}">
<table border="0" width="<c:out value="${width}"/>">
<tr>
  <td colspan="2">
    <b>Profile Fields</b>
    <hr size="1">

    NOTE: Changes will only take effect if the profile is saved.
    <br><br>

    <table width="<c:out value="${width}"/>" style="border:dotted 1px gray" cellpadding="5">
        <tr>
            <td><b>Field Name</b></td>
            <td><b>Label</b></td>
            <td><b>Type</b></td>
            <td><b>Required</b></td>
<%--
            <td><b>Options</b></td>
            <td><b>Value</b></td>
--%>
            <td><b>&nbsp;</b></td>
        </tr>
        <c:forEach items="${panel.profile.fields}" var="field">
        <tr>
            <td>
                <c:out value="${field.name}"/>
            </td>
            <td><c:out value="${field.label}"/></td>
            <td><c:out value="${field.type}"/></td>
            <td>
                <c:if test="${!empty field.validator}">Yes</c:if>
                <c:if test="${empty field.validator}">No</c:if>
            </td>
<%--
            <td><c:out value="${field.options}"/></td>
            <td><c:out value="${field.value}"/></td>
--%>
            <td nowrap>
                <x:event name="${panel.absoluteName}" type="${eventEdit}" param="name=${field.name}">Edit</x:event>
                <x:event name="${panel.absoluteName}" type="${eventDelete}" param="name=${field.name}" html="onclick=\"return confirm('Delete Field?')\"" >Delete</x:event>
                <x:event name="${panel.absoluteName}" type="${eventUp}" param="name=${field.name}">Up</x:event>
                <x:event name="${panel.absoluteName}" type="${eventDown}" param="name=${field.name}">Down</x:event>
            </td>
        </tr>
        </c:forEach>
    </table>

    <p>
<%--    <a href="#field">ADD NEW FIELD</a>--%>
    <input type="button" value="Add New Field" class="button" onclick="jump()">

    <c:if test="${!empty profileForm}">
    <p>
    <table border="0" width="<c:out value="${width}"/>" cellpadding="5">
    <tr>
      <td colspan="2">
       <b>Preview</b>
       <hr size="1">
       <blockquote>
       <div style="border:dotted 1px gray; width: <c:out value="${width}"/>">
            <x:display name="${profileForm.absoluteName}"/>
       </div>
       </blockquote>
      </td>
    </tr>
    </table>
    </c:if>

      </td>
    </tr>
    </table>
    </c:if>

<p>
<a name="field">
<table border="0" width="<c:out value="${width}"/>">
<c:if test="${form == form.rootForm}">
<form name="<c:out value="${form.absoluteName}"/>"
      action="?"
      method="<c:out value="${form.method}"/>"
      target="<c:out value="${form.target}"/>"
      <c:if test="${!empty form.enctype}">
          enctype="<c:out value="${form.enctype}"/>"
      </c:if>
      onSubmit="<c:out value="${form.attributeMap['onSubmit']}"/>"
      onReset="<c:out value="${form.attributeMap['onReset']}"/>"
>
</c:if>
<input type="hidden" name="cn" value="<c:out value="${form.absoluteName}"/>">
<tr>
  <td colspan="3">
   <b>New/Edit Field</b>
   <hr size="1">
  </td>
</tr>
<tr>
  <td valign="top" width="20%">Field Name</td>
  <td colspan="2"><x:display name="${form.childMap.tfName.absoluteName}"/></td>
</tr>
<tr>
  <td valign="top">Label</td>
  <td colspan="2"><x:display name="${form.childMap.tfLabel.absoluteName}"/></td>
</tr>
<tr>
  <td valign="top">Type</td>
  <td colspan="2"><x:display name="${form.childMap.sbType.absoluteName}"/></td>
</tr>
<tr>
  <td valign="top">Options</td>
  <td colspan="2">
    <x:display name="${form.childMap.tbOptions.absoluteName}"/>
    <br>
    Separate each option with a new line
    <br><br>
  </td>
</tr>
<tr>
  <td valign="top">Default Value</td>
  <td colspan="2"><x:display name="${form.childMap.tbValue.absoluteName}"/></td>
</tr>
<tr>
  <td valign="top">Required Field</td>
  <td colspan="2"><x:display name="${form.childMap.cbRequired.absoluteName}"/></td>
</tr>
<tr>
  <td>&nbsp;</td>
  <td colspan="2">
    <x:display name="${form.childMap.bUpdate.absoluteName}"/>
  </td>
</tr>
<tr>
  <td colspan="3">
   <hr size="1">
  </td>
</tr>
<tr>
  <td>&nbsp;</td>
  <td colspan="2">
    <div align="left">
        <x:display name="${optionsForm.absoluteName}"/>
        NOTE: Changes will only take effect if the profile is saved.
    </div>
  </td>
</tr>


<c:if test="${form == form.rootForm}">
</form>
</c:if>
</table>

<%--
<c:if test="${!empty profileForm}">
<p>
<table border="0" width="<c:out value="${width}"/>">
<tr>
  <td colspan="2">
    <b>Generated XML</b>
   <hr size="1">
   <c:out value="${panel.profile.definition}"/>
  </td>
</tr>
</table>
</c:if>
--%>


<c:if test="${param.et == eventEdit}">
    <script>
    <!--
        jump();
    //-->
    </script>
</c:if>


