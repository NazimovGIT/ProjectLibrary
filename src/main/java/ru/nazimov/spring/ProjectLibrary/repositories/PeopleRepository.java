package ru.nazimov.spring.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nazimov.spring.ProjectLibrary.models.Book;
import ru.nazimov.spring.ProjectLibrary.models.Person;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    //выполняет стандартные CRUD операции по сущности Person в БД (эти методы не прописываем)
    //дополнительно можем писать кастомные методы
    Optional<Person> findByFullName(String fullName);
    Optional<Person> findByBooksContains(Book book);
}
