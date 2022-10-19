package jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public abstract class ConnectionFactory {
    protected static final Logger logger = LogManager.getLogger(ConnectionFactory.class);

    private static ConnectionFactory instance = null;
    protected static String propertiesPath = "../../configurations/datasource.properties";
    private static String dbServer;

    // Creating a constructor to initialize the class,
    // type protected so that only subclasses can access it.
    protected ConnectionFactory() {
    }

    // Creating a method to get/create the attribute instance, that depends on the initialization of the "instance".
    // We only want one instance of this class.
    public static ConnectionFactory getInstance() throws IOException {
        if (instance == null) {
            Properties properties = new Properties();

            // Getting "server" from the file datasource
            try {
                InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream(propertiesPath);
                properties.load(input);

                dbServer = properties.getProperty("server");
            } catch (IOException error) {
                logger.error("ConnectionFactory catch: Error: " + error);

                System.err.println(error.getMessage());
                throw new IOException("Erro ao obter informações do banco de dados.");
            }

            // Check if the database is postgresql
            // if so creates an instance of PgConnectionFactory == Postgresql Connection Factory
            if (getDbServer().equals("postgresql")) {
                instance = new PgConnectionFactory();
            }
            else {
                throw new RuntimeException("Servidor de banco de dados não suportado.");
            }
        }

        return instance;
    }

    // @return the dbServer
    public static String getDbServer() {
        return dbServer;
    }

    public abstract Connection getConnection() throws IOException, SQLException, ClassNotFoundException;

}
