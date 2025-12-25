package dao;

import model.Room;
import util.JDBCUtils;

import java.sql.*;
import java. util.ArrayList;
import java. util.List;

/**
 * DAO cho bảng rooms
 */
public class RoomDAO {

    /**
     * Lấy tất cả phòng học
     */
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM rooms ORDER BY room_type, capacity DESC, room_code";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs. next()) {
                rooms.add(extractRoomFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils. closeAll(conn, pstmt, rs);
        }

        return rooms;
    }

    /**
     * Lấy phòng theo ID
     */
    public Room getRoomById(int roomId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils. getConnection();
            String sql = "SELECT * FROM rooms WHERE room_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt. setInt(1, roomId);
            rs = pstmt. executeQuery();

            if (rs.next()) {
                return extractRoomFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Lấy phòng theo loại
     */
    public List<Room> getRoomsByType(Room.RoomType roomType) {
        List<Room> rooms = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM rooms WHERE room_type = ? ORDER BY capacity DESC, room_code";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roomType.name());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                rooms.add(extractRoomFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return rooms;
    }

    /**
     * Lấy các phòng Lab (cho thực hành)
     */
    public List<Room> getLabRooms() {
        return getRoomsByType(Room.RoomType.LAB);
    }

    /**
     * Lấy các phòng lý thuyết (không phải lab)
     */
    public List<Room> getTheoryRooms() {
        List<Room> rooms = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM rooms WHERE room_type IN ('THEORY', 'LECTURE_HALL') ORDER BY capacity DESC, room_code";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt. executeQuery();

            while (rs.next()) {
                rooms. add(extractRoomFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return rooms;
    }

    /**
     * Lấy phòng phù hợp với số sinh viên và loại (quan trọng cho GA)
     */
    public List<Room> getSuitableRooms(int numStudents, boolean requiresLab) {
        List<Room> rooms = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql;
            
            if (requiresLab) {
                // Chỉ lấy phòng Lab
                sql = "SELECT * FROM rooms WHERE room_type = 'LAB' AND capacity >= ?  ORDER BY capacity ASC";
            } else {
                // Lấy phòng lý thuyết, ưu tiên phòng vừa đủ
                sql = "SELECT * FROM rooms WHERE room_type IN ('THEORY', 'LECTURE_HALL') AND capacity >= ? ORDER BY capacity ASC";
            }
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, numStudents);
            rs = pstmt. executeQuery();

            while (rs.next()) {
                rooms. add(extractRoomFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return rooms;
    }

    /**
     * Thêm phòng mới
     */
    public boolean insertRoom(Room room) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "INSERT INTO rooms (room_code, room_name, capacity, room_type, building) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, room.getRoomCode());
            pstmt.setString(2, room.getRoomName());
            pstmt.setInt(3, room.getCapacity());
            pstmt. setString(4, room.getRoomType().name());
            pstmt.setString(5, room.getBuilding());

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = pstmt. getGeneratedKeys();
                if (generatedKeys.next()) {
                    room.setRoomId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Cập nhật phòng
     */
    public boolean updateRoom(Room room) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils. getConnection();
            String sql = "UPDATE rooms SET room_code = ?, room_name = ?, capacity = ?, room_type = ?, building = ? WHERE room_id = ?";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, room.getRoomCode());
            pstmt.setString(2, room.getRoomName());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setString(4, room.getRoomType().name());
            pstmt.setString(5, room. getBuilding());
            pstmt.setInt(6, room.getRoomId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Xóa phòng
     */
    public boolean deleteRoom(int roomId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "DELETE FROM rooms WHERE room_id = ?";
            pstmt = conn. prepareStatement(sql);
            pstmt.setInt(1, roomId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Extract Room object từ ResultSet
     */
    private Room extractRoomFromResultSet(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        room.setRoomCode(rs. getString("room_code"));
        room.setRoomName(rs.getString("room_name"));
        room.setCapacity(rs.getInt("capacity"));
        room.setRoomType(Room.RoomType.valueOf(rs.getString("room_type")));
        room.setBuilding(rs.getString("building"));
        room.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return room;
    }
}