package ma.nabil.WRM.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericService<R, Q, ID> {
    R create(Q request);

    R findById(ID id);

    Page<R> findAll(Pageable pageable);

    R update(ID id, Q request);

    void delete(ID id);
}