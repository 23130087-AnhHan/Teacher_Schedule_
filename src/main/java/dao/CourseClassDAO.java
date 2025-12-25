package dao;

import model.CourseClass;
import util.JDBCUtils;

import java.sql.*;
import java. util.ArrayList;
import java. util.List;

/**
 * DAO cho bảng course_classes
 */
public class CourseClassDAO {

    /**
     * Lấy tất cả lớp học phần
     */
    public List<CourseClass> getAllCourseClasses() {
        List<CourseClass> classes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM course_classes ORDER BY class_code";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs. next()) {
                classes.add(extractCourseClassFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return classes;
    }

    /**
     * Lấy lớp học phần theo ID
     */
    public CourseClass getCourseClassById(int classId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM course_classes WHERE class_id = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, classId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCourseClassFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Lấy lớp học phần theo mã
     */
    public CourseClass getCourseClassByCode(String classCode) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM course_classes WHERE class_code = ?";
            pstmt = conn. prepareStatement(sql);
            pstmt.setString(1, classCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCourseClassFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Lấy lớp học phần theo học kỳ
     */
    public List<CourseClass> getCourseClassesBySemester(String semester, String academicYear) {
        List<CourseClass> classes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM course_classes WHERE semester = ? AND academic_year = ?  ORDER BY class_code";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, semester);
            pstmt.setString(2, academicYear);
            rs = pstmt. executeQuery();

            while (rs.next()) {
                classes. add(extractCourseClassFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return classes;
    }

    /**
     * Lấy lớp học phần theo môn học
     */
    public List<CourseClass> getCourseClassesBySubject(int subjectId) {
        List<CourseClass> classes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM course_classes WHERE subject_id = ? ORDER BY class_code";
            pstmt = conn.prepareStatement(sql);
            pstmt. setInt(1, subjectId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                classes.add(extractCourseClassFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return classes;
    }

    /**
     * Thêm lớp học phần mới
     */
    public boolean insertCourseClass(CourseClass courseClass) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "INSERT INTO course_classes (class_code, subject_id, total_students, semester, academic_year, duration_weeks) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, courseClass.getClassCode());
            pstmt.setInt(2, courseClass.getSubjectId());
            pstmt. setInt(3, courseClass. getTotalStudents());
            pstmt.setString(4, courseClass.getSemester());
            pstmt.setString(5, courseClass.getAcademicYear());
            pstmt.setInt(6, courseClass.getDurationWeeks());

            int rows = pstmt. executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    courseClass.setClassId(generatedKeys.getInt(1));
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
     * Cập nhật lớp học phần
     */
    public boolean updateCourseClass(CourseClass courseClass) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "UPDATE course_classes SET class_code = ?, subject_id = ?, total_students = ?, semester = ?, academic_year = ?, duration_weeks = ?  WHERE class_id = ?";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, courseClass.getClassCode());
            pstmt.setInt(2, courseClass.getSubjectId());
            pstmt.setInt(3, courseClass.getTotalStudents());
            pstmt.setString(4, courseClass.getSemester());
            pstmt.setString(5, courseClass.getAcademicYear());
            pstmt.setInt(6, courseClass.getDurationWeeks());
            pstmt.setInt(7, courseClass.getClassId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Xóa lớp học phần
     */
    public boolean deleteCourseClass(int classId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "DELETE FROM course_classes WHERE class_id = ?";
            pstmt = conn. prepareStatement(sql);
            pstmt.setInt(1, classId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Extract CourseClass object từ ResultSet
     */
    private CourseClass extractCourseClassFromResultSet(ResultSet rs) throws SQLException {
        CourseClass courseClass = new CourseClass();
        courseClass.setClassId(rs.getInt("class_id"));
        courseClass.setClassCode(rs.getString("class_code"));
        courseClass.setSubjectId(rs.getInt("subject_id"));
        courseClass.setTotalStudents(rs.getInt("total_students"));
        courseClass.setSemester(rs.getString("semester"));
        courseClass.setAcademicYear(rs.getString("academic_year"));
        courseClass. setDurationWeeks(rs. getInt("duration_weeks"));
        courseClass.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return courseClass;
    }
}