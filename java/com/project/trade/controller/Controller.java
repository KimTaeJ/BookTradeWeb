package com.project.trade.controller;

import com.project.trade.domain.Book;
import com.project.trade.domain.Member;
import com.project.trade.domain.Order;
import com.project.trade.service.BookService;
import com.project.trade.service.MemberService;
import com.project.trade.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@org.springframework.stereotype.Controller
public class Controller {

    private final MemberService memberService;
    private final BookService bookService;
    private final OrderService orderService;

    @Autowired
    public Controller(MemberService memberService, BookService bookService, OrderService orderService) {
        this.memberService = memberService;
        this.bookService = bookService;
        this.orderService = orderService;
    }

    //index page
    @GetMapping(value="/")
    public String home() {
        return "home";
    }
    @GetMapping(value="/books/main")
    public String mainPage(Model model, HttpServletRequest serv, RedirectAttributes redi) {
        HttpSession session = serv.getSession();
        model.addAttribute("member", session.getAttribute("member"));
        return "books/searchField";
    }

    @GetMapping(value = "/books/new")
    public String newBook() {
        return "/books/newBook";
    }

    // add new book to table books
    @PostMapping(value="/books/new")
    public String addBook(Model model, BookForm form, HttpServletRequest serv, RedirectAttributes redi) {
        HttpSession session = serv.getSession();
        Book book = new Book();
        book.setName(form.getName());
        book.setPublisher(form.getPublisher());
        book.setPrice(form.getPrice());
        book.setOwner(session.getAttribute("mail").toString());
        bookService.addBook(book);
        model.addAttribute("member", session.getAttribute("member"));
        return "books/searchField";
    }

    //search books
    @PostMapping(value = "/books/search")
    public String search(BookForm form, Model model, HttpServletRequest serv, RedirectAttributes redi) {
        HttpSession session = serv.getSession();
        List<Book> books = bookService.findCondBooks(form);
        model.addAttribute("books", books);
        model.addAttribute("member", session.getAttribute("member"));
        return "books/searchField";
    }
    //order book
    @PostMapping(value = "/books/order")
    public String orderBook(OrderForm form, Model model, HttpServletRequest serv, RedirectAttributes redi) {
        HttpSession session = serv.getSession();
        String mail = session.getAttribute("mail").toString();
        form.setClient(mail);
        orderService.addOrder(form);
        model.addAttribute("member", session.getAttribute("member"));
        return "books/searchField";
    }

    //authorize input member
    @PostMapping(value = "/member/in")
    public String login(MemberForm form, Model model, HttpServletRequest serv, RedirectAttributes redi) {
        HttpSession session = serv.getSession();
        Member member = memberService.findMember(form);
        if(member.getMail() != null) {
            System.out.println("pass");
            session.setAttribute("member", member);
            session.setAttribute("mail", member.getMail());
            model.addAttribute("member", session.getAttribute("member"));
            return "books/searchField";
        }
        else {
            System.out.println("non");
            session.invalidate();
            return "redirect:/";
        }
    }
    //logout
    @GetMapping(value="/member/out")
    public String logout(HttpServletRequest serv, RedirectAttributes redi) {
        HttpSession session = serv.getSession();
        session.invalidate();
        return "redirect:/";
    }

    //signIn page
    @GetMapping(value="/member/new")
    public String signIn() {
        return "/member/memberJoin";
    }

    // signIn member input
    @PostMapping(value="/member/new")
    public String signForm(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());
        member.setMail(form.getMail());
        member.setPass(form.getPass());
        memberService.join(member);
        return "redirect:/";
    }
    //show current user's order list
    @GetMapping(value = "/member/myBooks")
    public String myBooks(Model model, HttpServletRequest serv, RedirectAttributes red) {
        HttpSession session = serv.getSession();
        String mail = session.getAttribute("mail").toString();
        BookForm form = new BookForm();
        form.setOwner(mail);
        List<Book> books = bookService.findCondBooks(form);
        model.addAttribute("books", books);
        return "member/myBooks";
    }

    //mypage - booklist
    @GetMapping(value = "/member/myPage")
    public String myPage(Model model, HttpServletRequest serv, RedirectAttributes red) {
        HttpSession session = serv.getSession();
        String mail = session.getAttribute("mail").toString();
        BookForm bookForm = new BookForm();
        bookForm.setOwner(mail);
        List<Book> books = bookService.findCondBooks(bookForm);
        model.addAttribute("member", session.getAttribute("member"));
        model.addAttribute("books", books);
        return "member/myPage";
    }
    //mypage - request list
    @GetMapping(value = "/member/myOrders")
    public String myRequest(Model model, HttpServletRequest serv, RedirectAttributes red) {
        HttpSession session = serv.getSession();
        String mail = session.getAttribute("mail").toString();
        Order order = new Order();
        order.setClient(mail);
        List<Order> orders = orderService.findReq(order);
        model.addAttribute("member", session.getAttribute("member"));
        model.addAttribute("orders", orders);
        return "member/myRequest";
    }

    //mypage - quest list
    @GetMapping(value = "/member/myOrdersTo")
    public String myQuest(Model model, HttpServletRequest serv, RedirectAttributes red) {
        HttpSession session = serv.getSession();
        String mail = session.getAttribute("mail").toString();
        Order order = new Order();
        order.setOwner(mail);
        List<Order> orders = orderService.findQ(order);
        model.addAttribute("member", session.getAttribute("member"));
        model.addAttribute("orders", orders);
        return "member/myQuest";
    }



}
