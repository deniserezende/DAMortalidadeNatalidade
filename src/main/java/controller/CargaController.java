package controller;

import dao.*;
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
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

//import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static sun.font.CreatedFontTracker.MAX_FILE_SIZE;

@WebServlet(name = "CargaController",
        urlPatterns = {
                "",
                "/cargacreate",
                "/historico" //é o /user do projeto do professor
        }
)

public class CargaController extends HttpServlet{

    protected static final Logger logger = LogManager.getLogger(CargaController.class);
    List<String> column_mortality = Arrays.asList("cod_tipo_obito","data_obito","hora_obito","cod_municipio_nasc","data_nascimento","idade_falecido","cod_sexo","cod_raca_cor","cod_estado_civil","cod_local_obito","cod_municipio_obito","cod_circ_obito","id_registro");
    List<String> column_natality = Arrays.asList("cod_municipio_nasc","idade_mae","cod_estado_civil_mae","cod_tipo_parto","data_nascimento","hora_nascimento","cod_sexo","cod_raca_cor","peso_nascido_vivo","cod_raca_cor_mae","id_registro");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //HttpSession session;
        RequestDispatcher dispatcher;
        DAO<Carga> dao;

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
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DAO<Carga> daoCarga;
        DAO<Registrado> daoRegistrado = null;
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

                    // File uploaded
                    File uploadedFile = null;
                    while (iter.hasNext()) {
                        FileItem item = iter.next();

                        // Process a regular form field
                        if (item.isFormField()) {
                            String fieldName = item.getFieldName();
                            String fieldValue = item.getString();
                            logger.error("fieldName=" + fieldName);
                            logger.error("fieldValue=" + fieldValue);

                            // Creating carga
                            switch (fieldName) {
                                case "arquivo":
                                    carga.setNome_arquivo(fieldValue);
                                    break;
                                case "tipo_carga":
                                    if(Objects.equals("Registro de Natalidade", fieldValue)){
                                        logger.error("Setting carga.setTipo_carga == 1");
                                        carga.setTipo_carga(1);
                                    }
                                    else{ // if equals "Registro de Mortalidade"
                                        logger.error("Setting carga.setTipo_carga == 2");
                                        carga.setTipo_carga(2);
                                    }
                                    break;
                                case "responsavel":
                                    carga.setResponsavel(fieldValue);
                                    break;
                                case "email":
                                    carga.setEmail(fieldValue);
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

                            /* Inseting Time in Carga */
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
                            logger.error("Calling ReadCSVNatality");
                            ReadCSVNatality(reader, lines, daoRegistrado);
                        }
                        else{ // Mortality
                            logger.error("Calling ReadCSVMortality");
                            ReadCSVMortality(reader, lines, daoRegistrado);
                        }
                    }
                    else{
                        logger.error("No file uploaded");
                    }

                    daoCarga = daoFactory.getCargaDAO();

                    if (servletPath.equals("/cargacreate")) {
                        daoCarga.create(carga);
                    }

                    response.sendRedirect(request.getContextPath() + "/historico");

                } catch (ParseException error) {
                    logger.error("ParseException catch: " + error);

                    session.setAttribute("error", "O formato de data não é válido. Por favor entre data no formato dd/mm/aaaa");
                    response.sendRedirect(request.getContextPath() + servletPath);
                } catch (FileUploadException error) {
                    logger.error("FileUploadException catch: " + error);

                    session.setAttribute("error", "Erro ao fazer upload do arquivo.");
                    response.sendRedirect(request.getContextPath() + servletPath);
                } catch (ClassNotFoundException | IOException | SQLException error) {
                    logger.error("ClassNotFoundException | IOException | SQLException catch: " + error);

                    session.setAttribute("error", error.getMessage());
                    response.sendRedirect(request.getContextPath() + servletPath);
                } catch (Exception error) {
                    logger.error("Exception catch: " + error);

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
            }
        }
    }

    protected void ReadCSVMortality(BufferedReader reader, List<String> lines, DAO<Registrado> daoRegistrado) throws IOException {
        // Opening file and looping through it
        String line = null;
        // Looping line by line adding to a list
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        // Creating a list of all the columns names
        List<String> first_line = Arrays.asList(lines.get(0).split("\\s*,\\s*"));
        logger.info(first_line);

        // Loop through the lines
        for(int i = 1; i < lines.size(); i++){
            List<String> next_line = Arrays.asList(lines.get(i).split("\\s*,\\s*"));
            logger.info(next_line);

            //Create object for each new column
            Registro registro = new Registro();
            Obito obito = new Obito();
            Registrado registrado = new Registrado();

            // Link objects
            registrado.setObito(obito);
            registrado.getObito().setRegistro(registro);
            logger.info("objetcts linked");

            // Loop through the columns inserting attributes in object
            for(int j = 0; j < first_line.size(); j++){
                if(next_line.get(j) == ""){
                    logger.error("next == vazio");
                }
                else{
                    // If is a mortality attribute then insert
                    if (column_mortality.contains(first_line.get(j))) {
                        insertInObjectMortality(registro, obito, registrado, next_line, j, first_line.get(j));
                    }
                }
            }

            // Creating in the database the tuple
            try{
                daoRegistrado.create(registrado);
            }catch (Exception error){
                logger.error("ReadCSVMortality: Tried to create registrado: " + error);
            }
        }

    }

    protected void insertInObjectMortality(Registro registro, Obito obito, Registrado registrado, List<String> next_line, Integer index,
                                           String columnName){
        String dateInString;
        DateTimeFormatter formatter;
        LocalDate date_temp;
        switch (columnName) {
            case "\"TIPOBITO\"":
            case "cod_tipo_obito":
                logger.info("Inserting cod_tipo_obito");
                obito.setCod_tipo_obito(Integer.valueOf(next_line.get(index)));
                break;

            // TODO ver qual funcao usar no lugar de getYear
            case "\"DTOBITO\"":
            case "data_obito":
                logger.info("Inserting data_obito");
                dateInString = next_line.get(index);
                // Formatting Date
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

            case "\"HORAOBITO\"":
            case "hora_obito":
                logger.error("Inserting hora_obito: " + next_line.get(index));
                String timeInString = next_line.get(index);
                if(timeInString.length() == 3){
                    char temp = '0';
                    timeInString = temp + timeInString;
                }
                Integer timeInteger = Integer.valueOf(timeInString);

                int hours = timeInteger / 100;
                int minutes = timeInteger % 100;

                String timeValue = String.format("%02d:%02d", hours, minutes);
                logger.error("Inserting hora_obito formatada pela Denise: " + timeValue);
                obito.setHora_obito(Time.valueOf(timeValue + ":00"));
                break;

            case "\"CODMUNNATU\"":
            case "cod_municipio_nasc":
                logger.info("Inserting cod_municipio_nasc");
                registrado.setCod_municipio_nasc(Integer.valueOf(next_line.get(index)));
                break;

            case "\"DTNASC\"":
            case "data_nascimento":
                logger.info("Inserting data_nascimento");
                dateInString = next_line.get(index);
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

            case "\"SEXO\"":
            case "cod_sexo":
                logger.info("Inserting cod_sexo");
                registrado.setCod_sexo(Integer.valueOf(next_line.get(index)));
                break;

            case "\"RACACOR\"":
            case "cod_raca_cor":
                logger.info("Inserting cod_raca_cor");
                registrado.setCod_raca_cor(Integer.valueOf(next_line.get(index)));
                break;

            case "\"ESTCIV\"":
            case "cod_estado_civil":
                logger.info("Inserting cod_estado_civil");
                obito.setCod_est_civ_falecido(Integer.valueOf(next_line.get(index)));
                break;
            case "\"LOCOCOR\"":
            case "cod_local_obito":
                logger.info("Inserting cod_local_obito");
                obito.setCod_local_obito(Integer.valueOf(next_line.get(index)));
                break;

            case "\"CODMUNOCOR\"":
            case "cod_municipio_obito":
                logger.info("Inserting cod_municipio_obito");
                obito.setCod_municipio_obito(Integer.valueOf(next_line.get(index)));
                break;

            case "\"CIRCOBITO\"":
            case "cod_circ_obito":
                logger.info("Inserting cod_circ_obito");
                obito.setCod_circ_obito(Integer.valueOf(next_line.get(index)));
                break;

            case "\"CONTADOR\"":
            case "id_registro":
                logger.info("Inserting id_registro");
                registro.setId_registro(Integer.valueOf(next_line.get(index)));
                registro.setTipo_registro("obito");
                break;

            case "idade_mae":
                logger.info("Inserting idade_mae");

                registro.setId_registro(Integer.valueOf(next_line.get(index)));
                registro.setTipo_registro("obito");
                break;
            default:
                break;
        }

    }

    protected void ReadCSVNatality(BufferedReader reader, List<String> lines, DAO<Registrado> daoRegistrado) throws IOException {
        // Opening file and looping through it
        String line = null;
        // Looping line by line adding to a list
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        // Creating a list of all the columns names
        List<String> first_line = Arrays.asList(lines.get(0).split("\\s*,\\s*"));
        logger.info(first_line);

        // Loop through the lines
        for(int i = 1; i < lines.size(); i++){
            List<String> next_line = Arrays.asList(lines.get(i).split("\\s*,\\s*"));
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
                if(next_line.get(j) == ""){
                    logger.error("next == vazio");
                }
                else{
                    // If is a natality attribute then insert
                    if (column_natality.contains(first_line.get(j))) {
                        logger.info("Inserting attribute " + first_line.get(j) + " in object");
                        insertInObjectNatality(registro, nascimento, registrado, next_line, j, first_line.get(j));
                    }
                }
            }

            // Creating in the database the tuple
            try{
                daoRegistrado.create(registrado);
            }catch (Exception error){
                logger.error("ReadCSVNatality: Tried to create registrado: " + error);
            }
        }

    }

    protected void insertInObjectNatality(Registro registro, Nascimento nascimento, Registrado registrado, List<String> next_line, Integer index,
                                          String columnName){
        String dateInString;
        DateTimeFormatter formatter;
        LocalDate date_temp;
        switch (columnName) {
            case "\"CODMUNNATU\"":
            case "cod_municipio_nasc":
                logger.info("Inserting cod_municipio_nasc");
                registrado.setCod_municipio_nasc(Integer.valueOf(next_line.get(index)));
                break;

            case "\"DTNASC\"":
            case "data_nascimento":
                logger.info("Inserting data_nascimento");
                dateInString = next_line.get(index);
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

            case "\"SEXO\"":
            case "cod_sexo":
                logger.info("Inserting cod_sexo");
                registrado.setCod_sexo(Integer.valueOf(next_line.get(index)));
                break;

            case "\"RACACOR\"":
            case "cod_raca_cor":
                logger.info("Inserting cod_raca_cor");
                registrado.setCod_raca_cor(Integer.valueOf(next_line.get(index)));
                break;

            case "\"CONTADOR\"":
            case "id_registro":
                logger.info("Inserting id_registro");
                registro.setId_registro(Integer.valueOf(next_line.get(index)));
                registro.setTipo_registro("nascimento");
                break;

            case "idade_mae":
                logger.info("Inserting idade_mae");
                nascimento.setIdade_mae(Integer.valueOf(next_line.get(index)));
                break;

            case "cod_estado_civil_mae":
                logger.info("Inserting cod_estado_civil_mae");
                nascimento.setCod_estado_civil_mae(Integer.valueOf(next_line.get(index)));
                break;

            case "cod_tipo_parto":
                logger.info("Inserting cod_tipo_parto");
                nascimento.setCod_tipo_parto(Integer.valueOf(next_line.get(index)));
                break;

            case "hora_nascimento":
                logger.error("Inserting hora_nascimento: " + next_line.get(index));
                String timeInString = next_line.get(index);
                if(timeInString.length() == 3){
                    char temp = '0';
                    timeInString = temp + timeInString;
                }
                Integer timeInteger = Integer.valueOf(timeInString);

                int hours = timeInteger / 100;
                int minutes = timeInteger % 100;

                String timeValue = String.format("%02d:%02d", hours, minutes);
                logger.error("Inserting hora_nascimento formatada pela Denise: " + timeValue);
                nascimento.setHora_nascimento(Time.valueOf(timeValue + ":00"));
                break;

            case "peso_nascido_vivo":
                logger.info("Inserting peso_nascido_vivo");
                nascimento.setPeso_nascido_vivo(Integer.valueOf(next_line.get(index)));
                break;

            case "cod_raca_cor_mae":
                logger.info("Inserting cod_raca_cor_mae");
                nascimento.setCod_raca_cor_mae(Integer.valueOf(next_line.get(index)));
                break;

            default:
                break;
        }

    }

}