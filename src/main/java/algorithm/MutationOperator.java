package algorithm;

import model.*;
import java.util.*;

/**
 * Mutation Operator - Đột biến chromosome
 * Thay đổi ngẫu nhiên để tạo đa dạng
 */
public class MutationOperator {
    
    private Random random;
    
    public MutationOperator() {
        this.random = new Random();
    }
    
    /**
     * Mutate một chromosome
     */
    public void mutate(Chromosome chromosome, List<Room> allRooms, List<TimeSlot> allTimeSlots) {
        if (chromosome.getGeneCount() == 0) {
            return;
        }
        
        // Quyết định loại mutation
        double mutationType = random.nextDouble();
        
        if (mutationType < 0.4) {
            // 40%: Thay đổi phòng
            mutateRoom(chromosome, allRooms);
        } else if (mutationType < 0.8) {
            // 40%: Thay đổi slot
            mutateTimeSlot(chromosome, allTimeSlots);
        } else {
            // 20%: Swap 2 genes
            swapGenes(chromosome);
        }
    }
    
    /**
     * 1.  Thay đổi phòng của một gene ngẫu nhiên
     */
    private void mutateRoom(Chromosome chromosome, List<Room> allRooms) {
        if (allRooms.isEmpty()) return;
        
        // Chọn gene ngẫu nhiên
        int geneIndex = random.nextInt(chromosome.getGeneCount());
        Gene gene = chromosome.getGene(geneIndex);
        
        // Lấy phòng phù hợp
        List<Room> suitableRooms = getSuitableRooms(gene. getAssignment(), allRooms);
        
        if (! suitableRooms.isEmpty()) {
            // Chọn phòng ngẫu nhiên
            Room newRoom = suitableRooms.get(random.nextInt(suitableRooms.size()));
            gene.setRoom(newRoom);
        }
    }
    
    /**
     * 2. Thay đổi time slot của một gene ngẫu nhiên
     */
    private void mutateTimeSlot(Chromosome chromosome, List<TimeSlot> allTimeSlots) {
        if (allTimeSlots. isEmpty()) return;
        
        // Chọn gene ngẫu nhiên
        int geneIndex = random.nextInt(chromosome.getGeneCount());
        Gene gene = chromosome.getGene(geneIndex);
        
        // Chọn slot ngẫu nhiên
        TimeSlot newSlot = allTimeSlots.get(random. nextInt(allTimeSlots. size()));
        gene.setTimeSlot(newSlot);
    }
    
    /**
     * 3. Swap 2 genes ngẫu nhiên
     */
    private void swapGenes(Chromosome chromosome) {
        if (chromosome.getGeneCount() < 2) return;
        
        // Chọn 2 vị trí ngẫu nhiên
        int index1 = random.nextInt(chromosome.getGeneCount());
        int index2 = random. nextInt(chromosome.getGeneCount());
        
        // Đảm bảo 2 index khác nhau
        while (index2 == index1) {
            index2 = random.nextInt(chromosome.getGeneCount());
        }
        
        // Swap room và slot của 2 genes
        Gene gene1 = chromosome.getGene(index1);
        Gene gene2 = chromosome.getGene(index2);
        
        Room tempRoom = gene1.getRoom();
        TimeSlot tempSlot = gene1.getTimeSlot();
        
        gene1.setRoom(gene2.getRoom());
        gene1.setTimeSlot(gene2.getTimeSlot());
        
        gene2.setRoom(tempRoom);
        gene2.setTimeSlot(tempSlot);
    }
    
    /**
     * Lấy danh sách phòng phù hợp cho assignment
     */
    private List<Room> getSuitableRooms(TeachingAssignment assignment, List<Room> allRooms) {
        List<Room> suitable = new ArrayList<>();
        
        for (Room room : allRooms) {
            // Kiểm tra sức chứa
            if (room.getCapacity() < assignment.getNumStudents()) {
                continue;
            }
            
            // ✅ ƯU TIÊN: Nếu thực hành cần Lab → CHỈ LẤY LAB
            if (assignment.isPractice() && assignment.isRequiresLab()) {
                if (room.isLab()) {
                    suitable.add(room);
                }
            } 
            // Nếu lý thuyết → KHÔNG LẤY LAB
            else if (assignment.isTheory()) {
                if (!room.isLab()) {
                    suitable.add(room);
                }
            } 
            // Các trường hợp khác
            else {
                suitable.add(room);
            }
        }
        
        // ✅ NẾU KHÔNG TÌM ĐƯỢC PHÒNG PHÙ HỢP
        // → Trả về tất cả phòng đủ chỗ (bỏ qua ràng buộc loại)
        if (suitable.isEmpty()) {
            for (Room room : allRooms) {
                if (room. getCapacity() >= assignment.getNumStudents()) {
                    suitable. add(room);
                }
            }
        }
        
        return suitable;
    }
    public void mutatePopulation(List<Chromosome> population, List<Room> allRooms, List<TimeSlot> allTimeSlots) {
        for (Chromosome chromosome : population) {
            if (random.nextDouble() < GAConfig.MUTATION_RATE) {
                mutate(chromosome, allRooms, allTimeSlots);
            }
        }
    }
    /**
     * Sửa room type mismatches
     * Nếu thực hành cần Lab nhưng đang dùng Theory room → Đổi sang Lab
     */
    public void repairRoomTypes(Chromosome chromosome, List<Room> allRooms) {
        List<Room> labRooms = new ArrayList<>();
        List<Room> theoryRooms = new ArrayList<>();
        
        for (Room room : allRooms) {
            if (room.isLab()) {
                labRooms.add(room);
            } else {
                theoryRooms. add(room);
            }
        }
        
        if (labRooms.isEmpty()) {
            System.err.println("WARNING: No Lab rooms in system!");
            return;
        }
        
        Random random = new Random();
        
        for (Gene gene : chromosome.getGenes()) {
            TeachingAssignment assignment = gene.getAssignment();
            Room currentRoom = gene. getRoom();
            
            // Only process PRACTICE assignments requiring Lab
            if (assignment.isPractice() && assignment.isRequiresLab()) {
                
                // Find suitable Lab rooms (enough capacity)
                List<Room> suitableLabRooms = new ArrayList<>();
                for (Room labRoom : labRooms) {
                    if (labRoom. getCapacity() >= assignment.getNumStudents()) {
                        suitableLabRooms. add(labRoom);
                    }
                }
                
                // If suitable Lab exists -> Use Lab
                if (!suitableLabRooms.isEmpty()) {
                    // If currently using a suitable Lab -> keep it
                    if (currentRoom. isLab() && currentRoom.getCapacity() >= assignment.getNumStudents()) {
                        continue; // OK, no need to change
                    }
                    
                    // Otherwise, choose a suitable Lab
                    Room newLabRoom = suitableLabRooms. get(random.nextInt(suitableLabRooms.size()));
                    gene.setRoom(newLabRoom);
                } 
                // If no suitable Lab -> Use THEORY room (soft violation)
                else {
                    // Find suitable Theory rooms
                    List<Room> suitableTheoryRooms = new ArrayList<>();
                    for (Room theoryRoom : theoryRooms) {
                        if (theoryRoom.getCapacity() >= assignment.getNumStudents()) {
                            suitableTheoryRooms.add(theoryRoom);
                        }
                    }
                    
                    if (! suitableTheoryRooms.isEmpty()) {
                        // Choose smallest suitable Theory room
                        Room bestTheoryRoom = suitableTheoryRooms.get(0);
                        for (Room room : suitableTheoryRooms) {
                            if (room.getCapacity() < bestTheoryRoom.getCapacity() && 
                                room.getCapacity() >= assignment.getNumStudents()) {
                                bestTheoryRoom = room;
                            }
                        }
                        
                        gene.setRoom(bestTheoryRoom);
                        
                        // ✅ BỎ DÒNG IN WARNING
                    } else {
                        // Worst case: no room is enough
                        System.err.println("ERROR: Cannot find room for " + assignment.getGroupName() + 
                                         " (" + assignment.getNumStudents() + " students)");
                    }
                }
            }
        }
    }
    
    }
