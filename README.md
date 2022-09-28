# Analise-de-Dados

RESUMO: A proposta de trabalho é desenvolver um sistema que receba arquivos de dados abertos e apresente relatórios e gráficos com consultas sobre esses dados.
OBJETIVO: adquirir conhecimentos práticos de integração de bancos de dados em aplicações web em camadas, seguindo boas práticas de desenvolvimento.

DETALHAMENTO:
Devem ser escolhidos conjuntos de dados disponíveis em portais de dados abertos (e.g., https://dados.gov.br/, https://dadosabertos.capes.gov.br/, https://dadosabertos.tse.jus.br/, https://dados.antt.gov.br/, https://portal.londrina.pr.gov.br/dados-abertos). Em geral, deve-se combinar mais de um conjunto de dados associados, inclusive obtidos de portais diferentes, enriquecendo o banco de dados. É fundamental vislumbrar consultas e relatórios relevantes que possam ser obtidos a partir dos conjuntos de dados coletados.

Deve-se implementar um esquema relacional de banco de dados que faça o armazenamento integrado e normalizado dos dados dos conjuntos de dados escolhidos.

O sistema deve ter uma opção de upload de arquivos de dados para carga do banco de dados. Ao receber um arquivo, o sistema deverá processá-lo e fazer as inserções correspondentes no banco de dados, utilizando, necessariamente um ou mais controllers e os objetos das camadas de model e de acesso a dados. Também deve ser apresentada uma listagem das cargas, contendo o nome do conjunto de dados, a data e hora da carga de dados e o número de tuplas carregadas.

O sistema também deve apresentar relatórios a respeito dos conjuntos de dados, utilizando gráficos, tabelas e informações relevantes. Espera-se que as consultas utilizadas para produzir os relatórios explorem diferentes recursos da linguagem SQL (junções internas e externas, agregações, agrupamentos, ranking, ordenações, subconsultas, filtros diversos, etc.). Os relatórios também fazendo uso de objetos da camada de acesso a dados, possivelmente, objetos específicos para a geração dos relatórios.

Parte 1) Data: 13/10 (fase atual de desenvolvimento)
- Descrição dos conjuntos de dados escolhidos e indicação de relatórios relevantes a serem construídos.
- Sample dos conjuntos de dados, com até 30 tuplas cada
- Diagrama ER para acomodar os conjuntos de dados sem redundâncias, esquema relacional de banco de dados normalizado e script de implementação do esquema em SQL, com todas as restrições de integridade aplicáveis aos conjuntos de dados
- Instruções SQL de inserção das tuplas do sample apresentado no esquema implementado
- Banco de dados funcional e com algumas cargas efetuadas

Parte 2) Data: 03/11
- Sistema rodando com funcionalidades de carga de dados

Parte 3) Data: 30/11
- Sistema finalizado, incluindo os relatórios