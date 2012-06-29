<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="kacang.tld" prefix="x" %>


<script language ="JavaScript">
<!--
function preview() {
    document.ImagePreview.pic.src = document.forms[0].elements['InsertImage.UploadForm.FileUpload'].value;
    return false;
}

function insert() {
    var choice = document.ImageList.document.forms[0].choice;
    if (!choice) {
        return;
    }
    if(choice.length== null) {
        if(choice.checked == true) {
            window.returnValue = "http://<%= request.getServerName() %>:<%= request.getServerPort() %>" + choice.value;
            window.close();
         }
    }
    for(var i=0;i<choice.length;i++) {
        if(choice[i].checked == true) {
            if (choice[i].title != null && choice[i].title.length > 0) {
                var arr = new Array();
                arr[0] = "http://<%= request.getServerName() %>:<%= request.getServerPort() %>" + choice[i].value;
                arr[1] = choice[i].title;
                window.returnValue =arr;
            }
            else {
                var arr = "http://<%= request.getServerName() %>:<%= request.getServerPort() %>" + choice[i].value;
                window.returnValue =arr;
            }
            window.close();
        }
    }
}
//-->
</script>


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
    <head></head>
    <body>
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
                             <iframe src="imageSelectorList.jsp?<c:out value='id=${param.id}&new=${param.new}'/>"  NAME="ImageList" WIDTH=300 HEIGHT=250>
                             </iframe>
                             </td>
                        </tr>
                        <Tr>
                            <td>
                             <input type="hidden" name="id" value="<c:out value='${param.id}'/>">
                             <input type="submit" class="button" value="<fmt:message key='general.label.insertImage'/>" NAME="InsertImage" onClick="javascript:insert()"/>
                             <INPUT type="submit" class="button" value="<fmt:message key='general.label.cancel'/>" NAME="Cancel" onClick="javascript:window.close()"/>
                            </td>
                        </tr>
                     </table>
                </td>


                <td >
                   &nbsp; <fmt:message key='general.label.preview'/> <br>
                   <div>
                  <table>
                   <tr><td>
                   <iframe src="imageSelectorPreview.jsp" name="ImagePreview" width=300 height=210>
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
                           <INPUT TYPE="reset" value="<fmt:message key='general.label.preview'/>" NAME="Preview" onClick="javascript:return preview()"/>
                        </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </x:display>
    </body>

</html>