package com.bnguimgo.springboot.celad.repository;

import com.bnguimgo.springboot.celad.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//Don't replace the application default DataSource.
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"test"})//set this if exist
public class UserRepositoryTest {

/*    @Autowired
    private TestEntityManager entityManager;*/

    @Autowired
    private UserRepository userRepository;

    @Test
    public void getAllUsers() {

        List<User> users = userRepository.findAll();
        assertEquals(3, users.size());//on a 3 Users dans le fichier d'initialisation xxx.sql
    }

    @Test
    public void findByFirstNameOrLastName() {

        Set<User> users = userRepository.findByFirstNameIgnoreCaseOrLastNameIgnoreCase("BERTRAND", "NGUIMGO");
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("BERTRAND", users.iterator().next().getFirstName());
        assertEquals("NGUIMGO", users.iterator().next().getLastName());

    }

    @Test
    public void createUser() {
        User newUser = User.builder()
            .firstName("newUser_firstName")
            .lastName("newUser_lastName")
            .email("newUser_email@email.com")
            .build();
        User userSaved = userRepository.save(newUser);
        assertNotNull(userSaved.getId());
        assertEquals(4L, userSaved.getId());//five registrations in database
        assertEquals("newUser_firstName", userSaved.getFirstName());
    }

    @Test
    public void testUpdateUser() {//
        User userToUpdate = User.builder()
            .id(1L)//required for update
            .firstName("BERTRAND_Updated")
            .lastName("NGUIMGO")
            .email("bnguimgo@yahoo.fr")
            .build();
        User userUpdated = userRepository.save(userToUpdate);
        assertNotNull(userUpdated);
        assertEquals(1L, userUpdated.getId());
        assertEquals("BERTRAND_Updated", userUpdated.getFirstName());//test new firstName
        assertEquals("NGUIMGO", userUpdated.getLastName());
        assertEquals("bnguimgo@yahoo.fr", userUpdated.getEmail()); //see data.sql file init
    }

    @Test
    public void testDeleteUser() {
        userRepository.deleteById(1L);
        Optional<User> userFromDB = userRepository.findById(1L);
        assertEquals(Optional.empty(), Optional.of(userFromDB).get());
    }

}
