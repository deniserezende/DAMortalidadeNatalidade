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
        var graficoIdadeMae = new CanvasJS.Chart("idadeMaesPorAno", {
          title: {
            text: "Idade média das mães ao longo dos anos"
          },
          backgroundColor: "#fafafa",
          animationEnabled: true,
          axisX: {
            title: "Ano"
          },
          axisY: {
            title: "Idade (anos)",
            includeZero: true,
          },
          data: [
            {
              type: "column",
              showInLegend: true,
              legendText: "Idade média",
              dataPoints : <%
                                        List<String> listaIdadesMaes = (List<String>)request.getAttribute("listaIdadesMaes");
                                        String idadesMaes = listaIdadesMaes.get(0);
                                        String minIdadesMaes = listaIdadesMaes.get(1);
                                        String maxIdadesMaes = listaIdadesMaes.get(2);
                                        System.out.println(idadesMaes);
                                        System.out.println(minIdadesMaes);
                                        System.out.println(maxIdadesMaes);
                                        out.print(idadesMaes);
                        %>
            },
            {
              type: "line",
              showInLegend: true,
              legendText: "Idade da mãe mais nova",
              dataPoints: <%out.print(minIdadesMaes);%>
            },
            {
              type: "line",
              showInLegend: true,
              legendText: "Idade da mãe mais velha",
              dataPoints: <%out.print(maxIdadesMaes);%>
            }
          ],
          options: {
            plugins: {
              colors: {
                enabled: false
              }
            }
          }
        });
        var graficoTipoParto = new CanvasJS.Chart("graficoTipoParto", {
          animationEnabled: true,
          title:{
            text: "Tipos de parto",
          },
          backgroundColor: "#fafafa",
          data: [{
            type: "doughnut",
            startAngle: 60,
            indexLabelFontSize: 17,
            dataPoints: <%
                            List<String> listaTipoParto = (List<String>)request.getAttribute("qtdTipoParto");
                            String tipoParto = listaTipoParto.get(0);
                            System.out.println(tipoParto);
                            out.print(tipoParto);%>
          }]
        });
        graficoIdadeMae.render();
        graficoTipoParto.render();
      }
    </script>
    <br/>
    <div id="idadeMaesPorAno" style="height: 300px; width: 100%;"></div>
    <br/>
    <div id="graficoTipoParto" style="height: 300px; width: 100%;"></div>
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
</body>
</html>