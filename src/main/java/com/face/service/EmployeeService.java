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
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
        if (!Files.exists(Paths.get(uploadPath))) {
            Files.createDirectories(Paths.get(uploadPath));
        }
        String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName);
        photo.transferTo(filePath.toFile());

        String feature = extractFeature(filePath.toFile());

        Employee employee = new Employee();
        employee.setName(name);
        employee.setGender(gender);
        employee.setIdCard(idCard);
        employee.setAddress(address);
        employee.setPhone(phone);
        employee.setPhotoUrl(fileName);
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
            Map response = restTemplate.postForObject(url, requestEntity, Map.class);
            if (response == null) {
                return null;
            }
            Object data = response.get("data");
            if (data instanceof String && !((String) data).isBlank()) {
                return data.toString();
            }
            if (data instanceof Map<?, ?> dataMap) {
                Object featureObj = dataMap.get("feature");
                if (featureObj != null && !featureObj.toString().isBlank()) {
                    return featureObj.toString();
                }
            }
            Object feature = response.get("feature");
            if (feature != null && !feature.toString().isBlank()) {
                return feature.toString();
            }
            return null;
        } catch (Exception e) {
            System.err.println("Warning: Python service failed. " + e.getMessage());
            return null;
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeMapper.findAll();
    }

    public void deleteEmployee(Integer id) {
        employeeMapper.delete(id);
    }

    public Map<String, Object> backfillMissingFeatures() {
        List<Employee> employees = employeeMapper.findWithoutFeature();
        int total = employees.size();
        int updated = 0;
        int skipped = 0;
        List<Integer> failedIds = new ArrayList<>();

        for (Employee employee : employees) {
            File photoFile = resolvePhotoFile(employee.getPhotoUrl());
            if (photoFile == null || !photoFile.exists() || !photoFile.isFile()) {
                skipped++;
                failedIds.add(employee.getId());
                continue;
            }
            String feature = extractFeature(photoFile);
            if (feature == null || feature.isBlank()) {
                skipped++;
                failedIds.add(employee.getId());
                continue;
            }
            int rows = employeeMapper.updateFeature(employee.getId(), feature);
            if (rows > 0) {
                updated++;
            } else {
                skipped++;
                failedIds.add(employee.getId());
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", total);
        result.put("updated", updated);
        result.put("skipped", skipped);
        result.put("failedIds", failedIds);
        return result;
    }

    private File resolvePhotoFile(String photoUrl) {
        if (photoUrl == null || photoUrl.isBlank()) {
            return null;
        }
        File direct = new File(photoUrl);
        if (direct.exists()) {
            return direct;
        }
        File uploadCandidate = Paths.get(uploadPath, photoUrl).toFile();
        if (uploadCandidate.exists()) {
            return uploadCandidate;
        }
        File uploadRoot = new File(uploadPath);
        File projectRoot = uploadRoot.getParentFile();
        if (projectRoot != null) {
            File dataRelativeCandidate = new File(projectRoot, photoUrl);
            if (dataRelativeCandidate.exists()) {
                return dataRelativeCandidate;
            }
            File faceCandidate = new File(new File(uploadRoot, "faces"), photoUrl);
            if (faceCandidate.exists()) {
                return faceCandidate;
            }
        }
        return null;
    }
}
