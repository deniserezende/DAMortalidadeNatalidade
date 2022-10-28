<%--
  Created by IntelliJ IDEA.
  User: laura
  Date: 19/10/2022
  Time: 11:10
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>[Mortalidade e Natalidade App] Historico de Cargas</title>
        <%@include file="/view/include/head.jsp"  %>
    </head>
    <body>
    <div class="container">
        <div class="text-center div_inserir">
            <a class="btn btn-lg btn-primary" href="${pageContext.servletContext.contextPath}/carga/create">
                Nova carga
            </a>
            <a class="btn btn-default"
               href="${pageContext.servletContext.contextPath}/index.jsp"
               data-toggle="tooltip"
               data-original-title="Página inicial">
                <i class="fa fa-home"></i>
            </a>
        </div>

        <form class="form_histoico_cargas" action="${pageContext.servletContext.contextPath}/historico" method="POST">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th class="col-lg-2 h4">Data</th>
                    <th class="col-lg-5 h4">Horário</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="carga" items="${requestScope.cargaList}">
                    <tr>
                        <td>
                            <span class="h4"><c:out value="${carga.data_carga}"/></span>
                        </td>
                        <td>
                            <span class="h4"><c:out value="${carga.hora_carga}"/></span>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </form>

        <div class="modal modal-visualizar-usuario">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Detalhes</h4>
                        <button class="close" type="button" data-dismiss="modal"><span>&times;</span></button>
                    </div>
                    <div class="modal-body">
                        <div class="container-fluid">
                            <div class="row">
                                <div class="col-md-8">
                                    <p class="p_id"></p>
                                    <p class="p_login"></p>
                                    <p class="p_nome"></p>
                                    <p class="p_nascimento"></p>
                                </div>
                                <div class="col-md-4">
                                    <a href="#" class="thumbnail">
                                        <img class="usuario-img"
                                             src="${pageContext.request.contextPath}/img/default_avatar.png"
                                             height="160" width="120"/>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" type="button" data-dismiss="modal">Fechar</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%@include file="/view/include/scripts.jsp"%>
    <%--<script src="${pageContext.servletContext.contextPath}/assets/js/user.js"></script>--%>

    </body>
</html>
