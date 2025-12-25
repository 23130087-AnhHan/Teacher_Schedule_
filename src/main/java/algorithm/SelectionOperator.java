package algorithm;

import model. Chromosome;
import java.util.*;

/**
 * Selection Operator - Chọn lọc các chromosome tốt nhất
 * Sử dụng Tournament Selection
 */
public class SelectionOperator {
    
    private Random random;
    
    public SelectionOperator() {
        this.random = new Random();
    }
    
    /**
     * Tournament Selection
     * Chọn ngẫu nhiên k chromosome, lấy cái tốt nhất
     */
    public Chromosome tournamentSelection(List<Chromosome> population) {
        List<Chromosome> tournament = new ArrayList<>();
        
        // Chọn ngẫu nhiên k chromosome
        for (int i = 0; i < GAConfig.TOURNAMENT_SIZE; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }
        
        // Trả về chromosome tốt nhất trong tournament
        return getBestChromosome(tournament);
    }
    
    /**
     * Roulette Wheel Selection (Backup method)
     * Xác suất được chọn tỷ lệ với fitness
     */
    public Chromosome rouletteWheelSelection(List<Chromosome> population) {
        // Tính tổng fitness
        double totalFitness = 0.0;
        double minFitness = Double.MAX_VALUE;
        
        // Tìm fitness nhỏ nhất
        for (Chromosome chromosome : population) {
            if (chromosome.getFitnessScore() < minFitness) {
                minFitness = chromosome.getFitnessScore();
            }
        }
        
        // Shift fitness về dương nếu có giá trị âm
        double shift = 0.0;
        if (minFitness < 0) {
            shift = Math.abs(minFitness) + 1.0;
        }
        
        // Tính tổng fitness (đã shift)
        for (Chromosome chromosome : population) {
            totalFitness += chromosome.getFitnessScore() + shift;
        }
        
        // Random một giá trị
        double randomValue = random.nextDouble() * totalFitness;
        double currentSum = 0.0;
        
        // Tìm chromosome tương ứng
        for (Chromosome chromosome : population) {
            currentSum += chromosome.getFitnessScore() + shift;
            if (currentSum >= randomValue) {
                return chromosome;
            }
        }
        
        // Fallback: trả về chromosome cuối cùng
        return population.get(population.size() - 1);
    }
    
    /**
     * Elitism - Giữ lại những chromosome tốt nhất
     */
    public List<Chromosome> selectElite(List<Chromosome> population) {
        // Sort population theo fitness (giảm dần)
        List<Chromosome> sortedPopulation = new ArrayList<>(population);
        sortedPopulation.sort((c1, c2) -> Double.compare(c2.getFitnessScore(), c1.getFitnessScore()));
        
        // Lấy top ELITE_SIZE
        List<Chromosome> elite = new ArrayList<>();
        for (int i = 0; i < Math. min(GAConfig.ELITE_SIZE, sortedPopulation.size()); i++) {
            elite.add(sortedPopulation. get(i).clone());
        }
        
        return elite;
    }
    
    /**
     * Lấy chromosome tốt nhất trong danh sách
     */
    private Chromosome getBestChromosome(List<Chromosome> chromosomes) {
        Chromosome best = chromosomes.get(0);
        for (Chromosome chromosome : chromosomes) {
            if (chromosome.getFitnessScore() > best.getFitnessScore()) {
                best = chromosome;
            }
        }
        return best;
    }
}