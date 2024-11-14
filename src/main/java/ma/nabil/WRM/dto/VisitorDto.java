@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<VisitDto> visits;
}