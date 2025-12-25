package service;

import algorithm.GeneticAlgorithm;
import algorithm.GeneticAlgorithm.GAResult;

public class GAService {
    
    /**
     * Chạy GA cho một học kỳ
     */
    public GAResult runGA(String semester, String academicYear) {
        System.out.println("========================================");
        System.out.println("🚀 SERVICE: Starting GA for " + semester + " " + academicYear);
        System.out. println("========================================");
        
        GeneticAlgorithm ga = new GeneticAlgorithm();
        GAResult result = ga.run(semester, academicYear);
        
        if (result != null && result.getBestChromosome() != null) {
            System.out.println("✅ GA completed successfully!");
            System.out. println("   Generations: " + result.getGenerationsExecuted());
            System.out. println("   Time: " + result.getExecutionTimeSeconds() + "s");
            System.out. println("   Hard violations: " + result.getBestChromosome().getHardConstraintViolations());
        } else {
            System.err.println("❌ GA failed!");
        }
        
        return result;
    }
}