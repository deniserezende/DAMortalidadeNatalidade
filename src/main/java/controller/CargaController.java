package controller;

import dao.DAO;
import dao.DAOFactory;
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
import dao.PgCargaDAO;
import model.Carga;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
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
                    "/carga/create",
                    "/historico" //é o /user do projeto do professor
            }
)

public class CargaController extends HttpServlet{

    protected static final Logger logger = LogManager.getLogger(CargaController.class);
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
            case "/carga/create": {
                dispatcher = request.getRequestDispatcher("/view/carga/create.jsp");
                dispatcher.forward(request, response);
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DAO<Carga> dao;
        Carga carga = new Carga();
        HttpSession session = request.getSession();
        String servletPath = request.getServletPath();

        switch (request.getServletPath()) {
            case "/carga/create": {
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
                    while (iter.hasNext()) {
                        FileItem item = iter.next();

                        // Process a regular form field
                        if (item.isFormField()) {
                            String fieldName = item.getFieldName();
                            String fieldValue = item.getString();

                            // Creating carga
                            switch (fieldName) {
                                case "nome_arquivo":
                                    carga.setNome_arquivo(fieldName);
                                    break;
                                case "tipo_carga":
                                    carga.setTipo_carga(Integer.valueOf(fieldName));
                                    break;
                                case "data_carga":
                                    java.util.Date date = new SimpleDateFormat("ddMMyyyy").parse(fieldValue);
                                    carga.setData_carga((java.sql.Date) date);
                                    break;
                                case "hora_carga":
                                    java.sql.Time time = new Time(Integer.valueOf(fieldName));
//                                    date = new SimpleDateFormat("ddmmyyyy").parse(fieldValue);
                                    carga.setHora_carga(time);
                                    break;
                                case "responsavel":
                                    carga.setResponsavel(fieldName);
                                    break;
                                case "email":
                                    carga.setEmail(fieldName);
                                    break;
                            }
                        }
//                        else {
//                            String fieldName = item.getFieldName();
//                            String fileName = item.getName();
//                            if (fieldName.equals("avatar") && !fileName.isBlank()) {
//                                // Dados adicionais (não usado nesta aplicação)
//                                String contentType = item.getContentType();
//                                boolean isInMemory = item.isInMemory();
//                                long sizeInBytes = item.getSize();
//
//                                // Pega o caminho absoluto da aplicação
//                                String appPath = request.getServletContext().getRealPath("");
//                                // Grava novo arquivo na pasta img no caminho absoluto
//                                String savePath = appPath + File.separator + SAVE_DIR + File.separator + fileName;
//                                File uploadedFile = new File(savePath);
//                                item.write(uploadedFile);
//
//                                user.setAvatar(fileName);
//                            }
//                        }
                    }

                    dao = daoFactory.getCargaDAO();

                    if (servletPath.equals("/carga/create")) {
                        dao.create(carga);
                    }

                    response.sendRedirect(request.getContextPath() + "/view/carga/index.jsp"); // TODO verificar isso

                } catch (ParseException error) {
                    logger.error("ParseException catch: " + error);
                    logger.error(error.getMessage());

                    session.setAttribute("error", "O formato de data não é válido. Por favor entre data no formato dd/mm/aaaa");
                    response.sendRedirect(request.getContextPath() + servletPath);
                } catch (FileUploadException error) {
                    logger.error("FileUploadException catch: " + error);
                    logger.error(error.getMessage());

                    session.setAttribute("error", "Erro ao fazer upload do arquivo.");
                    response.sendRedirect(request.getContextPath() + servletPath);
                } catch (ClassNotFoundException | IOException | SQLException error) {
                    logger.error("ClassNotFoundException | IOException | SQLException catch: " + error);
                    logger.error(error.getMessage());

                    session.setAttribute("error", error.getMessage());
                    response.sendRedirect(request.getContextPath() + servletPath);
                } catch (Exception error) {
                    logger.error("Exception catch: " + error);
                    logger.error(error.getMessage());

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
//            case "/user/delete": {
//                String[] users = request.getParameterValues("delete");
//
//                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
//                    dao = daoFactory.getUserDAO();
//
//                    try {
//                        daoFactory.beginTransaction();
//
//                        for (String userId : users) {
//                            dao.delete(Integer.parseInt(userId));
//                        }
//
//                        daoFactory.commitTransaction();
//                        daoFactory.endTransaction();
//                    } catch (SQLException ex) {
//                        session.setAttribute("error", ex.getMessage());
//                        daoFactory.rollbackTransaction();
//                    }
//                } catch (ClassNotFoundException | IOException ex) {
//                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
//                    session.setAttribute("error", ex.getMessage());
//                } catch (SQLException ex) {
//                    Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, "Controller", ex);
//                    session.setAttribute("rollbackError", ex.getMessage());
//                }
//
//                response.sendRedirect(request.getContextPath() + "/user");
//                break;
//            }
        }
    }
}