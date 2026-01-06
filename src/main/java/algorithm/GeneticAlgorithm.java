package algorithm;

import model.*;
import dao.*;
import java.util.*;

/**
 * Genetic Algorithm - Thuật toán di truyền chính
 * Giải bài toán xếp lịch giảng dạy
 *
 * Chạy tự động đến khi đạt tối ưu (hard=0, soft=0) hoặc chạm ngưỡng dừng.
 * - In tiến trình ra console từng thế hệ (Gen 0, Gen 1, ...).
 * - Khởi tạo từ seed (lịch đã lưu) nếu có để tiếp tục tối ưu.
 * - Tiêm đa dạng quần thể và đột biến thích nghi để tránh kẹt.
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
     * Chạy thuật toán di truyền: tự động tiến hóa đến khi tối ưu/điều kiện dừng.
     */
    public GAResult run(String semester, String academicYear) {
        System.out.println("========================================");
        System.out.println("🧬 GENETIC ALGORITHM - BẮT ĐẦU");
        System.out.println("========================================");
        System.out.println("Học kỳ: " + semester + " " + academicYear);
        System.out.println("----------------------------------------");

        startTime = System.currentTimeMillis();

        // 1. Load data
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

        // 2. Khởi tạo population (tiếp tục tối ưu nếu có seed)
        System.out.println("🎲 Khởi tạo population (" + GAConfig.POPULATION_SIZE + " chromosomes)...");
        Chromosome seed = buildSeedChromosomeFromSavedSchedules(semester, academicYear);
        List<Chromosome> population = (seed != null && seed.getGeneCount() > 0)
                ? initializePopulationFromSeedWithDiversity(seed)
                : initializeRandomPopulation();

        // Repair ngay sau khởi tạo
        for (Chromosome chromosome : population) {
            mutationOperator.repairRoomTypes(chromosome, rooms);
        }

        // 3. Đánh giá ban đầu + in tiến trình Gen 0
        fitnessCalculator.calculatePopulationFitness(population, teachers, rooms, timeSlots);
        bestChromosome = fitnessCalculator.getBestChromosome(population);
        bestFitness = bestChromosome.getFitnessScore();
        printProgressLite(0);

        // 4. Vòng lặp tiến hóa tự động
        for (currentGeneration = 1; currentGeneration <= GAConfig.MAX_GENERATIONS; currentGeneration++) {
            List<Chromosome> newPopulation = new ArrayList<>();

            // Elitism
            newPopulation.addAll(selectionOperator.selectElite(population));

            // Tỷ lệ đột biến thích nghi
            double currentMutationRate = computeAdaptiveMutationRate();

            // Tạo thế hệ mới
            while (newPopulation.size() < GAConfig.POPULATION_SIZE) {
                Chromosome parent1 = selectionOperator.tournamentSelection(population);
                Chromosome parent2 = selectionOperator.tournamentSelection(population);

                Chromosome[] children = crossoverOperator.crossover(parent1, parent2);

                if (Math.random() < currentMutationRate) mutationOperator.mutate(children[0], rooms, timeSlots);
                if (Math.random() < currentMutationRate) mutationOperator.mutate(children[1], rooms, timeSlots);

                mutationOperator.repairRoomTypes(children[0], rooms);
                mutationOperator.repairRoomTypes(children[1], rooms);

                newPopulation.add(children[0]);
                if (newPopulation.size() < GAConfig.POPULATION_SIZE) newPopulation.add(children[1]);
            }

            population = newPopulation;
            fitnessCalculator.calculatePopulationFitness(population, teachers, rooms, timeSlots);

            Chromosome currentBest = fitnessCalculator.getBestChromosome(population);
            if (currentBest.getFitnessScore() > bestFitness) {
                bestFitness = currentBest.getFitnessScore();
                bestChromosome = currentBest.clone();
                noImprovementCount = 0;
            } else {
                noImprovementCount++;
            }

            // In tiến trình từng thế hệ
            printProgressLite(currentGeneration);

            // Dừng khi thật sự tối ưu (hard=0 và soft=0)
            if (bestChromosome.isValid() && bestChromosome.getSoftConstraintViolations() == 0) {
                System.out.println("\n🎉 Đạt lịch tối ưu (hard=0, soft=0) tại thế hệ " + currentGeneration + "!");
                break;
            }

            // Dừng khi không cải thiện lâu
            if (noImprovementCount >= GAConfig.NO_IMPROVEMENT_LIMIT) {
                System.out.println("\n⚠️ Dừng: Không cải thiện sau " + GAConfig.NO_IMPROVEMENT_LIMIT + " thế hệ");
                break;
            }
        }

        // 5. Kết thúc + in tổng kết
        long endTime = System.currentTimeMillis();
        int executionTime = (int) ((endTime - startTime) / 1000);

        System.out.println("\n========================================");
        System.out.println("🏁 KẾT QUẢ CUỐI CÙNG");
        System.out.println("========================================");
        System.out.printf("Gen %d | Best: %.2f | Hard: %d | Soft: %d | Time: %ds\n",
                currentGeneration, bestFitness,
                bestChromosome.getHardConstraintViolations(),
                bestChromosome.getSoftConstraintViolations(),
                executionTime);
        System.out.println("Trạng thái: " + (bestChromosome.isValid() ? "✅ HỢP LỆ" : "❌ CÓ VI PHẠM"));
        System.out.println("========================================\n");

        // 6. Lưu kết quả
        GAResult result = new GAResult();
        result.setBestChromosome(bestChromosome);
        result.setGenerationsExecuted(currentGeneration);
        result.setExecutionTimeSeconds(executionTime);
        result.setSemester(semester);
        result.setAcademicYear(academicYear);
        saveResults(result);

        return result;
    }

    /** Load dữ liệu từ DB */
    private void loadData(String semester, String academicYear) {
        assignments = assignmentDAO.getAssignmentsBySemester(semester, academicYear);
        teachers = teacherDAO.getAllTeachers();
        rooms = roomDAO.getAllRooms();
        timeSlots = timeSlotDAO.getAllTimeSlots();
    }

    /** In tiến trình đơn giản kiểu: Gen k | Best fitness | Hard | Soft */
    private void printProgressLite(int gen) {
        System.out.printf("Gen %d  | Best fitness: %8.2f | Hard: %3d | Soft: %3d\n",
                gen,
                bestFitness,
                bestChromosome.getHardConstraintViolations(),
                bestChromosome.getSoftConstraintViolations());
    }

    /** Xây dựng seed từ schedules đã lưu để tiếp tục tối ưu */
    private Chromosome buildSeedChromosomeFromSavedSchedules(String semester, String academicYear) {
        List<Schedule> saved = scheduleDAO.getSchedulesBySemester(semester, academicYear);
        if (saved == null || saved.isEmpty()) return null;

        Map<Integer, Schedule> byAssignmentId = new HashMap<>();
        for (Schedule s : saved) byAssignmentId.put(s.getAssignmentId(), s);

        Map<Integer, Room> roomMap = new HashMap<>();
        for (Room r : rooms) roomMap.put(r.getRoomId(), r);

        Map<Integer, TimeSlot> slotMap = new HashMap<>();
        for (TimeSlot ts : timeSlots) slotMap.put(ts.getSlotId(), ts);

        Chromosome seed = new Chromosome();
        Random rnd = new Random();

        for (TeachingAssignment assignment : assignments) {
            Schedule s = byAssignmentId.get(assignment.getAssignmentId());
            Room room;
            TimeSlot slot;

            if (s != null) {
                room = roomMap.get(s.getRoomId());
                slot = slotMap.get(s.getSlotId());
                if (room == null || slot == null) {
                    List<Room> suitableRooms = getSuitableRooms(assignment);
                    room = !suitableRooms.isEmpty() ? suitableRooms.get(rnd.nextInt(suitableRooms.size())) : null;
                    slot = timeSlots.get(rnd.nextInt(timeSlots.size()));
                }
            } else {
                List<Room> suitableRooms = getSuitableRooms(assignment);
                room = !suitableRooms.isEmpty() ? suitableRooms.get(rnd.nextInt(suitableRooms.size())) : null;
                slot = timeSlots.get(rnd.nextInt(timeSlots.size()));
            }

            seed.addGene(new Gene(assignment, room, slot));
        }

        return seed;
    }

    /** Khởi tạo population từ seed + đa dạng hóa bằng cá thể ngẫu nhiên */
    private List<Chromosome> initializePopulationFromSeedWithDiversity(Chromosome seed) {
        List<Chromosome> population = new ArrayList<>();
        Random rnd = new Random();

        int clonesTarget = (int) Math.round(GAConfig.POPULATION_SIZE * GAConfig.SEED_CLONE_RATIO);
        int randomTarget = GAConfig.POPULATION_SIZE - clonesTarget;

        // Clone và mutate nhẹ
        for (int i = 0; i < clonesTarget; i++) {
            Chromosome clone = seed.clone();
            int times = 1 + rnd.nextInt(3);
            for (int t = 0; t < times; t++) {
                mutationOperator.mutate(clone, rooms, timeSlots);
            }
            mutationOperator.repairRoomTypes(clone, rooms);
            population.add(clone);
        }

        // Tiêm cá thể ngẫu nhiên
        List<Chromosome> randoms = initializeRandomPopulation();
        for (int i = 0; i < randomTarget && i < randoms.size(); i++) {
            population.add(randoms.get(i));
        }

        // Bù cho đủ size nếu thiếu
        while (population.size() < GAConfig.POPULATION_SIZE) {
            population.add(seed.clone());
        }

        return population;
    }

    /** Khởi tạo population ngẫu nhiên (lần chạy đầu) */
    private List<Chromosome> initializeRandomPopulation() {
        List<Chromosome> population = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < GAConfig.POPULATION_SIZE; i++) {
            Chromosome chromosome = new Chromosome();

            for (TeachingAssignment assignment : assignments) {
                List<Room> suitableRooms = getSuitableRooms(assignment);
                Room randomRoom = null;
                if (!suitableRooms.isEmpty()) {
                    randomRoom = suitableRooms.get(random.nextInt(suitableRooms.size()));
                }
                TimeSlot randomSlot = timeSlots.get(random.nextInt(timeSlots.size()));

                chromosome.addGene(new Gene(assignment, randomRoom, randomSlot));
            }

            population.add(chromosome);
        }

        return population;
    }

    /** Lấy phòng phù hợp cho assignment */
    private List<Room> getSuitableRooms(TeachingAssignment assignment) {
        List<Room> suitable = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getCapacity() < assignment.getNumStudents()) continue;
            if (assignment.isPractice() && assignment.isRequiresLab()) {
                if (room.isLab()) suitable.add(room);
            } else if (assignment.isTheory()) {
                if (!room.isLab()) suitable.add(room);
            } else {
                suitable.add(room);
            }
        }
        return suitable;
    }

    /** Tính mutation rate thích nghi theo số thế hệ không cải thiện */
    private double computeAdaptiveMutationRate() {
        int triggers = noImprovementCount / GAConfig.ADAPTIVE_TRIGGER;
        double rate = GAConfig.MUTATION_RATE + triggers * GAConfig.ADAPTIVE_STEP;
        if (rate > GAConfig.ADAPTIVE_MUTATION_MAX) rate = GAConfig.ADAPTIVE_MUTATION_MAX;
        return rate;
    }

    /** Lưu kết quả vào DB (ghi đè lịch cũ của học kỳ/năm học) */
    private void saveResults(GAResult result) {
        try {
            scheduleDAO.deleteSchedulesBySemester(result.getSemester(), result.getAcademicYear());
            List<Schedule> schedules = result.getBestChromosome().toSchedules();
            scheduleDAO.insertSchedules(schedules);

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

    /** Kết quả GA để truyền ra UI */
    public static class GAResult {
        private Chromosome bestChromosome;
        private int generationsExecuted;
        private int executionTimeSeconds;
        private String semester;
        private String academicYear;

        public void setBestChromosome(Chromosome bestChromosome) { this.bestChromosome = bestChromosome; }
        public void setGenerationsExecuted(int generationsExecuted) { this.generationsExecuted = generationsExecuted; }
        public void setExecutionTimeSeconds(int executionTimeSeconds) { this.executionTimeSeconds = executionTimeSeconds; }
        public void setSemester(String semester) { this.semester = semester; }
        public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

        public Chromosome getBestChromosome() { return bestChromosome; }
        public int getGenerationsExecuted() { return generationsExecuted; }
        public int getExecutionTimeSeconds() { return executionTimeSeconds; }
        public String getSemester() { return semester; }
        public String getAcademicYear() { return academicYear; }

        public double getBestFitness() { return bestChromosome != null ? bestChromosome.getFitnessScore() : 0.0; }
        public int getBestHardViolations() { return bestChromosome != null ? bestChromosome.getHardConstraintViolations() : 0; }
        public int getBestSoftViolations() { return bestChromosome != null ? bestChromosome.getSoftConstraintViolations() : 0; }
        public int getScheduleCount() {
            try {
                return (bestChromosome != null && bestChromosome.toSchedules() != null)
                        ? bestChromosome.toSchedules().size() : 0;
            } catch (Exception e) {
                return 0;
            }
        }
        public boolean isValid() { return bestChromosome != null && bestChromosome.isValid(); }
    }
}