package com.example.lab9.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="characters")
public class Personajes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(nullable = false)
    private String name;
    private String url;
    private String identity;
    private String align;
    private String eye;
    private String hair;
    private String sex;
    private String gsm;
    private String alive;
    private int appearances;
    private String first_appearance;
    private int year;
}
