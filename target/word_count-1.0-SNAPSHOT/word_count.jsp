<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<table border="1">
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
