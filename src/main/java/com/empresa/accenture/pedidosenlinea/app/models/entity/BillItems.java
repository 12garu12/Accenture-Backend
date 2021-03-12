package com.empresa.accenture.pedidosenlinea.app.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bills_items")
public class BillItems implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_Id")
    private Product product;

/*  Getters and Setters ***********************************************************************************************/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double calculateSale(){
        Double vat = Double.parseDouble(product.getVAT().substring(0, product.getVAT().length() - 1));
        vat = ((product.getPrice() * vat) / 100) * this.quantity.longValue();
        return (this.quantity.longValue() * product.getPrice()) + vat;
    }

    private static final Long SerialVersionUID = 1L;
}
