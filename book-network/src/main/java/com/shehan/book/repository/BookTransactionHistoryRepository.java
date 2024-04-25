package com.shehan.book.repository;

import com.shehan.book.entity.history.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    @Query("""
            SELECT
            (COUNT (*) > 0 ) AS isBorrowed
            WHERE BookTransactionHistory.book.id = :bookId
            AND BookTransactionHistory.user.id = :userId
            AND BookTransactionHistory.returnApproved = false
            """)
    boolean isAlreadyBorrowed(Long bookId, Long userId);

    @Query("""
            SELECT transaction
            FROM BookTransactionHistory transaction
            WHERE transaction.book.id = :bookId
            AND transaction.user.id = :id
            AND transaction.returnApproved = false
            AND transaction.returned = false
            """)
    Optional<BookTransactionHistory> findBookIdAndUserId(Long bookId, Long userId);

    @Query("""
            SELECT transaction
            FROM BookTransactionHistory transaction
            WHERE transaction.book.id = :bookId
            AND transaction.book.owner.id = :ownerId
            AND transaction.returned = true
            AND transaction.returnApproved = false
            """)
    Optional<BookTransactionHistory> findBookIdAndOwnerId(
            Long bookId,
            Long ownerId);
}
