package com.arunnitd.swhdis.swhdis.service;

// package com.medical.woundhealing.service;

// import com.medical.woundhealing.model.Device;
// import com.medical.woundhealing.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arunnitd.swhdis.swhdis.models.Device;
import com.arunnitd.swhdis.swhdis.repository.DeviceRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Optional<Device> getDeviceById(Integer id) {
        return deviceRepository.findById(id);
    }

    public Optional<Device> getDeviceBySerial(String serial) {
        return deviceRepository.findByDeviceSerial(serial);
    }

    public List<Device> getDevicesByStatus(Device.DeviceStatus status) {
        return deviceRepository.findByStatus(status);
    }

    @Transactional
    public Device createDevice(Device device) {
        return deviceRepository.save(device);
    }

    @Transactional
    public Device updateDevice(Integer id, Device deviceDetails) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + id));

        device.setDeviceSerial(deviceDetails.getDeviceSerial());
        device.setDeviceModel(deviceDetails.getDeviceModel());
        device.setManufactureDate(deviceDetails.getManufactureDate());
        device.setLastCalibration(deviceDetails.getLastCalibration());
        device.setStatus(deviceDetails.getStatus());
        device.setFirmwareVersion(deviceDetails.getFirmwareVersion());

        return deviceRepository.save(device);
    }

    @Transactional
    public void deleteDevice(Integer id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + id));
        deviceRepository.delete(device);
    }
}