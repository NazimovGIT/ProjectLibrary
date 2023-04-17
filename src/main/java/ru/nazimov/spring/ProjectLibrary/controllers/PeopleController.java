package ru.nazimov.spring.ProjectLibrary.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nazimov.spring.ProjectLibrary.models.Book;
import ru.nazimov.spring.ProjectLibrary.models.Person;
import ru.nazimov.spring.ProjectLibrary.services.BooksService;
import ru.nazimov.spring.ProjectLibrary.services.PeopleService;
import ru.nazimov.spring.ProjectLibrary.util.PersonValidator;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final BooksService booksService;
    private final PersonValidator personValidator;      //для валидации с проверкой в БД
    @Autowired
    public PeopleController(PeopleService peopleService, BooksService booksService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.personValidator = personValidator;
    }
    //Get метод для отображения всех людей
    @GetMapping()
    public String index(Model model){
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }
    //Get метод для отображения одного человека по id
    @GetMapping("/{id}")
    public String show (@PathVariable("id") int id, Model model){
        model.addAttribute("person", peopleService.findOne(id));

        Optional<List<Book>> books = booksService.findByOwner(peopleService.findOne(id));

        //если человек взял книги
        //показать эти книги в представлении
        books.ifPresent(bookList -> model.addAttribute("books", bookList));
        return "people/show";
    }
    //Get метод отображения html формы для заполнения инфы о новом человеке
    @GetMapping("/new")
    public String newPerson (@ModelAttribute("person") Person person){  //передаем в модель по ключу "person" пустой новый объект
        return "people/new";
    }
    //POST метод создания нового человека на основе данных из тела запроса, полученного из формы
    @PostMapping()
    public String create(@ModelAttribute ("person") @Valid Person person,
                         BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);    //валидация данных с проверкой в БД
        if (bindingResult.hasErrors())
            return "people/new";

        peopleService.save(person);
        return "redirect:/people";      //в форме теперь можно получить доступ к ошибкам, так как person их содержит
    }
    //Get метод отображения страницы редактирования человека
    @GetMapping("/{id}/edit")
    public String edit (@PathVariable int id, Model model){
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }
    //PATCH метод обновления человека на основе данных из тела запроса, полученного из формы
    @PatchMapping("/{id}")
    public String update (@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                          @PathVariable("id") int id){
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/edit";
        peopleService.update(id, person);
        return "redirect:/people";
    }
    //DELETE метод удаления человека по его id
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        peopleService.delete(id);
        return "redirect:/people";
    }
}
