package ru.job4j.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;
import ru.job4j.domain.Room;
import ru.job4j.repository.MessageRepository;
import ru.job4j.repository.PersonRepository;
import ru.job4j.repository.RoleRepository;
import ru.job4j.repository.RoomRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private PersonRepository personRepository;
    private RoleRepository roleRepository;
    private RoomRepository roomRepository;
    private MessageRepository messageRepository;

    public ChatService(PersonRepository personRepository, RoleRepository roleRepository, RoomRepository roomRepository, MessageRepository messageRepository) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
    }

    public List<Person> getAllPersons() {
        List<Person> rsl = new ArrayList<>();
        personRepository.findAll().forEach(rsl :: add);
        return rsl;
    }

    public List<Role> getAllRoles() {
        List<Role> rsl = new ArrayList<>();
        roleRepository.findAll().forEach(rsl :: add);
        return rsl;
    }

    public List<Room> getAllRooms() {
        List<Room> rsl = new ArrayList<>();
        roomRepository.findAll().forEach(rsl :: add);
        return rsl;
    }

    public List<Message> getAllMessages() {
        List<Message> rsl = new ArrayList<>();
        messageRepository.findAll().forEach(rsl :: add);
        return rsl;
    }

    public Optional<Message> findMessageById(int id) {
         return messageRepository.findById(id);
    }

    public List<Message> findMessagesByRoomId(int id) {
        return messageRepository.findByRoomId(id);
    }

    public List<Message> findMessagesByPersonId(int id) {
        return messageRepository.findByPersonId(id);
    }

    public List<Message> findMessagesByRoomIdAndPersonId(int roomId, int personId) {
        return messageRepository.findByRoomIdAndAndPersonId(roomId, personId);
    }

    public Optional<Person> findPersonById(int id) {
        return personRepository.findById(id);
    }

    public Optional<Person> findPersonByName(String name) {
        return personRepository.findPersonByName(name);
    }

    public Optional<Role> findRoleById(int id) {
        return roleRepository.findById(id);
    }

    public Optional<Room> findRoomById(int id) {
        return roomRepository.findById(id);
    }

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public void deleteMessageById(int id) {
        messageRepository.deleteById(id);
    }

    public void deletePersonById(int id) {
        personRepository.deleteById(id);
    }

    public void deleteRoleById(int id) {
        roleRepository.deleteById(id);
    }

    public void deleteRoomById(int id) {
        roomRepository.deleteById(id);
    }

    public Message patch(int id, Message message) throws InvocationTargetException, IllegalAccessException {
        return (Message) patch(messageRepository, id, message);
    }

    public Person patch(int id, Person person) throws InvocationTargetException, IllegalAccessException {
        return (Person) patch(personRepository, id, person);
    }

    public Role patch(int id, Role role) throws InvocationTargetException, IllegalAccessException {
        return (Role) patch(roleRepository, id, role);
    }

    public Room patch(int id, Room room) throws InvocationTargetException, IllegalAccessException {
        return (Room) patch(roomRepository, id, room);
    }

    private Object patch(CrudRepository repository, int id, Object object) throws InvocationTargetException, IllegalAccessException {
        var current = repository.findById(id);
        if (!current.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var methods = current.get().getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method: methods) {
            var name = method.getName();
            if ((name.startsWith("get") || name.startsWith("set"))
                    && (!name.contains("setId") && !name.contains("getId"))) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid properties mapping");
                }
                var newValue = getMethod.invoke(object);
                if (newValue != null) {
                    setMethod.invoke(current.get(), newValue);
                }
            }
        }
        repository.save(current.get());
        return current.get();
    }
}
