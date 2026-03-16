package com.face;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.face.mapper")
public class FaceAttendanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FaceAttendanceApplication.class, args);
    }
}
