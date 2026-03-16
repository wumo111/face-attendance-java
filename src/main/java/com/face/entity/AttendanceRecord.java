package com.face.entity;

import lombok.Data;
import java.util.Date;

@Data
public class AttendanceRecord {
    private Integer id;
    private Integer employeeId;
    private Date timestamp;
    private Integer status; // 0: Normal, 1: Late, 2: Early Leave
}
