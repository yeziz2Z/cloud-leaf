package com.leaf.admin.sys.service;

import com.leaf.admin.sys.entity.SysDictData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
public interface ISysDictDataService extends IService<SysDictData> {


    List<Map<Object, Object>> getDictDataEnums(String type);

    void clearDictDataCache(Collection<String> types);

    void clearDictDataCache(String type);
}
