package com.face.controller;

import com.face.common.Result;
import com.face.entity.AttendanceRecord;
import com.face.mapper.AttendanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @PostMapping("/record")
    public Result<String> saveAttendance(@RequestBody AttendanceRecord record) {
        if (record.getTimestamp() == null) {
            record.setTimestamp(new Date());
        }
        // Ideally should check for duplicate punch-in within short time
        attendanceMapper.insert(record);
        return Result.success("Recorded");
    }

    @GetMapping("/list")
    public Result<Map<String, Object>> listAttendance(@RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        int offset = (page - 1) * size;
        List<Map<String, Object>> list = attendanceMapper.findAllWithName(offset, size);
        int total = attendanceMapper.countAllWithName();
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return Result.success(result);
    }
}
