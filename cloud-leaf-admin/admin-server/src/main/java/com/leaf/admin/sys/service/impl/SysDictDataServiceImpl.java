package com.leaf.admin.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.leaf.admin.common.AdminSystemConst;
import com.leaf.admin.sys.entity.SysDictData;
import com.leaf.admin.sys.mapper.SysDictDataMapper;
import com.leaf.admin.sys.service.ISysDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<Map<Object, Object>> getDictDataEnums(String type) {
        String key = AdminSystemConst.DICT_TYPE_CACHE + type;
        List<Map<Object, Object>> res = (List<Map<Object, Object>>) redisTemplate.opsForValue().get(key);
        if (CollectionUtil.isNotEmpty(res)) {
            return res;
        }
        LambdaQueryWrapper<SysDictData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictData::getType, type)
                .orderByAsc(SysDictData::getSort);
        List<SysDictData> list = this.list(wrapper);
        res = list
                .stream()
                .map(sysDictData -> MapUtil.builder().put("label", sysDictData.getLabel()).put("value", sysDictData.getValue()).build())
                .collect(Collectors.toList());
        redisTemplate.opsForValue().set(key, res, 1, TimeUnit.HOURS);
        return res;
    }

    @Override
    public void clearDictDataCache(Collection<String> types) {
        List<String> keys = types.stream().map(type -> AdminSystemConst.DICT_TYPE_CACHE + type).collect(Collectors.toList());
        redisTemplate.delete(keys);
    }

    @Override
    public void clearDictDataCache(String type) {
        redisTemplate.delete(AdminSystemConst.DICT_TYPE_CACHE + type);
    }

    @Override
    public boolean save(SysDictData entity) {
        clearDictDataCache(entity.getType());
        return super.save(entity);
    }

    @Override
    public boolean updateById(SysDictData entity) {
        clearDictDataCache(entity.getType());
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        clearDictDataCache(this.getById(id).getType());
        return super.removeById(id);
    }
}
