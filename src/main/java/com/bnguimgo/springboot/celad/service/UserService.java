package com.bnguimgo.springboot.celad.service;

import com.bnguimgo.springboot.celad.dto.UserDTO;
import com.bnguimgo.springboot.celad.exception.BusinessResourceException;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<UserDTO> getAllUsers();

    Set<UserDTO> findByFirstNameOrLastName(String firstName, String lastName);

    UserDTO createUser(UserDTO userDTO) throws BusinessResourceException;

    UserDTO updateUser(UserDTO userDTO) throws BusinessResourceException;

    void deleteUser(Long id) throws BusinessResourceException;
}