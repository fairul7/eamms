<%@ page import="webservices.joke.GetJokeLocator,
                 webservices.joke.GetJokeSoap"%>
<%@include file="/common/header.jsp" %>

<x:cache duration="3">
    <%
        try {
            GetJokeLocator l = new GetJokeLocator();
            GetJokeSoap s = l.getgetJokeSoap();
            String joke = s.getJoke("0");

            request.setAttribute("joke", joke);
        } catch(Exception e) {
            request.setAttribute("joke", e.getMessage());
        }
    %>
    <div align="left"><c:out value="${joke}" /></div>
</x:cache>