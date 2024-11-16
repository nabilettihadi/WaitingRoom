package ma.nabil.WRM.service.impl;

import jakarta.transaction.Transactional;
import ma.nabil.WRM.dto.mapper.VisitorMapper;
import ma.nabil.WRM.dto.request.VisitorRequest;
import ma.nabil.WRM.dto.response.VisitorResponse;
import ma.nabil.WRM.entity.Visitor;
import ma.nabil.WRM.exception.BusinessException;
import ma.nabil.WRM.repository.VisitorRepository;
import ma.nabil.WRM.service.VisitorService;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class VisitorServiceImpl extends GenericServiceImpl<Visitor, VisitorRequest, VisitorResponse, Long> implements VisitorService {
    private final VisitorRepository visitorRepository;
    private final VisitorMapper visitorMapper;

    public VisitorServiceImpl(VisitorRepository visitorRepository, VisitorMapper visitorMapper) {
        super(visitorRepository);
        this.visitorRepository = visitorRepository;
        this.visitorMapper = visitorMapper;
    }

    @Override
    protected Visitor toEntity(VisitorRequest request) {
        if (existsByFirstNameAndLastName(request.getFirstName(), request.getLastName())) {
            throw new BusinessException("Un visiteur avec le même nom et prénom existe déjà");
        }
        return visitorMapper.toEntity(request);
    }

    @Override
    protected VisitorResponse toResponse(Visitor visitor) {
        return visitorMapper.toResponse(visitor);
    }

    @Override
    public VisitorResponse create(VisitorRequest request) {
        Visitor visitor = visitorMapper.toEntity(request);
        visitor = visitorRepository.save(visitor);
        return visitorMapper.toResponse(visitor);
    }

    @Override
    public void delete(Long id) {
        visitorRepository.deleteById(id);
    }

    @Override
    protected void updateEntity(VisitorRequest request, Visitor visitor) {
        if (!visitor.getFirstName().equals(request.getFirstName())
                || !visitor.getLastName().equals(request.getLastName())) {
            if (existsByFirstNameAndLastName(request.getFirstName(), request.getLastName())) {
                throw new BusinessException("Un visiteur avec le même nom et prénom existe déjà");
            }
        }
        visitorMapper.updateEntity(request, visitor);
    }

    @Override
    protected String getEntityName() {
        return "Visitor";
    }


    public boolean existsByFirstNameAndLastName(String firstName, String lastName) {
        return visitorRepository.existsByFirstNameAndLastName(firstName, lastName);
    }
}