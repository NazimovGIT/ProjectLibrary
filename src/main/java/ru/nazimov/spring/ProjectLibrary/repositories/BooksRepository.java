package ru.nazimov.spring.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nazimov.spring.ProjectLibrary.models.Person;
import ru.nazimov.spring.ProjectLibrary.models.Book;

import java.util.List;
import java.util.Optional;
@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    //выполняет стандартные CRUD операции по сущности Person в БД (эти методы не прописываем)
    //дополнительно можем писать кастомные методы
    Optional<List<Book>> findByOwner(Person owner);
    Optional<List<Book>> findByTitleContains(String query);
}
