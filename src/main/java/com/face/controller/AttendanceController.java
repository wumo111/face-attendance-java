package com.face.controller;

import com.face.common.Result;
import com.face.entity.AttendanceRecord;
import com.face.mapper.AttendanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

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
    public Result<List<AttendanceRecord>> listAttendance(@RequestParam(defaultValue = "1") int page, 
                                                         @RequestParam(defaultValue = "10") int size) {
        int offset = (page - 1) * size;
        return Result.success(attendanceMapper.findAll(offset, size));
    }
}
