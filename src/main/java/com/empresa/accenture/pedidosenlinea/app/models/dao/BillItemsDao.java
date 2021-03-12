package com.empresa.accenture.pedidosenlinea.app.models.dao;

import com.empresa.accenture.pedidosenlinea.app.models.entity.BillItems;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BillItemsDao extends CrudRepository<BillItems, Long> {

    @Query(value = "select * from bills_items where bill_id = ?1", nativeQuery = true)
    List<BillItems> findByBillId(Long id);

}
