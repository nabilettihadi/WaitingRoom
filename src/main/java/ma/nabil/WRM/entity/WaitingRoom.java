package ma.nabil.WRM.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ma.nabil.WRM.enums.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private SchedulingAlgorithm algorithm;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    @OneToMany(mappedBy = "waitingRoom", cascade = CascadeType.ALL)
    private List<Visit> visits = new ArrayList<>();
}