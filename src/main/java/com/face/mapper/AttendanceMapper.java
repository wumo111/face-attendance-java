package com.face.mapper;

import com.face.entity.AttendanceRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import java.util.List;
import java.util.Map;

@Mapper
public interface AttendanceMapper {

    @Insert("INSERT INTO attendance_record(employee_id, timestamp, status) " +
            "VALUES(#{employeeId}, #{timestamp}, #{status})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    int insert(AttendanceRecord record);

    @Select("SELECT * FROM attendance_record ORDER BY timestamp DESC LIMIT #{limit} OFFSET #{offset}")
    List<AttendanceRecord> findAll(int offset, int limit);

    @Select("SELECT COUNT(*) FROM attendance_record")
    int countAll();

    @Select("SELECT a.*, e.name AS employeeName FROM attendance_record a " +
            "LEFT JOIN employee e ON a.employee_id = e.id " +
            "ORDER BY a.timestamp DESC LIMIT #{limit} OFFSET #{offset}")
    List<Map<String, Object>> findAllWithName(int offset, int limit);

    @Select("SELECT COUNT(*) FROM attendance_record a LEFT JOIN employee e ON a.employee_id = e.id")
    int countAllWithName();

    @Select("SELECT COUNT(DISTINCT employee_id) FROM attendance_record WHERE DATE(timestamp) = CURDATE()")
    int countTodayDistinct();
}
