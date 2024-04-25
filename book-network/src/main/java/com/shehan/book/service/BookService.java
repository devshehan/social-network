package com.shehan.book.service;

import com.shehan.book.dto.BookResponse;
import com.shehan.book.dto.BorrowedBookResponse;
import com.shehan.book.dto.record.BookRequest;
import com.shehan.book.entity.book.Book;
import com.shehan.book.entity.history.BookTransactionHistory;
import com.shehan.book.exception.OperationNotPermittedException;
import com.shehan.book.mapper.BookMapper;
import com.shehan.book.repository.BookRepository;
import com.shehan.book.repository.BookTransactionHistoryRepository;
import com.shehan.book.user.User;
import com.shehan.book.util.BookSpecification;
import com.shehan.book.util.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;

    public Long saveBook(BookRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Could not found book ID ::" + bookId));
    }

    public PageResponse<BookResponse> getAllBooks(
            int page,
            int size,
            Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponseList = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponseList,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(
            int page,
            int size,
            Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page,size,Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()),pageable);

        List<BookResponse> bookResponseList = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponseList,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );

    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(
            int page,
            int size,
            Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page,size,Sort.by("createdDate").descending());
        Page<BookTransactionHistory> bookTransactionHistories
                = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable,user.getId());

        List<BorrowedBookResponse> bookResponseList = bookTransactionHistories.stream()
                .map(bookMapper::toBookBorrowedResponse)
                .toList();
        return new PageResponse<>(
                bookResponseList,
                bookTransactionHistories.getNumber(),
                bookTransactionHistories.getSize(),
                bookTransactionHistories.getTotalElements(),
                bookTransactionHistories.getTotalPages(),
                bookTransactionHistories.isFirst(),
                bookTransactionHistories.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(
            int page,
            int size,
            Authentication connectedUser) {

        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page,size,Sort.by("createdDate").descending());
        Page<BookTransactionHistory> bookTransactionHistories
                = bookTransactionHistoryRepository.findAllReturnedBooks(pageable,user.getId());

        List<BorrowedBookResponse> bookResponseList = bookTransactionHistories.stream()
                .map(bookMapper::toBookBorrowedResponse)
                .toList();
        return new PageResponse<>(
                bookResponseList,
                bookTransactionHistories.getNumber(),
                bookTransactionHistories.getSize(),
                bookTransactionHistories.getTotalElements(),
                bookTransactionHistories.getTotalPages(),
                bookTransactionHistories.isFirst(),
                bookTransactionHistories.isLast()
        );
    }

    public Long updateShareableStatus(
            Long bookId,
            Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID : " + bookId));
        book.setShareable(true);
        User user = ((User) connectedUser.getPrincipal());

        if(!Objects.equals(book.getOwner().getId(),user.getId())){
            throw new OperationNotPermittedException("You are not allowed to change the shareable status");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Long updateArchiveStatus(
            Long bookId,
            Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID : " + bookId));
        book.setShareable(true);
        User user = ((User) connectedUser.getPrincipal());

        if(!Objects.equals(book.getOwner().getId(),user.getId())){
            throw new OperationNotPermittedException("You are not allowed to change archived status");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }

    public Long borrowBook(
            Long bookId,
            Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID : " + bookId));
        book.setShareable(true);
        User user = ((User) connectedUser.getPrincipal());
        if( book.isArchived() || !book.isShareable()){
            throw new OperationNotPermittedException("You are not allowed to change borrow" +
                    " this until archived or shareable status change");
        }
        if(!Objects.equals(book.getOwner().getId(),user.getId())){
            throw new OperationNotPermittedException("You can not borrow your own book");
        }
        boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowed(
                bookId,
                user.getId()
        );
        if (isAlreadyBorrowed){
            throw new OperationNotPermittedException("The book you tried to borrow is already borrowed");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }
}















