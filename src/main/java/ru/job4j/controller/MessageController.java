package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ru.job4j.domain.Message;
import ru.job4j.service.ChatService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class MessageController {

    private final ChatService service;

    public MessageController(final ChatService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Message> getAllMessages() {
        return service.getAllMessages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findMessageById(@PathVariable int id) {
        var message = service.findMessageById(id);
        return new ResponseEntity<>(
                message.orElse(new Message()),
                message.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @GetMapping("/room/{id}")
    public List<Message> findMessagesByRoomId(@PathVariable int id) {
        var messages = service.findMessagesByRoomId(id);
        if (messages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Messages not found");
        }
        return messages;
    }

    @GetMapping("/person/{id}")
    public List<Message> findMessagesByPersonId(@PathVariable int id) {
        var messages = service.findMessagesByPersonId(id);
        if (messages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Messages not found");
        }
        return messages;
    }

    @GetMapping(value = "/", params = {"rId", "pId"})
    public List<Message> findMessagesByRoomAndPersonId(int rId, int pId) {
        var messages = service.findMessagesByRoomIdAndPersonId(rId, pId);
        if (messages.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Messages not found");
        }
        return messages;
    }

    @PostMapping("/")
    public ResponseEntity<Message> save(@Valid @RequestBody Message message) {
        return new ResponseEntity<>(
                service.saveMessage(message),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/room/{id}")
    public ResponseEntity save(@PathVariable int id, @Valid @RequestBody Message message, HttpServletRequest req) {
        var room = service.findRoomById(id);
        String username = req.getSession().getAttribute("user_name").toString();
        if (room.isPresent()) {
            message.setRoom(room.get());
            message.setPerson(service.findPersonByName(username).get());
            service.saveMessage(message);
            String body = String.format("user : %s, message : %s, room : %s",
                    username, message.getBody(), room.get().getName());
            return new ResponseEntity<>(
                    body,
                    HttpStatus.CREATED
            );
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@Valid @RequestBody Message message) {
        this.service.saveMessage(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        this.service.deleteMessageById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/patch/{id}")
    public Message patch(@PathVariable int id, @Valid @RequestBody Message message) throws InvocationTargetException, IllegalAccessException {
        return this.service.patch(id, message);
    }


}
