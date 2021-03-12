package com.empresa.accenture.pedidosenlinea.app.models.dao;

import com.empresa.accenture.pedidosenlinea.app.models.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerDao extends CrudRepository<Customer, Long> {

    Customer findByIdCardAndAddress(String idCard, String address);

}
