package com.face.controller;

import com.face.common.Result;
import com.face.mapper.AttendanceMapper;
import com.face.mapper.CaptureMapper;
import com.face.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private CaptureMapper captureMapper;

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardStats() {
        int totalEmployees = employeeMapper.countAll();
        int totalAttendance = attendanceMapper.countTodayDistinct();
        // 统计迟到人数（status = 1）
        // 这里简化处理，实际可以通过SQL统计
        Map<String, Object> result = new HashMap<>();
        result.put("total", totalEmployees);
        result.put("actual", totalAttendance);
        result.put("absent", Math.max(totalEmployees - totalAttendance, 0));
        result.put("late", 0);
        return Result.success(result);
    }
}