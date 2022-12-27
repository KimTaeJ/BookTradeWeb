package com.project.trade.repository;

import com.project.trade.controller.OrderForm;
import com.project.trade.domain.Order;
import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcOrderRepository implements OrderRepository{
    private final DataSource dataSource;
    public JdbcOrderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 주문 등록
     * @param order
     * @return order
     */
    @Override
    public OrderForm save(OrderForm order) {
        String sql = "insert into orders(bookid, client) values(?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, order.getBookid());
            pstmt.setString(2, order.getClient());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                order.setOrderid(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return order;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);

        }
    }

    /**
     * 내 주문 확인용 orders 테이블에서 client 값이 sesion의 mail 값과 같은 항목 호출
     * @param form
     * @return orders
     */
    @Override
    public List<Order> findByOwner(Order form) {
        String sql = "select o.orderid as orderid, b.name as bookname, b.owner as owner, o.client as client " +
                "from orders o, books b where o.bookid like b.id and b.owner like ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, form.getOwner());
            rs = pstmt.executeQuery();
            List<Order> orders = new ArrayList<>();
            while(rs.next()) {
                Order order = new Order();
                order.setOrderid(rs.getLong("orderid"));
                order.setBookname(rs.getString("bookname"));
                order.setOwner(rs.getString("owner"));
                order.setClient(rs.getString("client"));
                orders.add(order);
            }

            return orders;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    /**
     * 받은 주문 확인용 orders 테이블의 bookid가 session이 books 테이블에서 owner인 항목 호출
     * @param form
     * @return orders
     */
    @Override
    public List<Order> findByClient(Order form) {
        String sql = "select o.orderid as orderid, b.name as bookname, b.owner as owner, o.client as client from orders o, books b where o.client like ? and o.bookid like b.id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, form.getClient());
            rs = pstmt.executeQuery();
            List<Order> orders = new ArrayList<>();
            while(rs.next()) {
                Order order = new Order();
                order.setOrderid(rs.getLong("orderid"));
                order.setBookname(rs.getString("bookname"));
                order.setOwner(rs.getString("owner"));
                order.setClient(rs.getString("client"));
                orders.add(order);
            }

            return orders;

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
