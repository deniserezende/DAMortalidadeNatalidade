package dao;

import java.sql.SQLException;
import java.util.List;

// T represents any object in Java
public interface DAO <T> {
    public void create(T t) throws SQLException;
    public T read(Integer id) throws SQLException;
    public void update(T t) throws SQLException;
    public void delete(Integer id) throws SQLException;
    public List<T> all() throws SQLException;
}