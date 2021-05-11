<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        .top {
            margin: auto;
        }
    </style>
</head>
<body>
<table border="1" class="top">
    <c:forEach items="${requestScope.words}" var="word">
        <tr>
            <td>${word.files}"</td>
            <td>${word.keys}"</td>
            <td>${word.count }</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
