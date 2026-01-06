package service;

import algorithm.GeneticAlgorithm;
import algorithm.GeneticAlgorithm.GAResult;

public class GAService {
    
    /**
     * Chạy GA cho một học kỳ
     * - Lần đầu: khởi tạo ngẫu nhiên
     * - Lần sau: tiếp tục tối ưu từ lịch đã lưu (seed)
     */
    public GAResult runGA(String semester, String academicYear) {
        System.out.println("========================================");
        System.out.println("🚀 SERVICE: Starting/Continuing GA for " + semester + " " + academicYear);
        System.out.println("========================================");
        
        GeneticAlgorithm ga = new GeneticAlgorithm();
        GAResult result = ga.run(semester, academicYear);
        
        if (result != null && result.getBestChromosome() != null) {
            System.out.println("✅ GA completed successfully!");
            System.out.println("   Generations: " + result.getGenerationsExecuted());
            System.out.println("   Time: " + result.getExecutionTimeSeconds() + "s");
            System.out.println("   Hard violations: " + result.getBestChromosome().getHardConstraintViolations());
        } else {
            System.err.println("❌ GA failed!");
        }
        
        return result;
    }
}