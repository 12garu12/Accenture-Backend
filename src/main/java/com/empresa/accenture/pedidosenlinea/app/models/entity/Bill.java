package com.empresa.accenture.pedidosenlinea.app.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills")
public class Bill implements Serializable {

    public static final Double HOME_DELIVERY = 15000.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Customer customer;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_id")
    @JsonIgnore
    private List<BillItems> items;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime createAt;

    private Double homeDelivery;

/*  Constructor ***********************************************************************************************/

    public Bill(){
        items = new ArrayList<>();
    }


/*  Getters and Setters ***********************************************************************************************/


    @PrePersist
    public void prePersist(){
        this.createAt = LocalDateTime.now();
        this.homeDelivery = homeDelivery();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<BillItems> getItems() {
        return items;
    }

    public void setItems(List<BillItems> items) {
        this.items = items;
    }

    public Double getHomeDelivery() {
        return homeDelivery;
    }

    public void setHomeDelivery(Double homeDelivery) {
        this.homeDelivery = homeDelivery;
    }

    public void addBillItem(BillItems item){
        this.items.add(item);
    }

    /**
     * Calcuala el precio total de la factura si la compra es mayor a 70000 pesos
     * entonces el sistema generara la factura con el iva.
     * @return
     */
    public Double getTotal(){
        Double total = 0.0;
        boolean flag = true;
        for (int i = 0; i < items.size(); i++) {
            if (flag == true) {
                if (total <= 70000) {
                        total += items.get(i).getQuantity() * items.get(i).getProduct().getPrice();
                    if(total > 70000){
                        flag = false;
                        i = -1;
                        total = 0.0;
                        continue;
                    }
                    if (i == items.size() - 1 && total <= 70000) {
                        return total;
                    }
                }
            }else {
                total += items.get(i).calculateSale();
            }
        }
        return total;
    }

    public Double homeDelivery(){
        Double total = getTotal();
        Double result = total > 70000.0 && total < 100000.0 ? HOME_DELIVERY : 0.0;
        return result;
    }

    private static final Long SerialVersionUID = 1L;
}

/*
LocalDateTime initialDate = LocalDateTime.of(2021, Month.MARCH, 4, 03, 30);
        System.out.println(initialDate);

        LocalDateTime finalDate = LocalDateTime.of(2021, Month.MARCH, 5, 06, 30);
        System.out.println(finalDate);

        Float workHours = ChronoUnit.SECONDS.between(initialDate, finalDate) / 3600.0f;
        System.out.println(workHours);
*/