package ge.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "borrow_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "მსესხებლის სახელი სავალდებულოა")
    @Column(nullable = false)
    private String borrowerName;

    @Column(nullable = false)
    @Builder.Default
    private LocalDate borrowDate = LocalDate.now();

    private LocalDate returnDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean returned = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
