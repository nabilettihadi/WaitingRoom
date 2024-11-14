package ma.nabil.WRM.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericService<T, R, ID> {
    R save(T request);

    R findById(ID id);

    Page<R> findAll(Pageable pageable);

    R update(ID id, T request);

    void deleteById(ID id);
}