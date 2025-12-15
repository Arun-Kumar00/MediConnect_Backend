package com.arunnitd.swhdis.swhdis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arunnitd.swhdis.swhdis.models.Bandage;
import com.arunnitd.swhdis.swhdis.repository.BandageRepository;
import com.arunnitd.swhdis.swhdis.repository.DeviceRepository;
import com.arunnitd.swhdis.swhdis.repository.PatientRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BandageService {

    private final BandageRepository bandageRepository;
    private final PatientRepository patientRepository;
    private final DeviceRepository deviceRepository;

    public List<Bandage> getAllBandages() {
        List<Bandage> bandages = bandageRepository.findAll();
        bandages.forEach(this::loadRelatedData);
        return bandages;
    }

    public Optional<Bandage> getBandageById(Integer id) {
        Optional<Bandage> bandage = bandageRepository.findById(id);
        bandage.ifPresent(this::loadRelatedData);
        return bandage;
    }

    public Optional<Bandage> getBandageBySerial(String serial) {
        Optional<Bandage> bandage = bandageRepository.findByBandageSerial(serial);
        bandage.ifPresent(this::loadRelatedData);
        return bandage;
    }

    public List<Bandage> getBandagesByPatientId(Integer patientId) {
        List<Bandage> bandages = bandageRepository.findByPatientId(patientId);
        bandages.forEach(this::loadRelatedData);
        return bandages;
    }

    public List<Bandage> getBandagesByDeviceId(Integer deviceId) {
        List<Bandage> bandages = bandageRepository.findByDeviceId(deviceId);
        bandages.forEach(this::loadRelatedData);
        return bandages;
    }

    public List<Bandage> getBandagesByStatus(Bandage.BandageStatus status) {
        List<Bandage> bandages = bandageRepository.findByStatus(status);
        bandages.forEach(this::loadRelatedData);
        return bandages;
    }

    public List<Bandage> getActiveBandages() {
        List<Bandage> bandages = bandageRepository.findAllActiveBandages();
        bandages.forEach(this::loadRelatedData);
        return bandages;
    }

    @Transactional
    public Bandage createBandage(Bandage bandage) {
        // Validate patient exists
        if (!patientRepository.existsById(bandage.getPatientId())) {
            throw new RuntimeException("Patient not found with id: " + bandage.getPatientId());
        }

        // Validate device exists
        if (!deviceRepository.existsById(bandage.getDeviceId())) {
            throw new RuntimeException("Device not found with id: " + bandage.getDeviceId());
        }

        Bandage saved = bandageRepository.save(bandage);
        loadRelatedData(saved);
        return saved;
    }

    @Transactional
    public Bandage updateBandage(Integer id, Bandage bandageDetails) {
        Bandage bandage = bandageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bandage not found with id: " + id));

        bandage.setBandageSerial(bandageDetails.getBandageSerial());
        bandage.setPatientId(bandageDetails.getPatientId());
        bandage.setDeviceId(bandageDetails.getDeviceId());
        bandage.setApplicationDate(bandageDetails.getApplicationDate());
        bandage.setRemovalDate(bandageDetails.getRemovalDate());
        bandage.setStatus(bandageDetails.getStatus());

        Bandage updated = bandageRepository.save(bandage);
        loadRelatedData(updated);
        return updated;
    }

    @Transactional
    public Bandage removeBandage(Integer id) {
        Bandage bandage = bandageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bandage not found with id: " + id));

        bandage.setRemovalDate(LocalDateTime.now());
        bandage.setStatus(Bandage.BandageStatus.removed);

        Bandage updated = bandageRepository.save(bandage);
        loadRelatedData(updated);
        return updated;
    }

    @Transactional
    public void deleteBandage(Integer id) {
        Bandage bandage = bandageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bandage not found with id: " + id));
        bandageRepository.delete(bandage);
    }

    // Helper method to load related patient and device data
    private void loadRelatedData(Bandage bandage) {
        patientRepository.findById(bandage.getPatientId())
                .ifPresent(bandage::setPatient);

        deviceRepository.findById(bandage.getDeviceId())
                .ifPresent(bandage::setDevice);
    }
}
