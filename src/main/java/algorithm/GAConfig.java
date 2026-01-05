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
    public static final int NO_IMPROVEMENT_LIMIT = 50;      // Dừng nếu 50 thế hệ không cải thiện
    public static final double TARGET_FITNESS = 10000.0;    // Fitness mục tiêu (không vi phạm)
    
    // Fitness weights (trọng số cho các ràng buộc)
    public static final double WEIGHT_TEACHER_CONFLICT = -1000.0;      // Vi phạm nghiêm trọng
    public static final double WEIGHT_ROOM_CONFLICT = -1000.0;         // Vi phạm nghiêm trọng
    public static final double WEIGHT_TEACHER_SUBJECT_MISMATCH = -500.0;
    public static final double WEIGHT_ROOM_CAPACITY_EXCEEDED = -500.0;
    public static final double WEIGHT_ROOM_TYPE_MISMATCH = -500.0;
    public static final double WEIGHT_MAX_HOURS_EXCEEDED = -300.0;
    public static final double WEIGHT_ROOM_UTILIZATION_OPTIMAL = 100.0; // Thưởng nếu phòng vừa đủ
    public static final double WEIGHT_ROOM_UTILIZATION_POOR = -50.0;    // Phạt nếu lãng phí
    public static final double WEIGHT_TEACHER_PREFERENCE = 20.0;        // Thưởng nếu đúng sở thích

    // Penalty cho gaps (như trước)
    public static final int MAX_EMPTY_SLOTS_PER_TEACHER = 3;    // ngưỡng mặc định (tune được)
    public static final double WEIGHT_TEACHER_EMPTY_SLOTS = -20.0; // phạt mỗi ca trống vượt quá (tune)

    // Nếu một giáo viên được phân công trên MAX_DAYS_PER_TEACHER ngày -> mỗi ngày vượt quá bị phạt WEIGHT_TEACHER_TOO_MANY_DAYS_PENALTY
    public static final int MAX_DAYS_PER_TEACHER = 4; // nếu dạy >4 ngày thì bị phạt
    public static final double WEIGHT_TEACHER_TOO_MANY_DAYS_PENALTY = -40.0; // phạt mỗi ngày vượt quá (tune)

    // Debugging
    public static final boolean DEBUG_MODE = true;          // In log chi tiết
    public static final int PRINT_EVERY_N_GENERATIONS = 10; // In progress mỗi 10 thế hệ
    
    // Default max hours (fallback)
    public static final int DEFAULT_MAX_TEACHING_HOURS = 18;
    
    
    private GAConfig() {
        // Private constructor to prevent instantiation
    }
}