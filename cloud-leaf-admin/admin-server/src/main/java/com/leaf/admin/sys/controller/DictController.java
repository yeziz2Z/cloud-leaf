package com.leaf.admin.sys.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leaf.admin.sys.entity.SysDictData;
import com.leaf.admin.sys.entity.SysDictType;
import com.leaf.admin.sys.service.ISysDictDataService;
import com.leaf.admin.sys.service.ISysDictTypeService;
import com.leaf.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liuk
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    ISysDictTypeService dictTypeService;
    @Autowired
    ISysDictDataService dictDataService;

    @GetMapping("/type/{id}")
    public Result<SysDictType> getDictTypeById(@PathVariable("id") Long id) {
        return Result.success(dictTypeService.getById(id));
    }

    @GetMapping("/type/page")
    public Result<Page<SysDictType>> page(Page page, String name, String type) {
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), SysDictType::getName, name)
                .eq(StrUtil.isNotBlank(type), SysDictType::getType, type)
                .orderByAsc(SysDictType::getId);
        dictTypeService.page(page, wrapper);
        return Result.success(page);
    }

    @PostMapping("/type")
    public Result addDictType(@RequestBody SysDictType sysDictType) {
        dictTypeService.save(sysDictType);
        return Result.success();
    }

    @PutMapping("/type")
    public Result editDictType(@RequestBody SysDictType sysDictType) {
        dictTypeService.updateById(sysDictType);
        return Result.success();
    }

    @DeleteMapping("/type/{dictTypeIds}")
    public Result deleteByDictTypeIds(@PathVariable("dictTypeIds") List<Long> dictTypeIds) {
        dictTypeService.removeByIds(dictTypeIds);
        return Result.success();
    }

    @GetMapping("/data/{id}")
    public Result<SysDictData> getDictDataById(@PathVariable("id") Long id) {
        return Result.success(dictDataService.getById(id));
    }

    @GetMapping("/data/type/{type}")
    public Result<List<SysDictData>> getByDictType(@PathVariable("type") String type) {
        QueryWrapper<SysDictData> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type).orderByAsc("sort");
        return Result.success(dictDataService.list(wrapper));
    }

    @PostMapping("/data")
    public Result addDictData(@RequestBody SysDictData sysDictData) {
        dictDataService.save(sysDictData);
        return Result.success();
    }

    @PutMapping("/data")
    public Result editDictData(@RequestBody SysDictData sysDictData) {
        dictDataService.updateById(sysDictData);
        return Result.success();
    }

    @DeleteMapping("/data/{dictDataId}")
    public Result deleteByDictDataIds(@PathVariable("dictDataId") Long dictDataId) {
        dictDataService.removeById(dictDataId);
        return Result.success();
    }

    @GetMapping("/data/options/{type}")
    public Result<List<Map<Object, Object>>> getDictDataOptions(@PathVariable("type") String type) {
        return Result.success(dictDataService.getDictDataEnums(type));
    }

}
