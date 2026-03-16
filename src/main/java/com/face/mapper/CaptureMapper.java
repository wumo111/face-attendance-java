package com.face.mapper;

import com.face.entity.CaptureRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CaptureMapper {

    @Insert("INSERT INTO capture_record(employee_id, timestamp, image_url, score) " +
            "VALUES(#{employeeId}, #{timestamp}, #{imageUrl}, #{score})")
    int insert(CaptureRecord record);

    @Select("SELECT * FROM capture_record ORDER BY timestamp DESC LIMIT #{limit} OFFSET #{offset}")
    List<CaptureRecord> findAll(int offset, int limit);
}
