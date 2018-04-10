package edu.gatech.cs2340.app;


import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;

import edu.gatech.cs2340.app.model.Shelter;
import edu.gatech.cs2340.app.model.ShelterDatabase;
import edu.gatech.cs2340.app.model.ShelterInfo;

/**
 * JUnit tests
 */

@SuppressWarnings("JavaDoc")
public class HylandTest {

    @Test
    public void testShelterClaimBed() {
        ShelterDatabase.clearFailureString();
        ArrayList<Integer> cap = new ArrayList<>();
        cap.add(256);
        double[] longitude = new double[2];
        longitude[0] = 0.00;
        longitude[1] = 1.00;
        ShelterInfo shelterInfo = new ShelterInfo("Test", "Test", "Test", "Test", "Test");
        Shelter shelter = new Shelter(0, cap, 256, longitude, shelterInfo);
        Assert.assertEquals(0, shelter.getUniqueKey());
        Assert.assertEquals(256, shelter.getRemainingCapacity());
        shelter.claimBeds(40);

        Assert.assertEquals(216, shelter.getRemainingCapacity());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));
    }

    @Test
    public void testShelterClaimMoreBeds() {
        ShelterDatabase.clearFailureString();
        ArrayList<Integer> cap = new ArrayList<>();
        cap.add(256);
        double[] longitude = new double[2];
        longitude[0] = 0.00;
        longitude[1] = 1.00;
        ShelterInfo shelterInfo = new ShelterInfo("Test", "Test", "Test", "Test", "Test");
        Shelter shelter = new Shelter(0, cap, 256, longitude, shelterInfo);
        Assert.assertEquals(0, shelter.getUniqueKey());
        Assert.assertEquals(256, shelter.getRemainingCapacity());

        shelter.claimBeds(40);

        Assert.assertEquals(216, shelter.getRemainingCapacity());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));

        shelter.claimBeds(40);

        Assert.assertEquals(176, shelter.getRemainingCapacity());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));
    }

    @Test
    public void testShelterClaimZeroBeds() {
        ShelterDatabase.clearFailureString();
        ArrayList<Integer> cap = new ArrayList<>();
        cap.add(256);
        double[] longitude = new double[2];
        longitude[0] = 0.00;
        longitude[1] = 1.00;
        ShelterInfo shelterInfo = new ShelterInfo("Test", "Test", "Test", "Test", "Test");
        Shelter shelter = new Shelter(0, cap, 256, longitude, shelterInfo);
        Assert.assertEquals(0, shelter.getUniqueKey());
        Assert.assertEquals(256, shelter.getRemainingCapacity());

        shelter.claimBeds(0);

        Assert.assertEquals(256, shelter.getRemainingCapacity());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));
    }

    @Test
    public void testShelterClaimBedsTooMany() {
        ShelterDatabase.clearFailureString();
        ArrayList<Integer> cap = new ArrayList<>();
        cap.add(256);
        double[] longitude = new double[2];
        longitude[0] = 0.00;
        longitude[1] = 1.00;
        ShelterInfo shelterInfo = new ShelterInfo("Test", "Test", "Test", "Test", "Test");
        Shelter shelter = new Shelter(0, cap, 256, longitude, shelterInfo);
        Assert.assertEquals(0, shelter.getUniqueKey());
        Assert.assertEquals(256, shelter.getRemainingCapacity());

        shelter.claimBeds(400);

        Assert.assertEquals(256, shelter.getRemainingCapacity());
        Assert.assertTrue(" This shelter does not have that many beds to spare."
                            .equals(ShelterDatabase.getFailureString()));
    }
}
