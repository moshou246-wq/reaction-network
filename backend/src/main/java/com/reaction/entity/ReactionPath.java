package com.reaction.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 反应路径实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("reaction_paths")
public class ReactionPath {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long fromCompoundId;
    
    private Long toCompoundId;
    
    private String reactionType;
    
    private BigDecimal energyChange;
    
    private BigDecimal activationEnergy;
    
    private String description;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
