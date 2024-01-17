package com.bnguimgo.springboot.celad.controller;

import com.bnguimgo.springboot.celad.dto.UserDTO;
import com.bnguimgo.springboot.celad.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

//@CrossOrigin(origins = "*", maxAge = 3600)
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600) //for an external project listened on port 8081
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(value = "/users", consumes = "application/json", produces = "application/json")
    @Transactional
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {

        UserDTO userDtoCreated = userService.createUser(userDTO);
        return new ResponseEntity<>(userDtoCreated, HttpStatus.CREATED);
    }

    @PutMapping(value = "/users", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO userDtoUpdated = userService.updateUser(userDTO);
        return new ResponseEntity<>(userDtoUpdated, HttpStatus.OK);
    }

    @DeleteMapping(value = "/users")
    public ResponseEntity<Void> deleteUser(@RequestParam(value = "id") Long id) {

        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.GONE);

    }

    @PostMapping(value = "/users/firstname/lastname", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Set<UserDTO>> findByFirstNameOrLastName(@RequestBody UserDTO userDTO) {
        Set<UserDTO> userFound = userService.findByFirstNameOrLastName(userDTO.getFirstName(), userDTO.getLastName());
        return new ResponseEntity<>(userFound, HttpStatus.FOUND);
    }

}
