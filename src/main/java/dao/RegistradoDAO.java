package dao;

import model.Registrado;

public interface RegistradoDAO extends DAO<Registrado> {

    public abstract RegistradoDAO getRegistradoDAO();


//    public void authenticate(User usuario) throws SQLException, SecurityException;
//    public User getByLogin(String login) throws SQLException;
}
