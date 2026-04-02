package ge.library.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "სათაური სავალდებულოა")
    @Size(min = 1, max = 200, message = "სათაური უნდა იყოს 1-დან 200 სიმბოლომდე")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "ISBN სავალდებულოა")
    @Pattern(regexp = "^[0-9\\-]{10,17}$", message = "ISBN ფორმატი არასწორია")
    @Column(unique = true, nullable = false)
    private String isbn;

    @Min(value = 1450, message = "გამოცემის წელი არ შეიძლება იყოს 1450-ზე ადრე")
    @Max(value = 2100, message = "გამოცემის წელი არასწორია")
    @Column(nullable = false)
    private Integer publishedYear;

    @NotBlank(message = "ჟანრი სავალდებულოა")
    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;

    // Many books belong to one author
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonBackReference
    private Author author;
}
