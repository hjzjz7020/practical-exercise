package com.example.server.api;

import com.example.server.controller.UserController;
import com.example.server.entity.dao.User;
import com.example.server.exception.InternalException;
import com.example.server.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private UserService service;

  @Autowired ObjectMapper mapper;

  @Test
  void testAddUser() throws Exception {
    User user = new User("zjz@gmail.com", "xxx", "A", "B");
    when(service.addUser(any())).thenReturn(user);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(user));

    this.mockMvc.perform(mockRequest).andExpect(status().isCreated());
  }

  @Test
  void testCreateUserExceptBadRequestIfUserDtoIsInvalid() throws Exception {
    User user = new User("zjz@gmail", "", "A", "B");
    when(service.addUser(any())).thenReturn(user);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(user));

    this.mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code", is("400 BAD_REQUEST")))
        .andExpect(jsonPath("$.message", is("password must not be empty. ")));
  }

  @Test
  void testGetUserExceptBadRequestIfEmailIsInvalid() throws Exception {
    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.get("/users/123")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    this.mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code", is("400 BAD_REQUEST")))
        .andExpect(
            jsonPath("$.message", is("getUserByEmail.email: must be a well-formed email address")));
  }

  @Test
  void testGetUser() throws Exception {
    String email = "zjz@gmail.com";
    String password = "xxxxx";
    String firstName = "A";
    String lastName = "B";

    User user = new User(email, password, firstName, lastName);
    when(service.getUserByEmail(email)).thenReturn(user);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.get("/users/" + email)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    this.mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email", is(email)))
        .andExpect(jsonPath("$.password", is(password)))
        .andExpect(jsonPath("$.firstName", is(firstName)))
        .andExpect(jsonPath("$.lastName", is(lastName)));
  }

  @Test
  void testReturnNullWhenCallGetUser() throws Exception {
    String email = "zjz@gmail.com";

    when(service.getUserByEmail(email))
        .thenThrow(new InternalException("User not found", HttpStatus.NOT_FOUND));

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.get("/users/" + email)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    this.mockMvc
        .perform(mockRequest)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code", is("404 NOT_FOUND")))
        .andExpect(jsonPath("$.message", is("User not found")));
  }

  @Test
  void testUpdateUser() throws Exception {
    String email = "zjz@gmail.com";
    String password = "xxxxx";
    String firstName = "A";
    String lastName = "B";

    User user = new User(email, password, firstName, lastName);

    when(service.updateUserByEmail(email, user)).thenReturn(user);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put("/users/" + email)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(user));

    this.mockMvc.perform(mockRequest).andExpect(status().isNoContent());
  }

  @Test
  void testUpdateUserExceptBadRequestIfEmailIsInvalid() throws Exception {
    String email = "zjz@gmail.com";
    String password = "xxxxx";
    String firstName = "A";
    String lastName = "B";

    User user = new User(email, password, firstName, lastName);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put("/users/123")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(user));

    this.mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code", is("400 BAD_REQUEST")))
        .andExpect(
            jsonPath(
                "$.message", is("updateUserByEmail.email: must be a well-formed email address")));
  }

  @Test
  void testReturnNullWhenCallUpdateUser() throws Exception {
    String email = "zjz@gmail.com";
    String password = "xxxxx";
    String firstName = "A";
    String lastName = "B";

    User user = new User(email, password, firstName, lastName);

    when(service.updateUserByEmail(any(), any()))
        .thenThrow(new InternalException("User not found", HttpStatus.NOT_FOUND));

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put("/users/" + email)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(user));

    this.mockMvc
        .perform(mockRequest)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code", is("404 NOT_FOUND")))
        .andExpect(jsonPath("$.message", is("User not found")));
  }

  @Test
  void testUpdateUserReturnBadRequestIfUserDtoIsInvalid() throws Exception {
    String email = "zjz@gmail.com";
    String password = "";
    String firstName = "A";
    String lastName = "B";

    User user = new User(email, password, firstName, lastName);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.put("/users/" + email)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(user));

    this.mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code", is("400 BAD_REQUEST")))
        .andExpect(jsonPath("$.message", is("password must not be empty. ")));
  }

  @Test
  void testDeleteUser() throws Exception {
    String email = "zjz@gmail.com";
    doNothing().when(service).deleteUserByEmail(email);

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.delete("/users/" + email)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    this.mockMvc.perform(mockRequest).andExpect(status().isNoContent());
  }

  @Test
  void testDeleteUserReturnBadRequestIfEmailIsInvalid() throws Exception {
    String email = "123";

    MockHttpServletRequestBuilder mockRequest =
        MockMvcRequestBuilders.delete("/users/" + email)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    this.mockMvc
        .perform(mockRequest)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code", is("400 BAD_REQUEST")))
        .andExpect(
            jsonPath(
                "$.message", is("deleteUserByEmail.email: must be a well-formed email address")));
  }
}
