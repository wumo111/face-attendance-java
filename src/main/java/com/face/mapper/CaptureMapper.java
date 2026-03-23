package com.face.mapper;

import com.face.entity.CaptureRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface CaptureMapper {

    @Insert("INSERT INTO capture_record(employee_id, timestamp, image_url, score) " +
            "VALUES(#{employeeId}, #{timestamp}, #{imageUrl}, #{score})")
    int insert(CaptureRecord record);

    @Select("SELECT * FROM capture_record ORDER BY timestamp DESC LIMIT #{limit} OFFSET #{offset}")
    List<CaptureRecord> findAll(int offset, int limit);

    @Select("SELECT COUNT(*) FROM capture_record")
    int countAll();

    @Select("SELECT c.id, c.employee_id AS employeeId, c.timestamp, c.image_url AS imageUrl, c.score, e.name AS employeeName " +
            "FROM capture_record c " +
            "LEFT JOIN employee e ON c.employee_id = e.id " +
            "ORDER BY c.timestamp DESC LIMIT #{limit} OFFSET #{offset}")
    List<Map<String, Object>> findAllWithName(int offset, int limit);

    @Select("SELECT COUNT(*) FROM capture_record c LEFT JOIN employee e ON c.employee_id = e.id")
    int countAllWithName();
}
