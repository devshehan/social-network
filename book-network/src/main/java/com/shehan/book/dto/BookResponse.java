package com.shehan.book.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private byte[] cover;
    private double rate;
    private String owner;
    private boolean archived;
    private boolean shareable;
}
