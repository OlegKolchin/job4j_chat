package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final ChatService service;

    public PersonController(final ChatService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Person> getAllPersons() {
        return service.getAllPersons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findPersonById(@PathVariable int id) {
        var person = this.service.findPersonById(id);
        return new ResponseEntity<Person>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> save(Person person) {
        return new ResponseEntity<Person>(
                service.savePerson(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        this.service.savePerson(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        this.service.deletePersonById(id);
        return ResponseEntity.ok().build();
    }
}
