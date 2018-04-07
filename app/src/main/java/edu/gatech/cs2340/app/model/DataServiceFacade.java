package edu.gatech.cs2340.app.model;

import java.util.List;

/**
 * Heavily borrowed from Professor Waters's example.
 *
 * This is a Controller for UI elements that need access to the data
 *
 * Implements the Facade and Singleton patterns.
 *
 * Primarily interacts the DataManager model class
 */

public final class DataServiceFacade {
    private static final DataServiceFacade INSTANCE = new DataServiceFacade();

    /**
     * Returns the single static instance variable of class DataServiceFacade
     * @return The static instance variable.
     */
    public static DataServiceFacade getInstance() { return INSTANCE; }

    private final DataManager theData = new DataManager(Model.getInstance().getShelters());

    private DataElement theLastAddedElement;


    private DataServiceFacade() {

    }

    /**
     * get a list of all the data
     * @return  the full list of data
     */
    public List<DataElement> getData() { return theData.getData();}

    /**
     * Add a new data element to the model
     * @param name   the name of the element
     * @param desc   textual description of the element
     * @param loc    location of the element
     * @param res The people restrictions this data element will have.
     */
    public void addDataElement(String name, String desc, Location loc, Restrictions res) {
        DataElement de = new DataElement(name, desc, loc, res);
        theData.addReport(de);
        theLastAddedElement = de;
    }

    /**
     * Return the last element added.  This method is mainly to support UI
     * @return the last element added to the model.
     */
    public DataElement getLastElementAdded() {
        return theLastAddedElement;
    }
}
