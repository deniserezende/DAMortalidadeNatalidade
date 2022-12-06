<%--
  Created by IntelliJ IDEA.
  User: laura
  Date: 25/11/2022
  Time: 14:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
  <%@include file="/view/include/head.jsp" %>
  <%@include file="/view/include/graphHead.jsp" %>
  <title>[Mortalidade e Natalidade App] Relatórios de Crescimento Populacional</title>
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
          <li>
            <a href="${pageContext.servletContext.contextPath}/relatoriosNatalidade">Relatórios de Natalidade</a>
          </li>
          <li>
            <a href="${pageContext.servletContext.contextPath}/relatoriosMortalidade">Relatórios de Mortalidade</a>
          </li>
          <li class="active">
            <a href="#">Relatórios de Crescimento Populacional</a>
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
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.servletContext.contextPath}/relatoriosNatalidade">Natalidade</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.servletContext.contextPath}/relatoriosMortalidade">Mortalidade</a>
            </li>
            <li class="nav-item active">
              <a class="nav-link" href="#">Crescimento Populacional</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <h2 title="RelatoriosCrescimentoPopulacional">Relatórios de Crescimento Populacional no Brasil</h2>

    <script type="text/javascript">
      window.onload = function() {
        var chart = new CanvasJS.Chart("CrescimentoPopulacional",
                {
                  title:{
                    text: "Registros ao longo dos anos",
                    margin: 15
                  },
                  backgroundColor: "#fafafa",
                  toolTip:{
                    shared: true
                  },
                  axisX: {
                    title: "Anos",
                  },
                  axisY:{
                    title: "Quantidade de mortes",
                    includeZero: true
                  },
                  legend:{
                    verticalAlign: "bottom",
                    horizontalAlign: "center"
                  },
                  data: [
                    {
                      type: "stackedArea",
                      name: "nascimentos",
                      showInLegend: "true",
                      dataPoints: <%
                                            List<String> qtdRegistrosPorAno = (List<String>) request.getAttribute("qtd_registros_por_ano");
                                            String nascimentos = qtdRegistrosPorAno.get(0);
                                            out.print(nascimentos);
                                        %>
                    },
                    {
                      type: "stackedArea",
                      name: "obitos",
                      showInLegend: "true",
                      dataPoints: <%
                                            String obitos = qtdRegistrosPorAno.get(1);
                                            out.print(obitos);
                                        %>
                    }
                  ]
                }
        );
        var chart2 = new CanvasJS.Chart("MortesPorRaca",
                {
                  title:{
                    text: "Obitos por raça"
                  },
                  backgroundColor: "#fafafa",
                  axisX: {
                    valueFormatString: "####",
                    interval: 1
                  },
                  data: [
                    {
                      type: "stackedBar",
                      legendText: "branca",
                      showInLegend: "true",
                      dataPoints: <%
                                        List<String> listaObitosPorRacaPorAno = (List<String>)request.getAttribute("listaObitosPorRacaPorAno");
                                        String obito_raca_cor = listaObitosPorRacaPorAno.get(0);
                                        out.print(obito_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "preta",
                      showInLegend: "true",
                      dataPoints: <%
                                        obito_raca_cor = listaObitosPorRacaPorAno.get(1);
                                        out.print(obito_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "amarela",
                      showInLegend: "true",
                      dataPoints: <%
                                        obito_raca_cor = listaObitosPorRacaPorAno.get(2);
                                        out.print(obito_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "parda",
                      showInLegend: "true",
                      dataPoints: <%
                                        obito_raca_cor = listaObitosPorRacaPorAno.get(3);
                                        out.print(obito_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "indigena",
                      showInLegend: "true",
                      dataPoints: <%
                                        obito_raca_cor = listaObitosPorRacaPorAno.get(4);
                                        out.print(obito_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "não consta",
                      showInLegend: "true",
                      dataPoints: <%
                                        obito_raca_cor = listaObitosPorRacaPorAno.get(5);
                                        out.print(obito_raca_cor);
                                %>
                    },
                  ]
                }
        );
        var chart3 = new CanvasJS.Chart("NascimentosPorRaca",
                {
                  title:{
                    text: "Nascimentos por raça"
                  },
                  backgroundColor: "#fafafa",
                  axisX: {
                    valueFormatString: "####",
                    interval: 1
                  },
                  data: [
                    {
                      type: "stackedBar",
                      legendText: "branca",
                      showInLegend: "true",
                      dataPoints: <%
                                        List<String> listaNascimentosPorRacaPorAno = (List<String>)request.getAttribute("listaNascimentosPorRacaPorAno");
                                        String nascimento_raca_cor = listaNascimentosPorRacaPorAno.get(0);
                                        out.print(nascimento_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "preta",
                      showInLegend: "true",
                      dataPoints: <%
                                        nascimento_raca_cor = listaNascimentosPorRacaPorAno.get(1);
                                        out.print(nascimento_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "amarela",
                      showInLegend: "true",
                      dataPoints: <%
                                        nascimento_raca_cor = listaNascimentosPorRacaPorAno.get(2);
                                        out.print(nascimento_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "parda",
                      showInLegend: "true",
                      dataPoints: <%
                                        nascimento_raca_cor = listaNascimentosPorRacaPorAno.get(3);
                                        out.print(nascimento_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "indigena",
                      showInLegend: "true",
                      dataPoints: <%
                                        nascimento_raca_cor = listaNascimentosPorRacaPorAno.get(4);
                                        out.print(nascimento_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "não consta",
                      showInLegend: "true",
                      dataPoints: <%
                                        nascimento_raca_cor = listaNascimentosPorRacaPorAno.get(5);
                                        out.print(nascimento_raca_cor);
                                %>
                    },
                  ]
                }
        );
        chart.render();
        chart2.render();
        chart3.render();
      }
    </script>
    <script type="text/javascript" src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
    <div id="CrescimentoPopulacional" style="height: 300px; width: 100%;"></div>
    <div id="MortesPorRaca" style="height: 300px; width: 100%;"></div>
    <div id="NascimentosPorRaca" style="height: 300px; width: 100%;"></div>
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