package com.empresa.accenture.pedidosenlinea.app.controllers;


import com.empresa.accenture.pedidosenlinea.app.models.entity.Bill;
import com.empresa.accenture.pedidosenlinea.app.models.entity.BillItems;
import com.empresa.accenture.pedidosenlinea.app.models.entity.Customer;
import com.empresa.accenture.pedidosenlinea.app.models.entity.Product;
import com.empresa.accenture.pedidosenlinea.app.models.service.IBillService;
import com.empresa.accenture.pedidosenlinea.app.models.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = {""}) //permisos de consulta
@RestController
@RequestMapping("/api")
public class BillControler {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IBillService billService;

    /**
     * Dado un cliente con la cedula de ciudadanía 12345 y con la
     * dirección carrera 11# 14-08, cuando el cliente selecciona los
     * productos a comprar con un valor de más de 70 mil pesos
     * entonces el sistema genera la factura con el iva y el valor del
     * domicilio.
     * @param idCard cedula del cliente.
     * @param address dirección del cliente.
     * @return la factura con todos sus items.
     */
    @GetMapping("/bills/{idCard}/{address}")
    public ResponseEntity<?> findBill(@PathVariable String idCard, @PathVariable String address ){

        Customer customer = customerService.findByIdCardAndAddress(idCard, address);

        Map<String, Object> bill = billService.findLastBill(customer);

        return new ResponseEntity<>(bill, HttpStatus.OK);

    }

    /**
     * Meotodo para guardar las facturas en la base de datos.
     * @param bill nueva factura a guardar en la base de datos.
     * @param idProduct Lista de ids de los productos pertenecientes a la factura.
     * @param quantity Lista de cantidades de cada producto de la factura.
     * @return el mensaje que la factura ha sido creada con éxito.
     */
    @PostMapping("/bills")
    public ResponseEntity<?> createBill(@RequestBody Bill bill,
                                        @RequestParam Long[] idProduct,
                                        @RequestParam Integer[] quantity){

        Customer customer = customerService.findCustomer(bill.getCustomer().getId());

        bill.setCustomer(customer);

        for (int i = 0; i < idProduct.length ; i++) {
            Product product = customerService.findProductById(idProduct[i]);

            BillItems row = new BillItems();

            row.setQuantity(quantity[i]);
            row.setProduct(product);

            bill.addBillItem(row);
        }

        billService.saveBill(bill);

        Map<String, Object> response = new HashMap<>();

        response.put("mensaje", "La factura se ha creado con éxito!");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Consulta la base de datos y busca la factura por su id.
     * @param id de la factura.
     * @return el objeto factura.
     */
    @GetMapping("/bills/{id}")
    public ResponseEntity<?> getBillById(@PathVariable Long id){

        Bill bill = billService.getBillById(id);

        return new ResponseEntity<>(bill, HttpStatus.OK);
    }

    /**
     * Desde el front-end llega de nuevo la lista de productos con los que
     * el cliente deja y los nuevos en este metodo se actualiza la lista
     * @param bill
     * @param idProduct
     * @param quantity
     * @return
     */
    @PutMapping("/bills")
    public ResponseEntity<?> updateBill(@RequestBody Bill bill,
                                        @RequestParam Long[] idProduct,
                                        @RequestParam Integer[] quantity){

        // actualiza la factura si es posible.
        Map<String, Object> response = billService.updateBill(bill.getId(), idProduct, quantity);

        /*
        * Dado que un cliente quiere agregar a su pedido un nuevo producto cuando el pedido
        * era por valor de 70 mil pesos y al agregar un nuevo producto el pedido pasa de 100 mil
        * pesos entonces el sistema debe restar el valor del domicilio.
        * En la clase BillService esta el metodo para calcular el valor del pedido sin importar el
        * valor de la factura.
        * */
        if (response.get("uddated").equals(true)){
            Customer customer = customerService.findCustomer(bill.getCustomer().getId());
            response = billService.findLastBill(customer);
            response.put("message", "Factura editada con exito!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Elimina los pedidos del cliente.
     * @param id de la factura
     * @return mensajes correspondientes a la eliminacion del pedido.
     */
    @DeleteMapping("/bills/{id}")
    public ResponseEntity<?> deleteBill(@PathVariable Long id){

        Map<String, Object> response = billService.deleteBillById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }




}
