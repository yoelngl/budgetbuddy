package com.budgetbuddy.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD contract for all repositories.
 *
 * Interface usage (rubric): defines the minimum repository API.
 * Spring Data JPA repositories satisfy this contract via JpaRepository,
 * which provides: save(), findAll(), findById(), deleteById(), count().
 *
 * @param <T>  entity type
 * @param <ID> primary key type
 */
public interface IRepository<T, ID> {

    T              save(T entity);

    List<T>        findAll();

    Optional<T>    findById(ID id);

    void           deleteById(ID id);

    long           count();
}
