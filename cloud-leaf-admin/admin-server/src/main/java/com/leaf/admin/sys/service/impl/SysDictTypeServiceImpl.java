package com.leaf.admin.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.leaf.admin.sys.entity.SysDictData;
import com.leaf.admin.sys.entity.SysDictType;
import com.leaf.admin.sys.mapper.SysDictTypeMapper;
import com.leaf.admin.sys.service.ISysDictDataService;
import com.leaf.admin.sys.service.ISysDictTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典类型表 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    @Autowired
    ISysDictDataService dictDataService;

    @Transactional
    @Override
    public boolean removeByIds(Collection list) {
        List<SysDictType> sysDictTypes = this.listByIds(list);
        List<String> types = sysDictTypes.stream().map(SysDictType::getType).collect(Collectors.toList());
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CollectionUtil.isNotEmpty(types), SysDictData::getType, types);
        dictDataService.remove(wrapper);

        dictDataService.clearDictDataCache(types);

        return super.removeByIds(list);
    }

    @Override
    public boolean updateById(SysDictType entity) {
        dictDataService.clearDictDataCache(entity.getType());
        return super.updateById(entity);
    }


}
