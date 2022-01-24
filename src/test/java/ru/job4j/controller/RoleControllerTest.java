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
import ru.job4j.domain.Role;
import ru.job4j.domain.Room;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Job4jChatApplication.class)
@AutoConfigureMockMvc
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Test
    @WithMockUser
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/role/"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    public void whenFindRole() throws Exception {
        this.mockMvc.perform(get("/role/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    public void whenRoleNotFound() throws Exception {
        this.mockMvc.perform(get("/role/20"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    public void whenCreate() throws Exception {
        Room room = Room.of("new Room");
        this.mockMvc.perform(post("/role/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(room)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void whenUpdate() throws Exception {
        Role role = Role.of("Admin");
        role.setId(1);
        this.mockMvc.perform(post("/role/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(role)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void whenDelete() throws Exception {
        this.mockMvc.perform(delete("/role/2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}