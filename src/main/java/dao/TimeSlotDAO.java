package dao;

import model.TimeSlot;
import util.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng time_slots
 */
public class TimeSlotDAO {

    /**
     * Lấy tất cả time slots (24 slots)
     */
    public List<TimeSlot> getAllTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils. getConnection();
            String sql = "SELECT * FROM time_slots ORDER BY FIELD(day_of_week, 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'), block_number";
            pstmt = conn. prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                timeSlots. add(extractTimeSlotFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return timeSlots;
    }

    /**
     * Lấy time slot theo ID
     */
    public TimeSlot getTimeSlotById(int slotId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM time_slots WHERE slot_id = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, slotId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractTimeSlotFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Lấy time slots theo ngày
     */
    public List<TimeSlot> getTimeSlotsByDay(TimeSlot.DayOfWeek dayOfWeek) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM time_slots WHERE day_of_week = ? ORDER BY block_number";
            pstmt = conn.prepareStatement(sql);
            pstmt. setString(1, dayOfWeek.name());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                timeSlots.add(extractTimeSlotFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils. closeAll(conn, pstmt, rs);
        }

        return timeSlots;
    }

    /**
     * Lấy time slots theo session (MORNING/AFTERNOON)
     */
    public List<TimeSlot> getTimeSlotsBySession(TimeSlot.SessionType sessionType) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM time_slots WHERE session_type = ?  ORDER BY day_of_week, block_number";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sessionType.name());
            rs = pstmt.executeQuery();

            while (rs. next()) {
                timeSlots.add(extractTimeSlotFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return timeSlots;
    }

    /**
     * Lấy time slot cụ thể theo ngày và ca
     */
    public TimeSlot getTimeSlot(TimeSlot.DayOfWeek dayOfWeek, int blockNumber) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT * FROM time_slots WHERE day_of_week = ?  AND block_number = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dayOfWeek.name());
            pstmt.setInt(2, blockNumber);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractTimeSlotFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeAll(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Extract TimeSlot object từ ResultSet
     */
    private TimeSlot extractTimeSlotFromResultSet(ResultSet rs) throws SQLException {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setSlotId(rs.getInt("slot_id"));
        timeSlot.setDayOfWeek(TimeSlot.DayOfWeek.valueOf(rs.getString("day_of_week")));
        timeSlot.setBlockNumber(rs.getInt("block_number"));
        timeSlot.setBlockName(rs.getString("block_name"));
        timeSlot.setStartTime(rs.getTime("start_time").toLocalTime());
        timeSlot.setEndTime(rs.getTime("end_time").toLocalTime());
        timeSlot.setSessionType(TimeSlot.SessionType.valueOf(rs.getString("session_type")));
        timeSlot.setPeriods(rs.getString("periods"));
        timeSlot.setCreatedAt(rs. getTimestamp("created_at").toLocalDateTime());
        return timeSlot;
    }
}