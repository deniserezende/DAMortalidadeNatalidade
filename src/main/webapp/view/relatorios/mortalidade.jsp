<%@ page import="java.util.List" %><%--
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
  <title>[Mortalidade e Natalidade App] Relatórios de Mortalidade</title>
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
          <li class="active">
            <a href="#">Relatórios de Mortalidade</a>
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
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.servletContext.contextPath}/relatoriosNatalidade">Natalidade</a>
            </li>
            <li class="nav-item active">
              <a class="nav-link" href="#">Mortalidade</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="${pageContext.servletContext.contextPath}/relatoriosCrescimentoPopulacional">Crescimento Populacional</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <h2 title="RelatoriosMortalidade">Relatórios de Mortalidade no Brasil</h2>

    <script type="text/javascript">
      window.onload = function () {
        const chart = new CanvasJS.Chart("MortesNaoNaturaisPorRaca",
                {
                  title: {
                    text: "Mortes não naturais"
                  },
                  axisX: {
                    valueFormatString: "MMM",
                    interval: 1,
                    intervalType: "month"
                  },
                  data: [
                    {
                      type: "stackedBar",
                      legendText: "branca",
                      showInLegend: "true",
                      dataPoints: <%
                                        List<String> listaObitosNaoNaturaisPorRacaPorAno = (List<String>)request.getAttribute("listaObitosNaoNaturaisPorRacaPorAno");
                                        String obito_raca_cor = listaObitosNaoNaturaisPorRacaPorAno.get(0);
                                        out.print(obito_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "preta",
                      showInLegend: "true",
                      dataPoints: <%
                                        obito_raca_cor = listaObitosNaoNaturaisPorRacaPorAno.get(1);
                                        out.print(obito_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "amarela",
                      showInLegend: "true",
                      dataPoints: <%
                                        obito_raca_cor = listaObitosNaoNaturaisPorRacaPorAno.get(2);
                                        out.print(obito_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "parda",
                      showInLegend: "true",
                      dataPoints: <%
                                        obito_raca_cor = listaObitosNaoNaturaisPorRacaPorAno.get(3);
                                        out.print(obito_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "indigena",
                      showInLegend: "true",
                      dataPoints: <%
                                        obito_raca_cor = listaObitosNaoNaturaisPorRacaPorAno.get(4);
                                        out.print(obito_raca_cor);
                                %>
                    },
                    {
                      type: "stackedBar",
                      legendText: "não consta",
                      showInLegend: "true",
                      dataPoints: <%
                                        obito_raca_cor = listaObitosNaoNaturaisPorRacaPorAno.get(5);
                                        out.print(obito_raca_cor);
                                %>
                    },
                  ]
                }
        );
        const chart2 = new CanvasJS.Chart("MortesPorSexo",
                {
                  title: {
                    text: "Mortes/sexo"
                  },
                  axisY: {
                    title: "Quantidade de mortes",
                    includeZero: true
                  },
                  data: [
                    {
                      type: "bar",
                      showInLegend: true,
                      legendText: "Homens",
                      color: "rgba(54,158,173,1)",
                      dataPoints: <%
                                        List<String> listaObitosPorSexoPorAno = (List<String>)request.getAttribute("listaObitosPorSexoPorAno");
                                        String obito_masculino = listaObitosPorSexoPorAno.get(0);
                                        out.print(obito_masculino);
                                %>
                    },
                    {
                      type: "bar",
                      showInLegend: true,
                      legendText: "Mulheres",
                      color: "pink",
                      dataPoints: <%
                                        String obito_feminino = listaObitosPorSexoPorAno.get(1);
                                        out.print(obito_feminino);
                                %>
                    },
                  ]
                }
        );
        const chart3 = new CanvasJS.Chart("MortesPorAno",
                {
                  title: {
                    text: "Mortes por ano"
                  },
                  axisY: {
                    title: "Quantidade",
                    includeZero: true
                  },
                  data: [
                    {
                      type: "splineArea",
                      showInLegend: true,
                      legendText: "Anos",
                      markerSize: 5,
                      color: "rgba(54,158,173,.7)",
                      dataPoints: <%
                                        List<String> listaObitosPorAno = (List<String>)request.getAttribute("listaObitosPorAno");
                                        String obito = listaObitosPorAno.get(0);
                                        out.print(obito);
                                %>
                    }
                  ]
                }
        );
        chart.render();
        chart2.render();
        chart3.render();
      }
    </script>

    <div id="MortesNaoNaturaisPorRaca" style="height: 300px; width: 100%;"></div>
    <div id="MortesPorSexo" style="height: 300px; width: 100%;"></div>
    <div id="MortesPorAno" style="height: 300px; width: 100%;"></div>

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
