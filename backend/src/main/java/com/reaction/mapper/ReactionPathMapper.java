package com.reaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reaction.entity.ReactionPath;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 反应路径Mapper接口
 */

public interface ReactionPathMapper extends BaseMapper<ReactionPath> {
    
    @Select("SELECT * FROM reaction_paths WHERE from_compound_id = #{fromCompoundId}")
    List<ReactionPath> selectByFromCompound(Long fromCompoundId);
}
