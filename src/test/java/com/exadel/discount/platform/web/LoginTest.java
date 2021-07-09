package com.exadel.discount.platform.web;

import com.exadel.discount.platform.service.dto.UserLoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest{
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(value = {"/insertTestUser.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteTestUser.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void loginValidUser() throws Exception{
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("test_user@gmail.com");
        loginDTO.setPassword("Test_user1234");
        String json = (new ObjectMapper()).writeValueAsString(loginDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Sql(value = {"/insertTestUser.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteTestUser.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void loginInvalidUser() throws Exception{
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setEmail("test_user@gmail.com");
        loginDTO.setPassword("Test_user123456789");
        String json = (new ObjectMapper()).writeValueAsString(loginDTO);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()));
    }
}
