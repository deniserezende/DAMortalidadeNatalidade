<%--
  Created by IntelliJ IDEA.
  User: laura
  Date: 25/11/2022
  Time: 14:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="/view/include/head.jsp" %>
    <%@include file="/view/include/graphHead.jsp" %>
    <title>[Mortalidade e Natalidade App] Relatórios de Natalidade</title>
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
            <li>
                <a href="${pageContext.servletContext.contextPath}/cargacreate">Nova carga</a>
            </li>
            <li>
                <a href="#pageSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">Relatórios</a>
                <ul class="collapse list-unstyled" id="pageSubmenu">
                    <li class="active">
                        <a href="#">Relatórios de Natalidade</a>
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
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.servletContext.contextPath}/cargacreate">Carga</a>
                        </li>
                        <li class="nav-item active">
                            <a class="nav-link" href="#">Natalidade</a>
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

        <h2 title="RelatoriosNatalidade">Relatórios de Natalidade no Brasil</h2>

        <script type="text/javascript">
            window.onload = function() {
                var chartP = new CanvasJS.Chart("chartContainer", {
                    title: {
                        text: "Idade média das mães ao longo dos anos"
                    },
                    animationEnabled: true,
                    axisX: {
                        title: "Ano"
                    },
                    axisY: {
                        title: "Idade (anos)",
                        includeZero: true
                    },
                    data: [{
                        type: "column",
                        dataPoints : <%
                                        String estado = (String)request.getAttribute("estado");
                                        List<String> listaIdadesMaes = (List<String>)request.getAttribute("listaIdadesMaes");
                                        String idadesMaes = listaIdadesMaes.get(0);
                                        out.print(idadesMaes);
                        %>
                    }],
                    options: {
                        plugins: {
                            colors: {
                                enabled: false
                            }
                        }
                    }
                });
                chartP.render();
            }
        </script>

        <br/><!-- Just so that JSFiddle's Result label doesn't overlap the Chart -->
        <select class="dropdown" id="estado">
            <option value="" selected="selected">Selecione um estado</option>
            <option value="AC">Acre</option>
            <option value="AL">Alagoas</option>
            <option value="AP">Amapá</option>
            <option value="AM">Amazonas</option>
            <option value="BA">Bahia</option>
            <option value="CE">Ceará</option>
            <option value="DF">Distrito Federal</option>
            <option value="ES">Espírito Santo</option>
            <option value="GO">Goiás</option>
            <option value="MA">Maranhão</option>
            <option value="MT">Mato Grosso</option>
            <option value="MS">Mato Grosso do Sul</option>
            <option value="MG">Minas Gerais</option>
            <option value="PA">Pará</option>
            <option value="PB">Paraíba</option>
            <option value="PR">Paraná</option>
            <option value="PE">Pernambuco</option>
            <option value="PI">Piauí</option>
            <option value="RJ">Rio de Janeiro</option>
            <option value="RN">Rio Grande do Norte</option>
            <option value="RS">Rio Grande do Sul</option>
            <option value="RO">Rondônia</option>
            <option value="RR">Roraima</option>
            <option value="SC">Santa Catarina</option>
            <option value="SP">São Paulo</option>
            <option value="SE">Sergipe</option>
            <option value="TO">Tocantins</option>
        </select>

        <div id="chartContainer" style="height: 300px; width: 100%;"></div>
    </div>
</div>

<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>

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
<script type="text/javascript">
    $( ".dropdown" ).change(function() {
        chartP.options.data[0].dataPoints = [];
        var e = document.getElementById("estado");
        var selected = e.options[e.selectedIndex].value; //recuperando estado selecionado
        chartP.options.data[0].dataPoints.push(<%out.print(idadesMaes);%>);
        chartP.render();
    });
</script>
</body>
</html>