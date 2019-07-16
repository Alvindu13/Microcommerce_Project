package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.exceptions.ProductFreeException;
import com.ecommerce.microcommerce.exceptions.ProductNotFoundException;
import com.ecommerce.microcommerce.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    @Autowired
    ProductDao productDao;

    //Récupérer la liste des produits
    @RequestMapping(value="/Produits", method=RequestMethod.GET)
    public List<Product> listeProduits() {
        return productDao.findAll();
    }

    //Récupérer un produit par son Id
    @GetMapping(value="/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) throws ProductNotFoundException {

       Product product = productDao.findById(id);

       if(product == null) throw new ProductNotFoundException("Le produit avec l'id " + id + " n'existe pas");

       return product;

    }

    //ajouter un produit
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

        Product productAdded =  productDao.save(product);

        if(product.getPrix() == 0) throw new ProductFreeException("Le prix de vente ne peut pas être nul");


        if (productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    //Calcule la marge d'un produit
    @GetMapping(value= "/Produits/Admin")
    public Map<String, Integer> calculerMargeProduit(){

        int marge;
        List<Product> products = productDao.findAll();

        Map<String, Integer> mapProductMarge = new HashMap<>();

        for (Product product : products) {
            marge = product.getPrix() - product.getPrixAchat();
            mapProductMarge.put(product.toStringNoOriginalP(), marge);
        }

        return mapProductMarge;
    }


    //Trie les produits par ordre alphabétique
    @GetMapping(value="/Produits/trier")
    public List<Product> trierProduitsParOrdreAlphabetique(){
        return productDao.findAllByOrderByNom();
    }





}
