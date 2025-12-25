package model;

import java.time.LocalDateTime;

public class GAExecutionLog {
    private int logId;
    private String semester;
    private String academicYear;
    
    // Tham số GA
    private int populationSize;
    private int maxGenerations;
    private double crossoverRate;
    private double mutationRate;
    
    // Kết quả
    private int generationsExecuted;
    private double bestFitnessScore;
    private double avgFitnessScore;
    
    // Vi phạm
    private int hardConstraintViolations;
    private int softConstraintViolations;
    
    private int executionTimeSeconds;
    private String notes;
    private LocalDateTime createdAt;

    // Constructors
    public GAExecutionLog() {
    }

    public GAExecutionLog(String semester, String academicYear, int populationSize, 
                          int maxGenerations, double crossoverRate, double mutationRate) {
        this.semester = semester;
        this.academicYear = academicYear;
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
    }

    // Getters and Setters - ĐẦY ĐỦ TẤT CẢ
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public void setMaxGenerations(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public int getGenerationsExecuted() {
        return generationsExecuted;
    }

    public void setGenerationsExecuted(int generationsExecuted) {
        this.generationsExecuted = generationsExecuted;
    }

    public double getBestFitnessScore() {
        return bestFitnessScore;
    }

    public void setBestFitnessScore(double bestFitnessScore) {
        this.bestFitnessScore = bestFitnessScore;
    }

    public double getAvgFitnessScore() {
        return avgFitnessScore;
    }

    public void setAvgFitnessScore(double avgFitnessScore) {
        this.avgFitnessScore = avgFitnessScore;
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

    public int getExecutionTimeSeconds() {
        return executionTimeSeconds;
    }

    public void setExecutionTimeSeconds(int executionTimeSeconds) {
        this.executionTimeSeconds = executionTimeSeconds;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public int getTotalViolations() {
        return hardConstraintViolations + softConstraintViolations;
    }

    public boolean isValid() {
        return hardConstraintViolations == 0;
    }

    public String getExecutionTimeFriendly() {
        int minutes = executionTimeSeconds / 60;
        int seconds = executionTimeSeconds % 60;
        return minutes > 0 ? minutes + " phút " + seconds + " giây" : seconds + " giây";
    }

    @Override
    public String toString() {
        return "GAExecutionLog{" +
                "logId=" + logId +
                ", semester='" + semester + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", populationSize=" + populationSize +
                ", maxGenerations=" + maxGenerations +
                ", generationsExecuted=" + generationsExecuted +
                ", bestFitnessScore=" + bestFitnessScore +
                ", hardViolations=" + hardConstraintViolations +
                ", executionTime=" + executionTimeSeconds + "s" +
                '}';
    }
}