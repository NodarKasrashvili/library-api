package ge.library.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowResponseDto {
    private Long id;
    private String borrowerName;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private Boolean returned;
    private String bookTitle;
    private Long bookId;
}
