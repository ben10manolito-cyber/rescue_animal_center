package co.edu.umanizales.rescue_animal_center.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    T update(ID id, T entity);
    void delete(ID id);
}
