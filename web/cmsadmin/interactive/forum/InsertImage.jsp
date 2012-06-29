<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="kacang.tld" prefix="x" %>
<%if(request.getParameter("id")!=null)session.setAttribute("id",request.getParameter("id"));%>

<script language ="JavaScript">

function preview()
{
    document.ImagePreview.pic.src = document.forms[0].elements['InsertImage.UploadForm.FileUpload'].value;
    return false;

}

function insert()
{
    var choice = document.ImageList.document.forms[0].choice;
    if(choice.length== null)
    {
        if(choice.checked == true)
        {
            window.returnValue = "http://<%= request.getServerName() %>:<%= request.getServerPort() %>"+choice.value;
            window.close();
         }
    }
    for(var i=0;i<choice.length;i++)
    {

        if(choice[i].checked == true)
        {

            window.returnValue ="http://<%= request.getServerName() %>:<%= request.getServerPort() %>"+choice[i].value;
            window.close();
        }
    }

}





</script>


<x:config>
    <page name="InsertImage">
      <form name="UploadForm">
        <fileupload name="FileUpload"/>
        <button name="UploadButton" text="Upload"/>
            <listener_script>
                 import kacang.services.storage.*;
                 String id = event.getRequest().getSession().getAttribute("id");
                 StorageFile sf = wm.getWidget("InsertImage.UploadForm.FileUpload").getStorageFile(event.getRequest());
                 if(sf != null )
                 {
                    StorageService ss = Application.getInstance().getService(new StorageService().getClass());
                    sf.setParentDirectoryPath("/forum/" + id);
                    ss.store(sf);
                 }
            </listener_script>
        <button name="PreviewButton" text="Preview" onClick="javascript:return preview()"/>
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
                    &nbsp;Available Images
                    <br>
                    <DIV>
                    <table width="100%">
                        <tr>
                            <td>
                             <iframe src="ListImage.jsp?id=<%=session.getAttribute("id")%>"  NAME ="ImageList" WIDTH=200 HEIGHT=250>
                             </iframe>
                             </td>
                        </tr>
                        <Tr>
                            <td>
                             <input type="submit" class="button" value="Insert Image" NAME="InsertImage" onClick="javascript:insert()"/>
                             <INPUT type="submit" class="button" value="Cancel" NAME="Cancel" onClick="javascript:window.close()"/>
                            </td>
                        </tr>
                     </table>
                </td>


                <td >
                   &nbsp; Preview <br>
                   <div>
                  <table>
                   <tr><td>
                   <iframe src="ImagePreview.jsp" name="ImagePreview" width=300 height=210>
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
                       <INPUT TYPE="reset" value="Preview" NAME="Preview" onClick="javascript:return preview()"/>
                        </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </x:display>
    </body>

</html>