package edu.gatech.cs2340.app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data manager heavily borrowed from Professor Waters's example.
 *
 * primary responsibility is to manage a group of DataElements
 */

class DataManager {
    private final List<DataElement> theData;

    DataManager(List<Shelter> shelters) {
        theData = new ArrayList<>();
        makeSomeData(shelters);
    }

    private void makeSomeData(List<Shelter> shelters) {
        for (int i = 0; i < shelters.size(); i++) {
            Shelter shelterItem = shelters.get(i);
            addReport(shelterItem.makeDataElement());
        }
    }

    private void addReport(DataElement de) {
        theData.add(de);
    }


    List<DataElement> getData() { return Collections.unmodifiableList(theData); }



}
