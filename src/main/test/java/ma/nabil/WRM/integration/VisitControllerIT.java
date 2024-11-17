package ma.nabil.WRM.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.nabil.WRM.dto.request.VisitRequest;
import ma.nabil.WRM.dto.request.VisitorRequest;
import ma.nabil.WRM.dto.request.WaitingRoomRequest;
import ma.nabil.WRM.entity.Visit;
import ma.nabil.WRM.enums.VisitorStatus;
import ma.nabil.WRM.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VisitControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VisitRepository visitRepository;

    private Long visitorId;
    private Long waitingRoomId;

    @BeforeEach
    void setUp() throws Exception {

        VisitorRequest visitorRequest = new VisitorRequest();
        visitorRequest.setFirstName("John");
        visitorRequest.setLastName("Doe");

        String visitorResult = mockMvc.perform(post("/api/visitors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visitorRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        visitorId = objectMapper.readTree(visitorResult).get("id").asLong();


        WaitingRoomRequest waitingRoomRequest = new WaitingRoomRequest();
        waitingRoomRequest.setName("Test Room");
        waitingRoomRequest.setCapacity(10);

        String roomResult = mockMvc.perform(post("/api/waiting-rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(waitingRoomRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        waitingRoomId = objectMapper.readTree(roomResult).get("id").asLong();
    }

    @Test
    void shouldCreateVisit() throws Exception {

        VisitRequest request = new VisitRequest();
        request.setVisitorId(visitorId);
        request.setWaitingRoomId(waitingRoomId);
        request.setPriority(1);
        request.setEstimatedProcessingTime(30);


        mockMvc.perform(post("/api/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visitorId").value(visitorId))
                .andExpect(jsonPath("$.waitingRoomId").value(waitingRoomId))
                .andExpect(jsonPath("$.status").value(VisitorStatus.WAITING.name()))
                .andExpect(jsonPath("$.priority").value(1))
                .andExpect(jsonPath("$.estimatedProcessingTime").value(30));


        Visit savedVisit = visitRepository.findById(new VisitId(visitorId, waitingRoomId))
                .orElseThrow();
        assertThat(savedVisit.getStatus()).isEqualTo(VisitorStatus.WAITING);
        assertThat(savedVisit.getPriority()).isEqualTo(1);
        assertThat(savedVisit.getEstimatedProcessingTime()).isEqualTo(30);
    }
}