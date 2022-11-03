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
    <%@include file="/view/include/head.jsp" %>
    <title>[Mortalidade e Natalidade App] Historico de Cargas</title>
</head>
<body>
<div class="wrapper">
    <!-- Sidebar Holder -->
    <nav id="sidebar">
        <div class="sidebar-header">
            <h3>Mortalidade e Natalidade</h3>
        </div>

        <ul class="list-unstyled components">
            <p>Menu</p>
            <li>
                <a href="${pageContext.servletContext.contextPath}/">Sobre</a>
            </li>
            <li class="active">
                <a href="#">Histórico de cargas</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/cargacreate">Nova carga</a>
            </li>
        </ul>
    </nav>

    <!-- Page Content Holder -->
    <div id="content">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">

                <button type="button" id="sidebarCollapse" class="navbar-btn">
                    <span></span>
                    <span></span>
                    <span></span>
                </button>
                <button class="btn btn-dark d-inline-block d-lg-none ml-auto" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <i class="fas fa-align-justify"></i>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="nav navbar-nav ml-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.servletContext.contextPath}/">Sobre</a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" href="#">Histórico</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.servletContext.contextPath}/cargacreate">Carga</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <h2 title="Historico">Histórico de cargas</h2>

        <form class="form_histoico_cargas" action="${pageContext.servletContext.contextPath}/historico" method="POST">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th scope="col">Data</th>
                    <th scope="col">Horário</th>
                    <th scope="col">Responsável</th>
                    <th scope="col">Email</th>
                    <th scope="col">Nome arquivo</th>
                    <th scope="col">Tipo de carga</th>
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
                        <td>
                            <span class="h4"><c:out value="${carga.responsavel}"/></span>
                        </td>
                        <td>
                            <span class="h4"><c:out value="${carga.email}"/></span>
                        </td>
                        <td>
                            <span class="h4"><c:out value="${carga.nome_arquivo}"/></span>
                        </td>
                        <td>
                        <span class="h4">
                            <c:if test="${carga.tipo_carga == 1}">
                                <c:out value="Natalidade"/>
                            </c:if>
                            <c:if test="${carga.tipo_carga == 2}">
                                <c:out value="Mortalidade"/>
                            </c:if>
                        </span>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </form>

    </div>
</div>

<!-- jQuery CDN - Slim version (=without AJAX) -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<!-- Popper.JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js" integrity="sha384-cs/chFZiN24E4KMATLdqdvsezGxaGsi4hLGOzlXwp5UZB1LY//20VyM2taTB4QvJ" crossorigin="anonymous"></script>
<!-- Bootstrap JS -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js" integrity="sha384-uefMccjFJAIv6A+rW+L4AHf99KvxDjWSu1z9VI8SKNVmz4sk7buKt/6v9KI65qnm" crossorigin="anonymous"></script>

<script type="text/javascript">
    $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
            $('#sidebar').toggleClass('active');
            $(this).toggleClass('active');
        });
    });
</script>
</body>
</html>