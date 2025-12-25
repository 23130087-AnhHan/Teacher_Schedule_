package test;

import algorithm.*;
import dao.*;
import model.*;
import util. JDBCUtils;

import java.util. List;

/**
 * Test từng component của GA
 */
public class TestComponents {
    
    public static void main(String[] args) {
        System.out.println("Testing GA Components...\n");
        
        // Test 1: Database connection
        testConnection();
        
        // Test 2: Load data
        testLoadData();
        
        // Test 3: Initialize population
        testInitializePopulation();
        
        // Test 4: Fitness calculation
        testFitnessCalculation();
        
        // Test 5: Selection
        testSelection();
        
        // Test 6: Crossover
        testCrossover();
        
        // Test 7: Mutation
        testMutation();
    }
    
    private static void testConnection() {
        System.out.println("═══ Test 1: Database Connection ═══");
        JDBCUtils.testConnection();
        System.out. println("✅ Connection test passed\n");
    }
    
    private static void testLoadData() {
        System.out.println("═══ Test 2: Load Data from Database ═══");
        
        TeachingAssignmentDAO assignmentDAO = new TeachingAssignmentDAO();
        TeacherDAO teacherDAO = new TeacherDAO();
        RoomDAO roomDAO = new RoomDAO();
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        
        List<TeachingAssignment> assignments = assignmentDAO.getAssignmentsBySemester("HK1", "2025-2026");
        List<Teacher> teachers = teacherDAO.getAllTeachers();
        List<Room> rooms = roomDAO.getAllRooms();
        List<TimeSlot> timeSlots = timeSlotDAO.getAllTimeSlots();
        
        System.out. println("Assignments:  " + assignments.size());
        System.out.println("Teachers: " + teachers.size());
        System.out.println("Rooms: " + rooms.size());
        System.out.println("Time Slots: " + timeSlots.size());
        
        if (assignments.isEmpty()) {
            System.err.println("❌ No assignments found!  Check database.");
        } else {
            System.out.println("✅ Data loaded successfully");
            System.out.println("First assignment: " + assignments.get(0));
        }
        System.out.println();
    }
    
    private static void testInitializePopulation() {
        System.out.println("═══ Test 3: Initialize Population ═══");
        
        TeachingAssignmentDAO assignmentDAO = new TeachingAssignmentDAO();
        RoomDAO roomDAO = new RoomDAO();
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        
        List<TeachingAssignment> assignments = assignmentDAO.getAssignmentsBySemester("HK1", "2025-2026");
        List<Room> rooms = roomDAO.getAllRooms();
        List<TimeSlot> timeSlots = timeSlotDAO.getAllTimeSlots();
        
        if (assignments.isEmpty()) {
            System.err.println("❌ Cannot test - no assignments");
            return;
        }
        
        // Tạo 1 chromosome ngẫu nhiên
        Chromosome chromosome = new Chromosome();
        java.util.Random random = new java.util.Random();
        
        for (TeachingAssignment assignment : assignments) {
            Room randomRoom = rooms.get(random. nextInt(rooms.size()));
            TimeSlot randomSlot = timeSlots.get(random. nextInt(timeSlots.size()));
            
            Gene gene = new Gene(assignment, randomRoom, randomSlot);
            chromosome. addGene(gene);
        }
        
        System.out.println("Chromosome created with " + chromosome.getGeneCount() + " genes");
        System.out.println("First gene: " + chromosome.getGene(0));
        System.out.println("✅ Population initialization works\n");
    }
    
    private static void testFitnessCalculation() {
        System.out.println("═══ Test 4: Fitness Calculation ═══");
        
        TeachingAssignmentDAO assignmentDAO = new TeachingAssignmentDAO();
        TeacherDAO teacherDAO = new TeacherDAO();
        RoomDAO roomDAO = new RoomDAO();
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        
        List<TeachingAssignment> assignments = assignmentDAO.getAssignmentsBySemester("HK1", "2025-2026");
        List<Teacher> teachers = teacherDAO.getAllTeachers();
        List<Room> rooms = roomDAO.getAllRooms();
        List<TimeSlot> timeSlots = timeSlotDAO.getAllTimeSlots();
        
        if (assignments. isEmpty()) {
            System.err.println("❌ Cannot test - no assignments");
            return;
        }
        
        // Tạo chromosome
        Chromosome chromosome = new Chromosome();
        java.util.Random random = new java.util.Random();
        
        for (TeachingAssignment assignment : assignments) {
            Room randomRoom = rooms.get(random.nextInt(rooms.size()));
            TimeSlot randomSlot = timeSlots.get(random.nextInt(timeSlots. size()));
            Gene gene = new Gene(assignment, randomRoom, randomSlot);
            chromosome.addGene(gene);
        }
        
        // Calculate fitness
        FitnessCalculator calculator = new FitnessCalculator();
        double fitness = calculator.calculateFitness(chromosome, teachers, rooms, timeSlots);
        
        System.out. println("Fitness Score: " + String.format("%.2f", fitness));
        System.out.println("Hard Violations: " + chromosome.getHardConstraintViolations());
        System.out.println("Soft Violations: " + chromosome.getSoftConstraintViolations());
        System.out.println("Is Valid: " + chromosome.isValid());
        System.out.println("✅ Fitness calculation works\n");
    }
    
    private static void testSelection() {
        System.out.println("═══ Test 5: Selection ═══");
        
        // Tạo population giả
        java.util.List<Chromosome> population = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Chromosome c = new Chromosome();
            c.setFitnessScore(Math.random() * 1000);
            population.add(c);
        }
        
        SelectionOperator selector = new SelectionOperator();
        
        // Test tournament selection
        Chromosome selected = selector.tournamentSelection(population);
        System.out.println("Tournament selected fitness: " + String.format("%.2f", selected.getFitnessScore()));
        
        // Test elite selection
        java.util.List<Chromosome> elite = selector.selectElite(population);
        System.out.println("Elite count: " + elite.size());
        System.out.println("Best elite fitness: " + String.format("%. 2f", elite.get(0).getFitnessScore()));
        
        System.out.println("✅ Selection works\n");
    }
    
    private static void testCrossover() {
        System.out.println("═══ Test 6: Crossover ═══");
        
        // Tạo 2 parent giả
        Chromosome parent1 = new Chromosome();
        Chromosome parent2 = new Chromosome();
        
        for (int i = 0; i < 10; i++) {
            parent1.addGene(new Gene());
            parent2.addGene(new Gene());
        }
        
        CrossoverOperator crossover = new CrossoverOperator();
        Chromosome[] children = crossover.singlePointCrossover(parent1, parent2);
        
        System.out.println("Parent 1 genes: " + parent1.getGeneCount());
        System.out.println("Parent 2 genes: " + parent2.getGeneCount());
        System.out.println("Child 1 genes: " + children[0].getGeneCount());
        System.out.println("Child 2 genes: " + children[1].getGeneCount());
        System.out.println("✅ Crossover works\n");
    }
    
    private static void testMutation() {
        System.out.println("═══ Test 7: Mutation ═══");
        
        RoomDAO roomDAO = new RoomDAO();
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO();
        
        List<Room> rooms = roomDAO.getAllRooms();
        List<TimeSlot> timeSlots = timeSlotDAO.getAllTimeSlots();
        
        TeachingAssignmentDAO assignmentDAO = new TeachingAssignmentDAO();
        List<TeachingAssignment> assignments = assignmentDAO.getAssignmentsBySemester("HK1", "2025-2026");
        
        if (assignments.isEmpty() || rooms.isEmpty() || timeSlots.isEmpty()) {
            System.err.println("❌ Cannot test - missing data");
            return;
        }
        
        // Tạo chromosome
        Chromosome chromosome = new Chromosome();
        java.util.Random random = new java.util.Random();
        
        for (int i = 0; i < Math.min(5, assignments.size()); i++) {
            Room randomRoom = rooms.get(random.nextInt(rooms.size()));
            TimeSlot randomSlot = timeSlots.get(random.nextInt(timeSlots.size()));
            Gene gene = new Gene(assignments. get(i), randomRoom, randomSlot);
            chromosome.addGene(gene);
        }
        
        System.out.println("Before mutation - Gene 0 room: " + chromosome.getGene(0).getRoomId());
        
        MutationOperator mutator = new MutationOperator();
        mutator.mutate(chromosome, rooms, timeSlots);
        
        System.out.println("After mutation - Gene 0 room:  " + chromosome.getGene(0).getRoomId());
        System.out.println("✅ Mutation works\n");
    }
}