/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.RentalArrangementDTO;
import dtos.UserDTO;
import entities.RentalArrangement;
import entities.Role;
import entities.User;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author 45319
 */





public class UserFacadeTest {
    
    private static EntityManagerFactory emf;
    private static UserFacade facade; 
    
    User u1,u2,u3;
    RentalArrangement ra1,ra2,ra3;
    
    public UserFacadeTest() {}
    
    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = UserFacade.getUserFacade(emf);
    }
    
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            u2 = new User("user2", "test321", "testvej2", "testby2", "1234", 55);
            u3 = new User("user3", "test3", "testvej3", "testby3", "1234", 100);
            ra1 = new RentalArrangement("testFilm1", 50);
            ra2 = new RentalArrangement("testFilm2", 200);
            ra3 = new RentalArrangement("testFilm3", 150);
            ra3.setToDate(LocalDate.now().minusDays(1));
            u3.addArrangement(ra2);
            u3.addArrangement(ra3);
            System.out.println(ra1.toString());
        try {
            em.getTransaction().begin();
            em.createNamedQuery("RentalArrangement.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
           
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(u2);
            em.persist(u3);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }  
    }
    
    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
//        emf.close();
    }
    
    @Test
    void create() throws Exception {
        System.out.println("Testing create(UserDTO u)");
        u1 = new User("user1", "test123", "testvej1", "testby1", "4321", 100);
        UserDTO u1DTO = new UserDTO(u1);
        UserDTO expected = u1DTO;
        UserDTO actual = facade.create(u1DTO);
        assertEquals(expected.getUserName(), actual.getUserName());
    }
    /*
    @Test
    void addRentalArrangement() throws Exception {
        System.out.println("Testing addRentalArrangement(RentalArrangementDTO raDTO, UserDTO uDTO)");
        UserDTO userDTO = new UserDTO(u2);
        RentalArrangementDTO raDTO = new RentalArrangementDTO(ra1);
        
        u2.addArrangement(ra1);
        User actual = facade.addRentalArrangement(raDTO, userDTO);
        assertEquals(u2.getRentalArrangement().size(), actual.getRentalArrangement().size());
    }*/
    
    @Test
    void checkArrangementStatusIfNotOverdue() throws Exception {
        System.out.println("Testing checkArrangementStatus(RentalArrangementDTO raDTO)");
        RentalArrangementDTO raDTO = new RentalArrangementDTO(ra2);
        
        RentalArrangementDTO actual = facade.checkArrangementStatus(raDTO);
        assertEquals(ra2.isStatus(), actual.isStatus());
    }
    
    @Test
    void checkArrangementStatusIfOverdue() throws Exception {
        System.out.println("Testing checkArrangementStatus(RentalArrangementDTO raDTO)");
        ra3.setStatus(false);
        RentalArrangementDTO raDTO = new RentalArrangementDTO(ra3);
        
        RentalArrangementDTO actual = facade.checkArrangementStatus(raDTO);
        assertEquals(ra3.isStatus(), actual.isStatus());
    }
    
   /* 
    @Test
    void addRentedMovie() throws Exception {
        
        UserDTO userDTO = new UserDTO(u2);
        RentedMoviesDTO movieDTO = new RentedMoviesDTO(rm1);
        u2.addMovie(rm1);
        User actual = facade.addRentedMovies(movieDTO, userDTO);
        System.out.println(u2.getRentedMovies() + "," + actual.getRentedMovies());
        assertEquals(rm1.getTitle(), actual.getRentedMovies().get(0).getTitle());  
    }
    */

    
      /* System.out.println("Testing addRentedMovies(RentedMoviesDTO rm, UserDTO u)");
        UserDTO u1DTO = new UserDTO(u2);
        u2.addMovie(rm1);
        RentedMoviesDTO rmDTO = new RentedMoviesDTO(rm1);
        int expected = u2.getRentedMovies().size();
        User actual = facade.addRentedMovies(rmDTO, u1DTO);
        
        assertEquals(expected, actual.getRentedMovies().size());*/
    }
    
    
    

