package ma.nabil.WRM.service;

import ma.nabil.WRM.dto.request.VisitorRequest;
import ma.nabil.WRM.dto.response.VisitorResponse;

public interface VisitorService extends GenericService<VisitorRequest, VisitorResponse, Long> {
    VisitorResponse create(VisitorRequest request);

    void delete(Long id);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}