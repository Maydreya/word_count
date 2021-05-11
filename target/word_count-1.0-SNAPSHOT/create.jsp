<%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 20.04.2021
  Time: 21:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Word count</title>
    <style>
        body {
            background-image: url(images/alena-aenami28.jpg);
        }
        .center {
            width: 200px; /* Ширина элемента в пикселах */
            color: white;
            padding: 10px; /* Поля вокруг текста */
            margin: auto; /* Выравниваем по центру */
        }
    </style>
</head>
<body>
<form class="center" method="post">
<label> Введите слово </label>
    <label>
        <input name="word" size="50%"/>
    </label>
    <input type="submit" value="Search">
</form>

</body>
</html>
