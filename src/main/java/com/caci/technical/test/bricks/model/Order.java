package com.caci.technical.test.bricks.model;

import javax.persistence.*;

@Entity
@Table(name = "ORDER_DETAILS" )
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_REF")
    private Long orderRef;

    @Column(name= "BRICKS_PURCHASED")
    private int numberOfBricks;


    public int getNumberOfBricks() {
        return numberOfBricks;
    }

    public Long getOrderRef() {
        return orderRef;
    }


    public Order() {
    }

    public Order(Long orderRef, int numberOfBricks) {
        this.orderRef = orderRef;
        this.numberOfBricks = numberOfBricks;
    }



}
