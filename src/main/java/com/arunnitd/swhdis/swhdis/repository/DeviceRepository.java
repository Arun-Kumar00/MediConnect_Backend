package com.arunnitd.swhdis.swhdis.repository;

// package com.medical.woundhealing.repository;

// import com.medical.woundhealing.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arunnitd.swhdis.swhdis.models.Device;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Optional<Device> findByDeviceSerial(String deviceSerial);

    List<Device> findByStatus(Device.DeviceStatus status);

    List<Device> findByDeviceModel(String deviceModel);
}
