package ru.nazimov.spring.ProjectLibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nazimov.spring.ProjectLibrary.models.Book;
import ru.nazimov.spring.ProjectLibrary.models.Person;
import ru.nazimov.spring.ProjectLibrary.repositories.BooksRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private BooksRepository booksRepository;
    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }
    //метод получения одной страницы книг (пагинация) с сортировкой по году, если эти параметры указаны
    public List<Book> findAll(Integer page, Integer booksPerPage, boolean isSortByYear){
        if (isSortByYear && booksPerPage!=null)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        else if (isSortByYear && booksPerPage==null)
            return booksRepository.findAll(Sort.by("year"));
        else if (!isSortByYear && booksPerPage!=null)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
        else return booksRepository.findAll();
    }
    public Book findOne(int id){
        return booksRepository.findById(id).orElse(null);
    }
    public Optional<List<Book>> findByOwner(Person owner){
        Optional<List<Book>> booksOfPerson = booksRepository.findByOwner(owner);
        //проверяем просроченные книги
        if (booksOfPerson.isPresent()) {
            booksOfPerson.get().forEach(book -> {
                if (LocalDateTime.now().minusDays(10).isAfter(book.getTakenAt()))
                    book.setOverdue(true);
                });
            }
        return booksOfPerson;
    }
    //метод поиска книги по названию
    public Optional<List<Book>> findByTitleContains(String query){
        return booksRepository.findByTitleContains(query);
    }
    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }
    @Transactional
    public void update(int id, Book updatedBook){
        Book bookToBeUpdated = booksRepository.findById(id).get();
        updatedBook.setId(id);                              //новому человеку без id назначаем id
        updatedBook.setOwner(bookToBeUpdated.getOwner());   //чтобы не терялась связь при обновлении
        booksRepository.save(updatedBook);   //при наличии такого id в БД вместо создания новой записи произойдет обновление старой
    }
    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }
    @Transactional
    public void release (int id){
        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(null);
            book.setTakenAt(null);
            book.setOverdue(false);
            booksRepository.save(book);
        });
    }
    @Transactional
    public void assign (int id, Person newOwner){
        booksRepository.findById(id).ifPresent(book -> {
            book.setOwner(newOwner);
            book.setTakenAt(LocalDateTime.now());
        });
    }
}
