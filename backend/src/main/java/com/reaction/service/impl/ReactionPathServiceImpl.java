package com.reaction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reaction.entity.ReactionPath;
import com.reaction.mapper.ReactionPathMapper;
import com.reaction.service.ReactionPathService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 反应路径Service实现类
 */
@Service
public class ReactionPathServiceImpl extends ServiceImpl<ReactionPathMapper, ReactionPath> implements ReactionPathService {
    
    @Override
    public ReactionPath addReactionPath(ReactionPath reactionPath) {
        reactionPath.setCreatedAt(LocalDateTime.now());
        reactionPath.setUpdatedAt(LocalDateTime.now());
        save(reactionPath);
        return reactionPath;
    }
    
    @Override
    public ReactionPath updateReactionPath(ReactionPath reactionPath) {
        reactionPath.setUpdatedAt(LocalDateTime.now());
        updateById(reactionPath);
        return reactionPath;
    }
    
    @Override
    public boolean deleteReactionPath(Long id) {
        return removeById(id);
    }
    
    @Override
    public List<ReactionPath> getAllReactionPaths() {
        return list();
    }
    
    @Override
    public List<ReactionPath> getPathsByFromCompound(Long fromCompoundId) {
        return baseMapper.selectByFromCompound(fromCompoundId);
    }
}
