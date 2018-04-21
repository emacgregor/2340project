package edu.gatech.cs2340.app.model;

import android.util.Log;

/**
 * Keeps track of everything related to shelter restrictions.
 */

public class Restrictions {
    private boolean men;
    private boolean women;
    private boolean nonBinary;
    private boolean family;
    private boolean famChildren;
    private boolean famNewborn;
    private boolean children;
    private boolean youngAdults;
    private boolean veterans;
    private boolean anyone;
    private String searchRestrictions = "Unspecified";

    /**
     * Constructor for restrictions but using string rather than boolean array.
     * @param restrictions The restrictions in String form.
     */
    public Restrictions(String restrictions) {
        makeSearchRestrictionsString(restrictions);
    }

    /**
     * Constructor for class Restrictions. Sets all booleans based on array and builds a String.
     * @param restrictions Boolean array
     */
    public Restrictions(boolean[] restrictions) {
        if (restrictions.length == 9) {
            men = restrictions[0];
            women = restrictions[1];
            nonBinary = restrictions[2];
            family = restrictions[3];
            famChildren = restrictions[4];
            famNewborn = restrictions[5];
            children = restrictions[6];
            youngAdults = restrictions[7];
            veterans = restrictions[8];
            anyone = false;
            makeSearchRestrictionsString();
        } else if (restrictions.length == 10) {
            men = restrictions[0];
            women = restrictions[1];
            nonBinary = restrictions[2];
            family = restrictions[3];
            famChildren = restrictions[4];
            famNewborn = restrictions[5];
            children = restrictions[6];
            youngAdults = restrictions[7];
            veterans = restrictions[8];
            anyone = restrictions[9];
            makeSearchRestrictionsString();
        } else {
            Log.d("Restrictions: ", "Somebody called Restrictions with an array "
                    + "that is not 9 or 10 in length");
        }
    }
    @SuppressWarnings({"OverlyLongMethod", "OverlyComplexMethod"})
    private void makeSearchRestrictionsString(String restrictions) {
        String lcRestrictions = restrictions.toLowerCase();
        if (lcRestrictions.contains("women")) {
            women = true;
            searchRestrictions = addToString("Women", searchRestrictions);
        }
        else if (lcRestrictions.contains("men")) {
            men = true;
            searchRestrictions = addToString("Men", searchRestrictions);
        }
        if (lcRestrictions.contains("non-binary")
                || lcRestrictions.contains("nonbinary")) {
            nonBinary = true;
            searchRestrictions = addToString("Non-binary", searchRestrictions);
        }
        if (lcRestrictions.contains("families w/ children")
                || lcRestrictions.contains("families with children")) {
            famChildren = true;
            searchRestrictions = addToString("Families with children", searchRestrictions);
        }
        if (lcRestrictions.contains("newborn")) {
            famNewborn = true;
            searchRestrictions = addToString("Families with newborns", searchRestrictions);
        }
        if (lcRestrictions.contains("famil") && !famChildren && !famNewborn) {
            family = true;
            searchRestrictions = addToString("Families", searchRestrictions);
        }
        if (lcRestrictions.contains("children") && !famChildren) {
            children = true;
            searchRestrictions = addToString("Children", searchRestrictions);
        }
        if (lcRestrictions.contains("young adult")) {
            youngAdults = true;
            searchRestrictions = addToString("Young adult", searchRestrictions);
        }
        if (lcRestrictions.contains("veteran")) {
            veterans = true;
            searchRestrictions = addToString("Veteran", searchRestrictions);
        }
        if (lcRestrictions.contains("anyone")) {
            anyone = true;
            searchRestrictions = addToString("Anyone", searchRestrictions);
        }
    }

    /**
     * Called to make string when using second constructor.
     */
    @SuppressWarnings("OverlyComplexMethod")
    private void makeSearchRestrictionsString() {
        if (women) {
            searchRestrictions = addToString("Women", searchRestrictions);
        }
        else if (men) {
            searchRestrictions = addToString("Men", searchRestrictions);
        }
        if (nonBinary) {
            searchRestrictions = addToString("Non-binary", searchRestrictions);
        }
        if (famChildren) {
            searchRestrictions = addToString("Families with children", searchRestrictions);
        }
        if (famNewborn) {
            searchRestrictions = addToString("Families with newborns", searchRestrictions);
        }
        if (family) {
            searchRestrictions = addToString("Families", searchRestrictions);
        }
        if (children) {
            searchRestrictions = addToString("Children", searchRestrictions);
        }
        if (youngAdults) {
            searchRestrictions = addToString("Young adult", searchRestrictions);
        }
        if (veterans) {
            searchRestrictions = addToString("Veteran", searchRestrictions);
        }
        if (anyone) {
            searchRestrictions = addToString("Anyone", searchRestrictions);
        }
    }
    private String addToString(String people, String searchRestrictions) {
        if (!"Unspecified".equals(searchRestrictions)) {
            return searchRestrictions + ", " + people.toLowerCase();
        } else {
            return people;
        }
    }

    /**
     * Returns a string that contains all the info about the restrictions
     * @return The string that contains all the info about the restrictions
     */
    public String getSearchRestrictions() {
        return searchRestrictions;
    }

    /**
     * Calls getSearchRestrictions()
     * @return searchRestrictions
     */
    public String toString() {
        return getSearchRestrictions();
    }

    /**
     * Returns whether the shelter allows men.
     * @return Whether the shelter allows men.
     */
    public boolean allowsMen() {
        return men;
    }
    
    /**
     * Returns whether the shelter allows women.
     * @return Whether the shelter allows women.
     */
    private boolean allowsWomen() {
        return women;
    }

    /**
     * Returns whether the shelter allows non-binary people.
     * @return Whether the shelter allows non-binary people.
     */
    private boolean allowsNonbinary() {
        return nonBinary;
    }

    /**
     * Returns whether the shelter allows families.
     * @return Whether the shelter allows families.
     */
    private boolean allowsFamilies() {
        return family;
    }

    /**
     * Returns whether the shelter allows families with children.
     * @return Whether the shelter allows families with children.
     */
    private boolean allowsFamiliesWithChildren() {
        return famChildren;
    }

    /**
     * Returns whether the shelter allows families with newborns.
     * @return Whether the shelter allows families with newborns.
     */
    private boolean allowsFamiliesWithNewborns() {
        return famNewborn;
    }

    /**
     * Returns whether the shelter allows children.
     * @return Whether the shelter allows children.
     */
    private boolean allowsChildren() {
        return children;
    }

    /**
     * Returns whether the shelter allows young adults.
     * @return Whether the shelter allows young adults.
     */
    private boolean allowsYoungAdults() {
        return youngAdults;
    }

    /**
     * Returns whether the shelter allows veterans.
     * @return Whether the shelter allows veterans.
     */
    private boolean allowsVeterans() {
        return veterans;
    }

    /**
     * Returns whether the shelter allows anyone.
     * @return Whether the shelter allows anyone.
     */
    private boolean allowsAnyone() {
        return anyone;
    }

    /**
     * Finds whether there is a matching truth value between this restrictions and the param
     * restrictions
     * @param otherRestrictions Another Restrictions class we are comparing to.
     * @return Whether there is a matching truth value.
     */
    @SuppressWarnings("OverlyComplexMethod")
    public boolean hasMatch(Restrictions otherRestrictions) {
        return ((men && otherRestrictions.allowsMen())
                || (women && otherRestrictions.allowsWomen())
                || (nonBinary && otherRestrictions.allowsNonbinary())
                || (family && otherRestrictions.allowsFamilies())
                || (famChildren && otherRestrictions.allowsFamiliesWithChildren())
                || (famNewborn && otherRestrictions.allowsFamiliesWithNewborns())
                || (children && otherRestrictions.allowsChildren())
                || (youngAdults && otherRestrictions.allowsYoungAdults())
                || (veterans && otherRestrictions.allowsVeterans())
                || anyone || otherRestrictions.allowsAnyone());
    }
}
