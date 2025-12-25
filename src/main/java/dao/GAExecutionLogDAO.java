package dao;

import model. GAExecutionLog;
import util.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng ga_execution_logs
 */
public class GAExecutionLogDAO {

    /**
     * Lấy tất cả execution logs
     */
    public List<GAExecutionLog> getAllLogs() {
        List<GAExecutionLog> logs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils. getConnection();
            String sql = "SELECT * FROM ga_execution_logs ORDER BY created_at DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(extractLogFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return logs;
    }

    /**
     * Lấy log theo ID
     */
    public GAExecutionLog getLogById(int logId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM ga_execution_logs WHERE log_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt. setInt(1, logId);
            rs = pstmt. executeQuery();

            if (rs.next()) {
                return extractLogFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Lấy logs theo học kỳ
     */
    public List<GAExecutionLog> getLogsBySemester(String semester, String academicYear) {
        List<GAExecutionLog> logs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM ga_execution_logs WHERE semester = ? AND academic_year = ? ORDER BY created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt. setString(1, semester);
            pstmt.setString(2, academicYear);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(extractLogFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return logs;
    }

    /**
     * Lấy log mới nhất của một học kỳ
     */
    public GAExecutionLog getLatestLog(String semester, String academicYear) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM ga_execution_logs WHERE semester = ? AND academic_year = ? ORDER BY created_at DESC LIMIT 1";
            pstmt = conn.prepareStatement(sql);
            pstmt. setString(1, semester);
            pstmt.setString(2, academicYear);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractLogFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Thêm log mới (sau khi chạy GA)
     */
    public boolean insertLog(GAExecutionLog log) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "INSERT INTO ga_execution_logs (semester, academic_year, population_size, max_generations, crossover_rate, mutation_rate, generations_executed, best_fitness_score, avg_fitness_score, hard_constraint_violations, soft_constraint_violations, execution_time_seconds, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, log.getSemester());
            pstmt.setString(2, log.getAcademicYear());
            pstmt.setInt(3, log.getPopulationSize());
            pstmt.setInt(4, log.getMaxGenerations());
            pstmt.setDouble(5, log.getCrossoverRate());
            pstmt.setDouble(6, log.getMutationRate());
            pstmt.setInt(7, log. getGenerationsExecuted());
            pstmt.setDouble(8, log.getBestFitnessScore());
            pstmt.setDouble(9, log.getAvgFitnessScore());
            pstmt. setInt(10, log.getHardConstraintViolations());
            pstmt.setInt(11, log.getSoftConstraintViolations());
            pstmt.setInt(12, log.getExecutionTimeSeconds());
            pstmt.setString(13, log. getNotes());

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    log.setLogId(generatedKeys.getInt(1));
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
     * Xóa log
     */
    public boolean deleteLog(int logId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "DELETE FROM ga_execution_logs WHERE log_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt. setInt(1, logId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt);
        }

        return false;
    }

    /**
     * Extract GAExecutionLog object từ ResultSet
     */
    private GAExecutionLog extractLogFromResultSet(ResultSet rs) throws SQLException {
        GAExecutionLog log = new GAExecutionLog();
        log.setLogId(rs. getInt("log_id"));
        log.setSemester(rs.getString("semester"));
        log.setAcademicYear(rs.getString("academic_year"));
        log.setPopulationSize(rs.getInt("population_size"));
        log.setMaxGenerations(rs.getInt("max_generations"));
        log.setCrossoverRate(rs. getDouble("crossover_rate"));
        log.setMutationRate(rs.getDouble("mutation_rate"));
        log.setGenerationsExecuted(rs.getInt("generations_executed"));
        log.setBestFitnessScore(rs.getDouble("best_fitness_score"));
        log.setAvgFitnessScore(rs.getDouble("avg_fitness_score"));
        log.setHardConstraintViolations(rs.getInt("hard_constraint_violations"));
        log.setSoftConstraintViolations(rs.getInt("soft_constraint_violations"));
        log.setExecutionTimeSeconds(rs. getInt("execution_time_seconds"));
        log.setNotes(rs.getString("notes"));
        log.setCreatedAt(rs. getTimestamp("created_at").toLocalDateTime());
        return log;
    }
}