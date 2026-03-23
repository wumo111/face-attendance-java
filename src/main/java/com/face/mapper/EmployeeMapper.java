package com.face.mapper;

import com.face.entity.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface EmployeeMapper {

    @Insert("INSERT INTO employee(name, gender, id_card, address, phone, photo_url, feature, create_time) " +
            "VALUES(#{name}, #{gender}, #{idCard}, #{address}, #{phone}, #{photoUrl}, #{feature}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Employee employee);

    @Delete("DELETE FROM employee WHERE id = #{id}")
    int delete(Integer id);

    @Select("SELECT * FROM employee")
    List<Employee> findAll();

    @Select("SELECT COUNT(*) FROM employee")
    int countAll();

    @Select("SELECT * FROM employee WHERE id = #{id}")
    Employee findById(Integer id);

    @Select("SELECT * FROM employee WHERE feature IS NULL OR TRIM(feature) = ''")
    List<Employee> findWithoutFeature();

    @Update("UPDATE employee SET feature = #{feature} WHERE id = #{id}")
    int updateFeature(@Param("id") Integer id, @Param("feature") String feature);
}
