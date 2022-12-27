package com.project.trade.service;

import com.project.trade.controller.BookForm;
import com.project.trade.domain.Book;
import com.project.trade.repository.BookRepository;

import java.util.List;

public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    //전체 도서 목록 조회
    public List<Book> findBooks() {
        return bookRepository.findAll();
    }


    public Long addBook(Book book) {
        bookRepository.save(book);
        return book.getId();
    }

    public List<Book> findCondBooks(BookForm form) {
        return bookRepository.findCond(form);
    }

}
