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
 * 化合物实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("compounds")
public class Compound {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String formula;
    
    private String description;
    
    private BigDecimal molarMass;
    
    private BigDecimal energy;
    
    private String imageUrl;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
