package com.reaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reaction.entity.Compound;
import com.reaction.mapper.CompoundMapper;
import com.reaction.service.CompoundService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 化合物Service实现类
 */
@Service
public class CompoundServiceImpl extends ServiceImpl<CompoundMapper, Compound> implements CompoundService {
    
    @Override
    public List<Compound> searchCompounds(String keyword) {
        QueryWrapper<Compound> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", keyword)
                .or()
                .like("formula", keyword);
        return list(queryWrapper);
    }
    
    @Override
    public Compound addCompound(Compound compound) {
        compound.setCreatedAt(LocalDateTime.now());
        compound.setUpdatedAt(LocalDateTime.now());
        save(compound);
        return compound;
    }
    
    @Override
    public Compound updateCompound(Compound compound) {
        compound.setUpdatedAt(LocalDateTime.now());
        updateById(compound);
        return compound;
    }
    
    @Override
    public boolean deleteCompound(Long id) {
        return removeById(id);
    }
}
