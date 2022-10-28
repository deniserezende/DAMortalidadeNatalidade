package jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PgConnectionFactory extends ConnectionFactory {
    protected static final Logger logger = LogManager.getLogger(PgConnectionFactory.class);

    // Attributes from ConnectionFactory are inherited
    private String dbHost;
    private String dbPort;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    // Creating a constructor to initialize the class,
    // type public so that the class can be instantiated from anywhere.
    public PgConnectionFactory() {
    }

    public void readProperties() throws IOException {
        Properties properties = new Properties();

        // Getting properties from the file datasource
        try {
            InputStream input = this.getClass().getClassLoader().getResourceAsStream(propertiesPath);
            properties.load(input);

            dbHost = properties.getProperty("host");
            dbPort = properties.getProperty("port");
            dbName = properties.getProperty("name");
            dbUser = properties.getProperty("user");
            dbPassword = properties.getProperty("password");
        } catch (IOException error) {
            logger.error("PgConnectionFactory: Error: " + error);

            System.err.println(error.getMessage());
            throw new IOException("Erro ao obter informações do banco de dados.");
        }
    }

    // Implementing the Connection Factory abstract function
    @Override
    public Connection getConnection() throws IOException, SQLException, ClassNotFoundException {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");

            readProperties();

            String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
            logger.info("String url: " + url);

            connection = DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (ClassNotFoundException error) {
            System.err.println(error.getMessage());

            throw new ClassNotFoundException("Erro de conexão ao banco de dados.");
        } catch (SQLException error) {
            System.err.println(error.getMessage());

            throw new SQLException("Erro de conexão ao banco de dados.");
        }

        return connection;

    }
}
