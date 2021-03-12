package com.empresa.accenture.pedidosenlinea.app.models.service;

import com.empresa.accenture.pedidosenlinea.app.config.exception.BadRequestException;
import com.empresa.accenture.pedidosenlinea.app.models.dao.BillDao;
import com.empresa.accenture.pedidosenlinea.app.models.dao.CustomerDao;
import com.empresa.accenture.pedidosenlinea.app.models.dao.ProductDao;
import com.empresa.accenture.pedidosenlinea.app.models.entity.Customer;
import com.empresa.accenture.pedidosenlinea.app.models.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService{

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private BillDao billDao;

    @Autowired
    private ProductDao productDao;

    /**
     * Consulta toda lista de los clientes.
     * @return una lista de clientes.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return (List<Customer>) customerDao.findAll();
    }

    /**
     * Consulta la base de datos por la cedula y dirección del cliente.
     * @param idCard cedula del cliente.
     * @param address dirección del cliente.
     * @return el objeto cliente.
     */
    @Override
    @Transactional(readOnly = true)
    public Customer findByIdCardAndAddress(String idCard, String address) {
        Customer customer = customerDao.findByIdCardAndAddress(idCard, address);
        if (customer == null){
            throw new BadRequestException("El cliente con el numero de cedula" + idCard + " no existe en la base de datos");
        }
        return customer;
    }

    /**
     * Consulta la base de datos por el id del cliente.
     * @param id identificacion del cliente.
     * @return el objeto cliete segun su id.
     */
    @Override
    public Customer findCustomer(Long id) {
        Customer customer = customerDao.findById(id).orElse(null);
        if (customer == null){
            throw new BadRequestException("El cliente con el id " + id + " no existe en la base de datos");
        }
        return customer;
    }

    /**
     * Consulta el producto por su id.
     * @param id del producto.
     * @return el objeto product.
     */
    @Override
    @Transactional(readOnly = true)
    public Product findProductById(Long id) {
        if (!productDao.existsById(id)){
            throw new BadRequestException("El producto con el " + id + " no existe en la base de datos");
        }
        return productDao.findById(id).orElse(null);
    }


}
