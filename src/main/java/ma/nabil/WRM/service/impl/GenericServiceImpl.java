package ma.nabil.WRM.service.impl;

import lombok.RequiredArgsConstructor;
import ma.nabil.WRM.exception.ResourceNotFoundException;
import ma.nabil.WRM.service.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public abstract class GenericServiceImpl<E, T, R, ID> implements GenericService<T, R, ID> {
    protected final JpaRepository<E, ID> repository;

    protected abstract E toEntity(T request);

    protected abstract R toResponse(E entity);

    protected abstract void updateEntity(T request, E entity);

    protected abstract String getEntityName();

    @Override
    public R save(T request) {
        E entity = toEntity(request);
        return toResponse(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public R findById(ID id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName() + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<R> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public R update(ID id, T request) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName() + " not found"));
        updateEntity(request, entity);
        return toResponse(repository.save(entity));
    }

    @Override
    public void deleteById(ID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(getEntityName() + " not found");
        }
        repository.deleteById(id);
    }
}