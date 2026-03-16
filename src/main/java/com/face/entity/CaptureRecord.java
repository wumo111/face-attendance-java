package com.face.entity;

import lombok.Data;
import java.util.Date;

@Data
public class CaptureRecord {
    private Integer id;
    private Integer employeeId;
    private Date timestamp;
    private String imageUrl;
    private Float score;
}
