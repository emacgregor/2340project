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
            Shelter shelterItem = shelters.get(i);
            addReport(new DataElement(shelterItem.getName(), shelterItem.getAddress() + "\n"
                    + shelterItem.getPhoneNumber(), shelterItem.getLocation(),
                    shelterItem.getRestrictions()));
        }
    }

    void addReport(DataElement de) {
        theData.add(de);
    }


    List<DataElement> getData() { return theData; }



}
