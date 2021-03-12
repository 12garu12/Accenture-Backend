package com.empresa.accenture.pedidosenlinea.app.models.service;

import com.empresa.accenture.pedidosenlinea.app.config.exception.BadRequestException;
import com.empresa.accenture.pedidosenlinea.app.config.exception.NotFoundException;
import com.empresa.accenture.pedidosenlinea.app.models.dao.BillDao;
import com.empresa.accenture.pedidosenlinea.app.models.dao.BillItemsDao;
import com.empresa.accenture.pedidosenlinea.app.models.dao.ProductDao;
import com.empresa.accenture.pedidosenlinea.app.models.entity.Bill;
import com.empresa.accenture.pedidosenlinea.app.models.entity.BillItems;
import com.empresa.accenture.pedidosenlinea.app.models.entity.Customer;
import com.empresa.accenture.pedidosenlinea.app.models.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class BillServiceImpl implements IBillService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private BillDao billDao;

    @Autowired
    private BillItemsDao billItemsDao;

    @Autowired
    private ProductDao productDao;

    /**
     * Consulta la factura por su id.
     * @param id llave primaria en la base de datos.
     * @return el objeto factura.
     */
    @Override
    public Bill getBillById(Long id) {
        return billDao.findById(id).orElse(null);
    }

    /**
     * Consulta y genera la ultima factura del cliente en la base de datos.
     * @param customer objeto customer para la busqueda en la base de datos
     * @return El valor actual de la factura con todos sus productos.
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> findLastBill(Customer customer) {
        Long customerId = customer.getId();

        if (!billDao.existsBillByCustomer(customer)) {
            throw new NotFoundException("No hay facturas relacionadas a dicho cliente!");
        }

        Bill bill = billDao.findByCustomerId(customerId);

        if (bill == null) throw new BadRequestException("La factura con el id " + bill.getId() +
                " no se encuentra en la base de datos");

        Map<String, Object> response = new HashMap<>();
        List<String> items = new ArrayList<>();

        response.put("createAt", bill.getCreateAt());
        response.put("customer", bill.getCustomer().getFirstName() + " " + bill.getCustomer().getLastName());
        response.put("idCard", bill.getCustomer().getIdCard());
        response.put("address", bill.getCustomer().getAddress());
        response.put("description", bill.getDescription());
        for (BillItems item : bill.getItems()) {
            items.add("producto: " + item.getProduct().getProductName());
            items.add("Cantidad: " + item.getQuantity());
            items.add("price: " + item.getProduct().getPrice());
            items.add("iva: " + item.getProduct().getVAT());
        }
        response.put("items", items);
        response.put("homeDelivery", bill.homeDelivery());
        response.put("total", bill.getTotal());
        response.put("fullTotal", bill.homeDelivery() + bill.getTotal());

        return response;
    }

    /**
     * Guarda la factura en la base de datos.
     *
     * @param bill objeto a guardar en la base de datos.
     */
    @Override
    @Transactional
    public void saveBill(Bill bill) {
        try {
            billDao.save(bill);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage() + e.getMostSpecificCause().getMessage(), e);
        }
    }

    /**
     * Actualiza la factura del cliente antes de las primeras 5 horas de
     * haber hecho el pedido si lod productos seleccionados custan igual o mas
     * que los anteriores.
     * @param id de la factura.
     * @param idProduct lista de id de los productos
     * @param quantity  lista de cantidades de los productos.
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> updateBill(Long id, Long[] idProduct, Integer[] quantity) {
        boolean updated = false;
        Bill oldBill = billDao.findById(id).get();

        Float hours = hoursDifference(oldBill);

        Map<String, Object> response = new HashMap<>();

        // Valida que la factura del cliente no ha transcurrido mas de 5 horas para poder editarla.
        if (hours > 5f) {
            response.put("updated", updated);
            response.put( "message","La factura no puede ser editada despues de 5 horas!");
            response.put("bill", oldBill);
            return response;
        }

        Bill newBill = new Bill(); // con una instancia de la nueva factura se podra calcular el valor total de la factura con los respectivos ivas.
        newBill.setId(oldBill.getId());
        newBill.setDescription(oldBill.getDescription());
        newBill.setCustomer(oldBill.getCustomer());

        List<BillItems> newItems = new ArrayList<>();

        for (int i = 0; i < idProduct.length; i++) {

            Product product = productDao.findById(idProduct[i]).orElse(null);

            BillItems row = new BillItems();

            row.setProduct(product);
            row.setQuantity(quantity[i]);

            newItems.add(row);
        }

        newBill.setItems(newItems);


        // Si el total de la factura antigua es mayor al total de la nueva factura no se podra editar la factura
        if (oldBill.getTotal() > newBill.getTotal()) {
            response.put("updated", updated);
            response.put( "message","La factura no puede ser editada el valor es menor al valor de la factura actual!");
            response.put("bill", oldBill);
            return response;
        }

        List<BillItems> oldItems = oldBill.getItems();


        List<BillItems> itemsBill = new ArrayList<>();

        List<BillItems> itemsOld = new ArrayList<>();
        List<BillItems> itemsNew = new ArrayList<>();

        // Compara cuantos items de la factura anterior hay en los nuevos items y verifica las cantidades y las renueva.
        for (int i = 0; i < oldItems.size(); i++) {

            for (int j = 0; j < newItems.size(); j++) {

                if (oldItems.get(i).getProduct().getId().equals(newItems.get(j).getProduct().getId())) {

                    BillItems item = oldItems.get(i).getQuantity() >= newItems.get(j).getQuantity() ?
                            oldItems.get(i) : newItems.get(j); // se le asigna el valor del elemento con mas cantidad.
                    itemsBill.add(item);
                    itemsOld.add(oldItems.get(i));
                    itemsNew.add(newItems.get(j));
                }
            }
        }

        oldItems.removeAll(itemsOld);
        newItems.removeAll(itemsNew);


        // Se saca el valor minimo de la lista de items de la vieja factura.
        Double minPrice = 0.0;
        if (oldItems.size() > 0) {
            minPrice = oldItems.stream()
                    .min((x, y) -> x.getProduct().getPrice() < y.getProduct().getPrice() ? -1 : 1)
                    .get().getProduct().getPrice();
        }

        //Se comparan los precios de los nuevos productos con el valor minimo de los productos a eliminar
        for (int j = 0; j < newItems.size(); j++) {
            if (newItems.get(j).getProduct().getPrice() > minPrice) {
                itemsBill.add(newItems.get(j));
                itemsNew.add(newItems.get(j));
            }
        }

        newItems.removeAll(itemsNew);

        if (newItems.size() > 0) {
            int cont = 0;

            for (BillItems newItem : newItems) {
                response.put("message " + cont++, "El producto " + newItem.getProduct().getProductName() +
                        " debe tener un valor mayor a " + minPrice);
            }


            response.put("updated", updated);
            response.put("bill", newBill);

            return response;
        }


        saveBill(newBill);
        updated = true;

        response.put("updated", updated);
        response.put("bill", newBill);

        return response;
    }

    /**
     * Eliminar el pedido antes de transcurrir 12 horas, si el tiempo transcurrido
     * es mayor a 12 horas se le cobrara el 10% del valor del pedido.
     * @param id de la factura
     * @return los mensajes correspondientes a la cancelación del pedido.
     */
    @Override
    @Transactional
    public Map<String, Object> deleteBillById(Long id) {

        Bill bill = null;

        Map<String, Object> response = new HashMap<>();

        try {

            bill = billDao.findById(id).get();

            Float hours = hoursDifference(bill);

            // Pasadas las 12 horas de haber creado la factura se facturara el 10% del valor de la factura.
            if (hours > 12f){
                Double percent = (bill.getTotal() * 10) / 100;

                response.put("totalAPagar", percent.toString());
                billDao.deleteById(id);
            }else {
                billDao.deleteById(id);
            }

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar la factura en la base de datos");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(
                    e.getMostSpecificCause().getMessage()));
            return (Map<String, Object>) new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        response.put("message", "La factura se ha eliminado con éxito!");
        return response;
    }


    public Float hoursDifference(Bill bill){
        LocalDateTime date = bill.getCreateAt();
        LocalDateTime currentDate = LocalDateTime.now();

        float hours = ChronoUnit.SECONDS.between(date, currentDate) / 3600.0f;

        return hours;
    }
}