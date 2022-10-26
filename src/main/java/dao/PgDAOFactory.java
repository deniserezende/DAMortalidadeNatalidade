package dao;

import java.sql.Connection;

public class PgDAOFactory extends DAOFactory {
    // connection is an inherited attribute from DAOFactory
    public PgDAOFactory(Connection connection) {
        this.connection = connection;
    }

    // TODO why do I have to implement this:
    @Override
    public void close() throws Exception {

    }
}
