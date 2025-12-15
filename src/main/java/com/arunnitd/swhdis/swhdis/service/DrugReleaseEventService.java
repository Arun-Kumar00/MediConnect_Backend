package com.arunnitd.swhdis.swhdis.service;

// package com.medical.woundhealing.service;

// import com.medical.woundhealing.model.DrugReleaseEvent;
// import com.medical.woundhealing.repository.BandageRepository;
// import com.medical.woundhealing.repository.DrugReleaseEventRepository;
// import com.medical.woundhealing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arunnitd.swhdis.swhdis.models.DrugReleaseEvent;
import com.arunnitd.swhdis.swhdis.repository.BandageRepository;
import com.arunnitd.swhdis.swhdis.repository.DrugReleaseEventRepository;
import com.arunnitd.swhdis.swhdis.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrugReleaseEventService {

    private final DrugReleaseEventRepository drugReleaseEventRepository;
    private final BandageRepository bandageRepository;
    private final UserRepository userRepository;

    public List<DrugReleaseEvent> getAllEvents() {
        List<DrugReleaseEvent> events = drugReleaseEventRepository.findAll();
        events.forEach(this::loadRelatedData);
        return events;
    }

    public Optional<DrugReleaseEvent> getEventById(Integer id) {
        Optional<DrugReleaseEvent> event = drugReleaseEventRepository.findById(id);
        event.ifPresent(this::loadRelatedData);
        return event;
    }

    public List<DrugReleaseEvent> getEventsByBandageId(Integer bandageId) {
        List<DrugReleaseEvent> events = drugReleaseEventRepository.findByBandageIdOrderByReleaseTimeDesc(bandageId);
        events.forEach(this::loadRelatedData);
        return events;
    }

    public List<DrugReleaseEvent> getEventsByDrugName(String drugName) {
        List<DrugReleaseEvent> events = drugReleaseEventRepository.findByDrugName(drugName);
        events.forEach(this::loadRelatedData);
        return events;
    }

    public List<DrugReleaseEvent> getEventsByTimeRange(LocalDateTime start, LocalDateTime end) {
        List<DrugReleaseEvent> events = drugReleaseEventRepository.findByReleaseTimeBetween(start, end);
        events.forEach(this::loadRelatedData);
        return events;
    }

    public Long getEventCountByBandage(Integer bandageId) {
        return drugReleaseEventRepository.countByBandageId(bandageId);
    }

    @Transactional
    public DrugReleaseEvent createEvent(DrugReleaseEvent event) {
        if (!bandageRepository.existsById(event.getBandageId())) {
            throw new RuntimeException("Bandage not found with id: " + event.getBandageId());
        }

        DrugReleaseEvent saved = drugReleaseEventRepository.save(event);
        loadRelatedData(saved);
        return saved;
    }

    @Transactional
    public void deleteEvent(Integer id) {
        DrugReleaseEvent event = drugReleaseEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drug release event not found with id: " + id));
        drugReleaseEventRepository.delete(event);
    }

    private void loadRelatedData(DrugReleaseEvent event) {
        bandageRepository.findById(event.getBandageId())
                .ifPresent(event::setBandage);

        if (event.getAdministeredBy() != null) {
            userRepository.findById(event.getAdministeredBy())
                    .ifPresent(event::setAdministeredByUser);
        }
    }
}
