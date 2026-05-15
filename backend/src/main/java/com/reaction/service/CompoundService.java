package com.reaction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reaction.entity.Compound;
import java.util.List;

/**
 * 化合物Service接口
 */
public interface CompoundService extends IService<Compound> {
    
    /**
     * 搜索化合物
     */
    List<Compound> searchCompounds(String keyword);
    
    /**
     * 添加化合物
     */
    Compound addCompound(Compound compound);
    
    /**
     * 更新化合物
     */
    Compound updateCompound(Compound compound);
    
    /**
     * 删除化合物
     */
    boolean deleteCompound(Long id);
}
