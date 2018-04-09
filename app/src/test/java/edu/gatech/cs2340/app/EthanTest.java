package edu.gatech.cs2340.app;



import junit.framework.Assert;

import org.junit.Test;
import edu.gatech.cs2340.app.model.ShelterDatabase;
import edu.gatech.cs2340.app.model.User;

/**
 * JUnit tests
 */

@SuppressWarnings("JavaDoc")
public class EthanTest {
    @Test
    public void testUserReleaseAllBeds() {
        ShelterDatabase.clearFailureString();
        User user = new User("Ethan", "password", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.setNumBedsClaimed(100);
        user.setShelterID(0);
        user.releaseBeds(100, 0);

        Assert.assertEquals(0, user.getNumBedsClaimed());
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));
    }
    @Test
    public void testUserReleaseSomeBeds() {
        ShelterDatabase.clearFailureString();
        User user = new User("Ethan", "password", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.setNumBedsClaimed(100);
        user.setShelterID(0);
        user.releaseBeds(50, 0);

        Assert.assertEquals(50, user.getNumBedsClaimed());
        Assert.assertEquals(0, user.getShelterID());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));
    }
    @Test
    public void testUserReleaseZeroBeds() {
        ShelterDatabase.clearFailureString();
        User user = new User("Ethan", "password", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.setNumBedsClaimed(100);
        user.setShelterID(0);
        user.releaseBeds(0, 0);

        Assert.assertEquals(100, user.getNumBedsClaimed());
        Assert.assertEquals(0, user.getShelterID());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));
    }
    @Test
    public void testUserReleaseWithZeroBeds() {
        ShelterDatabase.clearFailureString();
        User user = new User("Ethan", "password", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.setShelterID(0);
        user.releaseBeds(50, 0);

        Assert.assertEquals(0, user.getNumBedsClaimed());
        Assert.assertEquals(0, user.getShelterID());
        Assert.assertTrue(" You do not have this many beds.".equals(ShelterDatabase.getFailureString()));
    }
    @Test
    public void testUserReleaseZeroBedsWithZeroBeds() {
        ShelterDatabase.clearFailureString();
        User user = new User("Ethan", "password", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.releaseBeds(0, 0);

        Assert.assertEquals(0, user.getNumBedsClaimed());
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertTrue(" You do not own any beds.".equals(ShelterDatabase.getFailureString()));
    }
    @Test
    public void testUserReleaseTooManyBeds() {
        ShelterDatabase.clearFailureString();
        User user = new User("Ethan", "password", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.setNumBedsClaimed(100);
        user.setShelterID(0);
        user.releaseBeds(150, 0);

        Assert.assertEquals(100, user.getNumBedsClaimed());
        Assert.assertEquals(0, user.getShelterID());
        Assert.assertTrue(" You do not have this many beds.".equals(ShelterDatabase.getFailureString()));
    }
    @Test
    public void testUserReleaseAtWrongShelter() {
        ShelterDatabase.clearFailureString();
        User user = new User("Ethan", "password", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.setNumBedsClaimed(100);
        user.setShelterID(1);
        user.releaseBeds(50, 0);

        Assert.assertEquals(100, user.getNumBedsClaimed());
        Assert.assertEquals(1, user.getShelterID());
        Assert.assertTrue((" Your beds are from "
                + null + ".").equals(ShelterDatabase.getFailureString()));
    }
    @Test
    public void testUserReleaseAtInvalidShelter() {
        ShelterDatabase.clearFailureString();
        User user = new User("Ethan", "password", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.setNumBedsClaimed(100);
        user.setShelterID(0);
        user.releaseBeds(50, -2);

        Assert.assertEquals(100, user.getNumBedsClaimed());
        Assert.assertEquals(0, user.getShelterID());
        Assert.assertTrue((" Your beds are from "
                + null + ".").equals(ShelterDatabase.getFailureString()));
    }
    @Test
    public void testUserReleaseAtWrongShelterAndTooMany() {
        ShelterDatabase.clearFailureString();
        User user = new User("Ethan", "password", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.setNumBedsClaimed(100);
        user.setShelterID(1);
        user.releaseBeds(150, 0);

        Assert.assertEquals(100, user.getNumBedsClaimed());
        Assert.assertEquals(1, user.getShelterID());
        Assert.assertTrue((" Your beds are from "
                + null + "." + " You do not have this many beds.").equals(ShelterDatabase.getFailureString()));
    }
}
