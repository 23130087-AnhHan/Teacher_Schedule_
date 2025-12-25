package algorithm;

import model.Chromosome;
import model.Gene;
import java.util.*;

/**
 * Crossover Operator - Lai ghép 2 chromosome cha mẹ
 * Tạo ra 2 chromosome con
 */
public class CrossoverOperator {
    
    private Random random;
    
    public CrossoverOperator() {
        this.random = new Random();
    }
    
    /**
     * Single Point Crossover
     * Cắt tại 1 điểm, đổi chéo 2 phần
     */
    public Chromosome[] singlePointCrossover(Chromosome parent1, Chromosome parent2) {
        int size = Math.min(parent1.getGeneCount(), parent2.getGeneCount());
        
        if (size <= 1) {
            // Không thể crossover, trả về bản sao
            return new Chromosome[] { parent1.clone(), parent2.clone() };
        }
        
        // Chọn điểm cắt ngẫu nhiên
        int crossoverPoint = random.nextInt(size - 1) + 1;
        
        // Tạo 2 con
        Chromosome child1 = new Chromosome();
        Chromosome child2 = new Chromosome();
        
        // Phần 1: từ 0 đến crossoverPoint
        for (int i = 0; i < crossoverPoint; i++) {
            child1.addGene(parent1.getGene(i).clone());
            child2.addGene(parent2.getGene(i).clone());
        }
        
        // Phần 2: từ crossoverPoint đến hết
        for (int i = crossoverPoint; i < size; i++) {
            child1.addGene(parent2.getGene(i).clone());
            child2.addGene(parent1.getGene(i).clone());
        }
        
        return new Chromosome[] { child1, child2 };
    }
    
    /**
     * Two Point Crossover
     * Cắt tại 2 điểm, đổi chéo phần giữa
     */
    public Chromosome[] twoPointCrossover(Chromosome parent1, Chromosome parent2) {
        int size = Math. min(parent1.getGeneCount(), parent2.getGeneCount());
        
        if (size <= 2) {
            return singlePointCrossover(parent1, parent2);
        }
        
        // Chọn 2 điểm cắt
        int point1 = random.nextInt(size - 2) + 1;
        int point2 = random.nextInt(size - point1 - 1) + point1 + 1;
        
        // Tạo 2 con
        Chromosome child1 = new Chromosome();
        Chromosome child2 = new Chromosome();
        
        // Phần 1: 0 -> point1
        for (int i = 0; i < point1; i++) {
            child1.addGene(parent1.getGene(i).clone());
            child2.addGene(parent2.getGene(i).clone());
        }
        
        // Phần 2: point1 -> point2 (đổi chéo)
        for (int i = point1; i < point2; i++) {
            child1.addGene(parent2.getGene(i).clone());
            child2.addGene(parent1.getGene(i).clone());
        }
        
        // Phần 3: point2 -> end
        for (int i = point2; i < size; i++) {
            child1.addGene(parent1.getGene(i).clone());
            child2.addGene(parent2.getGene(i).clone());
        }
        
        return new Chromosome[] { child1, child2 };
    }
    
    /**
     * Uniform Crossover
     * Mỗi gene được chọn ngẫu nhiên từ 1 trong 2 cha mẹ
     */
    public Chromosome[] uniformCrossover(Chromosome parent1, Chromosome parent2) {
        int size = Math.min(parent1.getGeneCount(), parent2.getGeneCount());
        
        Chromosome child1 = new Chromosome();
        Chromosome child2 = new Chromosome();
        
        for (int i = 0; i < size; i++) {
            if (random.nextBoolean()) {
                // Lấy từ parent1 cho child1, parent2 cho child2
                child1.addGene(parent1.getGene(i).clone());
                child2.addGene(parent2.getGene(i).clone());
            } else {
                // Đảo ngược
                child1.addGene(parent2.getGene(i).clone());
                child2.addGene(parent1.getGene(i).clone());
            }
        }
        
        return new Chromosome[] { child1, child2 };
    }
    
    /**
     * Thực hiện crossover với xác suất
     */
    public Chromosome[] crossover(Chromosome parent1, Chromosome parent2) {
        if (random. nextDouble() < GAConfig.CROSSOVER_RATE) {
            // Thực hiện crossover (sử dụng single point)
            return singlePointCrossover(parent1, parent2);
        } else {
            // Không crossover, trả về bản sao cha mẹ
            return new Chromosome[] { parent1.clone(), parent2.clone() };
        }
    }
}