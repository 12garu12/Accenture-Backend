package com.empresa.accenture.pedidosenlinea.app.models.service;

import com.empresa.accenture.pedidosenlinea.app.models.entity.Customer;
import com.empresa.accenture.pedidosenlinea.app.models.entity.Product;

import java.util.List;

public interface ICustomerService {

    List<Customer> findAll();

    Customer findByIdCardAndAddress(String idCard, String address);

    Customer findCustomer(Long id);

    Product findProductById(Long id);

}
