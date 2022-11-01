<%--
  Created by IntelliJ IDEA.
  User: laura
  Date: 19/10/2022
  Time: 11:10
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/view/include/head.jsp"  %>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>[Mortalidade e Natalidade App] Nova carga</title>
</head>
    <body>
        <div class="container">
            <h2 class="text-center">Nova carga de dados</h2>
            <form
                    class="form"
                    action="${pageContext.servletContext.contextPath}/carga/create"
                    enctype="multipart/form-data"
                    method="POST">

                <div class="form-group">
                    <label class="control-label" for="responsavel">Responsavel</label>
                    <input id="responsavel" class="form-control" type="text" name="responsavel" required autofocus/>
                    <p class="help-block"></p>
                </div>

                <div class="form-group">
                    <label class="control-label" for="email">E-mail</label>
                    <input id="email" class="form-control" type="email" name="email" required autofocus/>
                    <p class="help-block"></p>
                </div>

                <div>
                    <input type="radio" id="Mortalidade" name="tipo_carga" value="Mortalidade">
                    <label for="Mortalidade">Registros de Mortalidade</label><br>
                    <input type="radio" id="Natalidade" name="tipo_carga" value="Natalidade">
                    <label for="Natalidade">Registros de Natalidade</label><br>
                </div>

                <div class="form-group">
                    <label for="arquivo">Arquivo (.csv)</label>
                    <input type="file"
                           class="form-control" id="arquivo"
                           name="arquivo"
                           accept=".csv"/>
                </div>

                <div class="text-center">
                    <button class="btn btn-lg btn-primary" type="submit">Salvar</button>
                </div>
            </form>
        </div>
    <%@include file="/view/include/scripts.jsp"%>
    </body>
</html>