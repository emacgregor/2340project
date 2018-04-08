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

@SuppressWarnings("UtilityClass")
public final class DataServiceFacade {
    //private static final DataServiceFacade INSTANCE = new DataServiceFacade();

// --Commented out by Inspection START (4/8/2018 15:18):
//    /**
//     * Returns the single static instance variable of class DataServiceFacade
//     * @return The static instance variable.
//     */
//    public static DataServiceFacade getInstance() { return INSTANCE; }
// --Commented out by Inspection STOP (4/8/2018 15:18)

    private static final DataManager theData = new DataManager(ShelterDatabase.getShelters());

    //private DataElement theLastAddedElement;


    private DataServiceFacade() {

    }

    /**
     * get a list of all the data
     * @return  the full list of data
     */
    public static List<DataElement> getData() { return theData.getData();}

    /*
      Add a new data element to the model
      @param name   the name of the element
     * @param desc   textual description of the element
     * @param loc    location of the element
     * @param res The people restrictions this data element will have.
     */
    /*
    public void addDataElement(String name, String desc, Location loc, Restrictions res) {
        DataElement de = new DataElement(name, desc, loc, res);
        theData.addReport(de);
        theLastAddedElement = de;
    }*/

    /*
      Return the last element added.  This method is mainly to support UI
      @return the last element added to the model.
     */
    /*
    public DataElement getLastElementAdded() {
        return theLastAddedElement;
    }
    */
}
