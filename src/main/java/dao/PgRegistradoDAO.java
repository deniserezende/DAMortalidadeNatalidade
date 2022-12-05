package dao;

import com.google.gson.Gson;
import model.Nascimento;
import model.Obito;
import model.Registrado;
import model.Registro;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings({"SqlDialectInspection", "Convert2Diamond"})
public class PgRegistradoDAO implements RegistradoDAO {

    private final Connection connection;
    protected static final Logger logger = LogManager.getLogger(PgRegistradoDAO.class);

    private static final String CREATE_REGISTRO =
    "INSERT INTO \"DAMortalidade_Natalidade\".\"REGISTRO\"(id_registro, tipo_registro, ano_registro) " +
            "VALUES(?, ?, ?);";
    private static final String CREATE_OBITO =
    "INSERT INTO \"DAMortalidade_Natalidade\".\"OBITO\"(id_registro, tipo_registro, ano_registro," +
            "cod_tipo_obito, data_obito, hora_obito, " +
            "idade_falecido, cod_est_civ_falecido, cod_local_obito, cod_municipio_obito, cod_circ_obito) " +
            "VALUES(?, ?, ?, ?, to_date(?, 'yyyy-mm-dd'), ?, ?, ?, ?, ?, ?);";

    private static final String CREATE_NASCIMENTO =
    "INSERT INTO \"DAMortalidade_Natalidade\".\"NASCIMENTO\"(id_registro, tipo_registro, ano_registro," +
            "hora_nascimento, cod_tipo_parto, cod_raca_cor_mae, idade_mae, cod_estado_civil_mae, peso_nascido_vivo)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String CREATE_REGISTRADO_OBT =
    "INSERT INTO \"DAMortalidade_Natalidade\".\"REGISTRADO\"(id_registro_obt, tipo_registro_obt, ano_registro_obt," +
            "cod_municipio_nasc, cod_raca_cor, data_nascimento, cod_sexo) " +
            "VALUES(?, ?, ?, ?, ?, to_date(?, 'yyyy-mm-dd'), ?);";

    private static final String CREATE_REGISTRADO_NASC =
    "INSERT INTO \"DAMortalidade_Natalidade\".\"REGISTRADO\"(id_registro_nasc, tipo_registro_nasc, ano_registro_nasc," +
            "cod_municipio_nasc, cod_raca_cor, data_nascimento, cod_sexo) " +
            "VALUES(?, ?, ?, ?, ?, to_date(?, 'yyyy-mm-dd'), ?);";

    private static final String READ_QUERY_OBT =
    "SELECT * FROM \"DAMortalidade_Natalidade\".\"REGISTRADO\", \"DAMortalidade_Natalidade\".\"OBITO\" " +
            "WHERE \"DAMortalidade_Natalidade\".\"OBITO\".id_registro = " +
            "\"DAMortalidade_Natalidade\".\"REGISTRADO\".id_registro_obt\n " +
            "AND \"DAMortalidade_Natalidade\".\"OBITO\".tipo_registro = " +
            "\"DAMortalidade_Natalidade\".\"REGISTRADO\".tipo_registro_obt\n " +
            "AND \"DAMortalidade_Natalidade\".\"OBITO\".ano_registro = " +
            "\"DAMortalidade_Natalidade\".\"REGISTRADO\".ano_registro_obt\n " +
            "AND id_registro_obt = ? AND tipo_registro_obt = ? AND ano_registro_obt = ?;";

    private static final String READ_QUERY_NASC =
    "SELECT * FROM \"DAMortalidade_Natalidade\".\"REGISTRADO\", \"DAMortalidade_Natalidade\".\"NASCIMENTO\" " +
            "WHERE \"DAMortalidade_Natalidade\".\"NASCIMENTO\".id_registro = " +
            "\"DAMortalidade_Natalidade\".\"REGISTRADO\".id_registro_nasc\n " +
            "AND \"DAMortalidade_Natalidade\".\"NASCIMENTO\".tipo_registro = " +
            "\"DAMortalidade_Natalidade\".\"REGISTRADO\".tipo_registro_nasc\n " +
            "AND \"DAMortalidade_Natalidade\".\"NASCIMENTO\".ano_registro = " +
            "\"DAMortalidade_Natalidade\".\"REGISTRADO\".ano_registro_nasc\n " +
            "AND id_registro_nasc = ? AND tipo_registro_nasc = ? AND ano_registro_nasc = ?;";

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
            "SET cod_municipio_nasc = ?, cod_raca_cor = ?, data_nascimento = ?, cod_sexo = ? " +
            "WHERE id_registro_obt = ? AND tipo_registro_obt = ? AND ano_registro_obt = ?;";

    private static final String DELETE_QUERY_OBT =
    "DELETE FROM \"DAMortalidade_Natalidade\".\"REGISTRADO\" +\n" +
            "WHERE id_registrado = ?; \n " +
            "DELETE FROM \"DAMortalidade_Natalidade\".\"OBITO\"" +
            "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?; \n " +
            "DELETE FROM \"DAMortalidade_Natalidade\".\"REGISTRO\"" +
            "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?; \n";

    private static final String DELETE_QUERY_NASC =
    "DELETE FROM \"DAMortalidade_Natalidade\".\"REGISTRADO\" +\n" +
            "WHERE id_registrado = ?; \n " +
            "DELETE FROM \"DAMortalidade_Natalidade\".\"NASCIMENTO\"" +
            "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?; \n " +
            "DELETE FROM \"DAMortalidade_Natalidade\".\"REGISTRO\"" +
            "WHERE id_registro = ? AND tipo_registro = ? AND ano_registro = ?; \n";

    private static final String ALL_QUERY_OBT =
    "SELECT * FROM \"DAMortalidade_Natalidade\".\"OBITO\", \"DAMortalidade_Natalidade\".\"REGISTRADO\" " +
            "WHERE \"DAMortalidade_Natalidade\".\"OBITO\".id_registro = \"DAMortalidade_Natalidade\".\"REGISTRADO\".id_registro_obt " +
            "AND \"DAMortalidade_Natalidade\".\"OBITO\".tipo_registro = \"DAMortalidade_Natalidade\".\"REGISTRADO\".tipo_registro_obt " +
            "AND \"DAMortalidade_Natalidade\".\"OBITO\".ano_registro = \"DAMortalidade_Natalidade\".\"REGISTRADO\".ano_registro_obt;";

    private static final String ALL_QUERY_NASC =
    "SELECT * FROM \"DAMortalidade_Natalidade\".\"NASCIMENTO\", \"DAMortalidade_Natalidade\".\"REGISTRADO\" " +
        "WHERE \"DAMortalidade_Natalidade\".\"NASCIMENTO\".id_registro = \"DAMortalidade_Natalidade\".\"REGISTRADO\".id_registro_nasc " +
        "AND \"DAMortalidade_Natalidade\".\"NASCIMENTO\".tipo_registro = \"DAMortalidade_Natalidade\".\"REGISTRADO\".tipo_registro_nasc " +
        "AND \"DAMortalidade_Natalidade\".\"NASCIMENTO\".ano_registro = \"DAMortalidade_Natalidade\".\"REGISTRADO\".ano_registro_nasc;";

    private static final String QTD_REGISTROS_POR_ANO =
    "SELECT COUNT(id_registro) AS qtd_registros, ano_registro, tipo_registro FROM \"DAMortalidade_Natalidade\".\"REGISTRO\"\n" +
            "GROUP BY ano_registro, tipo_registro\n" +
            "ORDER BY ano_registro;";

    private static final String IDADES_MAES_POR_ANO =
            "SELECT MIN(idade_mae) AS menor_idade_mae, \n" +
                    "\t MAX(idade_mae) AS maior_idade_mae, \n" +
                    "\t ROUND(AVG(idade_mae), 0) AS media_idade_mae,\n" +
                    "\t ano_registro\n" +
                    "FROM \"DAMortalidade_Natalidade\".\"NASCIMENTO\"\n" +
                    "GROUP BY ano_registro\n" +
                    "ORDER BY ano_registro;";

    private static final String OBITOS_POR_SEXO_POR_ANO =
            "SELECT COUNT(id_registro) AS qtd_registros, ano_registro, tipo_registro, sexo \n" +
                    "FROM \"DAMortalidade_Natalidade\".\"REGISTRADO\", \"DAMortalidade_Natalidade\".\"OBITO\", \"DAMortalidade_Natalidade\".\"SEXO\"\n" +
                    "WHERE \"DAMortalidade_Natalidade\".\"OBITO\".id_registro = \"DAMortalidade_Natalidade\".\"REGISTRADO\".id_registro_obt\n" +
                    "\tAND \"DAMortalidade_Natalidade\".\"OBITO\".tipo_registro = \"DAMortalidade_Natalidade\".\"REGISTRADO\".tipo_registro_obt\n" +
                    "\tAND \"DAMortalidade_Natalidade\".\"OBITO\".ano_registro = \"DAMortalidade_Natalidade\".\"REGISTRADO\".ano_registro_obt\n" +
                    "\tAND \"DAMortalidade_Natalidade\".\"SEXO\".cod_sexo = \"DAMortalidade_Natalidade\".\"REGISTRADO\".cod_sexo\n" +
                    "GROUP BY ano_registro, tipo_registro, sexo\n" +
                    "ORDER BY ano_registro;";

    private static final String OBITOS_POR_ANO =
            "SELECT COUNT(id_registro) AS qtd_registros, ano_registro, tipo_registro \n" +
                    "FROM \"DAMortalidade_Natalidade\".\"REGISTRADO\", \"DAMortalidade_Natalidade\".\"OBITO\"\n" +
                    "WHERE \"DAMortalidade_Natalidade\".\"OBITO\".id_registro = \"DAMortalidade_Natalidade\".\"REGISTRADO\".id_registro_obt\n" +
                    "\tAND \"DAMortalidade_Natalidade\".\"OBITO\".tipo_registro = \"DAMortalidade_Natalidade\".\"REGISTRADO\".tipo_registro_obt\n" +
                    "\tAND \"DAMortalidade_Natalidade\".\"OBITO\".ano_registro = \"DAMortalidade_Natalidade\".\"REGISTRADO\".ano_registro_obt\n" +
                    "GROUP BY ano_registro, tipo_registro\n" +
                    "ORDER BY ano_registro;";


    public PgRegistradoDAO(Connection connection) {
        this.connection = connection;
    }

    private void create_registro(Registro registro) {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_REGISTRO)) {
            statement.setInt(1, registro.getId_registro());
            statement.setString(2, registro.getTipo_registro());
            statement.setInt(3, registro.getAno_registro());

            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("create_registro catch: " + error);
        }
    }

    private void create_obito(Obito obito) {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_OBITO)) {
            statement.setInt(1, obito.getRegistro().getId_registro());
            statement.setString(2, obito.getRegistro().getTipo_registro());
            statement.setInt(3, obito.getRegistro().getAno_registro());
            if(obito.getCod_tipo_obito() != null) statement.setInt(4, obito.getCod_tipo_obito());
            else statement.setNull(4, java.sql.Types.NULL);
            if(obito.getData_obito() != null) statement.setDate(5, obito.getData_obito());
            else statement.setNull(5, java.sql.Types.NULL);
            if(obito.getHora_obito() != null) statement.setTime(6, obito.getHora_obito());
            else statement.setNull(6, java.sql.Types.NULL);
            if(obito.getIdade_falecido() != null) statement.setInt(7, obito.getIdade_falecido());
            else statement.setNull(7, java.sql.Types.NULL);
            if(obito.getCod_est_civ_falecido() != null) statement.setInt(8, obito.getCod_est_civ_falecido());
            else statement.setNull(8, java.sql.Types.NULL);
            if(obito.getCod_local_obito() != null) statement.setInt(9, obito.getCod_local_obito());
            else statement.setNull(9, java.sql.Types.NULL);
            if(obito.getCod_municipio_obito() != null) statement.setInt(10, obito.getCod_municipio_obito());
            else statement.setNull(10, java.sql.Types.NULL);
            if(obito.getCod_circ_obito() != null) statement.setInt(11, obito.getCod_circ_obito());
            else statement.setNull(11, java.sql.Types.NULL);


            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("create_obito catch: " + error);
        }
    }

    private void create_nascimento(Nascimento nascimento) {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_NASCIMENTO)) {
            statement.setInt(1, nascimento.getRegistro().getId_registro());
            statement.setString(2, nascimento.getRegistro().getTipo_registro());
            statement.setInt(3, nascimento.getRegistro().getAno_registro());
            if(nascimento.getHora_nascimento() != null) statement.setTime(4, nascimento.getHora_nascimento());
            else statement.setNull(4, java.sql.Types.NULL);
            if(nascimento.getCod_tipo_parto() != null) statement.setInt(5, nascimento.getCod_tipo_parto());
            else statement.setNull(5, java.sql.Types.NULL);
            if(nascimento.getCod_raca_cor_mae() != null) statement.setInt(6, nascimento.getCod_raca_cor_mae());
            else statement.setNull(6, java.sql.Types.NULL);
            if(nascimento.getIdade_mae() != null) statement.setInt(7, nascimento.getIdade_mae());
            else statement.setNull(7, java.sql.Types.NULL);
            if(nascimento.getCod_estado_civil_mae() != null) statement.setInt(8, nascimento.getCod_estado_civil_mae());
            else statement.setNull(8, java.sql.Types.NULL);
            if(nascimento.getPeso_nascido_vivo() != null) statement.setInt(9, nascimento.getPeso_nascido_vivo());
            else statement.setNull(9, java.sql.Types.NULL);

            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("create_nascimento catch: " + error);
        }
    }

    private void create_registrado_obt(Registrado registrado) {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_REGISTRADO_OBT)) {
            statement.setInt(1, registrado.getObito().getRegistro().getId_registro());
            statement.setString(2, registrado.getObito().getRegistro().getTipo_registro());
            statement.setInt(3, registrado.getObito().getRegistro().getAno_registro());
            if(registrado.getCod_municipio_nasc() != null) statement.setInt(4, registrado.getCod_municipio_nasc());
            else statement.setNull(4, java.sql.Types.NULL);
            if(registrado.getCod_raca_cor() != null) statement.setInt(5, registrado.getCod_raca_cor());
            else statement.setNull(5, java.sql.Types.NULL);
            if(registrado.getData_nascimento() != null) statement.setDate(6, registrado.getData_nascimento());
            else statement.setNull(6, java.sql.Types.NULL);
            if(registrado.getCod_sexo() != null) statement.setInt(7, registrado.getCod_sexo());
            else statement.setNull(7, java.sql.Types.NULL);

            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("create_registrado catch: " + error);
        }
    }

    private void create_registrado_nasc(Registrado registrado) {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_REGISTRADO_NASC)) {
            statement.setInt(1, registrado.getNascimento().getRegistro().getId_registro());
            statement.setString(2, registrado.getNascimento().getRegistro().getTipo_registro());
            statement.setInt(3, registrado.getNascimento().getRegistro().getAno_registro());
            if(registrado.getCod_municipio_nasc() != null) statement.setInt(4, registrado.getCod_municipio_nasc());
            else statement.setNull(4, java.sql.Types.NULL);
            if(registrado.getCod_raca_cor() != null) statement.setInt(5, registrado.getCod_raca_cor());
            else statement.setNull(5, java.sql.Types.NULL);
            if(registrado.getData_nascimento() != null) statement.setDate(6, registrado.getData_nascimento());
            else statement.setNull(6, java.sql.Types.NULL);
            if(registrado.getCod_sexo() != null) statement.setInt(7, registrado.getCod_sexo());
            else statement.setNull(7, java.sql.Types.NULL);

            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("create_registrado catch: " + error);
        }
    }

    // TODO colocar uma transação no create (?)
    @Override
    public void create(Registrado registrado){
        if(registrado.getObito() != null){
            try{
                create_registro(registrado.getObito().getRegistro());
                create_obito(registrado.getObito());
                create_registrado_obt(registrado);
            }
            catch (Exception error){
                logger.error("create catch mortalidade: " + error);
            }

        }
        else{
            try{
                create_registro(registrado.getNascimento().getRegistro());
                create_nascimento(registrado.getNascimento());
                create_registrado_nasc(registrado);
            }
            catch (Exception error){
                logger.error("create catch nascimento: " + error);
            }
        }
    }

    @Override
    public Registrado read_obito(Integer id_registro, String tipo_registro, Integer ano_registro) {
        Registrado registrado = new Registrado();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY_OBT)) {
            statement.setInt(1, id_registro);
            statement.setString(2, tipo_registro);
            statement.setInt(3, ano_registro);

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
                    registrado.setId_registrado(result.getInt("id_registrado"));
                    registrado.setCod_municipio_nasc(result.getInt("cod_municipio_nasc"));
                    registrado.setCod_sexo(result.getInt("cod_sexo"));
                    registrado.setCod_raca_cor(result.getInt("cod_raca_cor"));
                    registrado.setData_nascimento(result.getDate("data_nascimento"));
                } else {
                    logger.error("Erro ao visualizar: registrado não encontrado.");
                    return null;
                }
            }
        } catch (SQLException error) {
            logger.error("read_obito catch: " + error);
            return null;
        }
        return registrado;
    }

    @Override
    public Registrado read_nascimento(Integer id_registro, String tipo_registro, Integer ano_registro) {
        Registrado registrado = new Registrado();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY_NASC)) {
            statement.setInt(1, id_registro);
            statement.setString(2, tipo_registro);
            statement.setInt(3, ano_registro);

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
                    registrado.setId_registrado(result.getInt("id_registrado"));
                    registrado.setCod_municipio_nasc(result.getInt("cod_municipio_nasc"));
                    registrado.setCod_sexo(result.getInt("cod_sexo"));
                    registrado.setCod_raca_cor(result.getInt("cod_raca_cor"));
                    registrado.setData_nascimento(result.getDate("data_nascimento"));
                } else {
                    logger.error("Erro ao visualizar: registrado não encontrado.");
                    return null;
                }
            }
        } catch (SQLException error) {
            logger.error("read_nascimento catch: " + error);
            return null;
        }

        return registrado;
    }

    @Override
    public Registrado read(Integer id_registrado) {
        return null;
    }

    private void update_obito(Obito obito) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_OBITO)) {
            if(obito.getCod_tipo_obito() != null) statement.setInt(1, obito.getCod_tipo_obito());
            else statement.setNull(1, java.sql.Types.NULL);
            statement.setDate(2, obito.getData_obito());
            if(obito.getHora_obito() != null) statement.setTime(3, obito.getHora_obito());
            else statement.setNull(3, java.sql.Types.NULL);
            if(obito.getIdade_falecido() != null) statement.setInt(4, obito.getIdade_falecido());
            else statement.setNull(4, java.sql.Types.NULL);
            if(obito.getCod_est_civ_falecido() != null) statement.setInt(5, obito.getCod_est_civ_falecido());
            else statement.setNull(5, java.sql.Types.NULL);
            if(obito.getCod_local_obito() != null) statement.setInt(6, obito.getCod_local_obito());
            else statement.setNull(6, java.sql.Types.NULL);
            if(obito.getCod_municipio_obito() != null) statement.setInt(7, obito.getCod_municipio_obito());
            else statement.setNull(7, java.sql.Types.NULL);
            if(obito.getCod_circ_obito() != null) statement.setInt(8, obito.getCod_circ_obito());
            else statement.setNull(8, java.sql.Types.NULL);
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

    private void update_nascimento(Nascimento nascimento) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_NASCIMENTO)) {
            if(nascimento.getHora_nascimento() != null) statement.setTime(1, nascimento.getHora_nascimento());
            else statement.setNull(1, java.sql.Types.NULL);
            if(nascimento.getCod_tipo_parto() != null) statement.setInt(2, nascimento.getCod_tipo_parto());
            else statement.setNull(2, java.sql.Types.NULL);
            if(nascimento.getCod_raca_cor_mae() != null) statement.setInt(3, nascimento.getCod_raca_cor_mae());
            else statement.setNull(3, java.sql.Types.NULL);
            if(nascimento.getIdade_mae() != null) statement.setInt(4, nascimento.getIdade_mae());
            else statement.setNull(4, java.sql.Types.NULL);
            if(nascimento.getCod_estado_civil_mae() != null) statement.setInt(5, nascimento.getCod_estado_civil_mae());
            else statement.setNull(5, java.sql.Types.NULL);
            if(nascimento.getPeso_nascido_vivo() != null) statement.setInt(6, nascimento.getPeso_nascido_vivo());
            else statement.setNull(6, java.sql.Types.NULL);

            statement.setInt(7, nascimento.getRegistro().getId_registro());
            statement.setString(8, nascimento.getRegistro().getTipo_registro());
            statement.setInt(9, nascimento.getRegistro().getAno_registro());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Error to update: nascimento registrado não encontrado.");
            }
        } catch (SQLException error) {
            logger.error("update_nascimento catch: " + error);
        }
    }

    private void update_registrado(Registrado registrado) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_REGISTRADO)) {
            if(registrado.getCod_municipio_nasc() != null) statement.setInt(1, registrado.getCod_municipio_nasc());
            else statement.setNull(1, java.sql.Types.NULL);
            if(registrado.getCod_raca_cor() != null) statement.setInt(2, registrado.getCod_raca_cor());
            else statement.setNull(2, java.sql.Types.NULL);
            if(registrado.getData_nascimento() != null) statement.setDate(3, registrado.getData_nascimento());
            else statement.setNull(3, java.sql.Types.NULL);
            if(registrado.getCod_sexo() != null) statement.setInt(4, registrado.getCod_sexo());
            else statement.setNull(4, java.sql.Types.NULL);
            statement.setInt(5, registrado.getObito().getRegistro().getId_registro());
            statement.setString(6, registrado.getObito().getRegistro().getTipo_registro());
            statement.setInt(7, registrado.getObito().getRegistro().getAno_registro());

            statement.executeUpdate();
        } catch (SQLException error) {
            logger.error("update_registrado catch: " + error);
        }
    }

    @Override
    public void update(Registrado registrado){
        if(registrado.getObito() != null){
            update_obito(registrado.getObito());
        }
        else{
            update_nascimento(registrado.getNascimento());
        }
        update_registrado(registrado);
    }

    // TODO delete não testado
    public void delete_nascimento(Registrado registrado){
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY_NASC)) {
            statement.setInt(1, registrado.getId_registrado());
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

    public void delete_obito(Registrado registrado){
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY_OBT)) {
            statement.setInt(1, registrado.getId_registrado());
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
    public void delete(Integer id_registrado){
        Registrado registrado = read(id_registrado);
        if(registrado.getObito() != null){
            delete_obito(registrado);
        }
        else{
            delete_nascimento(registrado);
        }
    }

    @Override
    public List<Registrado> all_nascimento() {
        List<Registrado> registradoList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY_NASC);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {

                Registro registro = new Registro();
                Nascimento nascimento = new Nascimento();
                Registrado registrado = new Registrado();

                //atributos do registro
                registro.setId_registro(result.getInt("id_registro"));
                registro.setTipo_registro(result.getString("tipo_registro"));
                registro.setAno_registro(result.getInt("ano_registro"));

                //atributos do nascimento
                nascimento.setHora_nascimento(result.getTime("hora_nascimento"));
                nascimento.setIdade_mae(result.getInt("idade_mae"));
                nascimento.setPeso_nascido_vivo(result.getInt("peso_nascido_vivo"));
                nascimento.setCod_tipo_parto(result.getInt("cod_tipo_parto"));
                nascimento.setCod_raca_cor_mae(result.getInt("cod_raca_cor_mae"));
                nascimento.setCod_estado_civil_mae(result.getInt("cod_estado_civil_mae"));

                //atributos do registrado
                registrado.setId_registrado(result.getInt("id_registrado"));
                registrado.setData_nascimento(result.getDate("data_nascimento"));
                registrado.setCod_municipio_nasc(result.getInt("cod_municipio_nasc"));
                registrado.setCod_raca_cor(result.getInt("cod_raca_cor"));
                registrado.setCod_sexo(result.getInt("cod_sexo"));

                //vinculando ponteiros
                nascimento.setRegistro(registro);
                registrado.setNascimento(nascimento);

                //adicionando um novo registrado na lista
                registradoList.add(registrado);
            }
        } catch (SQLException error) {
            logger.error("all catch: " + error);
        }

        return registradoList;
    }

    //TODO implementar o all_obito igual o all_nascimento
    @Override
    public List<Registrado> all_obito() {
        List<Registrado> registradoList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY_OBT);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {

                Registro registro = new Registro();
                Obito obito = new Obito();
                Registrado registrado = new Registrado();

                //atributos do registro
                registro.setId_registro(result.getInt("id_registro"));
                registro.setTipo_registro(result.getString("tipo_registro"));
                registro.setAno_registro(result.getInt("ano_registro"));

                //atributos do obito
                obito.setCod_est_civ_falecido(result.getInt("cod_est_civ_falecido"));
                obito.setCod_municipio_obito(result.getInt("cod_municipio_obito"));
                obito.setCod_tipo_obito(result.getInt("cod_tipo_obito"));
                obito.setCod_circ_obito(result.getInt("cod_circ_obito"));
                obito.setCod_local_obito(result.getInt("cod_local_obito"));
                obito.setIdade_falecido(result.getInt("idade_falecido"));
                obito.setData_obito(result.getDate("data_obito"));
                obito.setHora_obito(result.getTime("hora_obito"));

                //atributos do registrado
                registrado.setId_registrado(result.getInt("id_registrado"));
                registrado.setData_nascimento(result.getDate("data_nascimento"));
                registrado.setCod_municipio_nasc(result.getInt("cod_municipio_nasc"));
                registrado.setCod_raca_cor(result.getInt("cod_raca_cor"));
                registrado.setCod_sexo(result.getInt("cod_sexo"));

                //vinculando ponteiros
                obito.setRegistro(registro);
                registrado.setObito(obito);

                //adicionando um novo registrado na lista
                registradoList.add(registrado);
            }
        } catch (SQLException error) {
            logger.error("all catch: " + error);
        }

        return registradoList;
    }

    @Override
    public List<Registrado> all(){
        return null;
    }

    public List<String> qtdRegistrosPorAno(){

        List<String> dataPoints = new ArrayList<>();
        Gson gsonObj = new Gson();
        Map<Object,Object> map;
        List<Map<Object,Object>> listaNascimentos = new ArrayList<Map<Object,Object>>();
        List<Map<Object,Object>> listaObitos = new ArrayList<Map<Object,Object>>();

        try (PreparedStatement statement = connection.prepareStatement(QTD_REGISTROS_POR_ANO);
             ResultSet result = statement.executeQuery()) {
                while(result.next()) {
                    map = new HashMap<Object,Object>();
                    map.put("label", result.getInt("ano_registro"));
                    map.put("y", result.getInt("qtd_registros"));

                    if(result.getString("tipo_registro").equals("nascimento")){
                        listaNascimentos.add(map);
                    }
                    else{
                        listaObitos.add(map);
                    }
                }
            String dataPointsNascimentos = gsonObj.toJson(listaNascimentos);
            String dataPointsObitos = gsonObj.toJson(listaObitos);

            dataPoints.add(dataPointsNascimentos);
            dataPoints.add(dataPointsObitos);

        } catch (SQLException error) {
            logger.error("all catch: " + error);
        }
        return dataPoints;
    }

    public List<String> idadesMaesPorAno(String estado){

        List<String> dataPoints = new ArrayList<>();
        Gson gsonObj = new Gson();
        Map<Object,Object> map;
        List<Map<Object,Object>> listaIdadesMaes = new ArrayList<Map<Object,Object>>();

        try (PreparedStatement statement = connection.prepareStatement(IDADES_MAES_POR_ANO);
             ResultSet result = statement.executeQuery()) {
            while(result.next()) {
                map = new HashMap<Object,Object>();
                map.put("label", result.getInt("ano_registro"));
                map.put("y", result.getInt("media_idade_mae"));

                listaIdadesMaes.add(map);
            }
            String dataPointsIdadesMaes = gsonObj.toJson(listaIdadesMaes);

            dataPoints.add(dataPointsIdadesMaes);

        } catch (SQLException error) {
            logger.error("all catch: " + error);
        }
        return dataPoints;
    }

    public List<String> obitosPorSexoPorAno(String estado){

        List<String> dataPoints = new ArrayList<>();
        Gson gsonObj = new Gson();
        Map<Object,Object> map;
        List<Map<Object,Object>> listaObitosPorSexoPorAnoF = new ArrayList<Map<Object,Object>>();
        List<Map<Object,Object>> listaObitosPorSexoPorAnoM = new ArrayList<Map<Object,Object>>();

        try (PreparedStatement statement = connection.prepareStatement(OBITOS_POR_SEXO_POR_ANO);
             ResultSet result = statement.executeQuery()) {
            while(result.next()) {
                if(Objects.equals(result.getString("sexo"), "masculino")){
                    map = new HashMap<Object,Object>();
                    map.put("label", result.getInt("ano_registro"));
                    map.put("y", result.getInt("qtd_registros"));
                    listaObitosPorSexoPorAnoM.add(map);
                }
                else{
                    map = new HashMap<Object,Object>();
                    map.put("label", result.getInt("ano_registro"));
                    map.put("y", result.getInt("qtd_registros"));
                    listaObitosPorSexoPorAnoF.add(map);
                }
            }
            String dataPointsObitosPorSexoPorAnoTwo = gsonObj.toJson(listaObitosPorSexoPorAnoM);
            String dataPointsObitosPorSexoPorAno = gsonObj.toJson(listaObitosPorSexoPorAnoF);
            dataPoints.add(dataPointsObitosPorSexoPorAnoTwo);
            dataPoints.add(dataPointsObitosPorSexoPorAno);
        } catch (SQLException error) {
            logger.error("all catch: " + error);
        }

        return dataPoints;
    }

    public List<String> obitosPorAno(String estado){

        List<String> dataPoints = new ArrayList<>();
        Gson gsonObj = new Gson();
        Map<Object,Object> map;
        List<Map<Object,Object>> listaObitosPorAno = new ArrayList<Map<Object,Object>>();

        try (PreparedStatement statement = connection.prepareStatement(OBITOS_POR_ANO);
             ResultSet result = statement.executeQuery()) {
            while(result.next()) {
                map = new HashMap<Object,Object>();
                map.put("label", result.getInt("ano_registro"));
                map.put("y", result.getInt("qtd_registros"));
                listaObitosPorAno.add(map);
                System.out.println(map);
            }
            String dataPointsObitosPorAno = gsonObj.toJson(listaObitosPorAno);
            dataPoints.add(dataPointsObitosPorAno);
        } catch (SQLException error) {
            logger.error("all catch: " + error);
        }

        return dataPoints;
    }
}