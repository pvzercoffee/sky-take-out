package com.sky.mapper;

import com.sky.entity.AddressBook;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 新增地址
     * @param addressBook
     */
    @Insert("insert into address_book(user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label) VALUES " +
            "(#{userId},#{consignee},#{sex},#{phone},#{provinceCode},#{provinceName},#{cityCode},#{cityName},#{districtCode},#{districtName},#{detail},#{label})")
    void insert(AddressBook addressBook);


    /**
     * 查询当前用户所有地址
     * @param userId
     * @return
     */
    @Select("select * from address_book where user_id = #{userId}")
    List<AddressBook> list(Long userId);

    @Update("update address_book set is_default = 0 where user_id = #{userId} and is_default = 1")
    void clearDefault(Long userId);

    @Update("update address_book set is_default = 1 where user_id = #{userId} and id = #{id}")
    void setDefault(Long userId,Long id);

    @Select("select * from address_book where user_id and is_default = #{status}")
    AddressBook get(Long userId, Integer status);
}
