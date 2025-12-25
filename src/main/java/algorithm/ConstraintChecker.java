package algorithm;

import model.*;
import dao.TeacherSubjectDAO;

import java.util.*;

public class ConstraintChecker {
    
    // ✅ Cache teacher-subject mappings trong memory
    private Set<String> validTeacherSubjectPairs;
    private Map<String, Boolean> theoryTeachingMap;
    private Map<String, Boolean> practiceTeachingMap;
    
    public ConstraintChecker() {
        // ✅ Load TẤT CẢ mappings vào memory 1 LẦN DUY NHẤT
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
            validTeacherSubjectPairs. add(key);
            theoryTeachingMap.put(key, mapping.isCanTeachTheory());
            practiceTeachingMap.put(key, mapping.isCanTeachPractice());
        }
        
        System.out.println("✅ Loaded " + validTeacherSubjectPairs.size() + " teacher-subject mappings into memory");
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
        
        for (Gene gene : chromosome. getGenes()) {
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
            
            // ✅ Kiểm tra trong memory - KHÔNG QUERY DATABASE! 
            if (! validTeacherSubjectPairs.contains(key)) {
                result.addHardViolation("TEACHER_SUBJECT_MISMATCH",
                    "Giáo viên " + assignment.getTeacherId() + 
                    " không dạy được môn " + assignment.getSubjectId());
                result.incrementTeacherSubjectMismatches();
                continue;
            }
            
            // Kiểm tra chi tiết LT hoặc TH
            if (assignment.isTheory() && !theoryTeachingMap.getOrDefault(key, false)) {
                result.addHardViolation("TEACHER_SUBJECT_MISMATCH",
                    "Giáo viên không dạy lý thuyết môn này");
                result.incrementTeacherSubjectMismatches();
            }
            
            if (assignment. isPractice() && !practiceTeachingMap.getOrDefault(key, false)) {
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
            TeachingAssignment assignment = gene. getAssignment();
            
            // ✅ NẾU thực hành cần Lab NHƯNG phòng Lab không đủ chỗ
            // → Cho phép dùng Theory room (soft violation)
            if (assignment.isPractice() && assignment.isRequiresLab() && !room.isLab()) {
                // Kiểm tra xem có Lab nào đủ chỗ không
                boolean hasEnoughLab = false;
                // (sẽ check ở mutation)
                
                result.addSoftViolation("ROOM_TYPE_MISMATCH",
                    "Thực hành " + assignment.getGroupName() + 
                    " (" + assignment.getNumStudents() + " SV) dùng phòng " + room.getRoomCode() + 
                    " (do không có Lab đủ chỗ)");
                // ✅ KHÔNG tăng hard violation nữa
            }
            
            // Lý thuyết dùng Lab vẫn là soft violation
            if (assignment.isTheory() && room.isLab()) {
                result.addSoftViolation("ROOM_TYPE_MISMATCH",
                    "Lý thuyết không nên dùng phòng Lab:  " + room.getRoomCode());
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
            teacherMap. put(teacher.getTeacherId(), teacher);
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
        for (Gene gene :  chromosome.getGenes()) {
            Room room = gene.getRoom();
            int numStudents = gene.getAssignment().getNumStudents();
            double utilization = room.getUtilizationRate(numStudents);
            
            if (utilization >= 80.0 && utilization <= 100.0) {
                result.addRoomUtilizationBonus();
            } else if (utilization < 50.0) {
                result. addRoomUtilizationPenalty();
                result.addSoftViolation("ROOM_UTILIZATION_POOR",
                    "Phòng chỉ sử dụng " + String.format("%.1f", utilization) + "%");
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
        
        public void addHardViolation(String type, String description) {
            hardViolations.add("[" + type + "] " + description);
        }
        
        public void addSoftViolation(String type, String description) {
            softViolations. add("[" + type + "] " + description);
        }
        
        public int getTotalHardViolations() {
            return teacherConflicts + roomConflicts + teacherSubjectMismatches + 
                   roomCapacityViolations + roomTypeMismatches;
        }
        
        public int getTotalSoftViolations() {
            return maxHoursViolations + roomUtilizationPenalties;
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
        
        // Incrementers
        public void incrementTeacherConflicts() { teacherConflicts++; }
        public void incrementRoomConflicts() { roomConflicts++; }
        public void incrementTeacherSubjectMismatches() { teacherSubjectMismatches++; }
        public void incrementRoomCapacityViolations() { roomCapacityViolations++; }
        public void incrementRoomTypeMismatches() { roomTypeMismatches++; }
        public void incrementMaxHoursViolations() { maxHoursViolations++; }
        public void addRoomUtilizationBonus() { roomUtilizationBonuses++; }
        public void addRoomUtilizationPenalty() { roomUtilizationPenalties++; }
    }
}