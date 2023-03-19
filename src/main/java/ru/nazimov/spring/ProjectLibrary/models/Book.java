package ru.nazimov.spring.ProjectLibrary.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_p")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotEmpty(message = "Название книги не может быть пустым")
    @Size(min = 2, max = 100, message = "Название книги должно содержать от 2 до 100 символов")
    @Column(name = "title")
    private String title;
    @NotEmpty(message = "Имя автора не может быть пустым")
    @Size(min = 2, max = 100, message = "Имя автора должно содержать от 2 до 100 символов")
    @Column(name = "author")
    private String author;
    @Min(value = 1, message = "Год выпуска книги должен быть больше, чем 1")
    @Max(value = 2022, message = "Год выпуска книги должен быть не позже 2023")
    @Column(name = "year")
    private int year;
    @ManyToOne()
    @JoinColumn(name = "person_b_id", referencedColumnName = "id")
    private Person owner;
    @Column(name = "taken_at")
    private LocalDateTime takenAt;     //для хранения времени взятия книги человеком и проверки просрочки
    @Transient
    private boolean isOverdue;

    public Book() {
    }

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }
}
