package com.shehan.book.entity.book;

import com.shehan.book.entity.common.BaseEntity;
import com.shehan.book.entity.feedback.FeedBack;
import com.shehan.book.entity.history.BookTransactionHistory;
import com.shehan.book.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<FeedBack> feedBacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> bookTransactionHistories;

    @Transient
    public double getRate() {
        if (feedBacks == null || feedBacks.isEmpty()) {
            return 0.0;
        }

        var rate = feedBacks.stream()
                .mapToDouble(FeedBack::getNote)
                .average()
                .orElse(0.0);

        double roundedRate = Math.round(rate * 10.0) / 10.0;
        return roundedRate;
    }
}
