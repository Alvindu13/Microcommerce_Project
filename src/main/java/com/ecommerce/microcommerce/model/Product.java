package com.ecommerce.microcommerce.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@JsonIgnoreProperties(value = {"prixAchat", "id"})
public class Product {

    @Id
    @GeneratedValue
    private int id;

    @Length(min=3, max = 20)
    private String nom;
    private int prix;

    //à ne pas afficher
    private int prixAchat;

}
