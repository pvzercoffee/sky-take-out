package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "C端-地址簿相关接口")
@RestController
@Slf4j
@RequestMapping("/user/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     * @param addressBook 地址信息
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result save(@RequestBody AddressBook addressBook){
        log.info("新增地址:{}",addressBook);
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 查询当前用户所有地址
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前用户所有地址")
    public Result<List<AddressBook>> list(){
        log.info("查询当前用户所有地址");
        List<AddressBook> records = addressBookService.list();
        return Result.success(records);
    }

    /**
     * 设置默认地址
     * @param id
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    //TODO:封装成DTO
    public Result setDefault(@RequestBody Map<String,Long> id){
        log.info("设置默认地址:{}",id);
        addressBookService.setDefault(id.get("id"));
        return Result.success();
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault(){
        log.info("查询默认地址...");
        AddressBook addressBook = addressBookService.queryDefault();
        return Result.success(addressBook);
    }

    /**
     * 根据id查询地址信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址信息")
    public Result<AddressBook> queryById(@PathVariable Long id){
        log.info("根据id查询地址信息:{}",id);

        AddressBook addressBook = addressBookService.queryById(id);
        return Result.success(addressBook);
    }

    @PutMapping
    @ApiOperation("根据id修改地址信息")
    public Result modifyById(@RequestBody AddressBook addressBook){
        log.info("根据id修改地址信息:{}",addressBook);

        addressBookService.modifyById(addressBook);

        return Result.success();
    }
}
