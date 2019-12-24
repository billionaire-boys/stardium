package com.bb.stardium.learn.relationtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChildRepositoryTest {
    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private ElementRespository elementRespository;

    @Test
    void name() {
        Element element = new Element("부모");
        Child child = new Child("자식");
        child.add(element);
        childRepository.save(child);

        childRepository.flush();

        List<Child> result = childRepository.findByElements(element);

        Child child1 = result.get(0);
        List<Element> elements = child1.getElements();
        assertThat(elements.size()).isEqualTo(1);
    }
}