package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import jdbc.ConnectionFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO why does it implement AutoCloseable
public abstract class DAOFactory implements AutoCloseable{
    protected Connection connection;
    protected static final Logger logger = LogManager.getLogger(DAOFactory.class);

    public static DAOFactory getInstance() throws ClassNotFoundException, IOException, SQLException {
        Connection connection = ConnectionFactory.getInstance().getConnection();
        DAOFactory factory;

        if (ConnectionFactory.getDbServer().equals("postgresql")) {
            factory = new PgDAOFactory(connection);
        }
        else {
            throw new RuntimeException("Servidor de banco de dados não suportado.");
        }

        return factory;
    }

    // TODO ver se todas essas funções são úteis.
//
//    public void beginTransaction() throws SQLException {
//        try {
//            connection.setAutoCommit(false);
//        } catch (SQLException error) {
//            logger.error("beginTransaction: " + error);
//
//            System.err.println(error.getMessage());
//            throw new SQLException("Erro ao abrir transação.");
//        }
//    }
//
//    public void commitTransaction() throws SQLException {
//        try {
//            connection.commit();
//        } catch (SQLException error) {
//            logger.error("commitTransaction: " + error);
//
//            System.err.println(error.getMessage());
//            throw new SQLException("Erro ao finalizar transação.");
//        }
//    }
//
//    public void rollbackTransaction() throws SQLException {
//        try {
//            connection.rollback();
//        } catch (SQLException error) {
//            logger.error("rollbackTransaction: " + error);
//
//            System.err.println(error.getMessage());
//            throw new SQLException("Erro ao executar transação.");
//        }
//    }
//
//    public void endTransaction() throws SQLException {
//        try {
//            connection.setAutoCommit(true);
//        } catch (SQLException error) {
//            logger.error("endTransaction: " + error);
//
//            System.err.println(error.getMessage());
//            throw new SQLException("Erro ao finalizar transação.");
//        }
//    }
//
//    public void closeConnection() throws SQLException {
//        try {
//            connection.close();
//        } catch (SQLException error) {
//            logger.error("closeConnection: " + error);
//
//            System.err.println(error.getMessage());
//            throw new SQLException("Erro ao fechar conexão ao banco de dados.");
//        }
//    }

    public abstract CargaDAO getCargaDAO();
    public abstract RegistradoDAO getRegistradoDAO();

}
