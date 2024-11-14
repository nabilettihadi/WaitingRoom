package ma.nabil.WRM.repository;

import ma.nabil.WRM.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
