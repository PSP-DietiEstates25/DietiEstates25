package com.dietiestates25.backend.controller;

import com.dietiestates25.backend.model.Service;
import com.dietiestates25.backend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Service getServiceById(@PathVariable Long id) {
        return serviceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    @PostMapping
    public Service createService(@RequestBody Service service) {
        return serviceRepository.save(service);
    }

    /* 
    @PutMapping("/{id}")
    public Service updateService(@PathVariable Long id, @RequestBody Service serviceDetails) {
        Service service = serviceRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Service not found"));
        service.setName(serviceDetails.getName());
        // altri campi
        return serviceRepository.save(service);
    }
    */

    @DeleteMapping("/{id}")
    public void deleteService(@PathVariable Long id) {
        serviceRepository.deleteById(id);
    }
}
