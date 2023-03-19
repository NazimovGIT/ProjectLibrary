package ru.nazimov.spring.ProjectLibrary.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.nazimov.spring.ProjectLibrary.models.Person;
import ru.nazimov.spring.ProjectLibrary.services.PeopleService;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator (PeopleService peopleService){
        this.peopleService = peopleService;
    }
    @Override
    //метод, указывающий к какому классу валидатор применяется
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }
    //метод валидации для объектов нашего класса
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;       //сюда приходит наш созданный или отредактированный объект из формы

        if (peopleService.findByFullName(person.getFullName()).isPresent()) {    //еcли человек с таким именем уже есть в БД
            // поместим в errors свою ошибку, которую далее отобразим пользователю
            errors.rejectValue("fullName",            //какое поле вызвало ошибку
                          "",                           //код ошибки, можно без него
            "Человек с таким ФИО уже существует");             //текст ошибки
        }
    }
}
