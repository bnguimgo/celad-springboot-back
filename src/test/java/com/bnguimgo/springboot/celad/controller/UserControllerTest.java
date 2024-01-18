package com.bnguimgo.springboot.celad.controller;

import com.bnguimgo.springboot.celad.UserManagerApplication;
import com.bnguimgo.springboot.celad.dto.UserDTO;
import com.bnguimgo.springboot.celad.exception.BusinessResourceExceptionDTO;
import com.bnguimgo.springboot.celad.mapper.DomainToDtoMapper;
import com.bnguimgo.springboot.celad.mapper.DtoToDomainMapper;
import com.bnguimgo.springboot.celad.model.User;
import com.bnguimgo.springboot.celad.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Please note that I prefer the test of the lowest layer --> do not mock service layer
 */
@SpringBootTest(classes = UserManagerApplication.class)
@ActiveProfiles({"test"})//set this if exist
@AutoConfigureMockMvc()
public class UserControllerTest {

    /*
     * Mocks
     */
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserRepository userRepository;
    @MockBean
    private DomainToDtoMapper toDtoMapper;
    @MockBean
    private DtoToDomainMapper toDomainMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getAllUsers() throws Exception {

        User user_1 = User.builder()
            .id(1L)
            .firstName("firstName_1")
            .lastName("lastName_1")
            .email("email_1@email.com")
            .build();
        User user_2 = User.builder()
            .id(2L)
            .firstName("firstName_2")
            .lastName("lastName_2")
            .email("email_2@email.com")
            .build();

        UserDTO userDTO_1 = UserDTO.builder()
            .id(1L)
            .firstName("firstName_1")
            .lastName("lastName_1")
            .email("email_1@email.com")
            .build();
        UserDTO userDTO_2 = UserDTO.builder()
            .id(2L)
            .firstName("firstName_2")
            .lastName("lastName_2")
            .email("email_2@email.com")
            .build();

        List<User> allUsers = List.of(user_1, user_2);
        List<UserDTO> allUsersDTO = List.of(userDTO_1, userDTO_2);
        //Mock
        //Note: Mock only the lowest layer (Dao), do not mock service layer
        when(userRepository.findAll()).thenReturn(allUsers);
        when(toDtoMapper.toDtoMapper(user_1)).thenReturn(userDTO_1);
        when(toDtoMapper.toDtoMapper(user_2)).thenReturn(userDTO_2);

        // Call method
        MvcResult result = mockMvc.perform(get("/api/user/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();

        // double check status)
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Search error");
        verify(userRepository).findAll();
        verify(toDtoMapper).toDtoMapper(user_1);
        verify(toDtoMapper).toDtoMapper(user_2);
        assertNotNull(result);
        List<UserDTO> users = objectMapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<>() {
            });
        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.containsAll(allUsersDTO));
    }

    @Test
    public void createUser() throws Exception {

        UserDTO userDTO_1 = UserDTO.builder()
            .firstName("firstName_1")
            .lastName("lastName_1")
            .email("email_1@email.com")
            .build();
        User userToCreate_1 = User.builder()
            .firstName("firstName_1")
            .lastName("lastName_1")
            .email("email_1@email.com")
            .build();

        User userCreated_1 = User.builder()
            .id(1L)
            .firstName("firstName_1")
            .lastName("lastName_1")
            .email("email_1@email.com")
            .build();
        UserDTO userCreatedDTO_1 = UserDTO.builder()
            .id(1L)
            .firstName("firstName_1")
            .lastName("lastName_1")
            .email("email_1@email.com")
            .build();

        when(toDomainMapper.toDomainModel(userDTO_1)).thenReturn(userToCreate_1);
        when(userRepository.save(userToCreate_1)).thenReturn(userCreated_1);
        when(toDtoMapper.toDtoMapper(userCreated_1)).thenReturn(userCreatedDTO_1);

        String jsonContent = objectMapper.writeValueAsString(userDTO_1);
        MvcResult result = mockMvc
            .perform(post("/api/user/users")
                .contentType(MediaType.APPLICATION_JSON).content(jsonContent))
            .andExpect(status().isCreated()).andReturn();

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus(), "Create user error");
        verify(userRepository).save(userToCreate_1);
        verify(toDomainMapper).toDomainModel(userDTO_1);
        verify(toDtoMapper).toDtoMapper(userCreated_1);
        UserDTO userResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(userResult);
        assertEquals(1L, userResult.getId());
        assertEquals(userDTO_1.getFirstName(), userResult.getFirstName());
        assertEquals(userDTO_1.getLastName(), userResult.getLastName());
        assertEquals(userDTO_1.getEmail(), userResult.getEmail());

    }

    @Test
    public void updateUser() throws Exception {

        UserDTO userDtoToUpdate = UserDTO.builder()
            .id(1L)
            .firstName("New firstName_1")
            .lastName("New lastName_1")
            .email("email_1@email.com")
            .build();
        User userFound = User.builder()
            .id(1L)
            .firstName("firstName_1")
            .lastName("lastName_1")
            .email("email_1@email.com")
            .build();

        User userToUpdate = User.builder()
            .id(1L)
            .firstName("New firstName_1")
            .lastName("New lastName_1")
            .email("email_1@email.com")
            .build();

        User userUpdated = User.builder()
            .id(1L)
            .firstName("New firstName_1")
            .lastName("New lastName_1")
            .email("email_1@email.com")
            .build();

        when(userRepository.findById(userDtoToUpdate.getId())).thenReturn(Optional.of(userFound));
        when(toDomainMapper.toDomainModel(userDtoToUpdate)).thenReturn(userToUpdate);
        when(userRepository.save(userToUpdate)).thenReturn(userUpdated);
        when(toDtoMapper.toDtoMapper(userUpdated)).thenReturn(userDtoToUpdate);

        String jsonContent = objectMapper.writeValueAsString(userDtoToUpdate);
        MvcResult result = mockMvc
            .perform(MockMvcRequestBuilders.put("/api/user/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(jsonContent))
            .andExpect(status().isOk()).andReturn();

        verify(userRepository).findById(userDtoToUpdate.getId());
        verify(toDomainMapper).toDomainModel(userDtoToUpdate);
        verify(userRepository).save(userToUpdate);
        verify(toDtoMapper).toDtoMapper(userUpdated);

        UserDTO userResult = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(userResult);
        assertEquals(Long.valueOf(1), userResult.getId());
        assertEquals("New firstName_1", userResult.getFirstName());
        assertEquals("New lastName_1", userResult.getLastName());
        assertEquals("email_1@email.com", userResult.getEmail());

    }

    @Test
    public void updateUser_Throws_BusinessResourceException() throws Exception {

        UserDTO userDtoToUpdate = UserDTO.builder()
            .id(1L)
            .firstName("New firstName_1")
            .lastName("New lastName_1")
            .email("email_1@email.com")
            .build();

        when(userRepository.findById(userDtoToUpdate.getId())).thenReturn(Optional.empty());

        String jsonContent = objectMapper.writeValueAsString(userDtoToUpdate);
        MvcResult result = mockMvc
            .perform(MockMvcRequestBuilders.put("/api/user/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(jsonContent))
            .andExpect(status().isNotFound()).andReturn();

        verify(userRepository).findById(userDtoToUpdate.getId());

        BusinessResourceExceptionDTO thrown = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(thrown);
        assertEquals("User not found with id: " + userDtoToUpdate.getId(), thrown.getErrorMessage());
        assertEquals("UserNotFound", thrown.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), thrown.getStatus().value());

    }

    @Test
    void deleteUser() throws Exception {

        UserDTO userDtoToUpdate = UserDTO.builder()
            .id(1L)
            .firstName("New firstName_1")
            .lastName("New lastName_1")
            .email("email_1@email.com")
            .build();
        User userFound = User.builder()
            .id(1L)
            .firstName("firstName_1")
            .lastName("lastName_1")
            .email("email_1@email.com")
            .build();

        when(userRepository.findById(userDtoToUpdate.getId())).thenReturn(Optional.of(userFound));
        doNothing().when(userRepository).deleteById(userDtoToUpdate.getId());

        MvcResult result = mockMvc.perform(delete("/api/user/users")
                .param("id", String.valueOf(1L)))
            .andExpect(status().isGone())
            .andReturn();
        assertEquals(HttpStatus.GONE.value(), result.getResponse().getStatus(), "Delete error");
        verify(userRepository).findById(userDtoToUpdate.getId());
        verify(userRepository).deleteById(userDtoToUpdate.getId());

    }

    @Test
    void deleteUser_Throws_BusinessResourceException() throws Exception {

        UserDTO userDtoToUpdate = UserDTO.builder()
            .id(1L)
            .firstName("New firstName_1")
            .lastName("New lastName_1")
            .email("email_1@email.com")
            .build();

        when(userRepository.findById(userDtoToUpdate.getId())).thenReturn(Optional.empty());
        doNothing().when(userRepository).deleteById(userDtoToUpdate.getId());

        MvcResult result = mockMvc.perform(delete("/api/user/users")
                .param("id", String.valueOf(1L)))
            .andExpect(status().isNotFound())
            .andReturn();

        verify(userRepository).findById(userDtoToUpdate.getId());

        BusinessResourceExceptionDTO thrown = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(thrown);
        assertEquals("User not found with id: " + userDtoToUpdate.getId(), thrown.getErrorMessage());
        assertEquals("UserNotFound", thrown.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), thrown.getStatus().value());

    }

    @Test
    public void findByFirstNameOrLastName() throws Exception {

        UserDTO userDto = UserDTO.builder()
            .firstName("firstName_1")
            .lastName("lastName_2")
            .build();

        User userFound_1 = User.builder()
            .id(1L)
            .firstName("firstName_1")
            .lastName("lastName_1")
            .email("email_1@email.com")
            .build();
        User userFound_2 = User.builder()
            .id(1L)
            .firstName("firstName_2")
            .lastName("lastName_2")
            .email("email_2@email.com")
            .build();

        UserDTO userFoundDTO_1 = UserDTO.builder()
            .id(1L)
            .firstName("firstName_1")
            .lastName("lastName_1")
            .email("email_1@email.com")
            .build();
        UserDTO userFoundDTO_2 = UserDTO.builder()
            .id(1L)
            .firstName("firstName_2")
            .lastName("lastName_2")
            .email("email_2@email.com")
            .build();
        Set<User> users = Set.of(userFound_1, userFound_2);
        Set<UserDTO> userDtoFounds = Set.of(userFoundDTO_1, userFoundDTO_2);
        when(userRepository.findByFirstNameIgnoreCaseOrLastNameIgnoreCase(userDto.getFirstName(), userDto.getLastName())).thenReturn(users);
        when(toDtoMapper.toDtoMapper(userFound_1)).thenReturn(userFoundDTO_1);
        when(toDtoMapper.toDtoMapper(userFound_2)).thenReturn(userFoundDTO_2);

        String jsonContent = objectMapper.writeValueAsString(userDto);
        MvcResult result = mockMvc
            .perform(post("/api/user/users/firstname/lastname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
            .andExpect(status().isFound()).andReturn();

        assertEquals(HttpStatus.FOUND.value(), result.getResponse().getStatus(), "Search error");
        verify(userRepository).findByFirstNameIgnoreCaseOrLastNameIgnoreCase(userDto.getFirstName(), userDto.getLastName());
        verify(toDtoMapper).toDtoMapper(userFound_1);
        verify(toDtoMapper).toDtoMapper(userFound_2);

        Set<UserDTO> userDTOs = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(userDTOs);
        assertEquals(Long.valueOf(2), userDTOs.size());
        assertTrue(userDTOs.containsAll(userDtoFounds));
    }

    @Test
    public void findByFirstNameOrLastName_with_InvalidParam() throws Exception {

        UserDTO userDto = UserDTO.builder()
            .firstName("")
            .lastName("")
            .build();

        String jsonContent = objectMapper.writeValueAsString(userDto);
        MvcResult result = mockMvc
            .perform(post("/api/user/users/firstname/lastname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
            .andExpect(status().isBadRequest()).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Search error");
        BusinessResourceExceptionDTO thrown = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(thrown);
        assertEquals("Both User firstName and User lastName cannot be null", thrown.getErrorMessage());
        assertEquals("InvalidParam", thrown.getErrorCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), thrown.getStatus().value());
    }

    @Test
    public void findByFirstNameOrLastName_return_empty_result() throws Exception {

        UserDTO userDto = UserDTO.builder()
            .firstName("firstName_1")
            .lastName("lastName_2")
            .build();

        when(userRepository.findByFirstNameIgnoreCaseOrLastNameIgnoreCase(userDto.getFirstName(), userDto.getLastName())).thenReturn(Set.of());

        String jsonContent = objectMapper.writeValueAsString(userDto);
        MvcResult result = mockMvc
            .perform(post("/api/user/users/firstname/lastname")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
            .andExpect(status().isNotFound()).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Search error");
        verify(userRepository).findByFirstNameIgnoreCaseOrLastNameIgnoreCase(userDto.getFirstName(), userDto.getLastName());

        BusinessResourceExceptionDTO thrown = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(thrown);
        assertEquals("User not found with firstName: " + userDto.getFirstName() + " or " + userDto.getLastName(), thrown.getErrorMessage());
        assertEquals("UserNotFound", thrown.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), thrown.getStatus().value());
    }
}
