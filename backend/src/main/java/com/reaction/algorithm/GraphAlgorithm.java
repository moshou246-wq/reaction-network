package com.reaction.algorithm;

import com.reaction.entity.ReactionPath;
import java.math.BigDecimal;
import java.util.*;

/**
 * 图论算法工具类
 */
public class GraphAlgorithm {
    
    /**
     * BFS最短路径算法
     * @param startId 起始节点ID
     * @param endId 目标节点ID
     * @param paths 所有反应路径
     * @return 最短路径（节点ID列表）
     */
    public static List<Long> findShortestPathBFS(Long startId, Long endId, List<ReactionPath> paths) {
        // 构建邻接表
        Map<Long, List<Long>> graph = buildGraph(paths);
        
        if (!graph.containsKey(startId)) {
            return new ArrayList<>();
        }
        
        Queue<Long> queue = new LinkedList<>();
        Map<Long, Long> parent = new HashMap<>();
        Set<Long> visited = new HashSet<>();
        
        queue.add(startId);
        visited.add(startId);
        parent.put(startId, null);
        
        while (!queue.isEmpty()) {
            Long current = queue.poll();
            
            if (current.equals(endId)) {
                return reconstructPath(parent, endId);
            }
            
            List<Long> neighbors = graph.getOrDefault(current, new ArrayList<>());
            for (Long neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    parent.put(neighbor, current);
                }
            }
        }
        
        return new ArrayList<>(); // 没有路径
    }
    
    /**
     * Dijkstra最低能量路径算法
     * @param startId 起始节点ID
     * @param endId 目标节点ID
     * @param paths 所有反应路径
     * @return 最低能量路径（节点ID列表）
     */
    public static List<Long> findLowestEnergyPathDijkstra(Long startId, Long endId, List<ReactionPath> paths) {
        // 构建带权图
        Map<Long, List<EdgeWithEnergy>> graph = buildWeightedGraph(paths);
        
        if (!graph.containsKey(startId)) {
            return new ArrayList<>();
        }
        
        Map<Long, BigDecimal> distances = new HashMap<>();
        Map<Long, Long> parent = new HashMap<>();
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
        Set<Long> visited = new HashSet<>();
        
        // 初始化距离
        for (Long node : graph.keySet()) {
            distances.put(node, new BigDecimal(Double.MAX_VALUE));
        }
        distances.put(startId, BigDecimal.ZERO);
        
        pq.add(new NodeDistance(startId, BigDecimal.ZERO));
        parent.put(startId, null);
        
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            Long currentNode = current.nodeId;
            
            if (visited.contains(currentNode)) {
                continue;
            }
            visited.add(currentNode);
            
            if (currentNode.equals(endId)) {
                return reconstructPath(parent, endId);
            }
            
            List<EdgeWithEnergy> neighbors = graph.getOrDefault(currentNode, new ArrayList<>());
            for (EdgeWithEnergy edge : neighbors) {
                Long neighbor = edge.toNode;
                BigDecimal newDistance = current.distance.add(edge.energy);
                
                if (newDistance.compareTo(distances.get(neighbor)) < 0) {
                    distances.put(neighbor, newDistance);
                    parent.put(neighbor, currentNode);
                    pq.add(new NodeDistance(neighbor, newDistance));
                }
            }
        }
        
        if (distances.get(endId).compareTo(new BigDecimal(Double.MAX_VALUE)) == 0) {
            return new ArrayList<>(); // 没有路径
        }
        
        return reconstructPath(parent, endId);
    }
    
    /**
     * 构建无权邻接表
     */
    private static Map<Long, List<Long>> buildGraph(List<ReactionPath> paths) {
        Map<Long, List<Long>> graph = new HashMap<>();
        
        for (ReactionPath path : paths) {
            graph.computeIfAbsent(path.getFromCompoundId(), k -> new ArrayList<>())
                    .add(path.getToCompoundId());
        }
        
        return graph;
    }
    
    /**
     * 构建带权邻接表
     */
    private static Map<Long, List<EdgeWithEnergy>> buildWeightedGraph(List<ReactionPath> paths) {
        Map<Long, List<EdgeWithEnergy>> graph = new HashMap<>();
        
        for (ReactionPath path : paths) {
            BigDecimal energy = path.getEnergyChange() != null ? 
                    path.getEnergyChange() : BigDecimal.ZERO;
            
            graph.computeIfAbsent(path.getFromCompoundId(), k -> new ArrayList<>())
                    .add(new EdgeWithEnergy(path.getToCompoundId(), energy));
        }
        
        return graph;
    }
    
    /**
     * 重建路径
     */
    private static List<Long> reconstructPath(Map<Long, Long> parent, Long endId) {
        List<Long> path = new ArrayList<>();
        Long current = endId;
        
        while (current != null) {
            path.add(0, current);
            current = parent.get(current);
        }
        
        return path;
    }
    
    /**
     * 带权边内部类
     */
    private static class EdgeWithEnergy {
        Long toNode;
        BigDecimal energy;
        
        EdgeWithEnergy(Long toNode, BigDecimal energy) {
            this.toNode = toNode;
            this.energy = energy;
        }
    }
    
    /**
     * 节点距离内部类
     */
    private static class NodeDistance implements Comparable<NodeDistance> {
        Long nodeId;
        BigDecimal distance;
        
        NodeDistance(Long nodeId, BigDecimal distance) {
            this.nodeId = nodeId;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(NodeDistance other) {
            return this.distance.compareTo(other.distance);
        }
    }
}
