package com.bb.stardium.bench.domain.relationtest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Element> elements = new ArrayList<>();

    public Child(String name, List<Element> elements) {
        this.name = name;
        this.elements = elements;
    }

    public Child(String name) {
        this.name = name;
    }

    public void add(Element element) {
        elements.add(element);
    }
}
