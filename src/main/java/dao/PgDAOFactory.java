package dao;

import java.sql.Connection;

public class PgDAOFactory extends DAOFactory {
    // The connection is an inherited attribute from DAOFactory
    public PgDAOFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public CargaDAO getCargaDAO() {
        return new PgCargaDAO(this.connection);
    }

    @Override
    public RegistradoDAO getRegistradoDAO() {
        return new PgRegistradoDAO(this.connection);
    }

    @Override
    public void close(){

    }
}