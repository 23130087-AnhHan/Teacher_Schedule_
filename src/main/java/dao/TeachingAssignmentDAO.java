package dao;

import model. TeachingAssignment;
import util.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng teaching_assignments
 * ĐÂY LÀ INPUT CHÍNH CHO THUẬT TOÁN DI TRUYỀN
 */
public class TeachingAssignmentDAO {

    /**
     * Lấy tất cả teaching assignments
     */
    public List<TeachingAssignment> getAllAssignments() {
        List<TeachingAssignment> assignments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teaching_assignments ORDER BY class_id, assignment_type, group_name";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs. next()) {
                assignments.add(extractAssignmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return assignments;
    }

    /**
     * Lấy assignment theo ID
     */
    public TeachingAssignment getAssignmentById(int assignmentId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils. getConnection();
            String sql = "SELECT * FROM teaching_assignments WHERE assignment_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, assignmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractAssignmentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Lấy assignments theo học kỳ (QUAN TRỌNG CHO GA)
     */
    public List<TeachingAssignment> getAssignmentsBySemester(String semester, String academicYear) {
        List<TeachingAssignment> assignments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teaching_assignments WHERE semester = ? AND academic_year = ?  ORDER BY class_id, assignment_type, group_name";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, semester);
            pstmt.setString(2, academicYear);
            rs = pstmt. executeQuery();

            while (rs.next()) {
                assignments. add(extractAssignmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return assignments;
    }

    /**
     * Lấy assignments theo giáo viên
     */
    public List<TeachingAssignment> getAssignmentsByTeacher(int teacherId, String semester, String academicYear) {
        List<TeachingAssignment> assignments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teaching_assignments WHERE teacher_id = ? AND semester = ?  AND academic_year = ? ORDER BY assignment_type, group_name";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, teacherId);
            pstmt.setString(2, semester);
            pstmt.setString(3, academicYear);
            rs = pstmt.executeQuery();

            while (rs. next()) {
                assignments.add(extractAssignmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return assignments;
    }

    /**
     * Lấy assignments theo lớp học phần
     */
    public List<TeachingAssignment> getAssignmentsByClass(int classId) {
        List<TeachingAssignment> assignments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teaching_assignments WHERE class_id = ? ORDER BY assignment_type, group_name";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, classId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                assignments.add(extractAssignmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return assignments;
    }

    /**
     * Lấy chỉ các assignments LÝ THUYẾT
     */
    public List<TeachingAssignment> getTheoryAssignments(String semester, String academicYear) {
        List<TeachingAssignment> assignments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teaching_assignments WHERE assignment_type = 'THEORY' AND semester = ? AND academic_year = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, semester);
            pstmt.setString(2, academicYear);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                assignments.add(extractAssignmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return assignments;
    }

    /**
     * Lấy chỉ các assignments THỰC HÀNH
     */
    public List<TeachingAssignment> getPracticeAssignments(String semester, String academicYear) {
        List<TeachingAssignment> assignments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teaching_assignments WHERE assignment_type = 'PRACTICE' AND semester = ? AND academic_year = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, semester);
            pstmt.setString(2, academicYear);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                assignments.add(extractAssignmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return assignments;
    }

    /**
     * Thêm teaching assignment mới
     */
    public boolean insertAssignment(TeachingAssignment assignment) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "INSERT INTO teaching_assignments (class_id, teacher_id, subject_id, assignment_type, group_name, num_students, start_week, end_week, blocks_per_week, requires_lab, min_room_capacity, semester, academic_year) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, assignment.getClassId());
            pstmt.setInt(2, assignment.getTeacherId());
            pstmt.setInt(3, assignment.getSubjectId());
            pstmt. setString(4, assignment.getAssignmentType().name());
            pstmt. setString(5, assignment.getGroupName());
            pstmt.setInt(6, assignment. getNumStudents());
            pstmt.setInt(7, assignment.getStartWeek());
            pstmt. setInt(8, assignment.getEndWeek());
            pstmt.setInt(9, assignment.getBlocksPerWeek());
            pstmt.setBoolean(10, assignment.isRequiresLab());
            pstmt.setInt(11, assignment.getMinRoomCapacity());
            pstmt.setString(12, assignment.getSemester());
            pstmt. setString(13, assignment.getAcademicYear());

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    assignment.setAssignmentId(generatedKeys.getInt(1));
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
     * Cập nhật assignment
     */
    public boolean updateAssignment(TeachingAssignment assignment) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "UPDATE teaching_assignments SET class_id = ?, teacher_id = ?, subject_id = ?, assignment_type = ?, group_name = ?, num_students = ?, start_week = ?, end_week = ?, blocks_per_week = ?, requires_lab = ?, min_room_capacity = ?, semester = ?, academic_year = ? WHERE assignment_id = ?";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, assignment.getClassId());
            pstmt. setInt(2, assignment.getTeacherId());
            pstmt.setInt(3, assignment.getSubjectId());
            pstmt.setString(4, assignment.getAssignmentType().name());
            pstmt.setString(5, assignment. getGroupName());
            pstmt.setInt(6, assignment.getNumStudents());
            pstmt.setInt(7, assignment.getStartWeek());
            pstmt.setInt(8, assignment.getEndWeek());
            pstmt. setInt(9, assignment.getBlocksPerWeek());
            pstmt.setBoolean(10, assignment.isRequiresLab());
            pstmt.setInt(11, assignment. getMinRoomCapacity());
            pstmt.setString(12, assignment.getSemester());
            pstmt.setString(13, assignment.getAcademicYear());
            pstmt.setInt(14, assignment.getAssignmentId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Xóa assignment
     */
    public boolean deleteAssignment(int assignmentId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "DELETE FROM teaching_assignments WHERE assignment_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt. setInt(1, assignmentId);

            return pstmt. executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Đếm số assignments theo học kỳ
     */
    public int countAssignments(String semester, String academicYear) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT COUNT(*) FROM teaching_assignments WHERE semester = ? AND academic_year = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, semester);
            pstmt.setString(2, academicYear);
            rs = pstmt.executeQuery();

            if (rs. next()) {
                return rs. getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return 0;
    }

    /**
     * Extract TeachingAssignment object từ ResultSet
     */
    private TeachingAssignment extractAssignmentFromResultSet(ResultSet rs) throws SQLException {
        TeachingAssignment assignment = new TeachingAssignment();
        assignment.setAssignmentId(rs.getInt("assignment_id"));
        assignment.setClassId(rs.getInt("class_id"));
        assignment.setTeacherId(rs.getInt("teacher_id"));
        assignment.setSubjectId(rs.getInt("subject_id"));
        assignment.setAssignmentType(TeachingAssignment.AssignmentType.valueOf(rs.getString("assignment_type")));
        assignment.setGroupName(rs.getString("group_name"));
        assignment.setNumStudents(rs. getInt("num_students"));
        assignment.setStartWeek(rs.getInt("start_week"));
        assignment.setEndWeek(rs.getInt("end_week"));
        assignment.setTotalWeeks(rs.getInt("total_weeks"));
        assignment. setBlocksPerWeek(rs.getInt("blocks_per_week"));
        assignment.setRequiresLab(rs.getBoolean("requires_lab"));
        assignment.setMinRoomCapacity(rs.getInt("min_room_capacity"));
        assignment.setSemester(rs.getString("semester"));
        assignment.setAcademicYear(rs. getString("academic_year"));
        assignment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return assignment;
    }
}