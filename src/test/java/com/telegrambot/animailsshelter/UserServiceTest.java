package com.telegrambot.animailsshelter;

import com.telegrambot.animailsshelter.model.User;
import com.telegrambot.animailsshelter.repository.UserRepository;
import com.telegrambot.animailsshelter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    void testSaveBotUser() {
        User user = new User(1L, "John", "Doe", "johndoe");
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.saveBotUser(1L, "John", "Doe", "johndoe");

        //assertEquals(user, savedUser);

    }

    @Test
    void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);

        List<User> retrievedUsers = userService.getAllUsers();
        assertEquals(userList, retrievedUsers);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        User user = new User(1L,  "John", "Doe", "johndoe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userService.getUserById(1L);
        assertTrue(retrievedUser.isPresent());
        assertEquals(user, retrievedUser.get());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteUser() {
        User user = new User(1L, "John", "Doe", "johndoe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Boolean result = userService.deleteUser(1L);
        assertTrue(result);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
