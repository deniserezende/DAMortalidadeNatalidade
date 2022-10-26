package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import jdbc.ConnectionFactory;

// TODO why does it implement AutoCloseable
public abstract class DAOFactory implements AutoCloseable{
    protected Connection connection;

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

    // TODO não sei o que são essas funções
//    public void beginTransaction() throws SQLException {
//        try {
//            connection.setAutoCommit(false);
//        } catch (SQLException error) {
//            System.err.println(error.getMessage());
//
//            throw new SQLException("Erro ao abrir transação.");
//        }
//    }
//
//    public void commitTransaction() throws SQLException {
//        try {
//            connection.commit();
//        } catch (SQLException error) {
//            System.err.println(error.getMessage());
//
//            throw new SQLException("Erro ao finalizar transação.");
//        }
//    }
//
//    public void rollbackTransaction() throws SQLException {
//        try {
//            connection.rollback();
//        } catch (SQLException error) {
//            System.err.println(error.getMessage());
//
//            throw new SQLException("Erro ao executar transação.");
//        }
//    }
//
//    public void endTransaction() throws SQLException {
//        try {
//            connection.setAutoCommit(true);
//        } catch (SQLException error) {
//            System.err.println(error.getMessage());
//
//            throw new SQLException("Erro ao finalizar transação.");
//        }
//    }
//
//    public void closeConnection() throws SQLException {
//        try {
//            connection.close();
//        } catch (SQLException error) {
//            System.err.println(error.getMessage());
//
//            throw new SQLException("Erro ao fechar conexão ao banco de dados.");
//        }
//    }
}
