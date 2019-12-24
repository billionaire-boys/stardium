package com.bb.stardium.bench.domain.relationtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class RelationTest {

    @Autowired
    private TestEntityManager tm;

    @Test
    void name() {
        Element element = new Element("부모");
        Child child = new Child("자식");
        child.add(element);
        tm.persist(child);
    }
}
