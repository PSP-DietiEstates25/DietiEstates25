package com.dietiestates.resource_server.utils;

import com.dietiestates.resource_server.model.Utility;

public class MatchingUtils {

    private MatchingUtils(){}

    public static boolean matchesUtilities(Utility searchUtility, Utility realEstateUtility) {
        return (!isTrue(searchUtility.getHasDoorman())          || isTrue(realEstateUtility.getHasDoorman()))
                && (!isTrue(searchUtility.getHasElevator())         || isTrue(realEstateUtility.getHasElevator()))
                && (!isTrue(searchUtility.getHasAirConditioning())  || isTrue(realEstateUtility.getHasAirConditioning()))
                && (!isTrue(searchUtility.getNearPark())            || isTrue(realEstateUtility.getNearPark()))
                && (!isTrue(searchUtility.getNearSchool())          || isTrue(realEstateUtility.getNearSchool()))
                && (!isTrue(searchUtility.getNearPublicTransport()) || isTrue(realEstateUtility.getNearPublicTransport()));
    }

    private static boolean isTrue(Boolean v) {
        return Boolean.TRUE.equals(v);
    }
}
