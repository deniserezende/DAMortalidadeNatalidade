<%--
  Created by IntelliJ IDEA.
  User: laura
  Date: 19/10/2022
  Time: 11:10
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html;charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
    <%@include file="/view/include/head.jsp" %>
    <title>[Mortalidade e Natalidade App] Nova carga</title>
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
            <li>
                <a href="${pageContext.servletContext.contextPath}/historico">Histórico de cargas</a>
            </li>
            <li class="active">
                <a href="#">Nova carga</a>
            </li>
            <li>
                <a href="#pageSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">Relatórios</a>
                <ul class="collapse list-unstyled" id="pageSubmenu">
                    <li>
                        <a href="${pageContext.servletContext.contextPath}/relatoriosNatalidade">Relatórios de Natalidade</a>
                    </li>
                    <li>
                        <a href="${pageContext.servletContext.contextPath}/relatoriosMortalidade">Relatórios de Mortalidade</a>
                    </li>
                    <li>
                        <a href="${pageContext.servletContext.contextPath}/relatoriosCrescimentoPopulacional">Relatórios de Crescimento Populacional</a>
                    </li>
                </ul>
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
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.servletContext.contextPath}/historico">Histórico</a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" href="#">Carga</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.servletContext.contextPath}/relatoriosNatalidade">Natalidade</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.servletContext.contextPath}/relatoriosMortalidade">Mortalidade</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.servletContext.contextPath}/relatoriosCrescimentoPopulacional">Crescimento Populacional</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <h2 title="NovaCarga">Nova carga</h2>
        <form
                class="form"
                action="${pageContext.servletContext.contextPath}/cargacreate"
                enctype="multipart/form-data"
                method="POST">

            <div class="form-group">
                <label class="control-label" for="titulo">Descrição carga</label>
                <input id="titulo" class="form-control" type="text" name="titulo" required autofocus/>
                <p class="help-block"></p>
            </div>

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
                <input type="radio" id="Mortalidade" name="tipo_carga" value="Registro de Mortalidade">
                <label for="Mortalidade">Registro de Mortalidade</label><br>
                <input type="radio" id="Natalidade" name="tipo_carga" value="Registro de Natalidade">
                <label for="Natalidade">Registro de Natalidade</label><br>
            </div>

            <div class="form-group">
                <label for="arquivo">Arquivo (.csv)</label>
                <input type="file"
                       class="form-control" id="arquivo"
                       name="arquivo"
                       accept=".csv"/>
            </div>

            <div>
                <input type="radio" id="comma" name="separador_csv" value=",">
                <label for="comma">Vírgula (,) como separador do CSV</label><br>
                <input type="radio" id="semicolon" name="separador_csv" value=";">
                <label for="semicolon">Ponto e vírgula (;) como separador do CSV</label><br>
            </div>

            <div class="text-center">
                <button class="btn btn-lg btn-primary" type="submit">Salvar</button>
            </div>
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