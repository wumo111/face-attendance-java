package com.face.mapper;

import com.face.entity.AttendanceRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface AttendanceMapper {

    @Insert("INSERT INTO attendance_record(employee_id, timestamp, status) " +
            "VALUES(#{employeeId}, #{timestamp}, #{status})")
    int insert(AttendanceRecord record);

    @Select("SELECT * FROM attendance_record ORDER BY timestamp DESC LIMIT #{limit} OFFSET #{offset}")
    List<AttendanceRecord> findAll(int offset, int limit);
}
