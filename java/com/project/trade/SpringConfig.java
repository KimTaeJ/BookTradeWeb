package com.project.trade;

import com.project.trade.repository.*;
import com.project.trade.service.BookService;
import com.project.trade.service.MemberService;
import com.project.trade.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final DataSource dataSource;
    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Bean
    public BookService bookService() {
        return new BookService(bookRepository());
    }

    @Bean
    public BookRepository bookRepository() {
        return new JdbcBookRepository(dataSource);
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JdbcMemberRepository(dataSource);
    }

    @Bean
    public OrderService orderService() {
        return new OrderService(orderRepository());
    }
    @Bean
    public OrderRepository orderRepository() {return new JdbcOrderRepository(dataSource);}
}
