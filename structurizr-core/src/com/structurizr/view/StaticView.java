package com.structurizr.view;

import com.structurizr.model.*;

import java.util.HashSet;
import java.util.Set;

public abstract class StaticView extends View {

    StaticView() {
    }

    StaticView(SoftwareSystem softwareSystem, String description) {
        super(softwareSystem, description);
    }

    /**
     * Adds all software systems in the model to this view.
     */
    public void addAllSoftwareSystems() {
        getModel().getSoftwareSystems().forEach(this::add);
    }

    /**
     * Adds the given software system to this view.
     *
     * @param softwareSystem        the SoftwareSystem to add
     * @deprecated use {@link StaticView#add(com.structurizr.model.SoftwareSystem)}
     */
    @Deprecated
    public void addSoftwareSystem(SoftwareSystem softwareSystem) {
        addElement(softwareSystem, true);
    }

    /**
     * Adds the given software system to this view.
     *
     * @param softwareSystem        the SoftwareSystem to add
     */
    public void add(SoftwareSystem softwareSystem) {
        addElement(softwareSystem, true);
    }

    /**
     * Removes the given software system from this view.
     *
     * @param softwareSystem        the SoftwareSystem to remove
     */
    public void remove(SoftwareSystem softwareSystem) {
        removeElement(softwareSystem);
    }

    /**
     * Adds all people in the model to this view.
     */
    public void addAllPeople() {
        getModel().getPeople().forEach(this::add);
    }

    /**
     * Adds the given person to this view.
     *
     * @param person        the Person to add
     * @deprecated use {@link StaticView#add(com.structurizr.model.Person)}
     */
    public void addPerson(Person person) {
        addElement(person, true);
    }

    /**
     * Adds the given person to this view.
     *
     * @param person        the Person to add
     */
    public void add(Person person) {
        addElement(person, true);
    }

    /**
     * Removes the given person from this view.
     *
     * @param person        the Person to add
     */
    public void remove(Person person) {
        removeElement(person);
    }

    public abstract void addAllElements();

    public abstract void addNearestNeighbours(Element element);

    protected <T extends Element> void addNearestNeighbours(Element element, Class<T> typeOfElement) {
        if (element == null) {
            return;
        }

        addElement(element, true);

        Set<Relationship> relationships = getModel().getRelationships();
        relationships.stream().filter(r -> r.getSource().equals(element) && typeOfElement.isInstance(r.getDestination()))
                .map(Relationship::getDestination)
                .forEach(d -> addElement(d, true));

        relationships.stream().filter(r -> r.getDestination().equals(element) && typeOfElement.isInstance(r.getSource()))
                .map(Relationship::getSource)
                .forEach(s -> addElement(s, true));
    }

    /**
     * Removes all elements that cannot be reached by traversing the graph of relationships
     * starting with the specified element.
     *
     * @param element       the starting element
     */
    public void removeElementsThatCantBeReachedFrom(Element element) {
        if (element != null) {
            Set<Element> elementsToShow = new HashSet<>();
            Set<Element> elementsVisited = new HashSet<>();
            findElementsToShow(element, element, elementsToShow, elementsVisited);

            for (ElementView elementView : getElements()) {
                if (!elementsToShow.contains(elementView.getElement())) {
                    removeElement(elementView.getElement());
                }
            }
        }
    }

    private void findElementsToShow(Element startingElement, Element element, Set<Element> elementsToShow, Set<Element> elementsVisited) {
        if (!elementsVisited.contains(element) && getElements().contains(new ElementView(element))) {
            elementsVisited.add(element);
            elementsToShow.add(element);

            // check that we've not gone back to the starting point of the graph
            if (!element.hasEfferentRelationshipWith(startingElement)) {
                element.getRelationships().forEach(r -> findElementsToShow(startingElement, r.getDestination(), elementsToShow, elementsVisited));
            }
        }
    }

}