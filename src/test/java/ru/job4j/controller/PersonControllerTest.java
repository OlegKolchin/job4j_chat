package ru.job4j.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.Job4jChatApplication;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Job4jChatApplication.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/person/"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void whenFindPerson() throws Exception {
        this.mockMvc.perform(get("/person/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void whenPersonNotFound() throws Exception {
        this.mockMvc.perform(get("/person/20"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void whenCreate() throws Exception {
        Person person = Person.of("Petr", "sample", "123", Role.of("role"));
        this.mockMvc.perform(post("/person/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(person)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenUpdate() throws Exception {
        Person person = Person.of("Petr", "sample", "123", Role.of("role"));
        person.setId(1);
        this.mockMvc.perform(post("/person/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(person)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenDelete() throws Exception {
        Person person = Person.of("Petr", "sample", "123", Role.of("role"));
        this.mockMvc.perform(post("/person/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(person)))
                .andExpect(status().isCreated());
        this.mockMvc.perform(delete("/person/3"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}