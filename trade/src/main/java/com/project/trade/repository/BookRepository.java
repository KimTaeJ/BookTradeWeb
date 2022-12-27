package com.project.trade.repository;

import com.project.trade.controller.BookForm;
import com.project.trade.domain.Book;
import java.util.List;

public interface BookRepository {

    List<Book> findAll();
    Book save(Book book);
    List<Book> findCond(BookForm form);

}
