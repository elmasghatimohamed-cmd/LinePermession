package ma.youcode.lineperm.dao;

import java.util.List;

public interface Dao<T> {
    void save(T obj);

    T findById(int id);

    List<T> findAll();
    
    void delete(int id);
}