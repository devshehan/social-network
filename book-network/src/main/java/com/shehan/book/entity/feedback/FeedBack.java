package com.shehan.book.entity.feedback;

import com.shehan.book.entity.book.Book;
import com.shehan.book.entity.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class FeedBack extends BaseEntity {

    private Double note; // 1- 5 stars
    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
