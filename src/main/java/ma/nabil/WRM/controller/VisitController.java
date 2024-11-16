package ma.nabil.WRM.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.nabil.WRM.dto.request.VisitRequest;
import ma.nabil.WRM.dto.response.VisitResponse;
import ma.nabil.WRM.entity.VisitId;
import ma.nabil.WRM.enums.VisitorStatus;
import ma.nabil.WRM.service.VisitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<VisitResponse> create(@Valid @RequestBody VisitRequest request) {
        return ResponseEntity.ok(visitService.create(request));
    }

    @GetMapping("/{visitorId}/{waitingRoomId}")
    public ResponseEntity<VisitResponse> findById(
            @PathVariable Long visitorId,
            @PathVariable Long waitingRoomId) {
        return ResponseEntity.ok(visitService.findById(new VisitId(visitorId, waitingRoomId)));
    }

    @GetMapping("/waiting-room/{waitingRoomId}")
    public ResponseEntity<Page<VisitResponse>> findAllByWaitingRoom(
            @PathVariable Long waitingRoomId,
            Pageable pageable) {
        return ResponseEntity.ok(visitService.findAllByWaitingRoomId(waitingRoomId, pageable));
    }

    @PutMapping("/{visitorId}/{waitingRoomId}")
    public ResponseEntity<VisitResponse> update(
            @PathVariable Long visitorId,
            @PathVariable Long waitingRoomId,
            @Valid @RequestBody VisitRequest request) {
        return ResponseEntity.ok(visitService.update(new VisitId(visitorId, waitingRoomId), request));
    }

    @DeleteMapping("/{visitorId}/{waitingRoomId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long visitorId,
            @PathVariable Long waitingRoomId) {
        visitService.delete(new VisitId(visitorId, waitingRoomId));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{visitorId}/{waitingRoomId}/status")
    public ResponseEntity<VisitResponse> updateStatus(
            @PathVariable Long visitorId,
            @PathVariable Long waitingRoomId,
            @RequestParam VisitorStatus status) {
        return ResponseEntity.ok(visitService.updateStatus(new VisitId(visitorId, waitingRoomId), status));
    }

    @PostMapping("/waiting-room/{waitingRoomId}/process-next")
    public ResponseEntity<Void> processNextVisit(@PathVariable Long waitingRoomId) {
        visitService.processNextVisit(waitingRoomId);
        return ResponseEntity.ok().build();
    }
}