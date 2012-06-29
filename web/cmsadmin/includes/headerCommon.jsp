<%
    startsWith(request, "");
%>
<%!
    public boolean startsWith(HttpServletRequest request, String path) {
        String uri = (String)request.getAttribute("cmsadmin.servlet.forward.servlet_path");
        if (uri == null) {
            uri = (String)request.getAttribute("javax.servlet.forward.servlet_path");
            if (uri == null) {
                uri = request.getServletPath();
            }
            request.setAttribute("cmsadmin.servlet.forward.servlet_path", uri);
        }
        return uri.startsWith(path);
    }
%>