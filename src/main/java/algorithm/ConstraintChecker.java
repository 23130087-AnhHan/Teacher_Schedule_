package algorithm;

import model.*;
import dao.TeacherSubjectDAO;

import java.util.*;

/**
 * ConstraintChecker - kiểm tra ràng buộc hard/soft cho một Chromosome
 * - Cache teacher-subject mappings để tránh query lặp lại
 * - Trả về ConstraintCheckResult chứa danh sách violations và các counters
 */
public class ConstraintChecker {
    
    // Cache teacher-subject mappings trong memory
    private Set<String> validTeacherSubjectPairs;
    private Map<String, Boolean> theoryTeachingMap;
    private Map<String, Boolean> practiceTeachingMap;
    
    public ConstraintChecker() {
        loadTeacherSubjectMappings();
    }
    
    private void loadTeacherSubjectMappings() {
        TeacherSubjectDAO dao = new TeacherSubjectDAO();
        List<TeacherSubject> allMappings = dao.getAllMappings();
        
        validTeacherSubjectPairs = new HashSet<>();
        theoryTeachingMap = new HashMap<>();
        practiceTeachingMap = new HashMap<>();
        
        for (TeacherSubject mapping :  allMappings) {
            String key = mapping.getTeacherId() + "-" + mapping.getSubjectId();
            validTeacherSubjectPairs.add(key);
            theoryTeachingMap.put(key, mapping.isCanTeachTheory());
            practiceTeachingMap.put(key, mapping.isCanTeachPractice());
        }
        
        if (GAConfig.DEBUG_MODE) {
            System.out.println("✅ Loaded " + validTeacherSubjectPairs.size() + " teacher-subject mappings into memory");
        }
    }
    
    public ConstraintCheckResult checkAllConstraints(Chromosome chromosome, 
                                                      List<Teacher> teachers,
                                                      List<Room> rooms,
                                                      List<TimeSlot> timeSlots) {
        ConstraintCheckResult result = new ConstraintCheckResult();
        
        checkTeacherConflicts(chromosome, result);
        checkRoomConflicts(chromosome, result);
        checkTeacherSubjectMatch(chromosome, result);
        checkRoomCapacity(chromosome, result);
        checkRoomType(chromosome, result);
        checkMaxTeachingHours(chromosome, teachers, result);
        evaluateRoomUtilization(chromosome, result);

        // Soft constraints
        evaluateTeacherEmptySlots(chromosome, teachers, timeSlots, result);
        evaluateTeacherTooManyDays(chromosome, teachers, timeSlots, result); // NEW: penalty if teacher teaches > MAX_DAYS_PER_TEACHER
        
        return result;
    }
    
    private void checkTeacherConflicts(Chromosome chromosome, ConstraintCheckResult result) {
        Map<String, Integer> teacherSlotCount = new HashMap<>();
        
        for (Gene gene : chromosome.getGenes()) {
            String key = gene.getTeacherId() + "-" + gene.getSlotId();
            teacherSlotCount.put(key, teacherSlotCount.getOrDefault(key, 0) + 1);
        }
        
        for (Map.Entry<String, Integer> entry : teacherSlotCount.entrySet()) {
            if (entry.getValue() > 1) {
                result.addHardViolation("TEACHER_TIME_CONFLICT", 
                    "Giáo viên dạy " + entry.getValue() + " lớp cùng slot");
                result.incrementTeacherConflicts();
            }
        }
    }
    
    private void checkRoomConflicts(Chromosome chromosome, ConstraintCheckResult result) {
        Map<String, Integer> roomSlotCount = new HashMap<>();
        
        for (Gene gene : chromosome.getGenes()) {
            String key = gene.getRoomId() + "-" + gene.getSlotId();
            roomSlotCount.put(key, roomSlotCount.getOrDefault(key, 0) + 1);
        }
        
        for (Map.Entry<String, Integer> entry : roomSlotCount.entrySet()) {
            if (entry.getValue() > 1) {
                result.addHardViolation("ROOM_TIME_CONFLICT",
                    "Phòng có " + entry.getValue() + " lớp cùng slot");
                result.incrementRoomConflicts();
            }
        }
    }
    
    private void checkTeacherSubjectMatch(Chromosome chromosome, ConstraintCheckResult result) {
        for (Gene gene : chromosome.getGenes()) {
            TeachingAssignment assignment = gene.getAssignment();
            String key = assignment.getTeacherId() + "-" + assignment.getSubjectId();
            
            // Kiểm tra trong memory - KHÔNG QUERY DATABASE trong vòng lặp
            if (!validTeacherSubjectPairs.contains(key)) {
                result.addHardViolation("TEACHER_SUBJECT_MISMATCH",
                    "Giáo viên " + assignment.getTeacherId() + 
                    " không dạy được môn " + assignment.getSubjectId());
                result.incrementTeacherSubjectMismatches();
                continue;
            }
            
            if (assignment.isTheory() && !theoryTeachingMap.getOrDefault(key, false)) {
                result.addHardViolation("TEACHER_SUBJECT_MISMATCH",
                    "Giáo viên không dạy lý thuyết môn này");
                result.incrementTeacherSubjectMismatches();
            }
            
            if (assignment.isPractice() && !practiceTeachingMap.getOrDefault(key, false)) {
                result.addHardViolation("TEACHER_SUBJECT_MISMATCH",
                    "Giáo viên không dạy thực hành môn này");
                result.incrementTeacherSubjectMismatches();
            }
        }
    }
    
    private void checkRoomCapacity(Chromosome chromosome, ConstraintCheckResult result) {
        for (Gene gene : chromosome.getGenes()) {
            Room room = gene.getRoom();
            int numStudents = gene.getAssignment().getNumStudents();
            
            if (room == null) continue;
            if (numStudents > room.getCapacity()) {
                result.addHardViolation("ROOM_CAPACITY_EXCEEDED",
                    "Phòng " + room.getRoomCode() + " không đủ chỗ");
                result.incrementRoomCapacityViolations();
            }
        }
    }
    
    private void checkRoomType(Chromosome chromosome, ConstraintCheckResult result) {
        for (Gene gene : chromosome.getGenes()) {
            Room room = gene.getRoom();
            TeachingAssignment assignment = gene.getAssignment();
            
            if (room == null || assignment == null) continue;
            
            // Treat ROOM_TYPE_MISMATCH as SOFT constraint (message + counter)
            if (assignment.isPractice() && assignment.isRequiresLab() && !room.isLab()) {
                result.addSoftViolation("ROOM_TYPE_MISMATCH",
                    "Thực hành " + assignment.getGroupName() + 
                    " (" + assignment.getNumStudents() + " SV) dùng phòng " + room.getRoomCode());
                result.incrementRoomTypeMismatches(); // counted as soft now
            }
            
            if (assignment.isTheory() && room.isLab()) {
                result.addSoftViolation("ROOM_TYPE_MISMATCH",
                    "Lý thuyết dùng phòng Lab: " + room.getRoomCode());
                result.incrementRoomTypeMismatches();
            }
        }
    }
    
    private void checkMaxTeachingHours(Chromosome chromosome, List<Teacher> teachers, 
                                       ConstraintCheckResult result) {
        Map<Integer, Integer> teacherHoursMap = new HashMap<>();
        
        for (Gene gene : chromosome.getGenes()) {
            int teacherId = gene.getTeacherId();
            teacherHoursMap.put(teacherId, teacherHoursMap.getOrDefault(teacherId, 0) + 3);
        }
        
        Map<Integer, Teacher> teacherMap = new HashMap<>();
        for (Teacher teacher : teachers) {
            teacherMap.put(teacher.getTeacherId(), teacher);
        }
        
        for (Map.Entry<Integer, Integer> entry : teacherHoursMap.entrySet()) {
            Teacher teacher = teacherMap.get(entry.getKey());
            if (teacher != null && entry.getValue() > teacher.getMaxTeachingHours()) {
                result.addSoftViolation("MAX_HOURS_EXCEEDED",
                    "Giáo viên vượt " + (entry.getValue() - teacher.getMaxTeachingHours()) + " giờ");
                result.incrementMaxHoursViolations();
            }
        }
    }
    
    private void evaluateRoomUtilization(Chromosome chromosome, ConstraintCheckResult result) {
        for (Gene gene : chromosome.getGenes()) {
            Room room = gene.getRoom();
            if (room == null) continue;
            int numStudents = gene.getAssignment().getNumStudents();
            double utilization = room.getUtilizationRate(numStudents);
            
            if (utilization >= 80.0 && utilization <= 100.0) {
                result.addRoomUtilizationBonus();
            } else if (utilization < 50.0) {
                result.addRoomUtilizationPenalty();
                result.addSoftViolation("ROOM_UTILIZATION_POOR",
                    "Phòng chỉ sử dụng " + String.format("%.1f", utilization) + "%");
            }
        }
    }
    
    /**
     * Đánh giá số ca trống (gaps) của giáo viên.
     * - Sử dụng ordering của timeSlots để xác định span
     * - emptyWithinSpan = span - assignedCount
     * - Nếu emptyWithinSpan > GAConfig.MAX_EMPTY_SLOTS_PER_TEACHER => soft penalties
     */
    private void evaluateTeacherEmptySlots(Chromosome chromosome, List<Teacher> teachers,
                                           List<TimeSlot> timeSlots, ConstraintCheckResult result) {
        if (timeSlots == null || timeSlots.isEmpty()) return;
        
        Map<Integer, Integer> slotIndexMap = new HashMap<>();
        for (int i = 0; i < timeSlots.size(); i++) {
            slotIndexMap.put(timeSlots.get(i).getSlotId(), i);
        }
        
        Map<Integer, Set<Integer>> teacherAssignedIndices = new HashMap<>();
        for (Gene gene : chromosome.getGenes()) {
            Integer idx = slotIndexMap.get(gene.getSlotId());
            if (idx == null) continue;
            teacherAssignedIndices.computeIfAbsent(gene.getTeacherId(), k -> new HashSet<>()).add(idx);
        }
        
        for (Teacher teacher : teachers) {
            int tid = teacher.getTeacherId();
            Set<Integer> indices = teacherAssignedIndices.getOrDefault(tid, Collections.emptySet());
            int assignedCount = indices.size();
            if (assignedCount == 0) continue;
            
            int minIdx = Integer.MAX_VALUE;
            int maxIdx = Integer.MIN_VALUE;
            for (int id : indices) {
                minIdx = Math.min(minIdx, id);
                maxIdx = Math.max(maxIdx, id);
            }
            int span = maxIdx - minIdx + 1;
            int emptyWithinSpan = span - assignedCount;
            
            if (emptyWithinSpan > GAConfig.MAX_EMPTY_SLOTS_PER_TEACHER) {
                int penaltyCount = emptyWithinSpan - GAConfig.MAX_EMPTY_SLOTS_PER_TEACHER;
                result.addSoftViolation("TEACHER_EMPTY_SLOTS",
                    "Giáo viên " + teacher.getTeacherCode() + " có " + emptyWithinSpan + " ca trống trong span [" + minIdx + "-" + maxIdx + "]");
                for (int i = 0; i < penaltyCount; i++) {
                    result.incrementTeacherEmptySlotPenalties();
                }
            }
        }
    }
    
    /**
     * NEW: Nếu giáo viên dạy quá nhiều ngày trong tuần (> MAX_DAYS_PER_TEACHER) thì phạt (soft).
     * - actualDays = số ngày khác nhau teacher được phân công (distinct dayOfWeek)
     * - If actualDays > GAConfig.MAX_DAYS_PER_TEACHER:
     *     excess = actualDays - MAX_DAYS_PER_TEACHER
     *     increment teacherTooManyDaysPenalties by excess
     */
    private void evaluateTeacherTooManyDays(Chromosome chromosome, List<Teacher> teachers,
                                           List<TimeSlot> timeSlots, ConstraintCheckResult result) {
        if (timeSlots == null || timeSlots.isEmpty()) return;
        
        Map<Integer, TimeSlot.DayOfWeek> slotDayMap = new HashMap<>();
        for (TimeSlot ts : timeSlots) {
            slotDayMap.put(ts.getSlotId(), ts.getDayOfWeek());
        }
        
        Map<Integer, Set<TimeSlot.DayOfWeek>> teacherDays = new HashMap<>();
        for (Gene gene : chromosome.getGenes()) {
            TimeSlot.DayOfWeek day = slotDayMap.get(gene.getSlotId());
            if (day == null) continue;
            teacherDays.computeIfAbsent(gene.getTeacherId(), k -> new HashSet<>()).add(day);
        }
        
        for (Teacher teacher : teachers) {
            int tid = teacher.getTeacherId();
            Set<TimeSlot.DayOfWeek> days = teacherDays.getOrDefault(tid, Collections.emptySet());
            int actualDays = days.size();
            if (actualDays <= GAConfig.MAX_DAYS_PER_TEACHER) continue;
            int excess = actualDays - GAConfig.MAX_DAYS_PER_TEACHER;
            result.addSoftViolation("TEACHER_TOO_MANY_DAYS",
                "Giáo viên " + teacher.getTeacherCode() + " dạy " + actualDays + " ngày (vượt " + excess + " ngày)");
            for (int i = 0; i < excess; i++) {
                result.incrementTeacherTooManyDaysPenalties();
            }
        }
    }
    
    public static class ConstraintCheckResult {
        private List<String> hardViolations = new ArrayList<>();
        private List<String> softViolations = new ArrayList<>();
        
        private int teacherConflicts = 0;
        private int roomConflicts = 0;
        private int teacherSubjectMismatches = 0;
        private int roomCapacityViolations = 0;
        private int roomTypeMismatches = 0;
        private int maxHoursViolations = 0;
        
        private int roomUtilizationBonuses = 0;
        private int roomUtilizationPenalties = 0;

        // Penalties / counters for soft constraints
        private int teacherEmptySlotPenalties = 0;
        private int teacherTooManyDaysPenalties = 0; // NEW: penalties for teaching too many days
        
        public void addHardViolation(String type, String description) {
            hardViolations.add("[" + type + "] " + description);
        }
        
        public void addSoftViolation(String type, String description) {
            softViolations.add("[" + type + "] " + description);
        }
        
        /**
         * ROOM_TYPE_MISMATCH is treated as a soft constraint in this code.
         */
        public int getTotalHardViolations() {
            return teacherConflicts + roomConflicts + teacherSubjectMismatches + 
                   roomCapacityViolations;
        }
        
        public int getTotalSoftViolations() {
            return maxHoursViolations + roomUtilizationPenalties + teacherEmptySlotPenalties + teacherTooManyDaysPenalties + roomTypeMismatches;
        }
        
        public boolean isValid() {
            return getTotalHardViolations() == 0;
        }
        
        // Getters
        public List<String> getHardViolations() { return hardViolations; }
        public List<String> getSoftViolations() { return softViolations; }
        public int getTeacherConflicts() { return teacherConflicts; }
        public int getRoomConflicts() { return roomConflicts; }
        public int getTeacherSubjectMismatches() { return teacherSubjectMismatches; }
        public int getRoomCapacityViolations() { return roomCapacityViolations; }
        public int getRoomTypeMismatches() { return roomTypeMismatches; }
        public int getMaxHoursViolations() { return maxHoursViolations; }
        public int getRoomUtilizationBonuses() { return roomUtilizationBonuses; }
        public int getRoomUtilizationPenalties() { return roomUtilizationPenalties; }

        // New getters for penalties
        public int getTeacherEmptySlotPenalties() { return teacherEmptySlotPenalties; }
        public int getTeacherTooManyDaysPenalties() { return teacherTooManyDaysPenalties; }
        
        // Incrementers
        public void incrementTeacherConflicts() { teacherConflicts++; }
        public void incrementRoomConflicts() { roomConflicts++; }
        public void incrementTeacherSubjectMismatches() { teacherSubjectMismatches++; }
        public void incrementRoomCapacityViolations() { roomCapacityViolations++; }
        public void incrementRoomTypeMismatches() { roomTypeMismatches++; }
        public void incrementMaxHoursViolations() { maxHoursViolations++; }
        public void addRoomUtilizationBonus() { roomUtilizationBonuses++; }
        public void addRoomUtilizationPenalty() { roomUtilizationPenalties++; }

        // New incrementers
        public void incrementTeacherEmptySlotPenalties() { teacherEmptySlotPenalties++; }
        public void incrementTeacherTooManyDaysPenalties() { teacherTooManyDaysPenalties++; }
    }
}