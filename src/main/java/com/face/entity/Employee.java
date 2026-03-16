package com.face.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Employee {
    private Integer id;
    private String name;
    private Integer gender; // 1: Male, 2: Female
    private String idCard;
    private String address;
    private String phone;
    private String photoUrl;
    private String feature; // Feature vector as string
    private Date createTime;
}
