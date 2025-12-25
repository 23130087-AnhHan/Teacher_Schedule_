package test;

import algorithm.GeneticAlgorithm;
import util.JDBCUtils;

/**
 * Test Genetic Algorithm
 */
public class TestGeneticAlgorithm {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════");
        System.out.println("  TEST GENETIC ALGORITHM");
        System.out.println("═══════════════════════════════════════════\n");
        
        // 1. Test connection
        System.out.println("1️⃣  Testing database connection...");
        JDBCUtils.testConnection();
        System.out.println();
        
        // 2. Run GA
        System.out.println("2️⃣  Running Genetic Algorithm...\n");
        GeneticAlgorithm ga = new GeneticAlgorithm();
        
        try {
            GeneticAlgorithm.GAResult result = ga.run("HK1", "2025-2026");
            
            if (result != null) {
                System.out.println("\n═══════════════════════════════════════════");
                System. out.println("  ✅ TEST COMPLETED SUCCESSFULLY");
                System.out.println("═══════════════════════════════════════════");
                System.out.println("Best Fitness: " + String.format("%.2f", result.getBestChromosome().getFitnessScore()));
                System.out.println("Generations:  " + result.getGenerationsExecuted());
                System.out. println("Time: " + result.getExecutionTimeSeconds() + " seconds");
                System.out.println("Hard Violations: " + result.getBestChromosome().getHardConstraintViolations());
                System.out.println("Soft Violations: " + result.getBestChromosome().getSoftConstraintViolations());
                System.out.println("Valid: " + (result.getBestChromosome().isValid() ? "YES ✅" : "NO ❌"));
            } else {
                System.err.println("\n❌ GA returned null result!");
            }
            
        } catch (Exception e) {
            System.err.println("\n❌ ERROR during GA execution:");
            e.printStackTrace();
        }
    }
}