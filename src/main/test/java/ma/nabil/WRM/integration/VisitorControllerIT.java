package ma.nabil.WRM.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.nabil.WRM.dto.request.VisitorRequest;
import ma.nabil.WRM.entity.Visitor;
import ma.nabil.WRM.repository.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VisitorControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VisitorRepository visitorRepository;

    @Test
    void shouldCreateVisitor() throws Exception {

        VisitorRequest request = new VisitorRequest();
        request.setFirstName("John");
        request.setLastName("Doe");

        mockMvc.perform(post("/api/visitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void shouldNotCreateDuplicateVisitor() throws Exception {
        VisitorRequest request = new VisitorRequest();
        request.setFirstName("John");
        request.setLastName("Doe");

        mockMvc.perform(post("/api/visitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/visitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateVisitor() throws Exception {
        VisitorRequest createRequest = new VisitorRequest();
        createRequest.setFirstName("John");
        createRequest.setLastName("Doe");

        String createResult = mockMvc.perform(post("/api/visitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long visitorId = objectMapper.readTree(createResult).get("id").asLong();

        VisitorRequest updateRequest = new VisitorRequest();
        updateRequest.setFirstName("Jane");
        updateRequest.setLastName("Doe");

        mockMvc.perform(put("/api/visitors/{id}", visitorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"));


        Visitor updatedVisitor = visitorRepository.findById(visitorId).orElseThrow();
        assertThat(updatedVisitor.getFirstName()).isEqualTo("Jane");
        assertThat(updatedVisitor.getLastName()).isEqualTo("Doe");
    }

    @Test
    void shouldDeleteVisitor() throws Exception {
        Long visitorId = createVisitor();

        mockMvc.perform(delete("/api/visitors/{id}", visitorId))
                .andExpect(status().isOk());

        assertThat(visitorRepository.findById(visitorId)).isEmpty();
    }

    @Test
    void shouldFindVisitorById() throws Exception {
        Long visitorId = createVisitor();

        mockMvc.perform(get("/api/visitors/{id}", visitorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    private Long createVisitor() throws Exception {
        VisitorRequest request = new VisitorRequest();
        request.setFirstName("John");
        request.setLastName("Doe");

        String result = mockMvc.perform(post("/api/visitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(result).get("id").asLong();
    }
}