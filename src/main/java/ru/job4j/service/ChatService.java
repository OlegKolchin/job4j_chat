package ru.job4j.service;

import antlr.debug.MessageAdapter;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;
import ru.job4j.domain.Room;
import ru.job4j.repository.MessageRepository;
import ru.job4j.repository.PersonRepository;
import ru.job4j.repository.RoleRepository;
import ru.job4j.repository.RoomRepository;

import java.util.ArrayList;
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
}
