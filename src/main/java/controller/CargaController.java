package controller;

import dao.*;
import model.*;

import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.sql.SQLException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CargaController",
        urlPatterns = {
                "",
                "/cargacreate",
                "/historico",
                "/relatoriosNatalidade",
                "/relatoriosMortalidade",
                "/relatoriosCrescimentoPopulacional"
        }
)

public class CargaController extends HttpServlet{

    protected static final Logger logger = LogManager.getLogger(CargaController.class);
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 1024;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //HttpSession session;
        RequestDispatcher dispatcher;
        DAO<Carga> dao;
        String servletPath = request.getServletPath();

        switch (request.getServletPath()) {
            case "": {
                dispatcher = request.getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
                break;
            }
            case "/historico": {
                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    dao = daoFactory.getCargaDAO();

                    List<Carga> cargaList = dao.all();
                    request.setAttribute("cargaList", cargaList);
                } catch (Exception error) {
                    request.getSession().setAttribute("error", error.getMessage());
                }
                dispatcher = request.getRequestDispatcher("/view/carga/index.jsp");
                dispatcher.forward(request, response);
                break;
            }
            case "/cargacreate": {
                dispatcher = request.getRequestDispatcher("/view/carga/create.jsp");
                dispatcher.forward(request, response);
                break;
            }
            case "/relatoriosNatalidade": {
                try(DAOFactory daoFactory = DAOFactory.getInstance()){
                    RegistradoDAO registradoDao = daoFactory.getRegistradoDAO();
                    List<String> listaIdadesMaes = registradoDao.idadesMaesPorAno();
                    request.setAttribute("listaIdadesMaes", listaIdadesMaes);
                    List<String> listaTipoParto = registradoDao.qtdTipoParto();
                    request.setAttribute("qtdTipoParto", listaTipoParto);
                    System.out.println(listaTipoParto);
                    dispatcher = request.getRequestDispatcher("/view/relatorios/natalidade.jsp");
                    dispatcher.forward(request, response);
                }
                catch(Exception error) {
                    //logger.error("ParseException catch: " + error);
                    request.getSession().setAttribute("error", "Não foi possível carregar os dados para plotar o gráfico");
                    response.sendRedirect(request.getContextPath() + servletPath);
                }
                break;
            }
            case "/relatoriosMortalidade": {
                try(DAOFactory daoFactory = DAOFactory.getInstance()){
                    RegistradoDAO registradoDao = daoFactory.getRegistradoDAO();
                    List<String> listaObitosPorSexoPorAno = registradoDao.obitosPorSexoPorAno("");
//                    request.setAttribute("estado", "");
                    request.setAttribute("listaObitosPorSexoPorAno", listaObitosPorSexoPorAno);
                    List<String> listaObitosPorAno = registradoDao.obitosPorAno("");
//                    request.setAttribute("estado", "");
                    request.setAttribute("listaObitosPorAno", listaObitosPorAno);
                    List<String> listaObitosNaoNaturaisPorRacaPorAno = registradoDao.obitosNaoNaturaisPorRacaPorAno("");
//                    request.setAttribute("estado", "");
                    request.setAttribute("listaObitosNaoNaturaisPorRacaPorAno", listaObitosNaoNaturaisPorRacaPorAno);
                    dispatcher = request.getRequestDispatcher("/view/relatorios/mortalidade.jsp");
                    dispatcher.forward(request, response);
                }
                catch(Exception error) {
                    //logger.error("ParseException catch: " + error);
                    request.getSession().setAttribute("error", "Não foi possível carregar os dados para plotar o gráfico");
                    response.sendRedirect(request.getContextPath() + servletPath);
                }
                break;
            }
            case "/relatoriosCrescimentoPopulacional": {

                try(DAOFactory daoFactory = DAOFactory.getInstance()){
                    RegistradoDAO registradoDao = daoFactory.getRegistradoDAO();

                    List<String> jsonRegistrosPorAno = registradoDao.qtdRegistrosPorAno();
                    request.setAttribute("qtd_registros_por_ano", jsonRegistrosPorAno);

                    List<String> listaObitosPorRacaPorAno = registradoDao.obitosPorRacaPorAno("");
//                    request.setAttribute("estado", "");
                    request.setAttribute("listaObitosPorRacaPorAno", listaObitosPorRacaPorAno);

                    List<String> listaNascimentosPorRacaPorAno = registradoDao.nascimentosPorRacaPorAno("");
//                    request.setAttribute("estado", "");
                    request.setAttribute("listaNascimentosPorRacaPorAno", listaNascimentosPorRacaPorAno);

                    dispatcher = request.getRequestDispatcher("/view/relatorios/crescimentoPopulacional.jsp");
                    dispatcher.forward(request, response);
                }
                catch(Exception error) {
                    //logger.error("ParseException catch: " + error);
                    request.getSession().setAttribute("error", "Não foi possível carregar os dados para plotar o gráfico");
                    response.sendRedirect(request.getContextPath() + servletPath);
                }
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        DAO<Carga> daoCarga;
        RegistradoDAO daoRegistrado;
        Carga carga = new Carga();
        HttpSession session = request.getSession();
        String servletPath = request.getServletPath();

        switch (request.getServletPath()) {
            case "/cargacreate": {
                // Create a factory for disk-based file items
                DiskFileItemFactory factory = new DiskFileItemFactory();

                // Set factory constraints
                factory.setSizeThreshold(MAX_FILE_SIZE);

                // Set the directory used to temporarily store files that are larger than the configured size threshold
                factory.setRepository(new File("/tmp"));

                // Create a new file upload handler
                ServletFileUpload upload = new ServletFileUpload(factory);

                // Set overall request size constraint
                upload.setSizeMax(MAX_FILE_SIZE);

                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    // Parse the request
                    List<FileItem> items = upload.parseRequest(request);

                    // Process the uploaded items
                    Iterator<FileItem> iter = items.iterator();

                    // Separator
                    String separator = ";";

                    // File uploaded
                    File uploadedFile = null;
                    while (iter.hasNext()) {
                        FileItem item = iter.next();

                        // Process a regular form field
                        if (item.isFormField()) {
                            String fieldName = item.getFieldName();
                            String fieldValue = item.getString();
                            logger.info("fieldName=" + fieldName);
                            logger.info("fieldValue=" + fieldValue);

                            // Creating carga
                            switch (fieldName) {
                                case "arquivo":
                                    carga.setNome_arquivo(fieldValue);
                                    break;
                                case "tipo_carga":
                                    if(Objects.equals("Registro de Natalidade", fieldValue)){
                                        logger.info("Setting carga.setTipo_carga == 1");
                                        carga.setTipo_carga(1);
                                    }
                                    else{ // if equals "Registro de Mortalidade"
                                        logger.info("Setting carga.setTipo_carga == 2");
                                        carga.setTipo_carga(2);
                                    }
                                    break;
                                case "responsavel":
                                    carga.setResponsavel(fieldValue);
                                    break;
                                case "email":
                                    carga.setEmail(fieldValue);
                                    break;
                                case "titulo":
                                    carga.setTitulo_carga(fieldValue);
                                    break;
                                case "separador_csv":
                                    separator = fieldValue;
                                    logger.info("Separador de CSV informado: " + fieldValue);
                                    break;
                            }
                            /* Inserting Date in Carga */
                            String dateInString = new SimpleDateFormat("ddMMyyyy").format(Calendar.getInstance().getTime());
                            // Formatting Date
                            if (dateInString.length() == 7){
                                char temp = '0';
                                dateInString = temp + dateInString;
                            }
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy",
                                    (new Locale("pt", "BR")));
                            LocalDate date_temp = LocalDate.parse(dateInString, formatter);
                            // Inserting in Carga
                            carga.setData_carga(Date.valueOf(date_temp));

                            /* Inserting Time in Carga */
                            Calendar date = Calendar.getInstance();
                            int hour = date.get(Calendar.HOUR_OF_DAY);
                            int minute = date.get(Calendar.MINUTE);
                            int second = date.get(Calendar.SECOND);
                            String timeInString = hour + ":" + minute + ":" + second;
                            carga.setHora_carga(Time.valueOf(timeInString));
                            logger.info("Hora da carga: " + carga.getHora_carga());

                        }
                        else { // if item isn't type FormField
                            // This means that the item is the file

                            // Getting and setting filename
                            String fileName = item.getName();
                            logger.info("filename = " + fileName);
                            carga.setNome_arquivo(fileName);

                            // Getting path to add to the file
                            String root = getServletContext().getRealPath("/");
                            File path = new File(root + "/fileuploads");

                            // If path doens't exists we create it
                            if (!path.exists()) {
                                boolean status = path.mkdirs();
                                logger.warn("Path didn't exist, new path = " + path);
                                logger.warn("status = " + status);
                            }
                            // Concatenating path with filename
                            uploadedFile = new File(path + "/" + fileName);

                            // Saving in the path the file
                            item.write(uploadedFile);
                        }
                    }
                    if (uploadedFile != null) {
                        // Opening file and looping through it
                        BufferedReader reader = new BufferedReader(new FileReader(uploadedFile));
                        daoRegistrado = daoFactory.getRegistradoDAO();
                        List<String> lines = new ArrayList<>();
                        if(carga.getTipo_carga() == 1){ // Natality
                            logger.info("Calling ReadCSVNatality");
                            ReadCSVNatality(reader, lines, daoRegistrado, separator);
                        }
                        else{ // Mortality
                            logger.info("Calling ReadCSVMortality");
                            ReadCSVMortality(reader, lines, daoRegistrado, separator);
                        }
                        // Deleting temporary file
                        uploadedFile.delete();
                    }
                    else{
                        //logger.error("No file uploaded");
                    }

                    daoCarga = daoFactory.getCargaDAO();

                    if (servletPath.equals("/cargacreate")) {
                        daoCarga.create(carga);
                    }

                    response.sendRedirect(request.getContextPath() + "/historico");

                } catch (ParseException error) {
                    //logger.error("ParseException catch: " + error);

                    session.setAttribute("error", "O formato de data não é válido. Por favor entre data no formato dd/mm/aaaa");
                    response.sendRedirect(request.getContextPath() + servletPath);
                } catch (FileUploadException error) {
                    //logger.error("FileUploadException catch: " + error);

                    session.setAttribute("error", "Erro ao fazer upload do arquivo.");
                    response.sendRedirect(request.getContextPath() + servletPath);
                } catch (ClassNotFoundException | IOException | SQLException error) {
                    //logger.error("ClassNotFoundException | IOException | SQLException catch: " + error);

                    session.setAttribute("error", error.getMessage());
                    response.sendRedirect(request.getContextPath() + servletPath);
                } catch (Exception error) {
                    //logger.error("Exception catch: " + error);

                    session.setAttribute("error", "Erro ao gravar arquivo no servidor.");
                    response.sendRedirect(request.getContextPath() + servletPath);
                }
                break;
            }
            case "/historico": {
                session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                response.sendRedirect(request.getContextPath() + "/view/carga/index.jsp");
                break;
            }
            case "/relatoriosNatalidade": {
                response.sendRedirect(request.getContextPath() + "/view/relatorios/natalidade.jsp");
                break;
            }
            case "/relatoriosMortalidade": {
                response.sendRedirect(request.getContextPath() + "/view/relatorios/mortalidade.jsp");
                break;
            }
            case "/relatoriosCrescimentoPopulacional": {
                response.sendRedirect(request.getContextPath() + "/view/relatorios/crescimentoPopulacional.jsp");
                break;
            }
        }
    }

    protected void ReadCSVMortality(BufferedReader reader, List<String> lines, RegistradoDAO daoRegistrado, String separator) throws IOException {
        // Opening file and looping through it
        String line;
        // Looping line by line adding to a list
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        String separator_regex = "\\s*" + separator.toString() + "\\s*";
        // Creating a list of all the columns names
        List<String> first_line = Arrays.asList(lines.get(0).split(separator_regex));
        //logger.error(first_line);

        List<String> next_line;
        // Loop through the lines
        for(int i = 1; i < lines.size(); i++){
            next_line = Arrays.asList(lines.get(i).split(separator_regex));

            logger.info(next_line);

            //Create object for each new column
            Registro registro = new Registro();
            Obito obito = new Obito();
            Registrado registrado = new Registrado();

            // Link objects
            registrado.setObito(obito);
            registrado.getObito().setRegistro(registro);
            logger.info("objetcts linked");

            //logger.error(first_line.size());
            // Loop through the columns inserting attributes in object
            for(int j = 0; j < first_line.size(); j++){
                // if next line doesn't have the same amount of columns program shouldn't try to insert
                if(j >= next_line.size()){
                    //logger.error("Line amount of columns isn't equal to the first line\nBecause last columns are null");
                    break;
                }

                if(next_line.get(j).equals("") || next_line.get(j) == ""){
                    //logger.warn("next == vazio");
                }
                else{
                    // If is a mortality attribute then insert
                    //logger.error("Inserting attribute " + first_line.get(j) + " in object; value = " + next_line.get(j));
                    insertInObjectMortality(registro, obito, registrado, next_line, j, first_line.get(j));
                }
            }
            // Creating in the database the tuple
            try{
                Registro registro_ = registrado.getObito().getRegistro();
                //logger.error("ReadCSVMortality: \nId = " + registro_.getId_registro() +
                        //"\nTipo = " + registro_.getTipo_registro() + "\nAno = " + registro_.getAno_registro());
                Registrado registrado_found = daoRegistrado.read_obito(registro_.getId_registro(),
                        registro_.getTipo_registro(), registro_.getAno_registro());

                if(registrado_found == null){
                    //logger.info("ReadCSVMortality: Tried to create registrado.");
                    daoRegistrado.create(registrado);
                }
                else{
                   // logger.info("ReadCSVMortality: Tried to update registrado.");
                    daoRegistrado.update(registrado);
                }
            }catch (Exception error){
                //logger.error("ReadCSVMortality: Tried to create/update registrado: " + error);
            }
        }

    }

    protected String getStringWithoutQuotationMarks(String string){
        if(string.contains("\"")){
            String[] splited_string = string.split("\"");
            //logger.error("In getStringWithoutQuotationMarks: " + splited_string[1]);
            return splited_string[1];
        }
        //logger.error("In getStringWithoutQuotationMarks string: " + string);
        return string;
    }

    protected void insertInObjectMortality(Registro registro, Obito obito, Registrado registrado, List<String> next_line, Integer index,
                                           String columnName){
        String dateInString;
        DateTimeFormatter formatter;
        LocalDate date_temp;
        String string;
        // Removing quotations marks
        String columnNameWithoutQM = getStringWithoutQuotationMarks(columnName);
        switch (columnNameWithoutQM) {
            case "CONTADOR":
            case "id_registro":
                logger.info("Inserting id_registro");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                registro.setId_registro(Integer.valueOf(string));
                registro.setTipo_registro("obito");
                break;

            case "TIPOBITO":
            case "cod_tipo_obito":
                logger.info("Inserting cod_tipo_obito");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                obito.setCod_tipo_obito(Integer.valueOf(string));
                break;

            case "DTOBITO":
            case "data_obito":
                logger.info("Inserting data_obito");
                dateInString = getStringWithoutQuotationMarks(next_line.get(index));
                // Formatting Date: dMMyyyy
                if (dateInString.length() == 7){
                    char temp = '0';
                    dateInString = temp + next_line.get(index);
                }
                formatter = DateTimeFormatter.ofPattern("ddMMyyyy",
                        (new Locale("pt", "BR")));
                date_temp = LocalDate.parse(dateInString, formatter);
                // Inserting in Obito
                obito.setData_obito(Date.valueOf(date_temp));

                // Formating and inserting year in registro
                registro.setAno_registro(date_temp.getYear());
                break;

            case "HORAOBITO":
            case "hora_obito":
                logger.info("Inserting hora_obito: " + next_line.get(index));
                String timeInString = getStringWithoutQuotationMarks(next_line.get(index));
                if(timeInString.length() == 3){
                    char temp = '0';
                    timeInString = temp + timeInString;
                }
                Integer timeInteger = Integer.valueOf(timeInString);

                int hours = timeInteger / 100;
                int minutes = timeInteger % 100;

                String timeValue = String.format("%02d:%02d", hours, minutes);
                logger.info("Inserting hora_obito formatada: " + timeValue);
                obito.setHora_obito(Time.valueOf(timeValue + ":00"));
                break;

            case "CODMUNNATU":
            case "cod_municipio_nasc":
                logger.info("Inserting cod_municipio_nasc");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                registrado.setCod_municipio_nasc(Integer.valueOf(string));
                break;

            case "DTNASC":
            case "data_nascimento":
                logger.info("Inserting data_nascimento");
                dateInString = getStringWithoutQuotationMarks(next_line.get(index));

                // Formatting Date
                if (dateInString.length() == 7){
                    char temp = '0';
                    dateInString = temp + next_line.get(index);
                }
                formatter = DateTimeFormatter.ofPattern("ddMMyyyy",
                        (new Locale("pt", "BR")));
                date_temp = LocalDate.parse(dateInString, formatter);
                // Inserting in Registrado
                registrado.setData_nascimento(Date.valueOf(date_temp));
                break;

            case "SEXO":
            case "cod_sexo":
                logger.info("Inserting cod_sexo");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                registrado.setCod_sexo(Integer.valueOf(string));
                break;

            case "RACACOR":
            case "cod_raca_cor":
                logger.info("Inserting cod_raca_cor");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                registrado.setCod_raca_cor(Integer.valueOf(string));
                break;

            case "ESTCIV":
            case "cod_estado_civil":
                logger.info("Inserting cod_estado_civil");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                obito.setCod_est_civ_falecido(Integer.valueOf(string));
                break;

            case "LOCOCOR":
            case "cod_local_obito":
                logger.info("Inserting cod_local_obito");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                obito.setCod_local_obito(Integer.valueOf(string));
                break;

            case "CODMUNOCOR":
            case "cod_municipio_obito":
                logger.info("Inserting cod_municipio_obito");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                obito.setCod_municipio_obito(Integer.valueOf(string));
                break;

            case "CIRCOBITO":
            case "cod_circ_obito":
                logger.info("Inserting cod_circ_obito");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                obito.setCod_circ_obito(Integer.valueOf(string));
                break;

            case "IDADE":
            case "idade_falecido":
                logger.info("Inserting idade_falecido");
                string = getStringWithoutQuotationMarks(next_line.get(index));

                Character unidadeIdade = string.charAt(0);              //o primeiro subcampo indica a unidade da idade
                if("12345".contains(unidadeIdade.toString())){          //1 = minuto, 2 = hora, 3 = mês, 4 = ano, 5 = idade maior que 100 anos
                    String qtdIdade = string.substring(1);    //quantidade de unidades da idade

                    switch (unidadeIdade){
                        case '1':                   //subcampo varia de 01 e 59 (minutos)
                        case '2':                   //subcampo varia de 01 a 23 (horas)
                        case '3':                   //subcampo varia de 01 a 11 (meses)
                            string = "0";
                            obito.setIdade_falecido(Integer.valueOf(string));
                            break;
                        case '4':                   //subcampo varia de 00 a 99 (anos)
                            string = qtdIdade;
                            obito.setIdade_falecido(Integer.valueOf(string));
                            break;
                        case '5':                   //idade maior que 100 anos
                            string = "1" + qtdIdade;
                            obito.setIdade_falecido(Integer.valueOf(string));
                            break;
                        default:                    //valor inválido, a idade não será registrada
                            break;
                    }
                }
                else{
                    //logger.error("Idade Falecido inválida!");
                }
                //obito.setIdade_falecido(Integer.valueOf(string));
                break;

            default:
                break;
        }

    }

    protected void ReadCSVNatality(BufferedReader reader, List<String> lines, RegistradoDAO daoRegistrado, String separator) throws IOException {
        // Opening file and looping through it
        String line;
        // Looping line by line adding to a list
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        String separator_regex = "\\s*" + separator + "\\s*";
        // Creating a list of all the columns names
        List<String> first_line = Arrays.asList(lines.get(0).split(separator_regex));
        logger.info(first_line);

        // Loop through the lines
        for(int i = 1; i < lines.size(); i++){
            List<String> next_line = Arrays.asList(lines.get(i).split(separator_regex));
            logger.info(next_line);

            //Create object for each new column
            Registro registro = new Registro();
            Nascimento nascimento = new Nascimento();
            Registrado registrado = new Registrado();

            // Link objects
            registrado.setNascimento(nascimento);
            registrado.getNascimento().setRegistro(registro);
            logger.info("objetcts linked");

            // Loop through the columns inserting attributes in object
            for(int j = 0; j < first_line.size(); j++){
                if(j >= next_line.size()){
                    //logger.error("Line amount of columns isn't equal to the first line\nBecause last columns are null");
                    break;
                }

                if(next_line.get(j).equals("") || next_line.get(j) == ""){
                    //logger.info("next == vazio");
                }
                else{
                    //logger.error("Inserting attribute " + first_line.get(j) + " in object; value = " + next_line.get(j));
                    insertInObjectNatality(registro, nascimento, registrado, next_line, j, first_line.get(j));
                }
            }

            // Creating in the database the tuple
            try{
                Registro registro_ = registrado.getNascimento().getRegistro();
                //logger.error("ReadCSVNatality: \nId = " + registro_.getId_registro() +
                        //"\nTipo = " + registro_.getTipo_registro() + "\nAno = " + registro_.getAno_registro());
                Registrado registrado_found = daoRegistrado.read_nascimento(registro_.getId_registro(),
                        registro_.getTipo_registro(), registro_.getAno_registro());

                if(registrado_found == null){
                    //logger.info("ReadCSVNatality: Tried to create registrado.");
                    daoRegistrado.create(registrado);
                }
                else{
                    //logger.info("ReadCSVNatality: Tried to update registrado.");
                    daoRegistrado.update(registrado);
                }
            }catch (Exception error){
                //logger.error("ReadCSVNatality: Tried to create/update registrado: " + error);
            }
        }

    }

    protected void insertInObjectNatality(Registro registro, Nascimento nascimento, Registrado registrado, List<String> next_line, Integer index,
                                          String columnName){
        String dateInString;
        DateTimeFormatter formatter;
        LocalDate date_temp;
        String string;
        // Removing quotations marks
        String columnNameWithoutQM = getStringWithoutQuotationMarks(columnName);

        switch (columnNameWithoutQM) {
            case "CODMUNNATU":
            case "cod_municipio_nasc":
                logger.info("Inserting cod_municipio_nasc");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                registrado.setCod_municipio_nasc(Integer.valueOf(string));
                break;

            case "DTNASC":
            case "data_nascimento":
                logger.info("Inserting data_nascimento");
                dateInString = getStringWithoutQuotationMarks(next_line.get(index));
                // Formatting Date
                if (dateInString.length() == 7){
                    char temp = '0';
                    dateInString = temp + next_line.get(index);
                }
                formatter = DateTimeFormatter.ofPattern("ddMMyyyy",
                        (new Locale("pt", "BR")));
                date_temp = LocalDate.parse(dateInString, formatter);
                // Inserting in Registrado
                registrado.setData_nascimento(Date.valueOf(date_temp));
                registro.setAno_registro(date_temp.getYear());
                break;

            case "SEXO":
            case "cod_sexo":
                logger.info("Inserting cod_sexo");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                registrado.setCod_sexo(Integer.valueOf(string));
                break;

            case "RACACOR":
            case "cod_raca_cor":
                logger.info("Inserting cod_raca_cor");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                registrado.setCod_raca_cor(Integer.valueOf(string));
                break;

            case "CONTADOR":
            case "id_registro":
                logger.info("Inserting id_registro");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                registro.setId_registro(Integer.valueOf(string));
                registro.setTipo_registro("nascimento");
                break;

            case "IDADEMAE":
            case "idade_mae":
                logger.info("Inserting idade_mae");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                nascimento.setIdade_mae(Integer.valueOf(string));
                break;

            case "ESTCIVMAE":
            case "cod_estado_civil_mae":
                logger.info("Inserting cod_estado_civil_mae");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                nascimento.setCod_estado_civil_mae(Integer.valueOf(string));
                break;

            case "PARTO":
            case "cod_tipo_parto":
                logger.info("Inserting cod_tipo_parto");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                nascimento.setCod_tipo_parto(Integer.valueOf(string));
                break;

            case "HORANASC":
            case "hora_nascimento":
                logger.info("Inserting hora_nascimento: " + next_line.get(index));
                String timeInString = getStringWithoutQuotationMarks(next_line.get(index));
                if(timeInString.length() == 3){
                    char temp = '0';
                    timeInString = temp + timeInString;
                }
                Integer timeInteger = Integer.valueOf(timeInString);

                int hours = timeInteger / 100;
                int minutes = timeInteger % 100;

                String timeValue = String.format("%02d:%02d", hours, minutes);
                logger.info("Inserting hora_nascimento formatada: " + timeValue);
                nascimento.setHora_nascimento(Time.valueOf(timeValue + ":00"));
                break;

            case "PESO":
            case "peso_nascido_vivo":
                logger.info("Inserting peso_nascido_vivo");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                nascimento.setPeso_nascido_vivo(Integer.valueOf(string));
                break;

            case "RACACORMAE":
            case "cod_raca_cor_mae":
                logger.info("Inserting cod_raca_cor_mae");
                string = getStringWithoutQuotationMarks(next_line.get(index));
                nascimento.setCod_raca_cor_mae(Integer.valueOf(string));
                break;

            default:
                break;
        }

    }
}