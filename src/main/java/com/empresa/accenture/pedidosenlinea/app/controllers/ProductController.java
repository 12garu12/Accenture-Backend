package com.empresa.accenture.pedidosenlinea.app.controllers;

import com.empresa.accenture.pedidosenlinea.app.models.entity.Product;
import com.empresa.accenture.pedidosenlinea.app.models.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {""}) //permisos de consulta
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/products")
    public List<Product> uploadProducts(){
        return productService.findAllProducts();
    }

}
