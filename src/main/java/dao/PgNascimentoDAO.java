package dao;

import model.Nascimento;
import model.Registrado;
import model.Registro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PgNascimentoDAO implements RegistradoDAO {

    private final Connection connection;
    protected static final Logger logger = LogManager.getLogger(PgNascimentoDAO.class);

    private static final String CREATE_REGISTRO =
            "INSERT INTO \"DAMortalidade_Natalidade\".\"REGISTRO\"(id_registro, tipo_registro, ano_registro) " +
                    "VALUES(?, ?, ?);";

    private static final String CREATE_NASCIMENTO =
            "INSERT INTO \"DAMortalidade_Natalidade\".\"NASCIMENTO\"(id_registro, tipo_registro, ano_registro," +
                    "hora_nascimento, cod_tipo_parto, cod_raca_cor_mae, idade_mae, cod_estado_civil_mae, peso_nascido_vivo)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            //to_date(?, 'ddmmyyyy')

    private static final String CREATE_REGISTRADO =
            "INSERT INTO \"DAMortalidade_Natalidade\".\"REGISTRADO\"(id_registro_nasc, tipo_registro_nasc, ano_registro_nasc" +
                    "cod_municipio_nasc, cod_raca_cor, data_nascimento, cod_sexo) " +
                    "VALUES(?, ?, ?, ?, to_date(?, 'ddmmyyyy'), ?);";

    private static final String READ_QUERY =
            "SELECT * FROM \"DAMortalidade_Natalidade\".\"REGISTRADO\", \"DAMortalidade_Natalidade\".\"NASCIMENTO\" " +
                    "WHERE \"DAMortalidade_Natalidade\".\"NASCIMENTO\".id_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".id_registro_nasc\n " +
                    "AND \"DAMortalidade_Natalidade\".\"NASCIMENTO\".tipo_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".tipo_registro_nasc\n " +
                    "AND \"DAMortalidade_Natalidade\".\"NASCIMENTO\".ano_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".ano_registro_nasc\n " +
                    "AND id_registrado = ?;";

    private static final String UPDATE_NASCIMENTO =
            "UPDATE \"DAMortalidade_Natalidade\".\"NASCIMENTO\"" +
                    "SET hora_nascimento = ?, cod_tipo_parto = ?, cod_raca_cor_mae = ?, idade_mae = ?, cod_estado_civil_mae = ?, peso_nascido_vivo = ?" +
                    "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?;";

    private static final String UPDATE_REGISTRADO =
            "UPDATE \"DAMortalidade_Natalidade\".\"REGISTRADO\"" +
                    "SET cod_municipio_nasc = ?, cod_raca_cor = ?, data_nascimento = ?, cod_sexo = ?, " +
                    "WHERE id_registro_nasc = ? AND tipo_registro_nasc = ? AND ano_registro_nasc = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM \"DAMortalidade_Natalidade\".\"REGISTRADO\" +\n" +
                    "WHERE id_registrado = ?; \n " +
                    "DELETE FROM \"DAMortalidade_Natalidade\".\"NASCIMENTO\" \" +\n" +
                    "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?; \n " +
                    "DELETE FROM \"DAMortalidade_Natalidade\".\"REGISTRO\" \" +\n" +
                    "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?; \n";

    private static final String ALL_QUERY =
            "SELECT *\n" +
                    "FROM \"DAMortalidade_Natalidade\".\"NASCIMENTO\", \"DAMortalidade_Natalidade\".\"REGISTRADO\"\n" +
                    "WHERE \"DAMortalidade_Natalidade\".\"NASCIMENTO\".id_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".id_registro_nasc\n" +
                    "AND \"DAMortalidade_Natalidade\".\"NASCIMENTO\".tipo_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".tipo_registro_nasc;" +
                    "AND \"DAMortalidade_Natalidade\".\"NASCIMENTO\".ano_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".ano_registro_nasc\n ";

    public PgNascimentoDAO(Connection connection) {
        this.connection = connection;
    }

    // TODO ta falando que tem código repetido, talvez tenhamos que separar o create_registro
    private void create_registro(Registro registro) throws SQLException{
        try (PreparedStatement statement = connection.prepareStatement(CREATE_REGISTRO)) {
            statement.setInt(1, registro.getId_registro());
            statement.setString(2, registro.getTipo_registro());
            statement.setInt(3, registro.getAno_registro());

            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("create_registro catch: " + error);
        }
    }

    private void create_nascimento(Nascimento nascimento) throws SQLException{
        try (PreparedStatement statement = connection.prepareStatement(CREATE_NASCIMENTO)) {
            statement.setTime(1, nascimento.getHora_nascimento());
            statement.setInt(2, nascimento.getCod_tipo_parto());
            statement.setInt(3, nascimento.getCod_raca_cor_mae());
            statement.setInt(4, nascimento.getIdade_mae());
            statement.setInt(5, nascimento.getCod_estado_civil_mae());
            statement.setInt(6, nascimento.getPeso_nascido_vivo());

            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("create_nascimento catch: " + error);
        }
    }

    private void create_registrado(Registrado registrado) throws SQLException{
        try (PreparedStatement statement = connection.prepareStatement(CREATE_REGISTRADO)) {
            statement.setInt(1, registrado.getNascimento().getRegistro().getId_registro());
            statement.setString(2, registrado.getNascimento().getRegistro().getTipo_registro());
            statement.setInt(3, registrado.getNascimento().getRegistro().getAno_registro());
            statement.setInt(4, registrado.getCod_municipio_nasc());
            statement.setInt(5, registrado.getCod_raca_cor());
            statement.setDate(6, registrado.getData_nascimento());
            statement.setInt(7, registrado.getCod_sexo());

            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("create_registrado catch: " + error);
        }
    }

    // TODO colocar uma transação no create
    @Override
    public void create(Registrado registrado) throws SQLException {
        create_registro(registrado.getNascimento().getRegistro());
        create_nascimento(registrado.getNascimento());
        create_registrado(registrado);
    }

    @Override
    public Registrado read(Integer id_registrado) throws SQLException {
        Registrado registrado = new Registrado();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id_registrado);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    // Creating Nascimento
                    if(registrado.getNascimento() == null){
                        Nascimento nascimento = new Nascimento();
                        registrado.setNascimento(nascimento);
                    }
                    // Creating Registro
                    if(registrado.getNascimento().getRegistro() == null){
                        Registro registro = new Registro();
                        registrado.getNascimento().setRegistro(registro);
                    }
                    // Setting attributes Registro
                    registrado.getNascimento().getRegistro().setId_registro(result.getInt("id_registro_nasc"));
                    registrado.getNascimento().getRegistro().setTipo_registro(result.getString("tipo_registro_nasc"));
                    registrado.getNascimento().getRegistro().setAno_registro(result.getInt("ano_registro_nasc"));

                    // Setting attributes Nascimento
                    registrado.getNascimento().setHora_nascimento(result.getTime("hora_nascimento"));
                    registrado.getNascimento().setCod_tipo_parto(result.getInt("cod_tipo_parto"));
                    registrado.getNascimento().setCod_raca_cor_mae(result.getInt("cod_raca_cor_mae"));
                    registrado.getNascimento().setIdade_mae(result.getInt("idade_mae"));
                    registrado.getNascimento().setCod_estado_civil_mae(result.getInt("cod_estado_civil_mae"));
                    registrado.getNascimento().setPeso_nascido_vivo(result.getInt("peso_nascido_vivo"));

                    // Setting attributes Registrado
                    registrado.setId_registrado(id_registrado);
                    registrado.setCod_municipio_nasc(result.getInt("cod_municipio_nasc"));
                    registrado.setCod_sexo(result.getInt("cod_sexo"));
                    registrado.setCod_raca_cor(result.getInt("cod_raca_cor"));
                    registrado.setData_nascimento(result.getDate("data_nascimento"));
                } else {
                    throw new SQLException("Erro ao visualizar: registrado não encontrado em PgNascimentoDAO.");
                }
            }
        } catch (SQLException error) {
            logger.error("read catch: " + error);
        }

        return registrado;
    }

    private void update_nascimento(Nascimento nascimento) throws SQLException{
        // TODO deletar isso depois se não for útil
        // poderia fazer aqui ele escolher qual query usar, se eu tivesse múltiplos
//        String query;
//
//        if(obito.getCod_tipo_obito() == null)
//
//        if ((user.getSenha() == null) || (user.getSenha().isBlank())) {
//            if ((user.getAvatar() == null) || (user.getAvatar().isBlank()))
//                query = UPDATE_QUERY;
//            else
//                query = UPDATE_WITH_AVATAR_QUERY;

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_NASCIMENTO)) {
            statement.setTime(1, nascimento.getHora_nascimento());
            statement.setInt(2, nascimento.getCod_tipo_parto());
            statement.setInt(3, nascimento.getCod_raca_cor_mae());
            statement.setInt(4, nascimento.getIdade_mae());
            statement.setInt(5, nascimento.getCod_estado_civil_mae());
            statement.setInt(6, nascimento.getPeso_nascido_vivo());

            statement.setInt(7, nascimento.getRegistro().getId_registro());
            statement.setString(8, nascimento.getRegistro().getTipo_registro());
            statement.setInt(9, nascimento.getRegistro().getAno_registro());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Error to update: nascimento registrado não encontrado em PgNascimentoDAO.");
            }
        } catch (SQLException error) {
            logger.error("update_nascimento catch in PgNascimentoDAO: " + error);
        }
    }

    private void update_registrado(Registrado registrado) throws SQLException{
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_REGISTRADO)) {
            statement.setInt(1, registrado.getCod_municipio_nasc());
            statement.setInt(2, registrado.getCod_raca_cor());
            statement.setDate(3, registrado.getData_nascimento());
            statement.setInt(4, registrado.getCod_sexo());
            statement.setInt(5, registrado.getNascimento().getRegistro().getId_registro());
            statement.setString(6, registrado.getNascimento().getRegistro().getTipo_registro());
            statement.setInt(7, registrado.getNascimento().getRegistro().getAno_registro());

            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("update_registrado catch: " + error);
        }
    }

    @Override
    public void update(Registrado registrado) throws SQLException {
        update_nascimento(registrado.getNascimento());
        update_registrado(registrado);
    }

    @Override
    public void delete(Integer id_registrado) throws SQLException {
        Registrado registrado = read(id_registrado);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id_registrado);
            statement.setInt(2, registrado.getNascimento().getRegistro().getId_registro());
            statement.setString(3, registrado.getNascimento().getRegistro().getTipo_registro());
            statement.setInt(4, registrado.getNascimento().getRegistro().getAno_registro());
            statement.setInt(5, registrado.getNascimento().getRegistro().getId_registro());
            statement.setString(6, registrado.getNascimento().getRegistro().getTipo_registro());
            statement.setInt(7, registrado.getNascimento().getRegistro().getAno_registro());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Error to delete: nascimento registrado não encontrado em PgNascimentoDAO.");
            }
        } catch (SQLException error) {
            logger.error("delete catch: " + error);
        }
    }

    @Override
    public List<Registrado> all() throws SQLException {
        List<Registrado> registradoList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Registrado registrado = new Registrado();
                // TODO colocar outras infos que vou querer nessa lista
                registrado.getNascimento().getRegistro().setId_registro(result.getInt("id_registro"));
                registrado.getNascimento().getRegistro().setTipo_registro(result.getString("tipo_registro"));
                registrado.getNascimento().getRegistro().setAno_registro(result.getInt("ano_registro"));

                registradoList.add(registrado);
            }
        } catch (SQLException error) {
            logger.error("all catch: " + error);
            throw new SQLException("Error listing registrados in PgNascimentoDAO.");
        }

        return registradoList;
    }

    @Override
    public RegistradoDAO getRegistradoDAO() {
        return null;
    }

    // TODO delete this file
    @Override
    public List<Registrado> all_obito() {
        return null;
    }

    @Override
    public List<Registrado> all_nascimento() {
        return null;
    }
}
