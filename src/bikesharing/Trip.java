/*
 * Bike Sharing System
 * Author: Yusuf Almahoozi
 *
 * Trip represents a single bike rental: the bike type, the start and end
 * stations, and the start and end hours. Starting a trip (checkIn) takes a
 * bike out of the start station; ending it (checkOut) returns the bike to the
 * end station, works out the price and credits the revenue to the stations
 * involved.
 *
 * Price = hours ridden x hourly rate for the bike type x membership multiplier.
 * If the trip starts and ends at the same station, that station keeps all of
 * the revenue; otherwise the revenue is split equally between the two.
 */
package bikesharing;

public class Trip {

    // ---------------------------------------------------------
    // Fields
    // ---------------------------------------------------------
    private String  bikeType;     // Standard, E-Bike, Kids or Tandem
    private Station startStation; // Station the bike was taken from
    private Station endStation;   // Station the bike was returned to (null while riding)
    private int     startTime;    // Hour the trip started (0-23)
    private int     endTime;      // Hour the trip ended (0-23)

    private double  discountRate; // Price multiplier from the rider's membership (1.0 = full price)

    // ---------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------
    // Creates a trip for the given bike type and price multiplier.
    // The trip has no stations or times until checkIn is called.
    public Trip(String bikeType, double discountRate) {
        this.bikeType     = bikeType;
        this.discountRate = discountRate;
    }

    // ---------------------------------------------------------
    // Starting the trip
    // ---------------------------------------------------------
    // Tries to take a bike of this trip's type out of the start station
    // at the given hour. Records the start station and hour and returns
    // true on success; returns false if no such bike is available.
    public boolean checkIn(Station startStation, int startTime) {
        if (startStation.checkoutBike(bikeType, startTime)) {
            this.startStation = startStation;
            this.startTime    = startTime;
            return true;
        }

        return false;
    }

    // ---------------------------------------------------------
    // Ending the trip
    // ---------------------------------------------------------
    // Returns the bike to the end station at the given hour, credits the
    // revenue to the station(s), and returns the amount charged.
    public double checkOut(Station endStation, int endTime) {
        this.endStation = endStation;
        this.endTime    = endTime;

        endStation.returnBike(bikeType, endTime);

        double amount = getAmountDue();

        if (startStation == endStation) {
            startStation.addRevenue(amount);
        }
        else {
            startStation.addRevenue(amount / 2.0);
            endStation.addRevenue(amount / 2.0);
        }

        return amount;
    }

    // ---------------------------------------------------------
    // Pricing
    // ---------------------------------------------------------
    // Returns (endTime - startTime) x discountRate x hourly rate, where
    // the hourly rate is Standard 1.00, E-Bike 5.00, Kids 0.50, Tandem 3.00.
    public double getAmountDue() {
        double baseRate = 0.0;

        if (endTime == startTime)
            return 0.0;

        if (bikeType.equals("Standard"))
            baseRate = 1.0;
        else if (bikeType.equals("E-Bike"))
            baseRate = 5.0;
        else if (bikeType.equals("Kids"))
            baseRate = 0.5;
        else if (bikeType.equals("Tandem"))
            baseRate = 3.0;

        return (endTime - startTime) * discountRate * baseRate;
    }

    // ---------------------------------------------------------
    // Accessors
    // ---------------------------------------------------------
    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    // ---------------------------------------------------------
    // toString
    // ---------------------------------------------------------
    // Returns a one-line summary of a finished trip, e.g.
    // [Start: Concordia at 5:00, End: McGill at 10:00, Bike: Standard, Amount Due: 5.00]
    @Override
    public String toString() {
        return "[Start: " + startStation.getName() + " at " + startTime + ":00, End: "
             + endStation.getName() + " at " + endTime + ":00, Bike: " + bikeType
             + ", Amount Due: " + String.format("%.2f", getAmountDue()) + "]";
    }
}
