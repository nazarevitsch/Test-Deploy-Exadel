package com.exadel.discount.platform.service.interfaces;

import java.util.List;
import java.util.UUID;

public interface CrudService<T, S, M> {

    List<S> getAll(boolean isDeleted);
    S save(T t);
    S getById(UUID id);
    S update(UUID id, M t);
    void toArchive(UUID id);
}
