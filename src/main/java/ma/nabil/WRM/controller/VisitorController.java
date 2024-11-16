package ma.nabil.WRM.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.nabil.WRM.dto.request.VisitorRequest;
import ma.nabil.WRM.dto.response.VisitorResponse;
import ma.nabil.WRM.service.VisitorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorController {
    private final VisitorService visitorService;

    @PostMapping
    public ResponseEntity<VisitorResponse> create(@Valid @RequestBody VisitorRequest request) {
        return ResponseEntity.ok(visitorService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitorResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(visitorService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<VisitorResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(visitorService.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitorResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VisitorRequest request) {
        return ResponseEntity.ok(visitorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        visitorService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByFirstNameAndLastName(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        return ResponseEntity.ok(visitorService.existsByFirstNameAndLastName(firstName, lastName));
    }
}