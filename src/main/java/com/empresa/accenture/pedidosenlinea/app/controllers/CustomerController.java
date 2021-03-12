package com.empresa.accenture.pedidosenlinea.app.controllers;

import com.empresa.accenture.pedidosenlinea.app.models.entity.Customer;
import com.empresa.accenture.pedidosenlinea.app.models.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {""}) //permisos de consulta
@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping("/customer")
    public List<Customer> toList(){
        return customerService.findAll();
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> findCustomer(@PathVariable Long id){

        Customer customer = customerService.findCustomer(id);

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }



}
