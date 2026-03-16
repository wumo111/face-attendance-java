package com.face.controller;

import com.face.common.Result;
import com.face.entity.Employee;
import com.face.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 添加员工
     * @param name 姓名
     * @param gender 性别 (1:男, 2:女)
     * @param idCard 身份证
     * @param address 地址
     * @param phone 电话
     * @param photo 照片文件
     */
    @PostMapping("/add")
    public Result<String> addEmployee(
            @RequestParam("name") String name,
            @RequestParam("gender") Integer gender,
            @RequestParam("idCard") String idCard,
            @RequestParam("address") String address,
            @RequestParam("phone") String phone,
            @RequestParam("photo") MultipartFile photo,
            HttpServletRequest request) {
        if (!isAdmin(request)) {
            return Result.error(403, "Permission denied");
        }
        
        try {
            employeeService.addEmployee(name, gender, idCard, address, phone, photo);
            return Result.success("Employee added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "Failed to add employee: " + e.getMessage());
        }
    }

    /**
     * 查询所有员工
     */
    @GetMapping("/list")
    public Result<List<Employee>> listEmployees() {
        return Result.success(employeeService.getAllEmployees());
    }

    /**
     * 删除员工 (管理员权限)
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteEmployee(@PathVariable Integer id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return Result.error(403, "Permission denied");
        }

        try {
            employeeService.deleteEmployee(id);
            return Result.success("Deleted successfully");
        } catch (Exception e) {
            return Result.error(500, "Failed to delete: " + e.getMessage());
        }
    }

    /**
     * 供Python调用：获取所有特征
     */
    @GetMapping("/features")
    public Result<List<Map<String, Object>>> getFeatures() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<Map<String, Object>> features = employees.stream().map(e -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", e.getId());
            item.put("name", e.getName());
            item.put("feature", e.getFeature());
            return item;
        }).collect(Collectors.toList());
        return Result.success(features);
    }

    private boolean isAdmin(HttpServletRequest request) {
        Object role = request.getAttribute("role");
        return role != null && "ADMIN".equals(role.toString());
    }
}
