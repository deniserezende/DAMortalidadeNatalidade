<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>[Mortalidade e Natalidade App] Início</title>
    <%@include file="/view/include/head.jsp"  %>
</head>
<body>
<div class="container">
    <h1 title="DAMortalidadeNatalidade">CRESCIMENTO POPULACIONAL NO BRASIL</h1>
    <h2 title="DAMortalidadeNatalidadeDescricao">Uma análise de dados abertos</h2>
    <h3>Descrição</h3>
    <p>
        Este é um sistema de análise de dados de Mortalidade e Natalidade do Brasil desenvolvido na disciplina Bancos de Dados I.
        Foram selecionados três bancos de dados abertos:
    </p>
    <h4>1) Sistema de Informação sobre Mortalidade – SIM (1979 a 2020)</h4>
    <p>
        O Sistema de Informação sobre Mortalidade (SIM) foi desenvolvido pelo Ministério da Saúde por meio
        da unificação de inúmeros modelos de Declaração de Óbito utilizados desde 1975.
    </p>
    <p>
        O monitoramento da mortalidade no Brasil é de extrema importância para avaliar quais as
        principais causas de óbitos (doenças, acidentes, assassinatos, suicídios, ...), bem como
        subsidiar a elaboração de políticas públicas de saúde, segurança social, etc.
    </p>
    <h4>2) Sistema de Informação sobre Nascidos Vivos – Sinasc - 1996 a 2020</h4>
    <p>
        O Sistema de Informação sobre Nascidos Vivos (Sinasc) é um projeto, implantado
        pelo Ministério da Saúde (por meio do DATASUS) a partir de 1990, que tem por objetivo
        coletar e fornecer dados epidemiológicos referentes aos nascidos vivos em todo o território nacional.
    </p>
    <p>
        O monitoramento da natalidade pode ser útil para mapear o perfil de saúde dos nascidos vivos de uma população,
        auxiliar em propostas de políticas e ações de vigilância à saúde, além de calcular indicadores de saúde.
    </p>
    <h4>3) Códigos dos municípios IBGE</h4>
    <p>
        Descrição
    </p>
    <h3>Carga de dados</h3>
    <p>
        Texto explicando como funciona a carga de dados.
    </p>
    <p>
        <a class="btn btn-lg btn-primary" href="${pageContext.servletContext.contextPath}/carga/create">
            Nova carga
        </a>
        <a class="btn btn-lg btn-primary" href="${pageContext.servletContext.contextPath}/historico">
            Histórico de cargas
        </a>
    </p>
</div>
</body>
</html>