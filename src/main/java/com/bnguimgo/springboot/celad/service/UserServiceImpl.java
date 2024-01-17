package com.bnguimgo.springboot.celad.service;

import com.bnguimgo.springboot.celad.dto.UserDTO;
import com.bnguimgo.springboot.celad.exception.BusinessResourceException;
import com.bnguimgo.springboot.celad.mapper.DomainToDtoMapper;
import com.bnguimgo.springboot.celad.mapper.DtoToDomainMapper;
import com.bnguimgo.springboot.celad.model.User;
import com.bnguimgo.springboot.celad.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    private DomainToDtoMapper toDtoMapper;
    @Autowired
    private DtoToDomainMapper toDomainMapper;

    public UserServiceImpl() {
        super();
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(user -> toDtoMapper.toDtoMapper(user)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) throws BusinessResourceException {
        try {
            User user = userRepository.save(toDomainMapper.toDomainModel(userDTO));
            return toDtoMapper.toDtoMapper(user);

        } catch (DataIntegrityViolationException ex) {
            log.error("User already exist", ex);
            throw new BusinessResourceException("DataIntegrityViolationException", ex.getMessage() + " for " + userDTO.getEmail(), HttpStatus.CONFLICT);
        }
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO userDTO) throws BusinessResourceException {
        try {
            checkExistingUser(userDTO.getId());
            User user = userRepository.save(toDomainMapper.toDomainModel(userDTO));
            return toDtoMapper.toDtoMapper(user);

        } catch (DataIntegrityViolationException ex) {
            log.error("User already exist", ex);
            throw new BusinessResourceException("DataIntegrityViolationException", ex.getMessage() + " for " + userDTO.getEmail(), HttpStatus.CONFLICT);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) throws BusinessResourceException {

        checkExistingUser(id);
        userRepository.deleteById(id);
    }

    @Override
    public Set<UserDTO> findByFirstNameOrLastName(String firstName, String lastName) {
        if (ObjectUtils.isEmpty(firstName) && ObjectUtils.isEmpty(lastName)) {
            log.error("User firstName and lastName are empties");
            throw new BusinessResourceException("InvalidParam", "Both User firstName and User lastName cannot be null", HttpStatus.BAD_REQUEST);
        }
        Set<User> users = userRepository.findByFirstNameIgnoreCaseOrLastNameIgnoreCase(firstName, lastName);
        if (CollectionUtils.isEmpty(users)) {
            log.error("User not found with firstName {} or with lastName {}", firstName, lastName);
            throw new BusinessResourceException("UserNotFound", "User not found with firstName: " + firstName + " or " + lastName, HttpStatus.NOT_FOUND);
        }

        return users.stream()
            .map(user -> toDtoMapper.toDtoMapper(user)).collect(Collectors.toSet());
    }

    private void checkExistingUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> {
            log.error("User not found with id {}", id);
            throw new BusinessResourceException("UserNotFound", "User not found with id: " + id, HttpStatus.NOT_FOUND);
        });
    }

}
