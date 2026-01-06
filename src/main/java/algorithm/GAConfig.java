package algorithm;

/**
 * Configuration cho Genetic Algorithm
 */
public class GAConfig {
    
    // Population settings
    public static final int POPULATION_SIZE = 50;           // Số chromosome trong population
    public static final int MAX_GENERATIONS = 500;          // Số thế hệ tối đa
    public static final int ELITE_SIZE = 5;                 // Số elite giữ lại mỗi thế hệ
    
    // Genetic operators
    public static final double CROSSOVER_RATE = 0.8;        // Tỷ lệ lai ghép (80%)
    public static final double MUTATION_RATE = 0.15;        // Tỷ lệ đột biến (15%)
    
    // Selection
    public static final int TOURNAMENT_SIZE = 5;            // Kích thước tournament selection
    
    // Termination
    public static final int NO_IMPROVEMENT_LIMIT = 500;     // Dừng nếu 100 thế hệ không cải thiện
    public static final double TARGET_FITNESS = 10000.0;    // (không còn dùng để disable nút)
    
    // Fitness weights
    public static final double WEIGHT_TEACHER_CONFLICT = -1000.0;
    public static final double WEIGHT_ROOM_CONFLICT = -1000.0;
    public static final double WEIGHT_TEACHER_SUBJECT_MISMATCH = -500.0;
    public static final double WEIGHT_ROOM_CAPACITY_EXCEEDED = -500.0;
    public static final double WEIGHT_ROOM_TYPE_MISMATCH = -500.0; // soft
    public static final double WEIGHT_MAX_HOURS_EXCEEDED = -300.0;
    public static final double WEIGHT_ROOM_UTILIZATION_OPTIMAL = 100.0;
    public static final double WEIGHT_ROOM_UTILIZATION_POOR = -50.0;
    public static final double WEIGHT_TEACHER_PREFERENCE = 20.0;

    public static final int MAX_EMPTY_SLOTS_PER_TEACHER = 3;
    public static final double WEIGHT_TEACHER_EMPTY_SLOTS = -20.0;

    public static final int MAX_DAYS_PER_TEACHER = 4;
    public static final double WEIGHT_TEACHER_TOO_MANY_DAYS_PENALTY = -40.0;

    // Debugging: in ra từng thế hệ
    public static final boolean DEBUG_MODE = true;
    public static final int PRINT_EVERY_N_GENERATIONS = 1; // In mỗi thế hệ
    
    public static final int DEFAULT_MAX_TEACHING_HOURS = 18;

    // Tiếp tục tối ưu
    public static final double SEED_CLONE_RATIO = 0.6;      
    public static final double RANDOM_INJECTION_RATIO = 0.4; 
    
    // Đột biến thích nghi
    public static final int ADAPTIVE_TRIGGER = 10;          
    public static final double ADAPTIVE_STEP = 0.05;        
    public static final double ADAPTIVE_MUTATION_MAX = 0.5; 
    
    private GAConfig() {}
}