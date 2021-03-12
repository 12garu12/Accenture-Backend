package com.empresa.accenture.pedidosenlinea.app.models.dao;

import com.empresa.accenture.pedidosenlinea.app.models.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductDao extends CrudRepository<Product, Long> {

}
