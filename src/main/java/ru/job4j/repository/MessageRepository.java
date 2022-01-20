package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.domain.Message;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    public List<Message> findByRoomId(int roomId);

    public List<Message> findByPersonId(int personId);

    public List<Message> findByRoomIdAndAndPersonId(int roomId, int personId);
}
