package com.project.trade.repository;

import com.project.trade.controller.MemberForm;
import com.project.trade.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {

    private final DataSource dataSource;
    public JdbcMemberRepository(DataSource dataSource) {this.dataSource = dataSource;}

    /**
     * 회원가입 시 사용
     * @param member
     * @return member
     */
    @Override
    public Member save(Member member) {
        String sql = "insert into members(name, mail, pass) values(?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getMail());
            pstmt.setString(3, member.getPass());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if(rs.next()) {
                member.setId(rs.getLong(1));
            }
            else {
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);

        }
    }

    @Override
    public Optional<Member> findByMail(String mail) {
        return Optional.empty();
    }

    /**
     * 로그인 시 ID, Password 검사
     * @param form
     * @return member
     */
    @Override
    public Member find(MemberForm form) {
        String sql = "select * from members where mail like ? and pass like ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, form.getMail());
            pstmt.setString(2, form.getPass());
            rs = pstmt.executeQuery();
            Member member = new Member();
            if(rs.next()) {
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                member.setMail(rs.getString("mail"));
                member.setPass(rs.getString("pass"));

            }
            else
                System.out.println("return null");

            return member;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if(rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }

}
