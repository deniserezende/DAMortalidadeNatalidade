package dao;

import model.Carga;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PgCargaDAO implements CargaDAO {
    private final Connection connection;
    protected static final Logger logger = LogManager.getLogger(PgCargaDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO \"DAMortalidade_Natalidade\".\"CARGA\"(data_carga, hora_carga, responsavel, email, " +
                    "nome_arquivo, tipo_carga, titulo_carga) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?);";

    private static final String READ_QUERY =
            "SELECT * FROM \"DAMortalidade_Natalidade\".\"CARGA\"" +
                    "WHERE id_carga = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM \"DAMortalidade_Natalidade\".\"CARGA\" +\n" +
                    "WHERE id_carga = ?;\n";


    private static final String ALL_QUERY =
            "SELECT *\nFROM \"DAMortalidade_Natalidade\".\"CARGA\"";

    public PgCargaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Carga carga){
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setDate(1, carga.getData_carga());
            statement.setTime(2, carga.getHora_carga());
            statement.setString(3, carga.getResponsavel());
            statement.setString(4, carga.getEmail());
            statement.setString(5, carga.getNome_arquivo());
            statement.setInt(6, carga.getTipo_carga());
            statement.setString(7, carga.getTitulo_carga());

            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("create catch: " + error);
        }
    }

    @Override
    public Carga read(Integer id){
        Carga carga = new Carga();
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    // Setting attributes Carga
                    carga.setData_carga(result.getDate("data_carga"));
                    carga.setHora_carga(result.getTime("hora_carga"));
                    carga.setTipo_carga(result.getInt("tipo_carga"));
                    carga.setNome_arquivo(result.getString("nome_arquivo"));
                    carga.setEmail(result.getString("email"));
                    carga.setResponsavel(result.getString("resposavel"));
                    carga.setTitulo_carga(result.getString("titulo_carga"));

                } else {
                    throw new SQLException("Erro ao visualizar: carga não encontrada.");
                }
            }
        } catch (SQLException error) {
            logger.error("read catch: " + error);
        }

        return carga;
    }

    @Override
    public void update(Carga carga){
    }

    @Override
    public void delete(Integer id){
        Carga carga = read(id);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Error to delete: carga não encontrada.");
            }
        } catch (SQLException error) {
            logger.error("delete catch: " + error);
        }
    }

    @Override
    public List<Carga> all() throws SQLException {
        List<Carga> cargaList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Carga carga = new Carga();
                carga.setNome_arquivo(result.getString("nome_arquivo"));
                carga.setData_carga(result.getDate("data_carga"));
                carga.setHora_carga(result.getTime("hora_carga"));
                carga.setTipo_carga(result.getInt("tipo_carga"));
                carga.setResponsavel(result.getString("responsavel"));
                carga.setEmail(result.getString("email"));
                carga.setTitulo_carga(result.getString("titulo_carga"));
                cargaList.add(carga);
            }
        } catch (SQLException error) {
            logger.error("all catch: " + error);
            throw new SQLException("Error listing cargas.");
        }
        return cargaList;
    }
}