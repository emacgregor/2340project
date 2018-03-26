package edu.gatech.cs2340.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertwaters on 3/13/18.
 *
 * Structurer
 *
 * primary responsibility is to manage a group of DataElements
 */

class DataManager {
    List<DataElement> theData;

    DataManager(ArrayList<Shelter> shelters) {
        theData = new ArrayList<>();
        makeSomeData(shelters);
    }

    private void makeSomeData(ArrayList<Shelter> shelters) {
        for (int i = 0; i < shelters.size(); i++) {
            addReport(new DataElement(shelters.get(i).getName(), shelters.get(i).getAddress(), shelters.get(i).getLocation()));
        }
            /*addReport(new DataElement("Coke Zero", "Sam's Deli", new Location(33.749, -84.388)));
            addReport(new DataElement("Pepsi", "Grandma Garage", new Location(33.8, -84.5)));*/
    }

    void addReport(DataElement de) {
        theData.add(de);
    }


    List<DataElement> getData() { return theData; }



}
