package com.example.springboot.grocerylist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "grocery")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Grocery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_quantity")
    private String productQuantity;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Grocery(String productName, String productQuantity, String note) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.note = note;
    }
}
