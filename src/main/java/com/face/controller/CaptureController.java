package com.face.controller;

import com.face.common.Result;
import com.face.entity.CaptureRecord;
import com.face.mapper.CaptureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

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
    public Result<List<CaptureRecord>> listCaptures(@RequestParam(defaultValue = "1") int page, 
                                                    @RequestParam(defaultValue = "10") int size) {
        int offset = (page - 1) * size;
        return Result.success(captureMapper.findAll(offset, size));
    }
}
