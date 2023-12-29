package com.telegrambot.animailsshelter;

import com.telegrambot.animailsshelter.model.User;
import com.telegrambot.animailsshelter.repository.UserRepository;
import com.telegrambot.animailsshelter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Test
    void testGetAllUsers() {
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);

        List<User> retrievedUsers = userService.getAllUsers();
        assertEquals(userList, retrievedUsers);

        verify(userRepository, times(1)).findAll();
    }
}
