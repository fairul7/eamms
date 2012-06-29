<%@ page import="com.tms.portlet.taglibs.PortalServerUtil"%>
<%@ include file="/common/header.jsp" %>

<x:config>
    <page name="InsertImage">
      <form name="UploadForm">
        <fileupload name="FileUpload"/>
        <button name="UploadButton" text="<fmt:message key='general.label.upload'/>"/>
        <listener_form class="com.tms.cms.image.ui.ImageSelectorFormListener" />
      </form>
   </page>
</x:config>

<html>
    <head>
        <link rel="stylesheet" href="<c:url value="/cmsadmin/styles/style.css"/>">

<script type="text/javascript" src="<c:url value="/common/htmlarea/popups/popup.js"/>"></script>
<script language ="JavaScript">
<!--
window.resizeTo(650, 550);
if (window.scrollbars) {
    window.scrollbars.visible=true;
}

function init() {
  __dlg_init();
  var param = window.dialogArguments;
  if (param) {
      document.getElementById("f_url").value = param["f_url"];
      document.getElementById("f_alt").value = param["f_alt"];
      document.getElementById("f_border").value = param["f_border"];
      document.getElementById("f_align").value = param["f_align"];
      document.getElementById("f_vert").value = param["f_vert"];
      document.getElementById("f_horiz").value = param["f_horiz"];
      if (document.all) {
          document.ImagePreview.pic.src = param.f_url;
      }
      else {
          document.getElementById('ImagePreview').contentDocument.getElementById('pic').src = param.f_url;
      }
  }
};

function preview() {
    if (document.all) {
        document.ImagePreview.pic.src = document.forms[0].elements['InsertImage.UploadForm.FileUpload'].value;
    }
    else {
        var file = document.forms[0].elements['InsertImage.UploadForm.FileUpload'].value;
        document.getElementById('ImagePreview').contentDocument.getElementById('pic').src = file;
        alert('<fmt:message key="general.label.noPermission"/>');
    }
    return false;
}

function insert() {
    var choice;
    var url;
    
    var host = "http://<%= request.getServerName() %>";
    <% if (request.getServerPort() != 80){
    %>
    	host += ":<%=request.getServerPort()%>";
    <%
    }
    %>
    
    if (document.all) {
        choice = document.ImageList.document.forms[0].choice;
    }
    else {
        choice = document.getElementById('ImageList').contentDocument.forms[0].choice;
    }
    if (!choice) {
        return false;
    }
    if(choice.length== null) {
        if(choice.checked == true) {
            url = host + choice.value;
         }
    }
    for(var i=0;i<choice.length;i++) {
        if(choice[i].checked == true) {
            url = host + choice[i].value;
        }
    }
    if (url == null || url == "") {
        url = document.getElementById("f_url").value;
    }
    if (url != null &&  url != "") {
        var param = new Object();
        param["f_url"] = url;
        param["f_alt"] = document.getElementById("f_alt").value;
        param["f_align"] = document.getElementById("f_align").value;
        param["f_border"] = document.getElementById("f_border").value;
        param["f_horiz"] = document.getElementById("f_horiz").value;
        param["f_vert"] = document.getElementById("f_vert").value;
        __dlg_close(param);
        return false;
    }

    return false;
}
//-->
</script>
    </head>
    <body onload="init()">
    <x:display name="InsertImage.UploadForm" body="custom">
        <table>
            <Tr>
                <Td>
                    &nbsp;<fmt:message key='general.label.availableImages'/>
                    <br>
                    <DIV>
                    <table width="100%">
                        <tr>
                            <td>
                             <iframe src="imageSelectorList.jsp?<c:out value='id=${param.id}&new=${param.new}'/>" id="ImageList" name="ImageList" width=300 height=250>
                             </iframe>
                             </td>
                        </tr>
                        <Tr>
                            <td>

                            <input type="hidden" name="url" id="f_url">

                            <div class="fl"><fmt:message key="richTextBox.alternateText"/>:</div>
                            <input type="text" name="alt" id="f_alt">

                            <div class="fl"><fmt:message key="richTextBox.alignment"/>:</div>
                            <select size="1" name="align" id="f_align">
                              <option value=""                             ><fmt:message key="richTextBox.notSet"/></option>
                              <option value="left"                         ><fmt:message key="richTextBox.left"/></option>
                              <option value="right"                        ><fmt:message key="richTextBox.right"/></option>
                              <option value="texttop"                      ><fmt:message key="richTextBox.texttop"/></option>
                              <option value="absmiddle"                    ><fmt:message key="richTextBox.absmiddle"/></option>
                              <option value="baseline" selected="1"        ><fmt:message key="richTextBox.baseline"/></option>
                              <option value="absbottom"                    ><fmt:message key="richTextBox.absbottom"/></option>
                              <option value="bottom"                       ><fmt:message key="richTextBox.bottom"/></option>
                              <option value="middle"                       ><fmt:message key="richTextBox.middle"/></option>
                              <option value="top"                          ><fmt:message key="richTextBox.top"/></option>
                            </select>

                            <div class="fl"><fmt:message key="richTextBox.borderThickness"/>:</div>
                            <input type="text" name="border" id="f_border" size="5">

                            <div class="fr"><fmt:message key="richTextBox.horizontal"/>:</div>
                            <input type="text" name="horiz" id="f_horiz" size="5">

                            <div class="fr"><fmt:message key="richTextBox.vertical"/>:</div>
                            <input type="text" name="vert" id="f_vert" size="5">

                            <div>
                                <br>
                                <input type="hidden" name="id" value="<c:out value='${param.id}'/>">
                                <input type="submit" class="button" value="<fmt:message key='general.label.insertImage'/>" name="InsertImage" onClick="return insert()"/>
                                <input type="submit" class="button" value="<fmt:message key='general.label.cancel'/>" name="Cancel" onClick="return window.close()"/>
                            </div>
                            </td>
                        </tr>
                     </table>
                </td>


                <td valign="top">
                   &nbsp; <fmt:message key='general.label.preview'/> <br>
                   <div>
                  <table>
                   <tr><td>
                   <iframe src="imageSelectorPreview.jsp" id="ImagePreview" name="ImagePreview" width=300 height=210>
                   </iframe>
                   </td></tr>
                   </table>
                   <div>
                   <hr>
                       <x:display name="InsertImage.UploadForm.FileUpload"/>

                    <table align="center" valign = "top">
                        <tr >
                        <td >
                           <x:display name="InsertImage.UploadForm.UploadButton"/>
                           <INPUT TYPE="reset" value="<fmt:message key='general.label.preview'/>" class="button" NAME="Preview" onClick="return preview()"/>
                        </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>


    </x:display>
    </body>

</html>