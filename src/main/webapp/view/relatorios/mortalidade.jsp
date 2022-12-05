<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: laura
  Date: 25/11/2022
  Time: 14:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%--<%--%>
<%--    List<String> lista = (List<String>)request.getAttribute("listaIdadesMortesNaoNaturais");--%>
<%--    String idadesMaes = lista.get(0);--%>
<%--    System.out.println(lista);--%>
<%--%>--%>

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
                var chart = new CanvasJS.Chart("chartContainer",
                    {
                        title:{
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
                                dataPoints: [
                                    { x: new Date(2012, 0o1, 1), y: 71 },
                                    { x: new Date(2012, 0o2, 1), y: 55},
                                    { x: new Date(2012, 0o3, 1), y: 50 },
                                    { x: new Date(2012, 0o4, 1), y: 65 },
                                    { x: new Date(2012, 0o5, 1), y: 95 }

                                ]
                            },
                            {
                                type: "stackedBar",
                                legendText: "preta",
                                showInLegend: "true",
                                dataPoints: [
                                    { x: new Date(2012, 0o1, 1), y: 71 },
                                    { x: new Date(2012, 0o2, 1), y: 55},
                                    { x: new Date(2012, 0o3, 1), y: 50 },
                                    { x: new Date(2012, 0o4, 1), y: 65 },
                                    { x: new Date(2012, 0o5, 1), y: 95 }

                                ]
                            },
                            {
                                type: "stackedBar",
                                legendText: "amarela",
                                showInLegend: "true",
                                dataPoints: [
                                    { x: new Date(2012, 0o1, 1), y: 71 },
                                    { x: new Date(2012, 0o2, 1), y: 55},
                                    { x: new Date(2012, 0o3, 1), y: 50 },
                                    { x: new Date(2012, 0o4, 1), y: 65 },
                                    { x: new Date(2012, 0o5, 1), y: 95 }

                                ]
                            },

                            {
                                type: "stackedBar",
                                legendText: "parda",
                                showInLegend: "true",
                                dataPoints: [
                                    { x: new Date(2012, 0o1, 1), y: 61 },
                                    { x: new Date(2012, 0o2, 1), y: 75},
                                    { x: new Date(2012, 0o3, 1), y: 80 },
                                    { x: new Date(2012, 0o4, 1), y: 85 },
                                    { x: new Date(2012, 0o5, 1), y: 105 }

                                ]
                            },
                            {
                                type: "stackedBar",
                                legendText: "indigena",
                                showInLegend: "true",
                                dataPoints: [
                                    { x: new Date(2012, 0o1, 1), y: 20 },
                                    { x: new Date(2012, 0o2, 1), y: 35},
                                    { x: new Date(2012, 0o3, 1), y: 30 },
                                    { x: new Date(2012, 0o4, 1), y: 45 },
                                    { x: new Date(2012, 0o5, 1), y: 25 }

                                ]
                            }
                        ]
                    }
                );

                var chart2 = new CanvasJS.Chart("chartContainer2",
                    {
                        title:{
                            text: "Mortes/sexo"
                        },
                        axisY: {
                            title: "Medals won",
                            maximum: 1010
                        },
                        data: [
                            {
                                type: "bar",
                                showInLegend: true,
                                legendText: "Homens",
                                color: "gold",
                                dataPoints: [
                                    { y: 198, label: "Italy"},
                                    { y: 201, label: "China"},
                                    { y: 202, label: "France"},
                                    { y: 236, label: "Great Britain"},
                                    { y: 395, label: "Soviet Union"},
                                    { y: 957, label: "USA"}
                                ]
                            },
                            {
                                type: "bar",
                                showInLegend: true,
                                legendText: "Mulheres",
                                color: "silver",
                                dataPoints: [
                                    { y: 166, label: "Italy"},
                                    { y: 144, label: "China"},
                                    { y: 223, label: "France"},
                                    { y: 272, label: "Great Britain"},
                                    { y: 319, label: "Soviet Union"},
                                    { y: 759, label: "USA"}
                                ]
                            },
                        ]
                    }
                );
                var chart3 = new CanvasJS.Chart("chartContainer3",
                    {
                        title:{
                            text: "Spline Area Chart"
                        },
                        axisY: {
                            title: "Units Sold",
                            valueFormatString: "#0,,.",
                            suffix: " m"
                        },
                        data: [
                            {
                                toolTipContent: "{y} units",
                                type: "splineArea",
                                showInLegend: true,
                                legendText: "source: Nielsen SoundScan",
                                markerSize: 5,
                                color: "rgba(54,158,173,.7)",
                                dataPoints: [
                                    {x: new Date(1992,0), y: 2506000},
                                    {x: new Date(1993,0), y: 2798000},
                                    {x: new Date(1994,0), y: 3386000},
                                    {x: new Date(1995,0), y: 6944000},
                                    {x: new Date(1996,0), y: 6026000},
                                    {x: new Date(1997,0), y: 2394000},
                                    {x: new Date(1998,0), y: 1872000},
                                    {x: new Date(1999,0), y: 2140000},
                                    {x: new Date(2000,0), y: 7289000, indexLabel: "highest"},
                                    {x: new Date(2001,0), y: 4830000},
                                    {x: new Date(2002,0), y: 2009000},
                                    {x: new Date(2003,0), y: 2840000},
                                    {x: new Date(2004,0), y: 2396000},
                                    {x: new Date(2005,0), y: 1613000},
                                    {x: new Date(2006,0), y: 2821000},
                                    {x: new Date(2007,0), y: 2000000},
                                    {x: new Date(2008,0), y: 1397000}
                                ]
                            }
                        ]
                    }
                );
                chart.render();
                chart2.render();
                chart3.render();
            }
        </script>

        <div id="chartContainer" style="height: 300px; width: 100%;"></div>
        <div id="chartContainer2" style="height: 300px; width: 100%;"></div>
        <div id="chartContainer3" style="height: 300px; width: 100%;"></div>

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


