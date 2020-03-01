package com.atguigu.gmall.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class PmsBaseCatalog3 implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String catalog2Id;


    public PmsBaseCatalog3(Long id, String name, String catalog2Id) {
        this.id = id;
        this.name = name;
        this.catalog2Id = catalog2Id;
    }

    public PmsBaseCatalog3() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCatalog2Id() {
        return catalog2Id;
    }

    public void setCatalog2Id(String catalog2Id) {
        this.catalog2Id = catalog2Id;
    }
}