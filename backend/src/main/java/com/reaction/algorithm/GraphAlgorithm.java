package com.reaction.algorithm;

import com.reaction.entity.ReactionPath;
import java.math.BigDecimal;
import java.util.*;

/**
 * 图论算法类
 * 实现了三种路径搜索算法：
 * 1. BFS（广度优先搜索）：找步数最少的路径
 * 2. Dijkstra（迪杰斯特拉）：找权重最小的路径（能量变化最低、活化能最低）
 */
public class GraphAlgorithm {
    
    /**
     * 路径结果类
     * 封装单条路径的详细信息
     */
    public static class PathResult {
        private List<Long> path;           // 路径节点列表（化合物ID列表）
        private int steps;                      // 步数（边数）
        private BigDecimal totalEnergyChange;  // 总能量变化
        private BigDecimal totalActivationEnergy; // 总活化能
        private String optimizationType;   // 优化类型：步数最少、能量变化最低、活化能最低
        private List<Long> edgeIds;       // 路径经过的边ID列表
        
        public PathResult() {}
        
        public PathResult(List<Long> path, int steps, BigDecimal totalEnergyChange, 
                         BigDecimal totalActivationEnergy, String optimizationType) {
            this.path = path;
            this.steps = steps;
            this.totalEnergyChange = totalEnergyChange;
            this.totalActivationEnergy = totalActivationEnergy;
            this.optimizationType = optimizationType;
        }
        
        public List<Long> getPath() { return path; }
        public void setPath(List<Long> path) { this.path = path; }
        public int getSteps() { return steps; }
        public void setSteps(int steps) { this.steps = steps; }
        public BigDecimal getTotalEnergyChange() { return totalEnergyChange; }
        public void setTotalEnergyChange(BigDecimal totalEnergyChange) { this.totalEnergyChange = totalEnergyChange; }
        public BigDecimal getTotalActivationEnergy() { return totalActivationEnergy; }
        public void setTotalActivationEnergy(BigDecimal totalActivationEnergy) { this.totalActivationEnergy = totalActivationEnergy; }
        public String getOptimizationType() { return optimizationType; }
        public void setOptimizationType(String optimizationType) { this.optimizationType = optimizationType; }
        public List<Long> getEdgeIds() { return edgeIds; }
        public void setEdgeIds(List<Long> edgeIds) { this.edgeIds = edgeIds; }
    }
    
    /**
     * 多路径结果类
     * 封装多条优化路径的结果集合
     */
    public static class MultiPathResult {
        private List<PathResult> paths;
        
        public MultiPathResult() {
            this.paths = new ArrayList<>();
        }
        
        public MultiPathResult(List<PathResult> paths) {
            this.paths = paths;
        }
        
        public List<PathResult> getPaths() { return paths; }
        public void setPaths(List<PathResult> paths) { this.paths = paths; }
    }
    
    /**
     * 边信息类
     * 用于构建加权图时记录每条边的信息
     */
    private static class EdgeInfo {
        Long toNode;              // 目标节点
        BigDecimal weight;       // 边的权重（用于Dijkstra算法）
        BigDecimal energyChange;   // 能量变化（实际值，用于计算总能量）
        BigDecimal activationEnergy; // 活化能（实际值，用于计算总活化能）
        Long edgeId;               // 边的ID
        
        EdgeInfo(Long toNode, BigDecimal weight, BigDecimal energyChange, BigDecimal activationEnergy, Long edgeId) {
            this.toNode = toNode;
            this.weight = weight;
            this.energyChange = energyChange;
            this.activationEnergy = activationEnergy;
            this.edgeId = edgeId;
        }
    }
    
    /**
     * 父节点信息类
     * 用于记录Dijkstra算法中记录实际选择的边信息
     * 确保路径搜索时选择的边与总能量/活化能计算时使用的边一致
     */
    private static class ParentInfo {
        Long parentNode;          // 父节点
        BigDecimal energyChange;   // 从父节点到当前节点的能量变化
        BigDecimal activationEnergy; // 从父节点到当前节点的活化能
        Long edgeId;               // 边的ID
        
        ParentInfo(Long parentNode, BigDecimal energyChange, BigDecimal activationEnergy, Long edgeId) {
            this.parentNode = parentNode;
            this.energyChange = energyChange;
            this.activationEnergy = activationEnergy;
            this.edgeId = edgeId;
        }
    }
    
    /**
     * BFS（广度优先搜索）找最短路径
     * 只考虑步数（边数），不考虑权重
     * 
     * @param startId 起始化合物ID
     * @param endId 目标化合物ID
     * @param paths 所有反应路径列表
     * @return 路径节点列表
     */
    public static List<Long> findShortestPathBFS(Long startId, Long endId, List<ReactionPath> paths) {
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
        
        // BFS核心循环：按层级遍历，找到目标时立即返回（保证步数最少）
        while (!queue.isEmpty()) {
            Long current = queue.poll();
            
            if (current.equals(endId)) {
                return reconstructPathSimple(parent, endId);
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
        
        return new ArrayList<>();
    }
    
    /**
     * Dijkstra算法找能量变化最低的路径
     * 使用能量变化作为权重
     * 
     * @param startId 起始化合物ID
     * @param endId 目标化合物ID
     * @param paths 所有反应路径列表
     * @return 路径节点列表
     */
    public static List<Long> findLowestEnergyPathDijkstra(Long startId, Long endId, List<ReactionPath> paths) {
        Map<Long, List<EdgeInfo>> graph = buildWeightedGraphByEnergy(paths);
        
        if (!graph.containsKey(startId)) {
            return new ArrayList<>();
        }
        
        Set<Long> allNodes = new HashSet<>();
        for (ReactionPath path : paths) {
            allNodes.add(path.getFromCompoundId());
            allNodes.add(path.getToCompoundId());
        }
        
        Map<Long, BigDecimal> distances = new HashMap<>();
        Map<Long, ParentInfo> parent = new HashMap<>();
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
        Set<Long> visited = new HashSet<>();
        
        // 初始化所有节点距离为无穷大
        for (Long node : allNodes) {
            distances.put(node, new BigDecimal(Double.MAX_VALUE));
        }
        distances.put(startId, BigDecimal.ZERO);
        
        pq.add(new NodeDistance(startId, BigDecimal.ZERO));
        parent.put(startId, null);
        
        // Dijkstra核心循环：优先队列
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            Long currentNode = current.nodeId;
            
            if (visited.contains(currentNode)) {
                continue;
            }
            visited.add(currentNode);
            
            if (currentNode.equals(endId)) {
                return reconstructPathSimpleFromParent(parent, endId);
            }
            
            List<EdgeInfo> neighbors = graph.getOrDefault(currentNode, new ArrayList<>());
            for (EdgeInfo edge : neighbors) {
                Long neighbor = edge.toNode;
                BigDecimal newDistance = current.distance.add(edge.weight);
                
                // 松弛操作：更新最短距离
                if (newDistance.compareTo(distances.get(neighbor)) < 0) {
                    distances.put(neighbor, newDistance);
                    parent.put(neighbor, new ParentInfo(currentNode, edge.energyChange, edge.activationEnergy, edge.edgeId));
                    pq.add(new NodeDistance(neighbor, newDistance));
                }
            }
        }
        
        if (distances.get(endId) == null || 
            distances.get(endId).compareTo(new BigDecimal(Double.MAX_VALUE)) == 0) {
            return new ArrayList<>();
        }
        
        return reconstructPathSimpleFromParent(parent, endId);
    }
    
    /**
     * 查找所有优化路径
     * 分别使用三种算法：
     * 1. 步数最少（BFS）
     * 2. 能量变化最低（Dijkstra）
     * 3. 活化能最低（Dijkstra）
     * 
     * @param startId 起始化合物ID
     * @param endId 目标化合物ID
     * @param paths 所有反应路径列表
     * @return 多条优化路径结果
     */
    public static MultiPathResult findAllOptimizedPaths(Long startId, Long endId, List<ReactionPath> paths) {
        MultiPathResult result = new MultiPathResult();
        
        PathResult stepsPath = findShortestPathBySteps(startId, endId, paths);
        if (stepsPath != null && !stepsPath.getPath().isEmpty()) {
            result.getPaths().add(stepsPath);
        }
        
        PathResult energyPath = findLowestEnergyChangePath(startId, endId, paths);
        if (energyPath != null && !energyPath.getPath().isEmpty()) {
            result.getPaths().add(energyPath);
        }
        
        PathResult activationPath = findLowestActivationEnergyPath(startId, endId, paths);
        if (activationPath != null && !activationPath.getPath().isEmpty()) {
            result.getPaths().add(activationPath);
        }
        
        return result;
    }
    
    /**
     * BFS算法找步数最少的路径
     * 使用广度优先搜索，只考虑边数
     * 
     * @param startId 起始化合物ID
     * @param endId 目标化合物ID
     * @param paths 所有反应路径列表
     * @return 路径结果（包含能量和活化能信息）
     */
    private static PathResult findShortestPathBySteps(Long startId, Long endId, List<ReactionPath> paths) {
        Map<Long, List<EdgeInfo>> graph = buildGraphWithEdgeInfo(paths);
        
        if (!graph.containsKey(startId)) {
            return null;
        }
        
        Queue<Long> queue = new LinkedList<>();
        Map<Long, ParentInfo> parent = new HashMap<>();
        Set<Long> visited = new HashSet<>();
        
        queue.add(startId);
        visited.add(startId);
        parent.put(startId, null);
        
        // BFS核心循环
        while (!queue.isEmpty()) {
            Long current = queue.poll();
            
            if (current.equals(endId)) {
                return buildPathResult(parent, endId, "步数最少");
            }
            
            List<EdgeInfo> neighbors = graph.getOrDefault(current, new ArrayList<>());
            for (EdgeInfo edge : neighbors) {
                Long neighbor = edge.toNode;
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    parent.put(neighbor, new ParentInfo(current, edge.energyChange, edge.activationEnergy, edge.edgeId));
                }
            }
        }
        
        return null;
    }
    
    /**
     * Dijkstra算法找能量变化最低的路径
     * 权重计算逻辑：
     * - 放热反应（能量变化<0）：权重 = 能量变化绝对值
     * - 吸热反应（能量变化>=0）：权重 = 能量变化 × 2（惩罚，使其权重更高）
     * 
     * 这样确保：
     * 1. 优先选择放热反应（权重更小）
     * 2. 对于同一对节点有多条边时，选择能量变化绝对值最小的边
     * 
     * @param startId 起始化合物ID
     * @param endId 目标化合物ID
     * @param paths 所有反应路径列表
     * @return 路径结果
     */
    private static PathResult findLowestEnergyChangePath(Long startId, Long endId, List<ReactionPath> paths) {
        Map<Long, List<EdgeInfo>> graph = buildWeightedGraphByEnergy(paths);
        
        if (!graph.containsKey(startId)) {
            return null;
        }
        
        Set<Long> allNodes = new HashSet<>();
        for (ReactionPath path : paths) {
            allNodes.add(path.getFromCompoundId());
            allNodes.add(path.getToCompoundId());
        }
        
        Map<Long, BigDecimal> distances = new HashMap<>();
        Map<Long, ParentInfo> parent = new HashMap<>();
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
        Set<Long> visited = new HashSet<>();
        
        for (Long node : allNodes) {
            distances.put(node, new BigDecimal(Double.MAX_VALUE));
        }
        distances.put(startId, BigDecimal.ZERO);
        
        pq.add(new NodeDistance(startId, BigDecimal.ZERO));
        parent.put(startId, null);
        
        // Dijkstra核心循环
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            Long currentNode = current.nodeId;
            
            if (visited.contains(currentNode)) {
                continue;
            }
            visited.add(currentNode);
            
            List<EdgeInfo> neighbors = graph.getOrDefault(currentNode, new ArrayList<>());
            for (EdgeInfo edge : neighbors) {
                Long neighbor = edge.toNode;
                BigDecimal newDistance = current.distance.add(edge.weight);
                
                if (newDistance.compareTo(distances.get(neighbor)) < 0) {
                    distances.put(neighbor, newDistance);
                    parent.put(neighbor, new ParentInfo(currentNode, edge.energyChange, edge.activationEnergy, edge.edgeId));
                    pq.add(new NodeDistance(neighbor, newDistance));
                }
            }
        }
        
        if (distances.get(endId) == null || 
            distances.get(endId).compareTo(new BigDecimal(Double.MAX_VALUE)) == 0) {
            return null;
        }
        
        return buildPathResult(parent, endId, "能量变化最低");
    }
    
    /**
     * Dijkstra算法找活化能最低的路径
     * 直接使用活化能作为权重
     * 
     * @param startId 起始化合物ID
     * @param endId 目标化合物ID
     * @param paths 所有反应路径列表
     * @return 路径结果
     */
    private static PathResult findLowestActivationEnergyPath(Long startId, Long endId, List<ReactionPath> paths) {
        Map<Long, List<EdgeInfo>> graph = buildWeightedGraphByActivation(paths);
        
        if (!graph.containsKey(startId)) {
            return null;
        }
        
        Set<Long> allNodes = new HashSet<>();
        for (ReactionPath path : paths) {
            allNodes.add(path.getFromCompoundId());
            allNodes.add(path.getToCompoundId());
        }
        
        Map<Long, BigDecimal> distances = new HashMap<>();
        Map<Long, ParentInfo> parent = new HashMap<>();
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
        Set<Long> visited = new HashSet<>();
        
        for (Long node : allNodes) {
            distances.put(node, new BigDecimal(Double.MAX_VALUE));
        }
        distances.put(startId, BigDecimal.ZERO);
        
        pq.add(new NodeDistance(startId, BigDecimal.ZERO));
        parent.put(startId, null);
        
        // Dijkstra核心循环
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            Long currentNode = current.nodeId;
            
            if (visited.contains(currentNode)) {
                continue;
            }
            visited.add(currentNode);
            
            List<EdgeInfo> neighbors = graph.getOrDefault(currentNode, new ArrayList<>());
            for (EdgeInfo edge : neighbors) {
                Long neighbor = edge.toNode;
                BigDecimal newDistance = current.distance.add(edge.weight);
                
                if (newDistance.compareTo(distances.get(neighbor)) < 0) {
                    distances.put(neighbor, newDistance);
                    parent.put(neighbor, new ParentInfo(currentNode, edge.energyChange, edge.activationEnergy, edge.edgeId));
                    pq.add(new NodeDistance(neighbor, newDistance));
                }
            }
        }
        
        if (distances.get(endId) == null || 
            distances.get(endId).compareTo(new BigDecimal(Double.MAX_VALUE)) == 0) {
            return null;
        }
        
        return buildPathResult(parent, endId, "活化能最低");
    }
    
    /**
     * 构建路径结果
     * 从终点回溯到起点，累加能量变化和活化能
     * 使用实际选择的边信息，确保与Dijkstra选择的边与计算时使用的边一致
     * 
     * @param parent 父节点信息映射
     * @param endId 终点ID
     * @param optimizationType 优化类型
     * @return 路径结果
     */
    private static PathResult buildPathResult(Map<Long, ParentInfo> parent, Long endId, String optimizationType) {
        List<Long> path = new ArrayList<>();
        List<Long> edgeIds = new ArrayList<>();
        BigDecimal totalEnergy = BigDecimal.ZERO;
        BigDecimal totalActivation = BigDecimal.ZERO;
        
        Long current = endId;
        while (current != null) {
            path.add(0, current);
            ParentInfo info = parent.get(current);
            if (info != null) {
                if (info.energyChange != null) {
                    totalEnergy = totalEnergy.add(info.energyChange);
                }
                if (info.activationEnergy != null) {
                    totalActivation = totalActivation.add(info.activationEnergy);
                }
                if (info.edgeId != null) {
                    edgeIds.add(0, info.edgeId);
                }
            }
            current = info != null ? info.parentNode : null;
        }
        
        if (path.size() < 2) {
            return null;
        }
        
        PathResult result = new PathResult(
            path,
            path.size() - 1,
            totalEnergy,
            totalActivation,
            optimizationType
        );
        result.setEdgeIds(edgeIds);
        return result;
    }
    
    /**
     * 构建无权图（用于BFS）
     * 只记录节点连接关系，不记录边信息
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
     * 构建带边信息的图（用于BFS）
     * 所有边权重为1
     */
    private static Map<Long, List<EdgeInfo>> buildGraphWithEdgeInfo(List<ReactionPath> paths) {
        Map<Long, List<EdgeInfo>> graph = new HashMap<>();
        
        for (ReactionPath path : paths) {
            BigDecimal energy = path.getEnergyChange() != null ? path.getEnergyChange() : BigDecimal.ZERO;
            BigDecimal activation = path.getActivationEnergy() != null ? path.getActivationEnergy() : BigDecimal.ZERO;
            
            graph.computeIfAbsent(path.getFromCompoundId(), k -> new ArrayList<>())
                    .add(new EdgeInfo(path.getToCompoundId(), BigDecimal.ONE, energy, activation, path.getId()));
        }
        
        return graph;
    }
    
    /**
     * 构建能量变化加权图（用于Dijkstra能量变化最低）
     * 权重计算：
     * - 放热反应（energyChange < 0）：权重 = 能量变化绝对值
     * - 吸热反应（energyChange >= 0）：权重 = 能量变化 × 2（惩罚）
     */
    private static Map<Long, List<EdgeInfo>> buildWeightedGraphByEnergy(List<ReactionPath> paths) {
        Map<Long, List<EdgeInfo>> graph = new HashMap<>();
        
        for (ReactionPath path : paths) {
            BigDecimal energy = path.getEnergyChange() != null ? path.getEnergyChange() : BigDecimal.ZERO;
            BigDecimal activation = path.getActivationEnergy() != null ? path.getActivationEnergy() : BigDecimal.ZERO;
            
            BigDecimal weight;
            if (energy.compareTo(BigDecimal.ZERO) < 0) {
                weight = BigDecimal.ZERO.subtract(energy);  // 放热反应：权重 = 能量变化绝对值
            } else {
                weight = energy.multiply(new BigDecimal("2"));  // 吸热反应：权重加倍惩罚
            }
            
            if (!graph.containsKey(path.getFromCompoundId())) {
                graph.put(path.getFromCompoundId(), new ArrayList<>());
            }
            graph.get(path.getFromCompoundId()).add(new EdgeInfo(path.getToCompoundId(), weight, energy, activation, path.getId()));
        }
        
        return graph;
    }
    
    /**
     * 构建活化能加权图（用于Dijkstra活化能最低）
     * 权重 = 活化能
     */
    private static Map<Long, List<EdgeInfo>> buildWeightedGraphByActivation(List<ReactionPath> paths) {
        Map<Long, List<EdgeInfo>> graph = new HashMap<>();
        
        for (ReactionPath path : paths) {
            BigDecimal energy = path.getEnergyChange() != null ? path.getEnergyChange() : BigDecimal.ZERO;
            BigDecimal activation = path.getActivationEnergy() != null ? path.getActivationEnergy() : BigDecimal.ZERO;
            
            graph.computeIfAbsent(path.getFromCompoundId(), k -> new ArrayList<>())
                    .add(new EdgeInfo(path.getToCompoundId(), activation, energy, activation, path.getId()));
        }
        
        return graph;
    }
    
    /**
     * 简单路径回溯（简单父节点映射）
     */
    private static List<Long> reconstructPathSimple(Map<Long, Long> parent, Long endId) {
        List<Long> path = new ArrayList<>();
        Long current = endId;
        
        while (current != null) {
            path.add(0, current);
            current = parent.get(current);
        }
        
        return path;
    }
    
    /**
     * 简单路径回溯（带边信息父节点映射）
     */
    private static List<Long> reconstructPathSimpleFromParent(Map<Long, ParentInfo> parent, Long endId) {
        List<Long> path = new ArrayList<>();
        Long current = endId;
        
        while (current != null) {
            path.add(0, current);
            ParentInfo info = parent.get(current);
            current = info != null ? info.parentNode : null;
        }
        
        return path;
    }
    
    /**
     * 节点距离类
     * 用于Dijkstra算法优先队列
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
