package com.reaction.controller;

import com.reaction.dto.ApiResponse;
import com.reaction.entity.Compound;
import com.reaction.service.CompoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 化合物控制器
 */
@RestController
@RequestMapping("/api/compounds")
@CrossOrigin(origins = "*")
public class CompoundController {
    
    @Autowired
    private CompoundService compoundService;
    
    /**
     * 获取所有化合物
     */
    @GetMapping
    public ApiResponse<List<Compound>> getAllCompounds() {
        try {
            List<Compound> compounds = compoundService.list();
            return ApiResponse.success("Get all compounds successful", compounds);
        } catch (Exception e) {
            return ApiResponse.error("Get all compounds failed: " + e.getMessage());
        }
    }
    
    /**
     * 搜索化合物
     */
    @GetMapping("/search")
    public ApiResponse<List<Compound>> searchCompounds(@RequestParam String keyword) {
        try {
            List<Compound> compounds = compoundService.searchCompounds(keyword);
            return ApiResponse.success("Search compounds successful", compounds);
        } catch (Exception e) {
            return ApiResponse.error("Search compounds failed: " + e.getMessage());
        }
    }
    
    /**
     * 获取化合物详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Compound> getCompound(@PathVariable Long id) {
        try {
            Compound compound = compoundService.getById(id);
            if (compound != null) {
                return ApiResponse.success("Get compound successful", compound);
            }
            return ApiResponse.error("Compound not found");
        } catch (Exception e) {
            return ApiResponse.error("Get compound failed: " + e.getMessage());
        }
    }
    
    /**
     * 新增化合物
     */
    @PostMapping
    public ApiResponse<Compound> addCompound(@RequestBody Compound compound) {
        try {
            Compound newCompound = compoundService.addCompound(compound);
            return ApiResponse.success("Add compound successful", newCompound);
        } catch (Exception e) {
            return ApiResponse.error("Add compound failed: " + e.getMessage());
        }
    }
    
    /**
     * 编辑化合物
     */
    @PutMapping("/{id}")
    public ApiResponse<Compound> updateCompound(@PathVariable Long id, @RequestBody Compound compound) {
        try {
            compound.setId(id);
            Compound updatedCompound = compoundService.updateCompound(compound);
            return ApiResponse.success("Update compound successful", updatedCompound);
        } catch (Exception e) {
            return ApiResponse.error("Update compound failed: " + e.getMessage());
        }
    }
    
    /**
     * 删除化合物
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCompound(@PathVariable Long id) {
        try {
            boolean success = compoundService.deleteCompound(id);
            if (success) {
                return ApiResponse.success("Delete compound successful", null);
            }
            return ApiResponse.error("Delete compound failed");
        } catch (Exception e) {
            return ApiResponse.error("Delete compound failed: " + e.getMessage());
        }
    }
}
