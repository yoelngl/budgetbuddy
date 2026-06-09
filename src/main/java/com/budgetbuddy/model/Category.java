package com.budgetbuddy.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "categories")
public class Category {

    /** Static collection — seeded once at application startup via DataInitializer */
    public static final List<String[]> DEFAULT_CATEGORIES = Collections.unmodifiableList(
        Arrays.asList(
            new String[]{"Makanan",       "Pengeluaran untuk makanan dan minuman"},
            new String[]{"Transportasi",  "Pengeluaran untuk transportasi dan BBM"},
            new String[]{"Hiburan",       "Pengeluaran untuk hiburan dan rekreasi"},
            new String[]{"Kesehatan",     "Pengeluaran untuk kesehatan dan obat"},
            new String[]{"Pendidikan",    "Pengeluaran untuk pendidikan dan kursus"},
            new String[]{"Lainnya",       "Pengeluaran lainnya"}
        )
    );

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama kategori tidak boleh kosong")
    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    public Category() {}

    public Category(String name, String description) {
        this.name        = name;
        this.description = description;
    }

    public Long   getId()                     { return id; }
    public void   setId(Long id)              { this.id = id; }

    public String getName()                   { return name; }
    public void   setName(String name)        { this.name = name; }

    public String getDescription()            { return description; }
    public void   setDescription(String desc) { this.description = desc; }

    @Override
    public String toString() {
        return "Category{id=" + id + ", name=" + name + "}";
    }
}
