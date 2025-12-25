package dao;

import model.Subject;
import util.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng subjects
 */
public class SubjectDAO {

    /**
     * Lấy tất cả môn học
     */
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM subjects ORDER BY subject_code";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt. executeQuery();

            while (rs.next()) {
                subjects. add(extractSubjectFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return subjects;
    }

    /**
     * Lấy môn học theo ID
     */
    public Subject getSubjectById(int subjectId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM subjects WHERE subject_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, subjectId);
            rs = pstmt. executeQuery();

            if (rs.next()) {
                return extractSubjectFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Lấy môn học theo mã
     */
    public Subject getSubjectByCode(String subjectCode) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM subjects WHERE subject_code = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, subjectCode);
            rs = pstmt.executeQuery();

            if (rs. next()) {
                return extractSubjectFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Thêm môn học mới
     */
    public boolean insertSubject(Subject subject) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "INSERT INTO subjects (subject_code, subject_name, total_credits, theory_credits, practice_credits, max_students_per_practice_group, department) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, subject.getSubjectCode());
            pstmt.setString(2, subject.getSubjectName());
            pstmt.setInt(3, subject.getTotalCredits());
            pstmt. setInt(4, subject.getTheoryCredits());
            pstmt.setInt(5, subject.getPracticeCredits());
            pstmt.setInt(6, subject.getMaxStudentsPerPracticeGroup());
            pstmt.setString(7, subject.getDepartment());

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    subject.setSubjectId(generatedKeys.getInt(1));
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
     * Cập nhật môn học
     */
    public boolean updateSubject(Subject subject) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "UPDATE subjects SET subject_code = ?, subject_name = ?, total_credits = ?, theory_credits = ?, practice_credits = ?, max_students_per_practice_group = ?, department = ? WHERE subject_id = ? ";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, subject.getSubjectCode());
            pstmt.setString(2, subject. getSubjectName());
            pstmt.setInt(3, subject.getTotalCredits());
            pstmt.setInt(4, subject.getTheoryCredits());
            pstmt. setInt(5, subject.getPracticeCredits());
            pstmt.setInt(6, subject.getMaxStudentsPerPracticeGroup());
            pstmt.setString(7, subject.getDepartment());
            pstmt.setInt(8, subject.getSubjectId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils. closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Xóa môn học
     */
    public boolean deleteSubject(int subjectId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "DELETE FROM subjects WHERE subject_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt. setInt(1, subjectId);

            return pstmt. executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Lấy các môn có thực hành
     */
    public List<Subject> getSubjectsWithPractice() {
        List<Subject> subjects = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM subjects WHERE practice_credits > 0 ORDER BY subject_code";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs. next()) {
                subjects.add(extractSubjectFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils. closeAll(conn, pstmt, rs);
        }

        return subjects;
    }

    /**
     * Extract Subject object từ ResultSet
     */
    private Subject extractSubjectFromResultSet(ResultSet rs) throws SQLException {
        Subject subject = new Subject();
        subject.setSubjectId(rs.getInt("subject_id"));
        subject.setSubjectCode(rs.getString("subject_code"));
        subject.setSubjectName(rs.getString("subject_name"));
        subject.setTotalCredits(rs. getInt("total_credits"));
        subject.setTheoryCredits(rs.getInt("theory_credits"));
        subject.setPracticeCredits(rs.getInt("practice_credits"));
        subject.setMaxStudentsPerPracticeGroup(rs.getInt("max_students_per_practice_group"));
        subject.setDepartment(rs.getString("department"));
        subject.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return subject;
    }
}