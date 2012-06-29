 <%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="kacang.tld" prefix="x" %>

 <c:set var="accessSelectBox" value="${widget}"/>

<script language="JavaScript">

users=new Array(<c:out value="${accessSelectBox.usersCount}"/>);
groups = new Array(<c:out value="${addRespirceView.groupsCount}"/>);
selectedUsers = new Array(<c:out value="${accessSelectBox.selectedUsersCount}"/>);
selectedGroups = new Array(<c:out value="${accessSelectBox.selectedGroupsCount}"/>);

<% int i=0;%><c:forEach items="${accessSelectBox.usersName}" var="username"   >
users[<%=i%>]="<c:out value="${username}" escapeXml="false" />";<%i++;%></c:forEach>

<% i=0;%><c:forEach items="${accessSelectBox.groupsName}" var="groupname"   >
groups[<%=i%>]="<c:out value="${groupname}" escapeXml="false"/>";<%i++;%></c:forEach>


function loadUsers(access)
{
var left = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.left.absoluteName}"/>'];
var right = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.right.absoluteName}"/>'];
var pre = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].preaccess;
    if(pre.value!= access.value)
    {
        right.options.length = 0;
        left.options.length = users.length;
        for(var i = 0;i<users.length;i++) {
            left.options[i]= new Option(users[i],users[i]); }
        pre.value = access.value;
        if(selectedUsers.length>0)
            right.options.length = selectedUsers.length;
        for(i=0;i<selectedUsers.length;i++){
            right.options[i] = new Option(selectedUsers[i],selectedUsers[i]);}
    }
}

function loadGroups(access)
{
var left = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.left.absoluteName}"/>'];
var right = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.right.absoluteName}"/>'];
var pre = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].preaccess;
    if(pre.value!= access.value)
    {
        //if(right.options.length>0)

        right.options.length = 0;
        left.options.length = groups.lenth;
        for(var i = 0;i<groups.length;i++) {
        left.options[i]= new Option(groups[i],groups[i]); }
        pre.value = access.value;

    }
}

function assign()
{
var left = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.left.absoluteName}"/>'];
var right = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.right.absoluteName}"/>'];
if(left.options.length <=0) return;
for(i=0;i<left.options.length;i++)
{
    if(left.options[i].selected)
    {
       // alert(left.options[i].value);
        right.options[right.options.length] = new Option(left.options[i].value,left.options[i].value);
        left.options[i] =null;
        i--;
    }
}
}

function remove()
{
var left = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.left.absoluteName}"/>'];
var right = document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].elements['<c:out value="${accessSelectBox.right.absoluteName}"/>'];
if(right.options.length<=0) return;
for(i=0;i<right.options.length;i++)
{
    if(right.options[i].selected)
    {
        left.options[left.options.length] = new Option(right.options[i].value,right.options[i].value);
        right.options[i] = null;
        i--;
    }
}
}




</script>

<input type="hidden" name="preaccess" value="-1"/>

<table width="100%" CELLPADDING=0 CELLSPACING=0>
    <tr>
         <td valign = "top">
            <INPUT type="radio" name="accesstype" value="0" onClick="javascript:loadGroups(this)"/>
            Group
            <INPUT type="radio" name="accesstype" value="1" onClick="javascript:loadUsers(this)"/>
            User
        </td>
    </tr>
    <tr>
        <Td>
            <table>
                <tr>
                    <td>
                        <x:display name="${accessSelectBox.left.absoluteName}" ></x:display>
                    </td>
                    <td valign="CENTER">
                        <INPUT TYPE="button" value=">>" onClick="assign();return false;"/>     <br>
                        <INPUT TYPE="button" value="<<" onClick="remove();return false;"/>
                    </td>
                    <td>
                      <x:display name="${accessSelectBox.right.absoluteName}" ></x:display>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

</table>

<script language="javascript">

if(selectedUsers.length>0)
{
 <% i=0;%><c:forEach items="${accessSelectBox.selectedUsersName}" var="username"   >
selectedUsers[<%=i%>]="<c:out value="${username}" escapeXml="false"/>";<%i++;%></c:forEach>
document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].accesstype[1].checked=true;
loadUsers(document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].accesstype);
}

if(selectedGroups.length>0)
{
 <% i=0;%><c:forEach items="${accessSelectBox.selectedGroupsName}" var="groupname"   >
selectedGroups[<%=i%>]="<c:out value="${groupname}" escapeXml="false"/>";<%i++;%></c:forEach>
   document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].accesstype[0].checked=true;
    loadGroups(document.forms['<c:out value = "${accessSelectBox.rootForm.absoluteName}"/>'].accesstype);
}

</script>