package com.reaction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reaction.entity.ReactionPath;
import java.util.List;

/**
 * 反应路径Service接口
 */
public interface ReactionPathService extends IService<ReactionPath> {
    
    /**
     * 添加反应路径
     */
    ReactionPath addReactionPath(ReactionPath reactionPath);
    
    /**
     * 更新反应路径
     */
    ReactionPath updateReactionPath(ReactionPath reactionPath);
    
    /**
     * 删除反应路径
     */
    boolean deleteReactionPath(Long id);
    
    /**
     * 获取所有反应路径
     */
    List<ReactionPath> getAllReactionPaths();
    
    /**
     * 根据起始化合物获取反应路径
     */
    List<ReactionPath> getPathsByFromCompound(Long fromCompoundId);
}
