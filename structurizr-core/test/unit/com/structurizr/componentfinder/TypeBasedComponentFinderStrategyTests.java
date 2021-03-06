package com.structurizr.componentfinder;

import com.structurizr.Workspace;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TypeBasedComponentFinderStrategyTests {

    private Container webApplication;

    @Before
    public void setUp() {
        Workspace workspace = new Workspace("Name", "Description");
        Model model = workspace.getModel();

        SoftwareSystem softwareSystem = model.addSoftwareSystem("Name", "Description");
        webApplication = softwareSystem.addContainer("Name", "Description", "Technology");
    }

    @Test
    public void test_findComponents() throws Exception {
        ComponentFinder componentFinder = new ComponentFinder(
                webApplication,
                "com.structurizr.componentfinder.myapp",
                new TypeBasedComponentFinderStrategy(
                        new NameSuffixTypeMatcher("Controller", "", "An MVC controller"),
                        new NameSuffixTypeMatcher("Repository", "", "A data access object")
                )
        );
        componentFinder.findComponents();

        assertEquals(2, webApplication.getComponents().size());

        Component myController = webApplication.getComponentWithName("MyController");
        assertNotNull(myController);
        assertEquals("MyController", myController.getName());
        assertEquals("com.structurizr.componentfinder.myapp.MyController", myController.getType());
        assertEquals("", myController.getDescription());

        Component myRepository = webApplication.getComponentWithName("MyRepository");
        assertNotNull(myRepository);
        assertEquals("MyRepository", myRepository.getName());
        assertEquals("com.structurizr.componentfinder.myapp.MyRepository", myRepository.getType());
        assertEquals("", myRepository.getDescription());

        assertEquals(1, myController.getRelationships().size());
        assertNotNull(myController.getRelationships().stream().filter(r -> r.getDestination() == myRepository).findFirst().get());
    }

}
