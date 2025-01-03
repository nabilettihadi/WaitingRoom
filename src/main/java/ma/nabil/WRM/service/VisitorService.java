package ma.nabil.WRM.service;

import ma.nabil.WRM.dto.request.VisitorRequest;
import ma.nabil.WRM.dto.response.VisitorResponse;

public interface VisitorService extends GenericService<VisitorResponse, VisitorRequest, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}