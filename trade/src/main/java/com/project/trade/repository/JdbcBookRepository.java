package com.project.trade.repository;

import com.project.trade.controller.BookForm;
import com.project.trade.domain.Book;
import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcBookRepository implements BookRepository {

    private final DataSource dataSource;
    public JdbcBookRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * mainpage 접근 시 books 테이블 전체 기본 호출 5줄
     * @return book 항목 list
     */
    public List<Book> findAll() {
        String sql = "select * from books b where not exists (select bookid from orders o where o.bookid=b.id) limit 5;";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Book> books = new ArrayList<>();
            while(rs.next()) {
                Book book = new Book();
                book.setId(rs.getLong("id"));
                book.setName(rs.getString("name"));
                book.setPublisher(rs.getString("publisher"));
                book.setPrice(rs.getInt("price"));
                book.setOwner(rs.getString("owner"));
                books.add(book);
            }
            return books;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    /**
     * books 테이블에 book 추가
     * @param book
     * @return book
     */
    public Book save(Book book) {
        String sql = "insert into books(name, publisher, price, owner) values(?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn=getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getPublisher());
            pstmt.setInt(3, book.getPrice());
            pstmt.setString(4, book.getOwner());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if(rs.next()) {
                book.setId(rs.getLong(1));
            } else {
                throw new SQLException("조회 실패");
            }
            return book;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }

    }

    /**
     * form 에서 검색 조건을 지정하고 sql 조립
     * sql문 결과를 books 테이블에서 book list로 반환
     * @param form
     * @return books
     */
    public List<Book> findCond(BookForm form) {
        String sql = "select * from books b where 1 ";
        if(form.getName()!=null)
            sql = sql + " AND name like '%"+form.getName()+"%'";
        if(form.getPublisher()!=null)
            sql = sql + " AND publisher like '%"+form.getPublisher()+"%'";
        if(form.getPrice()!=null)
            sql = sql + " AND price <= "+form.getPrice();
        if(form.getOwner()!=null) {
            sql = sql +"AND owner like '"+form.getOwner()+"'";
        }
        sql = sql+" and not exists (select bookid from orders o where b.id=o.bookid)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Book> books = new ArrayList<>();
            while(rs.next()) {
                Book book = new Book();
                book.setId(rs.getLong("id"));
                book.setName(rs.getString("name"));
                book.setPublisher(rs.getString("publisher"));
                book.setPrice(rs.getInt("price"));
                book.setOwner(rs.getString("owner"));
                books.add(book);
            }
            return books;
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
