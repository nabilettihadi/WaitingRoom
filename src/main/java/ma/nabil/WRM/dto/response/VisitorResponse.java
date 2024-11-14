package ma.nabil.WRM.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private List<VisitResponse> visits;
}