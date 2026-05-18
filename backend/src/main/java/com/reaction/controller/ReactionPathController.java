package com.reaction.controller;

import com.reaction.algorithm.GraphAlgorithm;
import com.reaction.dto.ApiResponse;
import com.reaction.entity.ReactionPath;
import com.reaction.service.ReactionPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反应路径控制器
 */
@RestController
@RequestMapping("/api/reaction-paths")
@CrossOrigin(origins = "*")
public class ReactionPathController {
    
    @Autowired
    private ReactionPathService reactionPathService;
    
    /**
     * 获取所有反应路径
     */
    @GetMapping
    public ApiResponse<List<ReactionPath>> getAllReactionPaths() {
        try {
            List<ReactionPath> paths = reactionPathService.getAllReactionPaths();
            return ApiResponse.success("Get all reaction paths successful", paths);
        } catch (Exception e) {
            return ApiResponse.error("Get all reaction paths failed: " + e.getMessage());
        }
    }
    
    /**
     * 获取反应路径详情
     */
    @GetMapping("/{id}")
    public ApiResponse<ReactionPath> getReactionPath(@PathVariable Long id) {
        try {
            ReactionPath path = reactionPathService.getById(id);
            if (path != null) {
                return ApiResponse.success("Get reaction path successful", path);
            }
            return ApiResponse.error("Reaction path not found");
        } catch (Exception e) {
            return ApiResponse.error("Get reaction path failed: " + e.getMessage());
        }
    }
    
    /**
     * 新增反应路径
     */
    @PostMapping
    public ApiResponse<ReactionPath> addReactionPath(@RequestBody ReactionPath reactionPath) {
        try {
            ReactionPath newPath = reactionPathService.addReactionPath(reactionPath);
            return ApiResponse.success("Add reaction path successful", newPath);
        } catch (Exception e) {
            return ApiResponse.error("Add reaction path failed: " + e.getMessage());
        }
    }
    
    /**
     * 修改反应路径
     */
    @PutMapping("/{id}")
    public ApiResponse<ReactionPath> updateReactionPath(@PathVariable Long id, @RequestBody ReactionPath reactionPath) {
        try {
            reactionPath.setId(id);
            ReactionPath updatedPath = reactionPathService.updateReactionPath(reactionPath);
            return ApiResponse.success("Update reaction path successful", updatedPath);
        } catch (Exception e) {
            return ApiResponse.error("Update reaction path failed: " + e.getMessage());
        }
    }
    
    /**
     * 删除反应路径
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteReactionPath(@PathVariable Long id) {
        try {
            boolean success = reactionPathService.deleteReactionPath(id);
            if (success) {
                return ApiResponse.success("Delete reaction path successful", null);
            }
            return ApiResponse.error("Delete reaction path failed");
        } catch (Exception e) {
            return ApiResponse.error("Delete reaction path failed: " + e.getMessage());
        }
    }
    
    /**
     * 查找最短路径（BFS）
     */
    @GetMapping("/shortest-path")
    public ApiResponse<Map<String, Object>> findShortestPath(
            @RequestParam Long startId,
            @RequestParam Long endId) {
        try {
            List<ReactionPath> allPaths = reactionPathService.getAllReactionPaths();
            List<Long> path = GraphAlgorithm.findShortestPathBFS(startId, endId, allPaths);
            
            Map<String, Object> result = new HashMap<>();
            result.put("path", path);
            result.put("length", path.size());
            
            return ApiResponse.success("Find shortest path successful", result);
        } catch (Exception e) {
            return ApiResponse.error("Find shortest path failed: " + e.getMessage());
        }
    }
    
    /**
     * 查找最低能量路径（Dijkstra）
     */
    @GetMapping("/lowest-energy-path")
    public ApiResponse<Map<String, Object>> findLowestEnergyPath(
            @RequestParam Long startId,
            @RequestParam Long endId) {
        try {
            List<ReactionPath> allPaths = reactionPathService.getAllReactionPaths();
            List<Long> path = GraphAlgorithm.findLowestEnergyPathDijkstra(startId, endId, allPaths);
            
            Map<String, Object> result = new HashMap<>();
            result.put("path", path);
            result.put("length", path.size());
            
            return ApiResponse.success("Find lowest energy path successful", result);
        } catch (Exception e) {
            return ApiResponse.error("Find lowest energy path failed: " + e.getMessage());
        }
    }
    
    /**
     * 查找所有优化路径（多目标优化）
     * 返回：步数最少、能量变化最低、活化能最低三条路径
     */
    @GetMapping("/all-optimized-paths")
    public ApiResponse<GraphAlgorithm.MultiPathResult> findAllOptimizedPaths(
            @RequestParam Long startId,
            @RequestParam Long endId) {
        try {
            List<ReactionPath> allPaths = reactionPathService.getAllReactionPaths();
            GraphAlgorithm.MultiPathResult result = GraphAlgorithm.findAllOptimizedPaths(startId, endId, allPaths);
            
            return ApiResponse.success("Find all optimized paths successful", result);
        } catch (Exception e) {
            return ApiResponse.error("Find all optimized paths failed: " + e.getMessage());
        }
    }
}
