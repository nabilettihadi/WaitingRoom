package ma.nabil.WRM.service.impl;

import lombok.RequiredArgsConstructor;
import ma.nabil.WRM.dto.request.VisitorRequest;
import ma.nabil.WRM.dto.response.VisitorResponse;
import ma.nabil.WRM.entity.Visitor;
import ma.nabil.WRM.exception.BusinessException;
import ma.nabil.WRM.exception.ResourceNotFoundException;
import ma.nabil.WRM.mapper.VisitorMapper;
import ma.nabil.WRM.repository.VisitorRepository;
import ma.nabil.WRM.service.VisitorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitorServiceImpl implements VisitorService {
    private final VisitorRepository visitorRepository;
    private final VisitorMapper visitorMapper;

    @Override
    public VisitorResponse create(VisitorRequest request) {
        validateVisitorUniqueness(request.getFirstName(), request.getLastName());
        Visitor visitor = visitorMapper.toEntity(request);
        visitor = visitorRepository.save(visitor);
        return visitorMapper.toResponse(visitor);
    }

    @Override
    public VisitorResponse findById(Long id) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visitor not found"));
        return visitorMapper.toResponse(visitor);
    }

    @Override
    public Page<VisitorResponse> findAll(Pageable pageable) {
        return visitorRepository.findAll(pageable).map(visitorMapper::toResponse);
    }

    @Override
    public VisitorResponse update(Long id, VisitorRequest request) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visitor not found"));

        if (!visitor.getFirstName().equals(request.getFirstName()) || !visitor.getLastName().equals(request.getLastName())) {
            validateVisitorUniqueness(request.getFirstName(), request.getLastName());
        }

        visitorMapper.updateEntity(request, visitor);
        visitor = visitorRepository.save(visitor);
        return visitorMapper.toResponse(visitor);
    }

    @Override
    public void delete(Long id) {
        if (!visitorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Visitor not found");
        }
        visitorRepository.deleteById(id);
    }

    @Override
    public boolean existsByFirstNameAndLastName(String firstName, String lastName) {
        return visitorRepository.existsByFirstNameAndLastName(firstName, lastName);
    }

    private void validateVisitorUniqueness(String firstName, String lastName) {
        if (existsByFirstNameAndLastName(firstName, lastName)) {
            throw new BusinessException("Un visiteur avec le même nom et prénom existe déjà");
        }
    }
}