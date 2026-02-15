package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "C端-分类相关接口")
@RestController("userCategoryController")
@Slf4j
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 分类条件查询
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("分类条件查询")
    public Result<List<Category>> list(Integer type){
        log.info("分类条件查询:{}",type);

        List<Category> records = categoryService.list(type);
        return Result.success(records);
    }
}
