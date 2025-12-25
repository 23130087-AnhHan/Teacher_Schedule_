package model;

import java.time.LocalDateTime;

/**
 * Model class cho bảng rooms
 */
public class Room {
    private int roomId;
    private String roomCode;
    private String roomName;
    private int capacity;
    private RoomType roomType;
    private String building;
    private LocalDateTime createdAt;

    // Enum cho room type
    public enum RoomType {
        LECTURE_HALL,  // 150 chỗ
        THEORY,        // 60-100 chỗ
        LAB            // 50 chỗ
    }

    // Constructors
    public Room() {
    }

    public Room(String roomCode, String roomName, int capacity, RoomType roomType, String building) {
        this.roomCode = roomCode;
        this.roomName = roomName;
        this.capacity = capacity;
        this.roomType = roomType;
        this.building = building;
    }

    // Getters and Setters
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public boolean isLab() {
        return roomType == RoomType.LAB;
    }

    public boolean canAccommodate(int numStudents) {
        return capacity >= numStudents;
    }

    /**
     * Tính tỷ lệ sử dụng phòng (%)
     */
    public double getUtilizationRate(int numStudents) {
        if (capacity == 0) return 0.0;
        return (double) numStudents / capacity * 100.0;
    }

    /**
     * Kiểm tra phòng có phù hợp không (80-100% sử dụng là tốt nhất)
     */
    public boolean isOptimalFor(int numStudents) {
        double utilization = getUtilizationRate(numStudents);
        return utilization >= 80.0 && utilization <= 100.0;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomCode='" + roomCode + '\'' +
                ", roomName='" + roomName + '\'' +
                ", capacity=" + capacity +
                ", roomType=" + roomType +
                ", building='" + building + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomId == room.roomId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(roomId);
    }
}
