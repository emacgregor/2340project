package edu.gatech.cs2340.app;
import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;

import edu.gatech.cs2340.app.model.Shelter;
import edu.gatech.cs2340.app.model.ShelterDatabase;
import edu.gatech.cs2340.app.model.ShelterInfo;



/**
 * Created by diana on 4/9/18.
 * JUnit Tests
 */

public class DianaTest {

    @Test
    public void testShelterReleaseBeds() {
        ShelterDatabase.clearFailureString();
        ArrayList<Integer> capacity = new ArrayList<>();
        capacity.add(100);
        double[] longitude = new double[2];
        longitude[0] = 0.00;
        longitude[1] = 1.00;
        ShelterInfo shelterInfo = new ShelterInfo("someName", "someAddress", "someNotes",
                "someNumber", "someRestrictions");
        Shelter shelter = new Shelter(5, capacity, 100, longitude, shelterInfo);
        shelter.claimBeds(10);
        Assert.assertEquals(90, shelter.getRemainingCapacity());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));

    }



    @Test
    public void testShelterReleaseAllBeds() {
        ShelterDatabase.clearFailureString();
        ArrayList<Integer> capacity = new ArrayList<>();
        capacity.add(100);
        double[] longitude = new double[2];
        longitude[0] = 0.00;
        longitude[1] = 1.00;
        ShelterInfo shelterInfo = new ShelterInfo("someName", "someAddress", "someNotes",
                "someNumber", "someRestrictions");
        Shelter shelter = new Shelter(5, capacity, 100, longitude, shelterInfo);
        Assert.assertEquals(5, shelter.getUniqueKey());
        Assert.assertEquals(100, shelter.getRemainingCapacity());

        shelter.claimBeds(10);
        Assert.assertEquals(90, shelter.getRemainingCapacity());
        shelter.releaseBeds(10);
        Assert.assertEquals(100, shelter.getRemainingCapacity());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));
    }


    @Test
    public void testShelterReleaseMoreBeds() {
        ShelterDatabase.clearFailureString();
        ArrayList<Integer> capacity = new ArrayList<>();
        capacity.add(100);
        double[] longitude = new double[2];
        longitude[0] = 0.00;
        longitude[1] = 1.00;
        ShelterInfo shelterInfo = new ShelterInfo("someName", "someAddress", "someNotes",
                "someNumber", "someRestrictions");
        Shelter shelter = new Shelter(5, capacity, 100, longitude, shelterInfo);

        shelter.claimBeds(10);
        Assert.assertEquals(90, shelter.getRemainingCapacity());

        shelter.releaseBeds(15);
        Assert.assertEquals(90, shelter.getRemainingCapacity());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));
    }




    @Test
    public void testShelterReleaseZeroBeds() {
        ShelterDatabase.clearFailureString();
        ArrayList<Integer> capacity = new ArrayList<>();
        capacity.add(100);
        double[] longitude = new double[2];
        longitude[0] = 0.00;
        longitude[1] = 1.00;
        ShelterInfo shelterInfo = new ShelterInfo("someName", "someAddress", "someNotes",
                "someNumber", "someRestrictions");
        Shelter shelter = new Shelter(5, capacity, 100, longitude, shelterInfo);

        shelter.claimBeds(10);
        shelter.releaseBeds(0);

        Assert.assertEquals(90, shelter.getRemainingCapacity());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));
    }





}




