package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Message;
import ru.job4j.service.ChatService;

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
        return service.findMessagesByRoomId(id);
    }

    @GetMapping("/person/{id}")
    public List<Message> findMessagesByPersonId(@PathVariable int id) {
        return service.findMessagesByPersonId(id);
    }

    @GetMapping(value = "/", params = {"rId", "pId"})
    public List<Message> findMessagesByRoomAndPersonId(int rId, int pId) {
        return service.findMessagesByRoomIdAndPersonId(rId, pId);
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
        this.service.saveMessage(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        this.service.deleteMessageById(id);
        return ResponseEntity.ok().build();
    }

}
