package com.exadel.discount.platform.service.interfaces;

import java.util.List;
import java.util.UUID;

public interface CrudService<T, S> {

    List<S> getAll();
    S save(T t);
    S getById(UUID id);
    S update(UUID id, T t);
    void toArchive(UUID id);
}
