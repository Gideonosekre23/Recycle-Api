package com.Ossolutions.RecycleApi.Model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    private String Email;
    private Integer points;
    private String location;
    private String userType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();


    public User() {
        // Default values
        this.points = 0;
        this.userType = "regular";
    }

    // Parameterized constructor for convenience
    public User(String username, String password, String firstName, String lastName, String location, String email) {
        this(); // Call the default constructor to set default values
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.Email = email;
        this.location = location;
    }

    public User(String firstName, String lastName, String email, String location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.Email = email;
        this.location = location;
    }
    public String getFullName() {
        return this.firstName + " " + this.lastName;

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", Email='" + Email + '\'' +
                ", points=" + points +
                ", location='" + location + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }


}
