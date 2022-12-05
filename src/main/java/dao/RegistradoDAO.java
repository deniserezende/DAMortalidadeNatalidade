package dao;

import com.google.gson.Gson;
import model.Registrado;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public interface RegistradoDAO extends DAO<Registrado> {

//    public abstract RegistradoDAO getRegistradoDAO();

    public abstract List<Registrado> all_obito();
    public abstract List<Registrado> all_nascimento();

    public abstract Registrado read_nascimento(Integer id_registro, String tipo_registro, Integer ano_registro);

    public abstract Registrado read_obito(Integer id_registro, String tipo_registro, Integer ano_registro);

    public List<String> qtdRegistrosPorAno();

    public List<String> idadesMaesPorAno(String estado);

    public List<String> obitosPorSexoPorAno(String estado);

    public List<String> obitosPorAno(String estado);

    public List<String> obitosPorRacaPorAno(String estado);

    public List<String> obitosNaoNaturaisPorRacaPorAno(String estado);

    public List<String> nascimentosPorRacaPorAno(String estado);
}
