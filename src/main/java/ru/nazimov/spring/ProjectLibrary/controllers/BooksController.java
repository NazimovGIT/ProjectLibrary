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

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;
    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }
    //показать все книги
    @GetMapping()
    public String index(@RequestParam(name = "page", required = false) Integer page,
                        @RequestParam(name = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(name = "sort_by_year", required = false) boolean isSortByYear,
                        Model model){
        //передаем в модель одну страницу списка после пагинации и сортировки, если эти параметры в запросе указаны
            model.addAttribute("books", booksService.findAll(page, booksPerPage, isSortByYear));
        return "books/index";
    }
    //показать одну книгу по id
    @GetMapping("/{id}")
    public String show (@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", booksService.findOne(id));

        Optional<Person> bookOwner = peopleService.findByBook(booksService.findOne(id));

        if (bookOwner.isPresent())                                  //если есть владелец
            model.addAttribute("owner", bookOwner.get());     //показать его
        else model.addAttribute("people", peopleService.findAll());//иначе все люди в выпадающем списке

        return "books/show";
    }
    //вернуть html форму для заполнения инфы о новой книге
    @GetMapping("/new")
    public String newBook (@ModelAttribute("book") Book book){
        return "books/new";
    }
    //создать новоую книгу на основе данных из тела запроса, полученного из формы
    @PostMapping()
    public String create(@ModelAttribute ("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        booksService.save(book);
        return "redirect:/books";
    }
    //показать заполненную страницу редактирования книги
    @GetMapping("/{id}/edit")
    public String edit (@PathVariable int id, Model model){
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }
    //обновить данные о книге на основе данных из тела запроса, полученного из формы
    @PatchMapping("/{id}")
    public String update (@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                          @PathVariable("id") int id){
        if (bindingResult.hasErrors())
            return "books/edit";
        booksService.update(id, book);
        return "redirect:/books";
    }
    //удалить книгу по его id
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }
    //освободить книгу от владельца при нажатии на странице /books/{id}
    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        booksService.release(id);
        return "redirect:/books/" + id;
    }
    //назначить книгу новому владельцу при выборе на странице /books/{id}
    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson){
        //у selectedPerson из формы пришло только id, остальные поля null
        //Person newOwner = peopleService.findOne(selectedPerson.getId()); //можно без этого
        booksService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }
    //поиск книги по названию
    @GetMapping("/search")
    public String searchPage(){
        return "/books/search";
    }
    @PostMapping("/search")
    public String makeSearch(Model model, @RequestParam(name = "query") String query){
        //если с формы /search пришел запрос, содержащий текст поиска query,
        //получить все книги, содержащие в названии этот текст и поместить в модель
            Optional<List<Book>> foundBooks = booksService.findByTitleContains(query);
            model.addAttribute("foundBooks", foundBooks.get());

        return "/books/search";
    }
}
