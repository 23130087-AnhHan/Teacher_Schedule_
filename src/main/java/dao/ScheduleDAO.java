package dao;

import model.Schedule;
import model.TeachingAssignment;
import util.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng schedules
 * ĐÂY LÀ OUTPUT CỦA THUẬT TOÁN DI TRUYỀN
 */
public class ScheduleDAO {

    /**
     * Lấy tất cả schedules
     */
    public List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM schedules ORDER BY teacher_id, slot_id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                schedules.add(extractScheduleFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return schedules;
    }

    /**
     * Lấy schedule theo ID
     */
    public Schedule getScheduleById(int scheduleId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils. getConnection();
            String sql = "SELECT * FROM schedules WHERE schedule_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, scheduleId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractScheduleFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils. closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Lấy schedules theo học kỳ (QUAN TRỌNG)
     */
    public List<Schedule> getSchedulesBySemester(String semester, String academicYear) {
        List<Schedule> schedules = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils. getConnection();
            String sql = "SELECT * FROM schedules WHERE semester = ? AND academic_year = ?  ORDER BY teacher_id, slot_id";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, semester);
            pstmt.setString(2, academicYear);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                schedules.add(extractScheduleFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return schedules;
    }

    /**
     * Lấy schedules theo giáo viên (xem lịch dạy của 1 GV)
     */
    public List<Schedule> getSchedulesByTeacher(int teacherId, String semester, String academicYear) {
        List<Schedule> schedules = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM schedules WHERE teacher_id = ?  AND semester = ? AND academic_year = ? ORDER BY slot_id";
            pstmt = conn. prepareStatement(sql);
            pstmt.setInt(1, teacherId);
            pstmt.setString(2, semester);
            pstmt.setString(3, academicYear);
            rs = pstmt. executeQuery();

            while (rs.next()) {
                schedules.add(extractScheduleFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return schedules;
    }

    /**
     * Lấy schedules theo phòng (xem lịch sử dụng phòng)
     */
    public List<Schedule> getSchedulesByRoom(int roomId, String semester, String academicYear) {
        List<Schedule> schedules = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM schedules WHERE room_id = ? AND semester = ? AND academic_year = ? ORDER BY slot_id";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            pstmt.setString(2, semester);
            pstmt. setString(3, academicYear);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                schedules.add(extractScheduleFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return schedules;
    }

    /**
     * Kiểm tra xung đột giáo viên (teacher đã dạy ở slot này chưa?)
     */
    public boolean hasTeacherConflict(int teacherId, int slotId, String semester, String academicYear) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT COUNT(*) FROM schedules WHERE teacher_id = ? AND slot_id = ? AND semester = ? AND academic_year = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, teacherId);
            pstmt. setInt(2, slotId);
            pstmt.setString(3, semester);
            pstmt.setString(4, academicYear);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return false;
    }

    /**
     * Kiểm tra xung đột phòng (phòng đã được dùng ở slot này chưa?)
     */
    public boolean hasRoomConflict(int roomId, int slotId, String semester, String academicYear) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT COUNT(*) FROM schedules WHERE room_id = ? AND slot_id = ?  AND semester = ? AND academic_year = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            pstmt.setInt(2, slotId);
            pstmt.setString(3, semester);
            pstmt.setString(4, academicYear);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return false;
    }

    /**
     * Thêm schedule mới
     */
    public boolean insertSchedule(Schedule schedule) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "INSERT INTO schedules (assignment_id, class_id, teacher_id, subject_id, room_id, slot_id, assignment_type, group_name, applicable_weeks, semester, academic_year) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, schedule.getAssignmentId());
            pstmt.setInt(2, schedule.getClassId());
            pstmt.setInt(3, schedule.getTeacherId());
            pstmt.setInt(4, schedule. getSubjectId());
            pstmt.setInt(5, schedule.getRoomId());
            pstmt.setInt(6, schedule.getSlotId());
            pstmt.setString(7, schedule.getAssignmentType().name());
            pstmt.setString(8, schedule.getGroupName());
            pstmt.setString(9, schedule.getApplicableWeeks());
            pstmt.setString(10, schedule.getSemester());
            pstmt.setString(11, schedule.getAcademicYear());

            int rows = pstmt. executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    schedule.setScheduleId(generatedKeys.getInt(1));
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
     * Lưu nhiều schedules cùng lúc (batch insert) - QUAN TRỌNG CHO GA
     */
    public boolean insertSchedules(List<Schedule> schedules) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            String sql = "INSERT INTO schedules (assignment_id, class_id, teacher_id, subject_id, room_id, slot_id, assignment_type, group_name, applicable_weeks, semester, academic_year) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            for (Schedule schedule : schedules) {
                pstmt.setInt(1, schedule. getAssignmentId());
                pstmt.setInt(2, schedule.getClassId());
                pstmt.setInt(3, schedule.getTeacherId());
                pstmt.setInt(4, schedule.getSubjectId());
                pstmt.setInt(5, schedule. getRoomId());
                pstmt.setInt(6, schedule.getSlotId());
                pstmt.setString(7, schedule.getAssignmentType().name());
                pstmt.setString(8, schedule. getGroupName());
                pstmt.setString(9, schedule.getApplicableWeeks());
                pstmt.setString(10, schedule.getSemester());
                pstmt.setString(11, schedule.getAcademicYear());
                
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            conn.commit(); // Commit transaction

            return results.length == schedules.size();

        } catch (SQLException e) {
            JDBCUtils.rollback(conn); // Rollback nếu lỗi
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Xóa schedule
     */
    public boolean deleteSchedule(int scheduleId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "DELETE FROM schedules WHERE schedule_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, scheduleId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Xóa tất cả schedules của một học kỳ (để chạy lại GA)
     */
    public boolean deleteSchedulesBySemester(String semester, String academicYear) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "DELETE FROM schedules WHERE semester = ? AND academic_year = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt. setString(1, semester);
            pstmt.setString(2, academicYear);

            int rows = pstmt.executeUpdate();
            System.out.println("✅ Đã xóa " + rows + " schedules");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Đếm số schedules theo học kỳ
     */
    public int countSchedules(String semester, String academicYear) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT COUNT(*) FROM schedules WHERE semester = ? AND academic_year = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt. setString(1, semester);
            pstmt.setString(2, academicYear);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return 0;
    }

    /**
     * Extract Schedule object từ ResultSet
     */
    private Schedule extractScheduleFromResultSet(ResultSet rs) throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(rs.getInt("schedule_id"));
        schedule.setAssignmentId(rs. getInt("assignment_id"));
        schedule.setClassId(rs.getInt("class_id"));
        schedule.setTeacherId(rs.getInt("teacher_id"));
        schedule.setSubjectId(rs.getInt("subject_id"));
        schedule.setRoomId(rs.getInt("room_id"));
        schedule.setSlotId(rs.getInt("slot_id"));
        schedule.setAssignmentType(TeachingAssignment.AssignmentType.valueOf(rs. getString("assignment_type")));
        schedule.setGroupName(rs.getString("group_name"));
        schedule.setApplicableWeeks(rs.getString("applicable_weeks"));
        schedule.setSemester(rs.getString("semester"));
        schedule.setAcademicYear(rs.getString("academic_year"));
        schedule.setCreatedAt(rs. getTimestamp("created_at").toLocalDateTime());
        schedule.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return schedule;
    }
}