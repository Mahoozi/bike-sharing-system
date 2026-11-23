/*
 * Bike Sharing System
 * Author: Yusuf Almahoozi
 *
 * Rider represents a member of the bike-sharing system. A rider has an ID, a
 * membership type that determines the price they pay, and a history of up to
 * 30 trips. Riders start and end trips through this class, and can report
 * their completed trips and the total amount they owe.
 */
package bikesharing;

public class Rider {

    // ---------------------------------------------------------
    // Fields
    // ---------------------------------------------------------
    private int      riderID;         // Rider ID
    private String   riderMembership; // Adult, Student, Senior or Kids
    private Trip[]   trips;           // Trip history (up to 30 trips)

    // ---------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------
    // Creates a rider with an ID, a membership type and no trips.
    public Rider(int riderID, String riderMembership) {
        this.riderID         = riderID;
        this.riderMembership = riderMembership;
        this.trips           = new Trip[30];
    }

    // ---------------------------------------------------------
    // Pricing
    // ---------------------------------------------------------
    // Returns the price multiplier for this rider's membership:
    // Adult 1.0 (full price), Student 0.8, Senior 0.6, Kids 0.5.
    // Any other membership type pays full price.
    public double getDiscountRate() {
        if (riderMembership.equals("Adult"))
            return 1.0;

        if (riderMembership.equals("Student"))
            return 0.8;

        if (riderMembership.equals("Senior"))
            return 0.6;

        if (riderMembership.equals("Kids"))
            return 0.5;

        return 1.0;
    }

    // Returns the total amount owed for all completed trips.
    public double getTotalAmountDue() {
        double total = 0.0;

        for (int i = 0; i < trips.length; i++) {
            if (trips[i] != null && trips[i].getEndStation() != null) {
                total = total + trips[i].getAmountDue();
            }
        }

        return total;
    }

    // ---------------------------------------------------------
    // Starting and ending trips
    // ---------------------------------------------------------
    // Starts a new trip by taking a bike of the given type from the
    // start station at the given hour. Returns false if the hour is
    // outside 0-23, the bike is not available, or the rider's trip
    // history is full.
    public boolean startTrip(String bikeType, Station startStation, int startTime) {
        for (int i = 0; i < trips.length; i++) {
            if (trips[i] == null) {
                Trip t = new Trip(bikeType, getDiscountRate());

                if (t.checkIn(startStation, startTime)) {
                    trips[i] = t;
                    return true;
                }

                return false;
            }
        }

        return false;
    }

    // Ends the rider's most recent unfinished trip at the given station
    // and hour, and returns that trip's summary. Returns null if the rider
    // has no trip in progress. Throws IllegalArgumentException if the end
    // hour is before the trip's start hour or after 23; the trip then
    // stays in progress.
    public String endTrip(Station endStation, int endTime) {
        for (int i = trips.length - 1; i >= 0; i--) {
            if (trips[i] != null && trips[i].getEndStation() == null) {
                trips[i].checkOut(endStation, endTime);
                return trips[i].toString();
            }
        }

        return null;
    }

    // ---------------------------------------------------------
    // toString
    // ---------------------------------------------------------
    // Returns the rider's ID and membership, each completed trip, and
    // the total amount due.
    @Override
    public String toString() {
        String result = "ID: " + riderID + ", Membership: " + riderMembership;
        int count = 1;

        for (int i = 0; i < trips.length; i++) {
            if (trips[i] != null && trips[i].getEndStation() != null) {
                result = result + "\nTrip " + count + ": " + trips[i].toString();
                count++;
            }
        }

        result = result + "\nTotal amount due: " + String.format("%.2f", getTotalAmountDue());

        return result;
    }
}
