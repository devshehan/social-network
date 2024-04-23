package com.shehan.book.mapper;

import com.shehan.book.dto.BookResponse;
import com.shehan.book.dto.record.BookRequest;
import com.shehan.book.entity.book.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                //.cover()
                .rate(book.getRate())
                .owner(book.getOwner().fullName())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .build();
    }
}
