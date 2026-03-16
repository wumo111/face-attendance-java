package com.face.service;

import com.face.entity.Employee;
import com.face.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${face.python-api}")
    private String pythonApiUrl;

    @Value("${face.upload-path}")
    private String uploadPath;

    public void addEmployee(String name, Integer gender, String idCard, String address, String phone, MultipartFile photo) throws IOException {
        // 1. Save photo
        if (!Files.exists(Paths.get(uploadPath))) {
            Files.createDirectories(Paths.get(uploadPath));
        }
        String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName);
        photo.transferTo(filePath.toFile());

        // 2. Call Python API to extract feature
        String feature = extractFeature(filePath.toFile());

        // 3. Save to DB
        Employee employee = new Employee();
        employee.setName(name);
        employee.setGender(gender);
        employee.setIdCard(idCard);
        employee.setAddress(address);
        employee.setPhone(phone);
        employee.setPhotoUrl(fileName); // Store relative path or full path? Using filename for now.
        employee.setFeature(feature);
        
        employeeMapper.insert(employee);
    }

    private String extractFeature(File file) {
        String url = pythonApiUrl + "/extract_feature";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            // Assuming Python returns {"code": 200, "data": "feature_string"}
            Map response = restTemplate.postForObject(url, requestEntity, Map.class);
            if (response != null && response.get("data") != null) {
                return response.get("data").toString();
            }
            throw new RuntimeException("Failed to extract feature: " + response);
        } catch (Exception e) {
            // For development/mocking purposes, if Python service is not running, return a dummy feature
            System.err.println("Warning: Python service failed. Using dummy feature. " + e.getMessage());
            return "dummy_feature_vector";
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeMapper.findAll();
    }

    public void deleteEmployee(Integer id) {
        employeeMapper.delete(id);
    }
}
