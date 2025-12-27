package algorithm;

import model.*;
import dao.*;
import java.util.*;

/**
 * Genetic Algorithm - Thuật toán di truyền chính
 * Giải bài toán xếp lịch giảng dạy
 */
public class GeneticAlgorithm {

    // DAOs
    private TeachingAssignmentDAO assignmentDAO;
    private TeacherDAO teacherDAO;
    private RoomDAO roomDAO;
    private TimeSlotDAO timeSlotDAO;
    private ScheduleDAO scheduleDAO;
    private GAExecutionLogDAO logDAO;

    // Data
    private List<TeachingAssignment> assignments;
    private List<Teacher> teachers;
    private List<Room> rooms;
    private List<TimeSlot> timeSlots;

    // GA Components
    private FitnessCalculator fitnessCalculator;
    private SelectionOperator selectionOperator;
    private CrossoverOperator crossoverOperator;
    private MutationOperator mutationOperator;

    // Statistics
    private int currentGeneration;
    private Chromosome bestChromosome;
    private double bestFitness;
    private int noImprovementCount;
    private long startTime;

    public GeneticAlgorithm() {
        // Initialize DAOs
        this.assignmentDAO = new TeachingAssignmentDAO();
        this.teacherDAO = new TeacherDAO();
        this.roomDAO = new RoomDAO();
        this.timeSlotDAO = new TimeSlotDAO();
        this.scheduleDAO = new ScheduleDAO();
        this.logDAO = new GAExecutionLogDAO();

        // Initialize GA components
        this.fitnessCalculator = new FitnessCalculator();
        this.selectionOperator = new SelectionOperator();
        this.crossoverOperator = new CrossoverOperator();
        this.mutationOperator = new MutationOperator();

        // Initialize statistics
        this.currentGeneration = 0;
        this.bestFitness = Double.NEGATIVE_INFINITY;
        this.noImprovementCount = 0;
    }

    /**
     * Chạy thuật toán di truyền
     */
    public GAResult run(String semester, String academicYear) {
        System.out.println("========================================");
        System.out.println("🧬 GENETIC ALGORITHM - BẮT ĐẦU");
        System.out.println("========================================");
        System.out.println("Học kỳ: " + semester + " " + academicYear);
        System.out.println("----------------------------------------");

        startTime = System.currentTimeMillis();

        // 1. Load data từ database
        System.out.println("📥 Đang load dữ liệu...");
        loadData(semester, academicYear);

        if (assignments.isEmpty()) {
            System.err.println("❌ Không có assignments để xếp lịch!");
            return null;
        }

        System.out.println("✅ Đã load:");
        System.out.println("   - " + assignments.size() + " assignments");
        System.out.println("   - " + teachers.size() + " giáo viên");
        System.out.println("   - " + rooms.size() + " phòng");
        System.out.println("   - " + timeSlots.size() + " time slots");
        System.out.println("----------------------------------------");

        // 2. Khởi tạo population ban đầu
        System.out.println("🎲 Khởi tạo population (" + GAConfig.POPULATION_SIZE + " chromosomes)...");
        List<Chromosome> population = initializePopulation();

        // ✅ Repair toàn bộ population ngay sau khi khởi tạo
        System.out.println("🔧 Repairing room type violations...");
        for (Chromosome chromosome : population) {
            mutationOperator.repairRoomTypes(chromosome, rooms);
        }
        System.out.println("✅ Repair completed");

        // 3. Evaluate population ban đầu
        fitnessCalculator.calculatePopulationFitness(population, teachers, rooms, timeSlots);
        bestChromosome = fitnessCalculator.getBestChromosome(population);
        bestFitness = bestChromosome.getFitnessScore();

        System.out.println("✅ Population khởi tạo xong");
        System.out.println("   - Best fitness: " + String.format("%.2f", bestFitness));
        System.out.println("   - Hard violations: " + bestChromosome.getHardConstraintViolations());
        System.out.println("========================================\n");

        // 4. Evolution loop
        for (currentGeneration = 1; currentGeneration <= GAConfig.MAX_GENERATIONS; currentGeneration++) {
            // 4.1 Selection
            List<Chromosome> newPopulation = new ArrayList<>();

            // Elitism - giữ lại elite
            List<Chromosome> elite = selectionOperator.selectElite(population);
            newPopulation.addAll(elite);

            // 4.2 Crossover & Mutation để tạo thế hệ mới
            while (newPopulation.size() < GAConfig.POPULATION_SIZE) {
                // Selection
                Chromosome parent1 = selectionOperator.tournamentSelection(population);
                Chromosome parent2 = selectionOperator.tournamentSelection(population);

                // Crossover
                Chromosome[] children = crossoverOperator.crossover(parent1, parent2);

                // Mutation
                if (new Random().nextDouble() < GAConfig.MUTATION_RATE) {
                    mutationOperator.mutate(children[0], rooms, timeSlots);
                }
                if (new Random().nextDouble() < GAConfig.MUTATION_RATE) {
                    mutationOperator.mutate(children[1], rooms, timeSlots);
                }

                // ✅ REPAIR:  Sửa room type mismatches
                mutationOperator.repairRoomTypes(children[0], rooms);
                mutationOperator.repairRoomTypes(children[1], rooms);

                newPopulation.add(children[0]);
                if (newPopulation.size() < GAConfig.POPULATION_SIZE) {
                    newPopulation.add(children[1]);
                }
            }

            // 4.3 Evaluate new population
            population = newPopulation;
            fitnessCalculator.calculatePopulationFitness(population, teachers, rooms, timeSlots);

            // 4.4 Update best
            Chromosome currentBest = fitnessCalculator.getBestChromosome(population);
            if (currentBest.getFitnessScore() > bestFitness) {
                bestFitness = currentBest.getFitnessScore();
                bestChromosome = currentBest.clone();
                noImprovementCount = 0;
            } else {
                noImprovementCount++;
            }

            // 4.5 Print progress
            if (GAConfig.DEBUG_MODE && currentGeneration % GAConfig.PRINT_EVERY_N_GENERATIONS == 0) {
                printProgress(population);
            }

            // 4.6 Termination check
            if (bestChromosome.isValid() && bestFitness >= GAConfig.TARGET_FITNESS) {
                System.out.println("\n🎉 Tìm thấy lịch hợp lệ tại thế hệ " + currentGeneration + "!");
                break;
            }

            if (noImprovementCount >= GAConfig.NO_IMPROVEMENT_LIMIT) {
                System.out.println("\n⚠️  Dừng:  Không cải thiện sau " + GAConfig.NO_IMPROVEMENT_LIMIT + " thế hệ");
                break;
            }

            if (bestChromosome.getHardConstraintViolations() > 0) {
                System.out.println("\n🔍 PHÂN TÍCH CHI TIẾT HARD VIOLATIONS:");
                System.out.println("========================================");

                ConstraintChecker checker = new ConstraintChecker();
                ConstraintChecker.ConstraintCheckResult result = checker.checkAllConstraints(
                        bestChromosome, teachers, rooms, timeSlots
                );

                System.out.println("📊 Thống kê vi phạm:");
                System.out.println("   - Teacher conflicts: " + result.getTeacherConflicts());
                System.out.println("   - Room conflicts: " + result.getRoomConflicts());
                System.out.println("   - Teacher-subject mismatches: " + result.getTeacherSubjectMismatches());
                System.out.println("   - Room capacity violations: " + result.getRoomCapacityViolations());
                System.out.println("   - Room type mismatches:  " + result.getRoomTypeMismatches());

                System.out.println("\n📋 Chi tiết violations:");
                for (String violation : result.getHardViolations()) {
                    System.out.println("   ❌ " + violation);
                }
                System.out.println("========================================");
            }
        }

        // 5. Kết thúc
        long endTime = System.currentTimeMillis();
        int executionTime = (int) ((endTime - startTime) / 1000);

        System.out.println("\n========================================");
        System.out.println("🏁 KẾT QUẢ CUỐI CÙNG");
        System.out.println("========================================");
        System.out.println("Thế hệ: " + currentGeneration);
        System.out.println("Best fitness: " + String.format("%.2f", bestFitness));
        System.out.println("Hard violations: " + bestChromosome.getHardConstraintViolations());
        System.out.println("Soft violations: " + bestChromosome.getSoftConstraintViolations());
        System.out.println("Thời gian:  " + executionTime + " giây");
        System.out.println("Trạng thái: " + (bestChromosome.isValid() ? "✅ HỢP LỆ" : "❌ CÓ VI PHẠM"));
        System.out.println("========================================\n");
        
     // PHÂN TÍCH CHI TIẾT LỖI HARD VIOLATION SAU KHI KẾT THÚC GA
        if (bestChromosome.getHardConstraintViolations() > 0) {
            System.out.println("📋 Chi tiết các VI PHẠM CỨNG cuối cùng:");
            ConstraintChecker checker = new ConstraintChecker();
            ConstraintChecker.ConstraintCheckResult result = checker.checkAllConstraints(
                    bestChromosome, teachers, rooms, timeSlots
            );
            for (String violation : result.getHardViolations()) {
                System.out.println("❌ HARD VIOLATION: " + violation);
            }
            System.out.println("========================================\n");
        }
     

        // 6. Lưu kết quả
        GAResult result = new GAResult();
        result.setBestChromosome(bestChromosome);
        result.setGenerationsExecuted(currentGeneration);
        result.setExecutionTimeSeconds(executionTime);
        result.setSemester(semester);
        result.setAcademicYear(academicYear);

        // 7. Save to database
        saveResults(result);

        return result;
    }


    /**
     * Load dữ liệu từ database
     */
    private void loadData(String semester, String academicYear) {
        assignments = assignmentDAO.getAssignmentsBySemester(semester, academicYear);
        teachers = teacherDAO.getAllTeachers();
        rooms = roomDAO.getAllRooms();
        timeSlots = timeSlotDAO.getAllTimeSlots();
    }

    /**
     * Khởi tạo population ban đầu (random)
     */
    private List<Chromosome> initializePopulation() {
        List<Chromosome> population = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < GAConfig.POPULATION_SIZE; i++) {
            Chromosome chromosome = new Chromosome();

            for (TeachingAssignment assignment : assignments) {
                // Random chọn room và slot
                List<Room> suitableRooms = getSuitableRooms(assignment);
                // Nếu không có phòng nào hợp lệ, gene này mặc định sẽ bị hard violation
                Room randomRoom = null;
                if (!suitableRooms.isEmpty()) {
                    randomRoom = suitableRooms.get(random.nextInt(suitableRooms.size()));
                }
                TimeSlot randomSlot = timeSlots.get(random.nextInt(timeSlots.size()));

                Gene gene = new Gene(assignment, randomRoom, randomSlot);
                chromosome.addGene(gene);
            }

            population.add(chromosome);
        }

        return population;
    }

    /**
     * Lấy phòng phù hợp cho assignment - KHÔNG BAO GIỜ CHỌN PHÒNG KHÔNG ĐỦ SỨC CHỨA!
     * - Thực hành: chỉ phòng LAB, capacity đủ ≥ số SV nhóm
     * - Lý thuyết: chỉ phòng LECTURE_HALL/THROY, capacity đủ
     */
    private List<Room> getSuitableRooms(TeachingAssignment assignment) {
        List<Room> suitable = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getCapacity() < assignment.getNumStudents()) {
                // KHÔNG ĐƯỢC chọn phòng thiếu sức chứa!
                continue;
            }
            if (assignment.isPractice() && assignment.isRequiresLab()) {
                if (room.isLab()) suitable.add(room);
            } else if (assignment.isTheory()) {
                if (!room.isLab()) suitable.add(room);
            }
        }
        // Không dùng fallback lấy phòng bất kỳ vượt sức chứa!
        return suitable;
    }

    /**
     * In tiến trình
     */
    private void printProgress(List<Chromosome> population) {
        double avgFitness = fitnessCalculator.getAverageFitness(population);

        System.out.printf("Gen %4d | Best: %8.2f | Avg: %8.2f | Hard: %3d | Soft: %3d | NoImprove: %3d\n",
                currentGeneration,
                bestFitness,
                avgFitness,
                bestChromosome.getHardConstraintViolations(),
                bestChromosome.getSoftConstraintViolations(),
                noImprovementCount);
    }

    /**
     * Lưu kết quả vào database
     */
    private void saveResults(GAResult result) {
        try {
            // 1. Xóa schedules cũ
            scheduleDAO.deleteSchedulesBySemester(result.getSemester(), result.getAcademicYear());

            // 2. Lưu schedules mới
            List<Schedule> schedules = result.getBestChromosome().toSchedules();
            scheduleDAO.insertSchedules(schedules);

            // 3. Lưu execution log
            GAExecutionLog log = new GAExecutionLog();
            log.setSemester(result.getSemester());
            log.setAcademicYear(result.getAcademicYear());
            log.setPopulationSize(GAConfig.POPULATION_SIZE);
            log.setMaxGenerations(GAConfig.MAX_GENERATIONS);
            log.setCrossoverRate(GAConfig.CROSSOVER_RATE);
            log.setMutationRate(GAConfig.MUTATION_RATE);
            log.setGenerationsExecuted(result.getGenerationsExecuted());
            log.setBestFitnessScore(bestFitness);
            log.setAvgFitnessScore(0.0);
            log.setHardConstraintViolations(bestChromosome.getHardConstraintViolations());
            log.setSoftConstraintViolations(bestChromosome.getSoftConstraintViolations());
            log.setExecutionTimeSeconds(result.getExecutionTimeSeconds());
            log.setNotes("Genetic Algorithm completed");

            logDAO.insertLog(log);

            System.out.println("✅ Đã lưu " + schedules.size() + " schedules vào database");

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lưu kết quả: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Inner class:  Kết quả GA
     */
    // ---------- BỔ SUNG: CLASS GAResult HOÀN THIỆN DÙNG CHO JSP/SERVLET ----------
    public static class GAResult {
        private Chromosome bestChromosome;
        private int generationsExecuted;
        private int executionTimeSeconds;
        private String semester;
        private String academicYear;

        // --- Setters ---
        public void setBestChromosome(Chromosome bestChromosome) {
            this.bestChromosome = bestChromosome;
        }
        public void setGenerationsExecuted(int generationsExecuted) {
            this.generationsExecuted = generationsExecuted;
        }
        public void setExecutionTimeSeconds(int executionTimeSeconds) {
            this.executionTimeSeconds = executionTimeSeconds;
        }
        public void setSemester(String semester) {
            this.semester = semester;
        }
        public void setAcademicYear(String academicYear) {
            this.academicYear = academicYear;
        }

        // --- Getters cho JSP/servlet ---
        public Chromosome getBestChromosome() {
            return bestChromosome;
        }

        public int getGenerationsExecuted() {
            return generationsExecuted;
        }

        public int getExecutionTimeSeconds() {
            return executionTimeSeconds;
        }

        public String getSemester() {
            return semester;
        }

        public String getAcademicYear() {
            return academicYear;
        }

        // ===== THÊM GETTER ĐỂ JSP/SERVLET SỬ DỤNG TRỰC TIẾP =====

        /** Điểm fitness tốt nhất (đã lưu trong Chromosome) */
        public double getBestFitness() {
            return bestChromosome != null ? bestChromosome.getFitnessScore() : 0.0;
        }

        /** Số vi phạm hard của lịch tốt nhất */
        public int getBestHardViolations() {
            return bestChromosome != null ? bestChromosome.getHardConstraintViolations() : 0;
        }

        /** Số vi phạm soft của lịch tốt nhất */
        public int getBestSoftViolations() {
            return bestChromosome != null ? bestChromosome.getSoftConstraintViolations() : 0;
        }

        /** Số lịch đã tạo (bảng schedules) */
        public int getScheduleCount() {
            try {
                return (bestChromosome != null && bestChromosome.toSchedules() != null)
                    ? bestChromosome.toSchedules().size() : 0;
            } catch (Exception e) {
                return 0;
            }
        }

        /** Lịch có hợp lệ không (không có vi phạm cứng) */
        public boolean isValid() {
            return bestChromosome != null && bestChromosome.isValid();
        }
        
    }
    
}