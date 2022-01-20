package ru.job4j.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Role;
import ru.job4j.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {
    private final ChatService service;

    public RoleController(final ChatService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Role> getAllRoles() {
        return service.getAllRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findRoleById(@PathVariable int id) {
        var role = service.findRoleById(id);
        return new ResponseEntity<Role>(
                role.orElse(new Role()),
                role.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Role> save(Role role) {
        return new ResponseEntity<Role>(
                service.saveRole(role),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Role role) {
        this.service.saveRole(role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        this.service.deleteRoleById(id);
        return ResponseEntity.ok().build();
    }
}
