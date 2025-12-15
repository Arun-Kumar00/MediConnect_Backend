package com.arunnitd.swhdis.swhdis.controller;

// package com.medical.woundhealing.controller;

// import com.medical.woundhealing.model.DrugReleaseEvent;
// import com.medical.woundhealing.service.DrugReleaseEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.arunnitd.swhdis.swhdis.models.DrugReleaseEvent;
import com.arunnitd.swhdis.swhdis.service.DrugReleaseEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/drug-releases")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DrugReleaseEventController {

    private final DrugReleaseEventService drugReleaseEventService;

    @GetMapping
    public ResponseEntity<List<DrugReleaseEvent>> getAllEvents() {
        return ResponseEntity.ok(drugReleaseEventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrugReleaseEvent> getEventById(@PathVariable Integer id) {
        return drugReleaseEventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bandage/{bandageId}")
    public ResponseEntity<List<DrugReleaseEvent>> getEventsByBandageId(@PathVariable Integer bandageId) {
        return ResponseEntity.ok(drugReleaseEventService.getEventsByBandageId(bandageId));
    }

    @GetMapping("/drug/{drugName}")
    public ResponseEntity<List<DrugReleaseEvent>> getEventsByDrugName(@PathVariable String drugName) {
        return ResponseEntity.ok(drugReleaseEventService.getEventsByDrugName(drugName));
    }

    @GetMapping("/range")
    public ResponseEntity<List<DrugReleaseEvent>> getEventsByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(drugReleaseEventService.getEventsByTimeRange(start, end));
    }

    @GetMapping("/bandage/{bandageId}/count")
    public ResponseEntity<Long> getEventCount(@PathVariable Integer bandageId) {
        return ResponseEntity.ok(drugReleaseEventService.getEventCountByBandage(bandageId));
    }

    @PostMapping
    public ResponseEntity<DrugReleaseEvent> createEvent(@RequestBody DrugReleaseEvent event) {
        try {
            DrugReleaseEvent savedEvent = drugReleaseEventService.createEvent(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        try {
            drugReleaseEventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}