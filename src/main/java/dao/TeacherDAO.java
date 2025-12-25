package dao;




import model.Teacher;
import util.JDBCUtils;

import java.sql.*;
import java. util.ArrayList;
import java. util.List;

/**
 * DAO cho bảng teachers
 */
public class TeacherDAO {

    /**
     * Lấy tất cả giáo viên
     */
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teachers ORDER BY teacher_code";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                teachers.add(extractTeacherFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return teachers;
    }

    /**
     * Lấy giáo viên theo ID
     */
    public Teacher getTeacherById(int teacherId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teachers WHERE teacher_id = ?";
            pstmt = conn. prepareStatement(sql);
            pstmt.setInt(1, teacherId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractTeacherFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Lấy giáo viên theo mã
     */
    public Teacher getTeacherByCode(String teacherCode) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teachers WHERE teacher_code = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, teacherCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractTeacherFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Thêm giáo viên mới
     */
    public boolean insertTeacher(Teacher teacher) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "INSERT INTO teachers (teacher_code, full_name, department, max_teaching_hours) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, teacher.getTeacherCode());
            pstmt. setString(2, teacher.getFullName());
            pstmt.setString(3, teacher. getDepartment());
            pstmt.setInt(4, teacher.getMaxTeachingHours());

            int rows = pstmt. executeUpdate();

            if (rows > 0) {
                // Lấy ID vừa insert
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    teacher.setTeacherId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils. closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Cập nhật giáo viên
     */
    public boolean updateTeacher(Teacher teacher) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "UPDATE teachers SET teacher_code = ?, full_name = ?, department = ?, max_teaching_hours = ? WHERE teacher_id = ?";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, teacher.getTeacherCode());
            pstmt.setString(2, teacher.getFullName());
            pstmt.setString(3, teacher.getDepartment());
            pstmt. setInt(4, teacher.getMaxTeachingHours());
            pstmt.setInt(5, teacher.getTeacherId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Xóa giáo viên
     */
    public boolean deleteTeacher(int teacherId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils. getConnection();
            String sql = "DELETE FROM teachers WHERE teacher_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, teacherId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils. closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Lấy giáo viên theo khoa
     */
    public List<Teacher> getTeachersByDepartment(String department) {
        List<Teacher> teachers = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM teachers WHERE department = ?  ORDER BY teacher_code";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, department);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                teachers.add(extractTeacherFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return teachers;
    }

    /**
     * Extract Teacher object từ ResultSet
     */
    private Teacher extractTeacherFromResultSet(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(rs.getInt("teacher_id"));
        teacher.setTeacherCode(rs.getString("teacher_code"));
        teacher.setFullName(rs.getString("full_name"));
        teacher.setDepartment(rs. getString("department"));
        teacher.setMaxTeachingHours(rs.getInt("max_teaching_hours"));
        teacher.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return teacher;
    }
}