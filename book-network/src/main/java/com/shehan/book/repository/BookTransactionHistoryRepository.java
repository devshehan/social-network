package com.shehan.book.repository;

import com.shehan.book.entity.history.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory,Long> {

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.user.id = :id
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(
            Pageable pageable,
            Long id);

    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.returned = true
            AND history.book.owner.id = :id
            """
    )
    Page<BookTransactionHistory> findAllReturnedBooks(
            Pageable pageable,
            Long id
    );
}
