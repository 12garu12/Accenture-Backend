package com.empresa.accenture.pedidosenlinea.app.models.service;

import com.empresa.accenture.pedidosenlinea.app.models.entity.Bill;
import com.empresa.accenture.pedidosenlinea.app.models.entity.Customer;

import java.util.Map;

public interface IBillService {

    Bill getBillById(Long id);

    Map<String, Object> findLastBill(Customer customer);

    void saveBill(Bill bill);

    Map<String, Object> updateBill(Long id, Long[] idProduct, Integer[] quantity);

    Map<String, Object> deleteBillById(Long id);

}
