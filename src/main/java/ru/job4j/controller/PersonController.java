package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Person;
import ru.job4j.service.ChatService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final ChatService service;

    private BCryptPasswordEncoder encoder;

    private final ObjectMapper objectMapper;

    public PersonController(ChatService service, BCryptPasswordEncoder encoder, ObjectMapper objectMapper) {
        this.service = service;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
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

    @PostMapping("/sign-up")
    public ResponseEntity<Person> save(@RequestBody Person person) {
        if (person.getName().equalsIgnoreCase("admin")) {
            throw new IllegalArgumentException("Username can not be 'admin'");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        return new ResponseEntity<>(
                service.savePerson(person),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/person/patch/{id}")
    public Person patch(@PathVariable int id, @RequestBody Person person) throws InvocationTargetException, IllegalAccessException {
        return this.service.patch(id, person);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        } }));
    }
}
