package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 新增地址
     * @param addressBook
     */
    void save(AddressBook addressBook);

    /**
     * 查询当前登录用户的所有地址
     * @return
     */
    List<AddressBook> list();


    /**
     * 查询默认地址
     * @return
     */
    AddressBook queryDefault();

    /**
     * 根据id修改地址
     * @param addressBook
     */
    void modifyById(AddressBook addressBook);

    /**
     * 根据id删除地址
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    AddressBook queryById(Long id);

    /**
     * 设置默认地址
     * @param id
     */
    void setDefault(Long id);

}
