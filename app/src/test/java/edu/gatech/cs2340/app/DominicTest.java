package edu.gatech.cs2340.app;


        import junit.framework.Assert;

        import org.junit.Test;

        import edu.gatech.cs2340.app.model.ShelterDatabase;
        import edu.gatech.cs2340.app.model.User;

/**
 * JUnit tests
 */

@SuppressWarnings("JavaDoc")
public class DominicTest {
    @Test
    public void testUserClaimBed() {
        ShelterDatabase.clearFailureString();
        User user = new User("Dominic", "pass", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.setNumBedsClaimed(1);
        user.setShelterID(2);

        Assert.assertEquals(1, user.getNumBedsClaimed());
        Assert.assertEquals(2, user.getShelterID());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));
    }
    @Test
    public void testUserClaimMoreBeds() {
        ShelterDatabase.clearFailureString();
        User user = new User("Dominic", "pass", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.setNumBedsClaimed(100);
        user.setShelterID(0);

        Assert.assertEquals(100, user.getNumBedsClaimed());
        Assert.assertEquals(0, user.getShelterID());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));
    }
    @Test
    public void testUserClaimZeroBeds() {
        ShelterDatabase.clearFailureString();
        User user = new User("Dominic", "pass", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.setNumBedsClaimed(0);
        user.setShelterID(0);

        Assert.assertEquals(0, user.getNumBedsClaimed());
        Assert.assertEquals(0, user.getShelterID());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));
    }
    @Test
    public void testUserClaimBedsDiffShelter() {
        ShelterDatabase.clearFailureString();
        User user = new User("Dominic", "pass", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.setNumBedsClaimed(1);
        user.setShelterID(0);

        user.claimBeds(1, 1);

        Assert.assertEquals(1, user.getNumBedsClaimed());
        Assert.assertEquals(0, user.getShelterID());
        Assert.assertTrue((" You already own beds at " +
                ShelterDatabase.getNameByID(0) + ".").equals(ShelterDatabase.getFailureString()));
    }
    @Test
    public void testUserClaimBedsSameShelter() {
        ShelterDatabase.clearFailureString();
        User user = new User("Dominic", "pass", "Admin");
        Assert.assertEquals(-1, user.getShelterID());
        Assert.assertEquals(0, user.getNumBedsClaimed());

        user.claimBeds(2, 1);

        Assert.assertEquals(2, user.getNumBedsClaimed());
        Assert.assertEquals(1, user.getShelterID());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));

        user.claimBeds(3, 1);

        Assert.assertEquals(5, user.getNumBedsClaimed());
        Assert.assertEquals(1, user.getShelterID());
        Assert.assertTrue("".equals(ShelterDatabase.getFailureString()));

    }

}
