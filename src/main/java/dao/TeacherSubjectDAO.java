package dao;

import model.TeacherSubject;
import util.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng teacher_subjects (Many-to-Many relationship)
 */
public class TeacherSubjectDAO {

    /**
     * Lấy tất cả teacher-subject mappings
     */
    public List<TeacherSubject> getAllMappings() {
        List<TeacherSubject> mappings = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teacher_subjects ORDER BY teacher_id, subject_id";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                mappings.add(extractTeacherSubjectFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return mappings;
    }

    /**
     * Lấy các môn mà giáo viên dạy được
     */
    public List<TeacherSubject> getSubjectsByTeacher(int teacherId) {
        List<TeacherSubject> mappings = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teacher_subjects WHERE teacher_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, teacherId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                mappings.add(extractTeacherSubjectFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return mappings;
    }

    /**
     * Lấy các giáo viên dạy được môn này
     */
    public List<TeacherSubject> getTeachersBySubject(int subjectId) {
        List<TeacherSubject> mappings = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teacher_subjects WHERE subject_id = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, subjectId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                mappings.add(extractTeacherSubjectFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return mappings;
    }

    /**
     * Kiểm tra giáo viên có thể dạy môn này không
     */
    public boolean canTeach(int teacherId, int subjectId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT COUNT(*) FROM teacher_subjects WHERE teacher_id = ? AND subject_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, teacherId);
            pstmt.setInt(2, subjectId);
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
     * Kiểm tra giáo viên có thể dạy lý thuyết môn này không
     */
    public boolean canTeachTheory(int teacherId, int subjectId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT can_teach_theory FROM teacher_subjects WHERE teacher_id = ? AND subject_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, teacherId);
            pstmt.setInt(2, subjectId);
            rs = pstmt. executeQuery();

            if (rs.next()) {
                return rs.getBoolean("can_teach_theory");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return false;
    }

    /**
     * Kiểm tra giáo viên có thể dạy thực hành môn này không
     */
    public boolean canTeachPractice(int teacherId, int subjectId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT can_teach_practice FROM teacher_subjects WHERE teacher_id = ? AND subject_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt. setInt(1, teacherId);
            pstmt.setInt(2, subjectId);
            rs = pstmt.executeQuery();

            if (rs. next()) {
                return rs. getBoolean("can_teach_practice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils. closeAll(conn, pstmt, rs);
        }

        return false;
    }

    /**
     * Thêm mapping mới
     */
    public boolean insertMapping(TeacherSubject mapping) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "INSERT INTO teacher_subjects (teacher_id, subject_id, can_teach_theory, can_teach_practice, priority) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            
            pstmt. setInt(1, mapping.getTeacherId());
            pstmt.setInt(2, mapping.getSubjectId());
            pstmt.setBoolean(3, mapping.isCanTeachTheory());
            pstmt.setBoolean(4, mapping.isCanTeachPractice());
            pstmt.setInt(5, mapping.getPriority());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils. closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Xóa mapping
     */
    public boolean deleteMapping(int teacherId, int subjectId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "DELETE FROM teacher_subjects WHERE teacher_id = ? AND subject_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, teacherId);
            pstmt.setInt(2, subjectId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Extract TeacherSubject object từ ResultSet
     */
    private TeacherSubject extractTeacherSubjectFromResultSet(ResultSet rs) throws SQLException {
        TeacherSubject mapping = new TeacherSubject();
        mapping.setTeacherId(rs.getInt("teacher_id"));
        mapping.setSubjectId(rs.getInt("subject_id"));
        mapping.setCanTeachTheory(rs.getBoolean("can_teach_theory"));
        mapping.setCanTeachPractice(rs.getBoolean("can_teach_practice"));
        mapping.setPriority(rs.getInt("priority"));
        mapping.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return mapping;
    }
}