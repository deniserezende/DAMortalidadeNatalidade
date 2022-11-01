package dao;

import model.Registrado;

import java.sql.SQLException;
import java.util.List;

public interface RegistradoDAO extends DAO<Registrado> {

//    public abstract RegistradoDAO getRegistradoDAO();

    public abstract List<Registrado> all_obito();
    public abstract List<Registrado> all_nascimento();



//    public void authenticate(User usuario) throws SQLException, SecurityException;
//    public User getByLogin(String login) throws SQLException;
}
