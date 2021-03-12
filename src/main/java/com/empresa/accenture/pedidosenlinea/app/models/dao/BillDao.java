package com.empresa.accenture.pedidosenlinea.app.models.dao;

import com.empresa.accenture.pedidosenlinea.app.models.entity.Bill;
import com.empresa.accenture.pedidosenlinea.app.models.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BillDao extends CrudRepository<Bill, Long> {

    List<Bill> findByCustomer(Customer customer);

    /**
     * Consulta en la base de datas si exite la llave foranea en relacion
     * con la tabla customer.
     * @param customer objeto de la tabla customer.
     * @return boolean si exite dicha llave foranea.
     */
    boolean existsBillByCustomer(Customer customer);

    /**
     * Consulta nativa de mysql.
     * @param id llave primaria de la tabla customer en la base de datos.
     * @return un registro de la tabla bills.
     */
    @Query(value = "SELECT * FROM bills AS b ORDER BY b.customer_id DESC LIMIT 1", nativeQuery = true)
    Bill findByCustomerId(Long id);


}
