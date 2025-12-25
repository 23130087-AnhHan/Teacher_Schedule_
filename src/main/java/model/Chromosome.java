package model;

import java.util. ArrayList;
import java.util. List;

/**
 * Chromosome - Đại diện cho một lịch học hoàn chỉnh
 * Chromosome = List<Gene>
 */
public class Chromosome {
    private List<Gene> genes;
    private double fitnessScore;
    private int hardConstraintViolations;
    private int softConstraintViolations;

    // Constructors
    public Chromosome() {
        this.genes = new ArrayList<>();
        this.fitnessScore = 0.0;
        this. hardConstraintViolations = 0;
        this.softConstraintViolations = 0;
    }

    public Chromosome(List<Gene> genes) {
        this.genes = new ArrayList<>(genes);
        this.fitnessScore = 0.0;
        this.hardConstraintViolations = 0;
        this.softConstraintViolations = 0;
    }

    // Getters and Setters
    public List<Gene> getGenes() {
        return genes;
    }

    public void setGenes(List<Gene> genes) {
        this.genes = genes;
    }

    public double getFitnessScore() {
        return fitnessScore;
    }

    public void setFitnessScore(double fitnessScore) {
        this.fitnessScore = fitnessScore;
    }

    public int getHardConstraintViolations() {
        return hardConstraintViolations;
    }

    public void setHardConstraintViolations(int hardConstraintViolations) {
        this.hardConstraintViolations = hardConstraintViolations;
    }

    public int getSoftConstraintViolations() {
        return softConstraintViolations;
    }

    public void setSoftConstraintViolations(int softConstraintViolations) {
        this.softConstraintViolations = softConstraintViolations;
    }

    // Helper methods
    public int getGeneCount() {
        return genes.size();
    }

    public Gene getGene(int index) {
        return genes.get(index);
    }

    public void setGene(int index, Gene gene) {
        genes.set(index, gene);
    }

    public void addGene(Gene gene) {
        genes.add(gene);
    }

    public boolean isValid() {
        return hardConstraintViolations == 0;
    }

    public int getTotalViolations() {
        return hardConstraintViolations + softConstraintViolations;
    }

    /**
     * Clone chromosome
     */
    public Chromosome clone() {
        List<Gene> clonedGenes = new ArrayList<>();
        for (Gene gene : genes) {
            clonedGenes.add(gene.clone());
        }
        
        Chromosome cloned = new Chromosome(clonedGenes);
        cloned.setFitnessScore(this.fitnessScore);
        cloned.setHardConstraintViolations(this.hardConstraintViolations);
        cloned.setSoftConstraintViolations(this.softConstraintViolations);
        
        return cloned;
    }

    /**
     * Chuyển chromosome thành danh sách schedules
     */
    public List<Schedule> toSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        for (Gene gene : genes) {
            if (gene.isValid()) {
                schedules.add(gene.toSchedule());
            }
        }
        return schedules;
    }

    @Override
    public String toString() {
        return "Chromosome{" +
                "geneCount=" + genes.size() +
                ", fitnessScore=" + String.format("%.2f", fitnessScore) +
                ", hardViolations=" + hardConstraintViolations +
                ", softViolations=" + softConstraintViolations +
                ", isValid=" + isValid() +
                '}';
    }

    /**
     * So sánh 2 chromosome theo fitness score
     */
    public int compareTo(Chromosome other) {
        return Double.compare(this.fitnessScore, other.fitnessScore);
    }
}
