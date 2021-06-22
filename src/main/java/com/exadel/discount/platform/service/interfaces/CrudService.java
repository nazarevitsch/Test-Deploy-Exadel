package com.exadel.discount.platform.service.interfaces;

import java.util.List;
import java.util.UUID;

public interface CrudService<T> {

    List<T> getAll();
    T save(T t);
    T getById(UUID id);
    T update(UUID id, T t);
    void toArchive(UUID id);
}
