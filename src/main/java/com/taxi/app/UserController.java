package com.taxi.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users") //The path
public class UserController {
    List<User> users = new ArrayList<User>();

    public UserController(){
        users.add(new User(1, "John", "Smith"));
        users.add(new User(2, "Jerome", "Tuck"));
        users.add(new User(3, "Ruth", "Langmore"));
    }

    // GET to http://localhost:8080/users
    @GetMapping
    public List<User> getUsers() {
        return users;
    }

    @GetMapping(value="/{id}")
    public ResponseEntity getUsersById(@PathVariable("id") long id)
    {
        User userFound = null;

        for(User user: users)
        {
            if(user.getId() == id)
            {
                userFound = user;
                break;
            }
        }

        if (userFound == null)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(userFound, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity add(@RequestBody User u)
    {
        users.add(u);
        return new ResponseEntity(u, HttpStatus.CREATED);
    }

    @PutMapping
    //Reminder PATCH is better with DB, PUT is better for testing.
    //TODO Updating is not working
    public ResponseEntity addOrUpdate(@RequestBody User u)
    {
        ResponseEntity response;

        if(users.contains(u))
        {
            //update
            int index = users.indexOf(u);
            users.set(index, u);
            response = new ResponseEntity(u, HttpStatus.OK);
        }
        else
        {
            users.add(u);
            response = new ResponseEntity(u, HttpStatus.CREATED);
        }

        return response;
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity delete(@PathVariable("id") long id)
    {
        boolean found = false;

        for (User user: users)
        {
            if(user.getId() == id)
            {
                users.remove(user);
                found = true;
                break;
            }
        }

        if (found == false)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


}
