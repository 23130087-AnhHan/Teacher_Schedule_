package algorithm;

import model.*;
import java.util. List;

/**
 * Tính fitness score cho chromosome
 * Fitness càng cao càng tốt
 */
public class FitnessCalculator {
    
    private ConstraintChecker constraintChecker;
    
    public FitnessCalculator() {
        this.constraintChecker = new ConstraintChecker();
    }
    
    /**
     * Tính fitness score cho một chromosome
     * @return fitness score (càng cao càng tốt)
     */
    public double calculateFitness(Chromosome chromosome, 
                                   List<Teacher> teachers,
                                   List<Room> rooms,
                                   List<TimeSlot> timeSlots) {
        // Kiểm tra tất cả ràng buộc
        ConstraintChecker.ConstraintCheckResult result = 
            constraintChecker.checkAllConstraints(chromosome, teachers, rooms, timeSlots);
        
        // Tính điểm fitness
        double fitness = 0.0;
        
        // 1. HARD CONSTRAINTS (vi phạm trừ điểm nặng)
        fitness += result. getTeacherConflicts() * GAConfig.WEIGHT_TEACHER_CONFLICT;
        fitness += result.getRoomConflicts() * GAConfig.WEIGHT_ROOM_CONFLICT;
        fitness += result.getTeacherSubjectMismatches() * GAConfig.WEIGHT_TEACHER_SUBJECT_MISMATCH;
        fitness += result.getRoomCapacityViolations() * GAConfig.WEIGHT_ROOM_CAPACITY_EXCEEDED;
        fitness += result.getRoomTypeMismatches() * GAConfig.WEIGHT_ROOM_TYPE_MISMATCH;
        
        // 2. SOFT CONSTRAINTS (vi phạm trừ điểm nhẹ)
        fitness += result.getMaxHoursViolations() * GAConfig.WEIGHT_MAX_HOURS_EXCEEDED;
        fitness += result.getRoomUtilizationPenalties() * GAConfig.WEIGHT_ROOM_UTILIZATION_POOR;
        
        // 3.  BONUSES (thưởng điểm)
        fitness += result.getRoomUtilizationBonuses() * GAConfig.WEIGHT_ROOM_UTILIZATION_OPTIMAL;
        
        // 4. BASE SCORE (điểm cơ bản nếu không vi phạm)
        if (result.isValid()) {
            fitness += 10000.0; // Thưởng lớn cho lịch hợp lệ
        }
        
        // Lưu kết quả vào chromosome
        chromosome.setFitnessScore(fitness);
        chromosome.setHardConstraintViolations(result.getTotalHardViolations());
        chromosome.setSoftConstraintViolations(result.getTotalSoftViolations());
        
        return fitness;
    }
    
    /**
     * Tính fitness cho toàn bộ population
     */
    public void calculatePopulationFitness(List<Chromosome> population,
                                          List<Teacher> teachers,
                                          List<Room> rooms,
                                          List<TimeSlot> timeSlots) {
        for (Chromosome chromosome : population) {
            calculateFitness(chromosome, teachers, rooms, timeSlots);
        }
    }
    
    /**
     * Lấy chromosome tốt nhất trong population
     */
    public Chromosome getBestChromosome(List<Chromosome> population) {
        if (population.isEmpty()) {
            return null;
        }
        
        Chromosome best = population.get(0);
        for (Chromosome chromosome : population) {
            if (chromosome.getFitnessScore() > best.getFitnessScore()) {
                best = chromosome;
            }
        }
        return best;
    }
    
    /**
     * Lấy chromosome tệ nhất
     */
    public Chromosome getWorstChromosome(List<Chromosome> population) {
        if (population.isEmpty()) {
            return null;
        }
        
        Chromosome worst = population.get(0);
        for (Chromosome chromosome : population) {
            if (chromosome.getFitnessScore() < worst.getFitnessScore()) {
                worst = chromosome;
            }
        }
        return worst;
    }
    
    /**
     * Tính fitness trung bình
     */
    public double getAverageFitness(List<Chromosome> population) {
        if (population.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (Chromosome chromosome : population) {
            sum += chromosome.getFitnessScore();
        }
        return sum / population.size();
    }
}