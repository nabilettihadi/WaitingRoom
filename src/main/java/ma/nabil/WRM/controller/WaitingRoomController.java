package ma.nabil.WRM.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.nabil.WRM.dto.request.WaitingRoomRequest;
import ma.nabil.WRM.dto.response.WaitingRoomResponse;
import ma.nabil.WRM.enums.SchedulingAlgorithm;
import ma.nabil.WRM.enums.WorkMode;
import ma.nabil.WRM.service.WaitingRoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/waiting-rooms")
@RequiredArgsConstructor
public class WaitingRoomController {
    private final WaitingRoomService waitingRoomService;

    @PostMapping
    public ResponseEntity<WaitingRoomResponse> create(@Valid @RequestBody WaitingRoomRequest request) {
        WaitingRoomResponse response = waitingRoomService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaitingRoomResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(waitingRoomService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<WaitingRoomResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(waitingRoomService.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WaitingRoomResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody WaitingRoomRequest request) {
        return ResponseEntity.ok(waitingRoomService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        waitingRoomService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/algorithm")
    public ResponseEntity<WaitingRoomResponse> updateAlgorithm(
            @PathVariable Long id,
            @RequestParam SchedulingAlgorithm algorithm) {
        return ResponseEntity.ok(waitingRoomService.updateAlgorithm(id, algorithm));
    }

    @PutMapping("/{id}/work-mode")
    public ResponseEntity<WaitingRoomResponse> updateWorkMode(
            @PathVariable Long id,
            @RequestParam WorkMode workMode) {
        return ResponseEntity.ok(waitingRoomService.updateWorkMode(id, workMode));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<WaitingRoomResponse.WaitingRoomStats> getStats(@PathVariable Long id) {
        WaitingRoomResponse.WaitingRoomStats stats = waitingRoomService.getStats(id);
        return ResponseEntity.ok(stats);
    }
}