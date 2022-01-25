package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ru.job4j.domain.Message;
import ru.job4j.service.ChatService;

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
    public ResponseEntity<Message> save(Message message) {
        return new ResponseEntity<>(
                service.saveMessage(message),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/room/{id}")
    public ResponseEntity save(@PathVariable int id, Message message) {
        var room = service.findRoomById(id);
        if (room.isPresent()) {
            message.setRoom(room.get());
        }
        return new ResponseEntity<>(
                service.saveMessage(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        if (message.getBody() == null) {
            throw new NullPointerException("Body cannot be empty");
        }
        this.service.saveMessage(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        this.service.deleteMessageById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/patch/{id}")
    public Message patch(@PathVariable int id, @RequestBody Message message) throws InvocationTargetException, IllegalAccessException {
        return this.service.patch(id, message);
    }


}
