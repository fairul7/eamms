<%@include file="/common/header.jsp" %>
<c:set var="hostPath" value="${widget.hostPath}" scope="page"/><%--
<link rel="stylesheet" type="text/css" href="${hostPath}/jw/css/portlet.css">
--%><link rel="stylesheet" type="text/css" href="/common/css/wflow_portlet.css">
<script type="text/javascript" src="${hostPath}/jw/js/jquery/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="${hostPath}/jw/js/json/util.js"></script>
<div id="inbox1"><center><img src="${hostPath}/jw/images/v3/portlet_loading.gif"/></center></div>
<script type="text/javascript">
    $(document).ready( function() {
        var callback = {
            'success' : function () {
                $.getScript('${hostPath}/jw/web/js/client/inbox.js?id=1&rows=5&divId=inbox1',null);
                }
            };
        AssignmentManager.login('${hostPath}/jw',
        '${widget.userName}',
        '${widget.secureHash}',
        callback);
    });
</script>
