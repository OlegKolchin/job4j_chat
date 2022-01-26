package ru.job4j.domain;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Message body must contain symbols")
    private String body;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;


    public static Message of(String body, Person person, Room room) {
        Message message = new Message();
        message.body = body;
        message.person = person;
        message.room = room;
        return message;
    }

    public int getId() {
        return id;
    }

    public void setId(int ind) {
        this.id = ind;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
