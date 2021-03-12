package com.empresa.accenture.pedidosenlinea.app.models.service;

import com.empresa.accenture.pedidosenlinea.app.models.dao.ProductDao;
import com.empresa.accenture.pedidosenlinea.app.models.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> findAllProducts() {
        return (List<Product>) productDao.findAll();
    }
}
