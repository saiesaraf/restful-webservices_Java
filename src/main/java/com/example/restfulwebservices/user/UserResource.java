package com.example.restfulwebservices.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserResource {
    //retrieve all users
    @Autowired
    private UserDaoService service;

    @GetMapping("/users")
    public List<User> retriveUser(){
        return service.findAll();
    }

    //retrieveUser(int id)
    @GetMapping("/users/{id}")
    public EntityModel<User> retriveUser(@PathVariable int id) {
        User user = service.findOne(id);
        if(user==null){
            throw new UserNotFoundException("id-" + id);
        }
        EntityModel<User> resource = EntityModel.of(user);

        WebMvcLinkBuilder linkTo =
                linkTo(methodOn(this.getClass()).retriveUser());

        resource.add(linkTo.withRel("all-users"));

        //HATEOAS

        return resource;
        //"all users", SERVER_PATH +"/users"
        //retriveUsers

    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
        User savedUser = service.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
       return ResponseEntity.created(location).build();

    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);
        if (user == null)
            throw new UserNotFoundException("id-" + id);

    }

}
