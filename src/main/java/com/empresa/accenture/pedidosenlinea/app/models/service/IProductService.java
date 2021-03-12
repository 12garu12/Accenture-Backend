package com.empresa.accenture.pedidosenlinea.app.models.service;

import com.empresa.accenture.pedidosenlinea.app.models.entity.Product;

import java.util.List;

public interface IProductService {

    List<Product> findAllProducts();

}
