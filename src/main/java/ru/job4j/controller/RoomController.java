package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Room;
import ru.job4j.service.ChatService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


@RestController
@RequestMapping("/room")
public class RoomController {

    private final ChatService service;

    public RoomController(final ChatService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Room> getAllRooms() {
        return service.getAllRooms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findRoomById(@PathVariable int id) {
        var room = service.findRoomById(id);
        return new ResponseEntity<>(
                room.orElse(new Room()),
                room.isPresent() ?  HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Room> save(Room room) {
        return new ResponseEntity<>(
                service.saveRoom(room),
                HttpStatus.CREATED
        );
    }
    @PatchMapping("/patch/{id}")
    public Room patch(@PathVariable int id, @RequestBody Room room) throws InvocationTargetException, IllegalAccessException {
        return this.service.patch(id, room);
    }


    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Room room) {
        this.service.saveRoom(room);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        this.service.deleteRoomById(id);
        return ResponseEntity.ok().build();
    }


}
