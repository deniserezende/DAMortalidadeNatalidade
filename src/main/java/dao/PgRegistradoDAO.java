package dao;

import model.Nascimento;
import model.Obito;
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

// TODO not tested
public class PgRegistradoDAO implements RegistradoDAO {

    private final Connection connection;
    protected static final Logger logger = LogManager.getLogger(PgRegistradoDAO.class);

    private static final String CREATE_REGISTRO =
            "INSERT INTO \"DAMortalidade_Natalidade\".\"REGISTRO\"(id_registro, tipo_registro, ano_registro) " +
                    "VALUES(?, ?, ?);";
    private static final String CREATE_OBITO =
            "INSERT INTO \"DAMortalidade_Natalidade\".\"OBITO\"(cod_tipo_obito, data_obito, hora_obito, " +
                    "idade_falecido, cod_est_civ_falecido, cod_local_obito, cod_municipio_obito, cod_circ_obito, " +
                    "id_registro, tipo_registro, ano_registro) " +
                    "VALUES(?, to_date(?, 'ddmmyyyy'), ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String CREATE_NASCIMENTO =
            "INSERT INTO \"DAMortalidade_Natalidade\".\"NASCIMENTO\"(id_registro, tipo_registro, ano_registro," +
                    "hora_nascimento, cod_tipo_parto, cod_raca_cor_mae, idade_mae, cod_estado_civil_mae, peso_nascido_vivo)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    //to_date(?, 'ddmmyyyy')

    // TODO verificar se preciso mesmo desse to_date()
    private static final String CREATE_REGISTRADO_OBT =
            "INSERT INTO \"DAMortalidade_Natalidade\".\"REGISTRADO\"(id_registro_obt, tipo_registro_obt, ano_registro_obt" +
                    "cod_municipio_nasc, cod_raca_cor, data_nascimento, cod_sexo) " +
                    "VALUES(?, ?, ?, ?, to_date(?, 'ddmmyyyy'), ?);";

    private static final String CREATE_REGISTRADO_NASC =
            "INSERT INTO \"DAMortalidade_Natalidade\".\"REGISTRADO\"(id_registro_nasc, tipo_registro_nasc, ano_registro_nasc" +
                    "cod_municipio_nasc, cod_raca_cor, data_nascimento, cod_sexo) " +
                    "VALUES(?, ?, ?, ?, to_date(?, 'ddmmyyyy'), ?);";

    private static final String READ_QUERY_OBT =
            "SELECT * FROM \"DAMortalidade_Natalidade\".\"REGISTRADO\", \"DAMortalidade_Natalidade\".\"OBITO\" " +
                    "WHERE \"DAMortalidade_Natalidade\".\"OBITO\".id_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".id_registro_obt\n " +
                    "AND \"DAMortalidade_Natalidade\".\"OBITO\".tipo_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".tipo_registro_obt\n " +
                    "AND \"DAMortalidade_Natalidade\".\"OBITO\".ano_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".ano_registro_obt\n " +
                    "AND id_registrado = ?;";

    private static final String READ_QUERY_NASC =
            "SELECT * FROM \"DAMortalidade_Natalidade\".\"REGISTRADO\", \"DAMortalidade_Natalidade\".\"NASCIMENTO\" " +
                    "WHERE \"DAMortalidade_Natalidade\".\"NASCIMENTO\".id_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".id_registro_nasc\n " +
                    "AND \"DAMortalidade_Natalidade\".\"NASCIMENTO\".tipo_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".tipo_registro_nasc\n " +
                    "AND \"DAMortalidade_Natalidade\".\"NASCIMENTO\".ano_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".ano_registro_nasc\n " +
                    "AND id_registrado = ?;";
    private static final String UPDATE_OBITO =
            "UPDATE \"DAMortalidade_Natalidade\".\"OBITO\"" +
                    "SET cod_tipo_obito = ?, data_obito = ?, hora_obito = ?, idade_falecido = ?, " +
                    "cod_est_civ_falecido = ?, cod_local_obito = ?, cod_municipio_obito = ?, cod_circ_obito = ?" +
                    "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?;";

    private static final String UPDATE_NASCIMENTO =
            "UPDATE \"DAMortalidade_Natalidade\".\"NASCIMENTO\"" +
                    "SET hora_nascimento = ?, cod_tipo_parto = ?, cod_raca_cor_mae = ?, idade_mae = ?, cod_estado_civil_mae = ?, peso_nascido_vivo = ?" +
                    "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?;";

    private static final String UPDATE_REGISTRADO =
            "UPDATE \"DAMortalidade_Natalidade\".\"REGISTRADO\"" +
                    "SET cod_municipio_nasc = ?, cod_raca_cor = ?, data_nascimento = ?, cod_sexo = ?, " +
                    "WHERE id_registro_obt = ? AND tipo_registro_obt = ? AND ano_registro_obt = ?;";

    private static final String DELETE_QUERY_OBT =
            "DELETE FROM \"DAMortalidade_Natalidade\".\"REGISTRADO\" +\n" +
                    "WHERE id_registrado = ?; \n " +
                    "DELETE FROM \"DAMortalidade_Natalidade\".\"OBITO\" \" +\n" +
                    "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?; \n " +
                    "DELETE FROM \"DAMortalidade_Natalidade\".\"REGISTRO\" \" +\n" +
                    "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?; \n";

    private static final String DELETE_QUERY_NASC =
            "DELETE FROM \"DAMortalidade_Natalidade\".\"REGISTRADO\" +\n" +
                    "WHERE id_registrado = ?; \n " +
                    "DELETE FROM \"DAMortalidade_Natalidade\".\"NASCIMENTO\" \" +\n" +
                    "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?; \n " +
                    "DELETE FROM \"DAMortalidade_Natalidade\".\"REGISTRO\" \" +\n" +
                    "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?; \n";

    private static final String ALL_QUERY_OBT =
            "SELECT *\n" +
                    "FROM \"DAMortalidade_Natalidade\".\"OBITO\", \"DAMortalidade_Natalidade\".\"REGISTRADO\"\n" +
                    "WHERE \"DAMortalidade_Natalidade\".\"OBITO\".id_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".id_registro_obt\n" +
                    "AND \"DAMortalidade_Natalidade\".\"OBITO\".tipo_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".tipo_registro_obt" +
                    "AND \"DAMortalidade_Natalidade\".\"OBITO\".ano_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".ano_registro_obt\n ";

    private static final String ALL_QUERY_NASC =
            "SELECT *\n" +
                    "FROM \"DAMortalidade_Natalidade\".\"NASCIMENTO\", \"DAMortalidade_Natalidade\".\"REGISTRADO\"\n" +
                    "WHERE \"DAMortalidade_Natalidade\".\"NASCIMENTO\".id_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".id_registro_nasc\n" +
                    "AND \"DAMortalidade_Natalidade\".\"NASCIMENTO\".tipo_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".tipo_registro_nasc;" +
                    "AND \"DAMortalidade_Natalidade\".\"NASCIMENTO\".ano_registro = " +
                    "\"DAMortalidade_Natalidade\".\"REGISTRADO\".ano_registro_nasc\n ";

    public PgRegistradoDAO(Connection connection) {
        this.connection = connection;
    }

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

    private void create_obito(Obito obito) throws SQLException{
        try (PreparedStatement statement = connection.prepareStatement(CREATE_OBITO)) {
            statement.setInt(1, obito.getCod_tipo_obito());
            statement.setDate(2, obito.getData_obito());
            statement.setTime(3, obito.getHora_obito());
            statement.setInt(4, obito.getIdade_falecido());
            statement.setInt(5, obito.getCod_est_civ_falecido());
            statement.setInt(6, obito.getCod_local_obito());
            statement.setInt(7, obito.getCod_municipio_obito());
            statement.setInt(8, obito.getCod_circ_obito());
            statement.setInt(9, obito.getRegistro().getId_registro());
            statement.setString(10, obito.getRegistro().getTipo_registro());
            statement.setInt(11, obito.getRegistro().getAno_registro());

            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("create_obito catch: " + error);
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
        try (PreparedStatement statement = connection.prepareStatement(CREATE_REGISTRADO_OBT)) {
            statement.setInt(1, registrado.getObito().getRegistro().getId_registro());
            statement.setString(2, registrado.getObito().getRegistro().getTipo_registro());
            statement.setInt(3, registrado.getObito().getRegistro().getAno_registro());
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
        create_registro(registrado.getObito().getRegistro());
        if(registrado.getObito() != null){
            create_obito(registrado.getObito());
        }
        else{
            create_nascimento(registrado.getNascimento());
        }
        create_registrado(registrado);
    }


    private Registrado read_obito(Integer id_registrado) throws SQLException{
        Registrado registrado = new Registrado();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY_OBT)) {
            statement.setInt(1, id_registrado);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    // Creating Obito
                    if(registrado.getObito() == null){
                        Obito obito = new Obito();
                        registrado.setObito(obito);
                    }
                    // Creating Registro
                    if(registrado.getObito().getRegistro() == null){
                        Registro registro = new Registro();
                        registrado.getObito().setRegistro(registro);
                    }
                    // Setting attributes Registro
                    registrado.getObito().getRegistro().setId_registro(result.getInt("id_registro_obt"));
                    registrado.getObito().getRegistro().setTipo_registro(result.getString("tipo_registro_obt"));
                    registrado.getObito().getRegistro().setAno_registro(result.getInt("ano_registro_obt"));

                    // Setting attributes Obito
                    registrado.getObito().setData_obito(result.getDate("data_obito"));
                    registrado.getObito().setCod_circ_obito(result.getInt("cod_circ_obito"));
                    registrado.getObito().setHora_obito(result.getTime("hora_obito"));
                    registrado.getObito().setCod_municipio_obito(result.getInt("cod_municipio_obito"));
                    registrado.getObito().setCod_local_obito(result.getInt("cod_local_obito") );
                    registrado.getObito().setCod_est_civ_falecido(result.getInt("cod_est_civ_falecido"));
                    registrado.getObito().setIdade_falecido(result.getInt("idade_falecido"));
                    registrado.getObito().setCod_tipo_obito(result.getInt("cod_tipo_obito"));

                    // Setting attributes Registrado
                    registrado.setId_registrado(id_registrado);
                    registrado.setCod_municipio_nasc(result.getInt("cod_municipio_nasc"));
                    registrado.setCod_sexo(result.getInt("cod_sexo"));
                    registrado.setCod_raca_cor(result.getInt("cod_raca_cor"));
                    registrado.setData_nascimento(result.getDate("data_nascimento"));
                } else {
                    throw new SQLException("Erro ao visualizar: registrado não encontrado.");
                }
            }
        } catch (SQLException error) {
            logger.error("read catch: " + error);
        }

        return registrado;
    }

    private Registrado read_nascimento(Integer id_registrado) throws SQLException{
        Registrado registrado = new Registrado();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY_NASC)) {
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

    // TODO não sei se tá certo fazer assim
    @Override
    public Registrado read(Integer id_registrado) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY_OBT)) {
            statement.setInt(1, id_registrado);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return read_obito(id_registrado);
                }
                else{
                    return read_nascimento(id_registrado);
                }
            }
        }
    }

    private void update_obito(Obito obito) throws SQLException{
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

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_OBITO)) {
            statement.setInt(1, obito.getCod_tipo_obito());
            statement.setDate(2, obito.getData_obito());
            statement.setTime(3, obito.getHora_obito());
            statement.setInt(4, obito.getIdade_falecido());
            statement.setInt(5, obito.getCod_est_civ_falecido());
            statement.setInt(6, obito.getCod_local_obito());
            statement.setInt(7, obito.getCod_municipio_obito());
            statement.setInt(8, obito.getCod_circ_obito());
            statement.setInt(9, obito.getRegistro().getId_registro());
            statement.setString(10, obito.getRegistro().getTipo_registro());
            statement.setInt(11, obito.getRegistro().getAno_registro());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Error to update: obito registrado não encontrado.");
            }
        } catch (SQLException error) {
            logger.error("update_obito catch: " + error);
        }
    }

    private void update_registrado(Registrado registrado) throws SQLException{
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_REGISTRADO)) {
            statement.setInt(1, registrado.getCod_municipio_nasc());
            statement.setInt(2, registrado.getCod_raca_cor());
            statement.setDate(3, registrado.getData_nascimento());
            statement.setInt(4, registrado.getCod_sexo());
            statement.setInt(5, registrado.getObito().getRegistro().getId_registro());
            statement.setString(6, registrado.getObito().getRegistro().getTipo_registro());
            statement.setInt(7, registrado.getObito().getRegistro().getAno_registro());

            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("update_registrado catch: " + error);
        }
    }

    @Override
    public void update(Registrado registrado) throws SQLException {
        update_obito(registrado.getObito());
        update_registrado(registrado);
    }

    @Override
    public void delete(Integer id_registrado) throws SQLException {
        Registrado registrado = read(id_registrado);
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY_OBT)) {
            statement.setInt(1, id_registrado);
            statement.setInt(2, registrado.getObito().getRegistro().getId_registro());
            statement.setString(3, registrado.getObito().getRegistro().getTipo_registro());
            statement.setInt(4, registrado.getObito().getRegistro().getAno_registro());
            statement.setInt(5, registrado.getObito().getRegistro().getId_registro());
            statement.setString(6, registrado.getObito().getRegistro().getTipo_registro());
            statement.setInt(7, registrado.getObito().getRegistro().getAno_registro());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Error to delete: obito registrado não encontrado.");
            }
        } catch (SQLException error) {
            logger.error("delete catch: " + error);
        }
    }

    @Override
    public List<Registrado> all() throws SQLException {
        List<Registrado> registradoList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY_OBT);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Registrado registrado = new Registrado();
                // TODO colocar outras infos que vou querer nessa lista
                registrado.getObito().getRegistro().setId_registro(result.getInt("id_registro"));
                registrado.getObito().getRegistro().setTipo_registro(result.getString("tipo_registro"));
                registrado.getObito().getRegistro().setAno_registro(result.getInt("ano_registro"));

                registradoList.add(registrado);
            }
        } catch (SQLException error) {
            logger.error("all catch: " + error);
            throw new SQLException("Error listing registrados.");
        }

        return registradoList;
    }

    @Override
    public RegistradoDAO getRegistradoDAO() {
        return null;
    }
}