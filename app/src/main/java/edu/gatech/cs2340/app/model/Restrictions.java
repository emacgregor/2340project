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
    private String searchRestrictions;
    public Restrictions(String restrictions) {
        makeSearchRestrictionsString(restrictions);
    }
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
        } else {
            Log.d("Restrictions: ", "Somebody called Restrictions with an array "
                    + "that is not 9 length");
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
    private String addToString(String people, String searchRestrictions) {
        if (!"Unspecified".equals(searchRestrictions)) {
            return searchRestrictions + ", " + people.toLowerCase();
        } else {
            return people;
        }
    }
    public String getSearchRestrictions() {
        return searchRestrictions;
    }
    public String toString() {
        return getSearchRestrictions();
    }
    public boolean allowsMen() {
        return men;
    }
    public boolean allowsWomen() {
        return women;
    }
    public boolean allowsNonbinary() {
        return nonBinary;
    }
    public boolean allowsFamilies() {
        return family;
    }
    public boolean allowsFamiliesWithChildren() {
        return famChildren;
    }
    public boolean allowsFamiliesWithNewborns() {
        return famNewborn;
    }
    public boolean allowsChildren() {
        return children;
    }
    public boolean allowsYoungAdults() {
        return youngAdults;
    }
    public boolean allowsVeterans() {
        return veterans;
    }
    public boolean allowsAnyone() {
        return anyone;
    }
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
