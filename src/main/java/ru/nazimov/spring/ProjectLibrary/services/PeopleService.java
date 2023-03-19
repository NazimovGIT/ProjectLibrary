package ru.nazimov.spring.ProjectLibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nazimov.spring.ProjectLibrary.models.Book;
import ru.nazimov.spring.ProjectLibrary.models.Person;
import ru.nazimov.spring.ProjectLibrary.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private PeopleRepository peopleRepository;
    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }
    public Person findOne(int id){
       return peopleRepository.findById(id).orElse(null);
    }
    public Optional<Person> findByBook(Book book){
        return peopleRepository.findByBooksContains(book);
    }
    //метод получения человека по fullName, которого нужно проверить на существование
    //используется в PersonValidator для исключения создания человека с существующим в БД fullName
    public Optional<Person> findByFullName(String fullName){
        return peopleRepository.findByFullName(fullName);
    }
    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }
    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);                //новому человеку без id назначаем id
        peopleRepository.save(updatedPerson);   //при наличии такого id в БД вместо создания новой записи произойдет обновление старой
    }
    @Transactional
    public void delete(int id){
        peopleRepository.findById(id).ifPresent(person -> person.getBooks().forEach(book -> book.setOverdue(false)));
        peopleRepository.deleteById(id);
    }
}
