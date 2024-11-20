package ma.nabil.WRM.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.nabil.WRM.dto.request.WaitingRoomRequest;
import ma.nabil.WRM.entity.WaitingRoom;
import ma.nabil.WRM.enums.WorkMode;
import ma.nabil.WRM.repository.WaitingRoomRepository;
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
class WaitingRoomControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WaitingRoomRepository waitingRoomRepository;

    @Test
    void shouldCreateWaitingRoom() throws Exception {
        WaitingRoomRequest request = createWaitingRoomRequest();

        mockMvc.perform(post("/api/waiting-rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Room"))
                .andExpect(jsonPath("$.capacity").value(10));
    }

    @Test
    void shouldUpdateWaitingRoom() throws Exception {
        Long roomId = createWaitingRoom();

        WaitingRoomRequest updateRequest = new WaitingRoomRequest();
        updateRequest.setName("Updated Room");
        updateRequest.setCapacity(20);

        mockMvc.perform(put("/api/waiting-rooms/{id}", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Room"))
                .andExpect(jsonPath("$.capacity").value(20));
    }

    @Test
    void shouldUpdateWorkMode() throws Exception {
        Long roomId = createWaitingRoom();

        mockMvc.perform(put("/api/waiting-rooms/{id}/work-mode", roomId)
                        .param("workMode", WorkMode.MANUAL.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workMode").value(WorkMode.MANUAL.name()));

        WaitingRoom updatedRoom = waitingRoomRepository.findById(roomId).orElseThrow();
        assertThat(updatedRoom.getWorkMode()).isEqualTo(WorkMode.MANUAL);
    }

    private WaitingRoomRequest createWaitingRoomRequest() {
        WaitingRoomRequest request = new WaitingRoomRequest();
        request.setName("Test Room");
        request.setCapacity(10);
        return request;
    }

    private Long createWaitingRoom() throws Exception {
        WaitingRoomRequest request = createWaitingRoomRequest();
        String result = mockMvc.perform(post("/api/waiting-rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(result).get("id").asLong();
    }
}