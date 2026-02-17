package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     * @param addressBook
     */
    @Override
    public void save(AddressBook addressBook) {
        if(addressBook.getDetail() == null || addressBook.getPhone() == null || addressBook.getSex() == null)
        {
            throw new AddressBookBusinessException("缺少必要地址信息");
        }
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.insert(addressBook);
    }

    /**
     * 查询当前登录用户的所有地址
     * @return
     */
    @Override
    public List<AddressBook> list() {

        Long userId = BaseContext.getCurrentId();
        List<AddressBook> addressBooks = addressBookMapper.list(userId);

        return addressBooks;
    }

    /**
     * 查询默认地址
     * @return
     */
    @Override
    public AddressBook queryDefault() {
        Long userId = BaseContext.getCurrentId();
        return addressBookMapper.get(userId, StatusConstant.ENABLE);
    }

    /**
     * 根据id修改地址
     * @param addressBook
     */
    @Override
    public void modifyById(AddressBook addressBook) {

    }

    /**
     * 根据id删除地址
     * @param id
     */
    @Override
    public void deleteById(Long id) {

    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @Override
    public AddressBook queryById(Long id) {
        return null;
    }

    /**
     * 设置默认地址
     * @param id
     */
    @Override
    public void setDefault(Long id) {
        Long userId = BaseContext.getCurrentId();
        //清除原来的默认地址
        addressBookMapper.clearDefault(userId);

        //创建新的默认地址
        addressBookMapper.setDefault(userId,id);
    }
}
