package com.face.controller;

import com.face.common.Result;
import com.face.entity.CaptureRecord;
import com.face.mapper.CaptureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/capture")
public class CaptureController {

    @Autowired
    private CaptureMapper captureMapper;

    @PostMapping("/save")
    public Result<String> saveCapture(@RequestBody CaptureRecord record) {
        if (record.getTimestamp() == null) {
            record.setTimestamp(new Date());
        }
        captureMapper.insert(record);
        return Result.success("Saved");
    }

    @GetMapping("/list")
    public Result<Map<String, Object>> listCaptures(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        int offset = (page - 1) * size;
        List<Map<String, Object>> list = captureMapper.findAllWithName(offset, size);
        int total = captureMapper.countAllWithName();
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return Result.success(result);
    }
}
