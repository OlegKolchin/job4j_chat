package ru.job4j.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.Job4jChatApplication;
import ru.job4j.domain.Message;
import ru.job4j.domain.Person;
import ru.job4j.domain.Role;
import ru.job4j.domain.Room;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Job4jChatApplication.class)
@AutoConfigureMockMvc
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;


    @Test
    @WithMockUser
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/chat/"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    public void whenFindMessage() throws Exception {
        this.mockMvc.perform(get("/chat/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    public void whenFindMessageByRoomId() throws Exception {
        this.mockMvc.perform(get("/chat/room/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenFindMessageByPersonId() throws Exception {
        this.mockMvc.perform(get("/chat/person/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenFindByRoomIdAndPersonId() throws Exception {
        this.mockMvc.perform(get("/chat/")
                        .param("rId", "1").param("pId", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void whenMessageNotFound() throws Exception {
        this.mockMvc.perform(get("/chat/20"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    public void whenCreate() throws Exception {
        Message message = Message.of("Привет", Person.of("test", "test", "test",
                Role.of("test")), Room.of("test"));
        this.mockMvc.perform(post("/chat/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(message)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void whenUpdate() throws Exception {
        Message message = Message.of("Привет", Person.of("test", "test", "test",
                Role.of("test")), Room.of("test"));
        message.setId(1);
        this.mockMvc.perform(post("/chat/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(message)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void whenDelete() throws Exception {
        Message message = Message.of("Привет", Person.of("test", "test", "test",
                Role.of("test")), Room.of("test"));
        this.mockMvc.perform(post("/chat/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(message)))
                .andExpect(status().isCreated());
        this.mockMvc.perform(delete("/chat/2"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}