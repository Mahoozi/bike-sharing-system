/*
 * Bike Sharing System
 * Author: Yusuf Almahoozi
 *
 * Station represents a bike-sharing station. It holds one BikeInventory for
 * each of the four bike types (Standard, E-Bike, Kids and Tandem), lets bikes
 * be checked out and returned at a given hour, keeps a running total of the
 * revenue the station has earned, and prints its hourly inventory as a table.
 */
package bikesharing;

public class Station {

    // ---------------------------------------------------------
    // Fields
    // ---------------------------------------------------------
    private String          name;          // Station name, e.g. "Concordia"
    private BikeInventory[] inventory;     // One inventory per bike type
    private double          revenue = 0.0; // Total revenue earned by this station

    // ---------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------
    // Creates a station with the given starting number of bikes for
    // each type. Inventory order: Standard, E-Bike, Kids, Tandem.
    public Station(String name, int standard, int ebike, int kid, int tandem) {
        this.name      = name;
        this.inventory = new BikeInventory[4];

        inventory[0] = new BikeInventory("Standard", standard);
        inventory[1] = new BikeInventory("E-Bike",   ebike);
        inventory[2] = new BikeInventory("Kids",     kid);
        inventory[3] = new BikeInventory("Tandem",   tandem);
    }

    // ---------------------------------------------------------
    // Accessors
    // ---------------------------------------------------------
    // Returns the name of the station.
    public String getName() {
        return name;
    }

    // Returns the total number of bikes of all types at the given hour.
    public int getTotalBikes(int time) {
        int total = 0;

        for (int i = 0; i < inventory.length; i++) {
            total = total + inventory[i].getNumOfBikes(time);
        }

        return total;
    }

    // Returns the number of bikes of the given type at the given hour,
    // or 0 if the bike type is not recognised.
    public int checkInventory(String bike, int time) {
        if (bike.equals("Standard"))
            return inventory[0].getNumOfBikes(time);

        if (bike.equals("E-Bike"))
            return inventory[1].getNumOfBikes(time);

        if (bike.equals("Kids"))
            return inventory[2].getNumOfBikes(time);

        if (bike.equals("Tandem"))
            return inventory[3].getNumOfBikes(time);

        return 0;
    }

    // ---------------------------------------------------------
    // Checking bikes out and in
    // ---------------------------------------------------------
    // Takes one bike of the given type out of the station from the
    // given hour onward. Returns false if no bike of that type is
    // available for the rest of the day from that hour, or the type
    // is not recognised.
    public boolean checkoutBike(String bike, int time) {
        if (bike.equals("Standard"))
            return inventory[0].removeBikes(time, 1);

        if (bike.equals("E-Bike"))
            return inventory[1].removeBikes(time, 1);

        if (bike.equals("Kids"))
            return inventory[2].removeBikes(time, 1);

        if (bike.equals("Tandem"))
            return inventory[3].removeBikes(time, 1);

        return false;
    }

    // Puts one bike of the given type back into the station from the
    // given hour onward. Unrecognised bike types are ignored.
    public void returnBike(String bike, int time) {
        if (bike.equals("Standard")) {
            inventory[0].addBikes(time, 1);
            return;
        }

        if (bike.equals("E-Bike")) {
            inventory[1].addBikes(time, 1);
            return;
        }

        if (bike.equals("Kids")) {
            inventory[2].addBikes(time, 1);
            return;
        }

        if (bike.equals("Tandem")) {
            inventory[3].addBikes(time, 1);
            return;
        }
    }

    // ---------------------------------------------------------
    // Revenue
    // ---------------------------------------------------------
    // Returns the total revenue the station has earned so far.
    public double getRevenue() {
        return revenue;
    }

    // Adds the given amount to the station's revenue.
    public void addRevenue(double amount) {
        revenue = revenue + amount;
    }

    // ---------------------------------------------------------
    // equals
    // ---------------------------------------------------------
    // Two stations are considered equal when they have earned the same
    // revenue (compared with a small tolerance for rounding).
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof Station))
            return false;

        Station other = (Station) obj;

        return Math.abs(this.revenue - other.revenue) < 0.000001;
    }

    // ---------------------------------------------------------
    // toString
    // ---------------------------------------------------------
    // Returns the station's inventory as a table: one column per hour
    // (0-23), one row per bike type, plus a row with the hourly total.
    @Override
    public String toString() {
        String result = "=== Bike Inventory of " + name + " Station ===\n\n";

        result = result + String.format("%-9s", "Hour");
        for (int i = 0; i < 24; i++) {
            result = result + String.format("%-3d", i);
        }
        result = result + "\n";

        result = result + "         ";
        for (int i = 0; i < 72; i++) {
            result = result + "-";
        }
        result = result + "\n";

        result = result + String.format("%-9s", "Standard");
        for (int i = 0; i < 24; i++) {
            result = result + String.format("%-3d", inventory[0].getNumOfBikes(i));
        }
        result = result + "\n";

        result = result + String.format("%-9s", "E-Bike");
        for (int i = 0; i < 24; i++) {
            result = result + String.format("%-3d", inventory[1].getNumOfBikes(i));
        }
        result = result + "\n";

        result = result + String.format("%-9s", "Kids");
        for (int i = 0; i < 24; i++) {
            result = result + String.format("%-3d", inventory[2].getNumOfBikes(i));
        }
        result = result + "\n";

        result = result + String.format("%-9s", "Tandem");
        for (int i = 0; i < 24; i++) {
            result = result + String.format("%-3d", inventory[3].getNumOfBikes(i));
        }
        result = result + "\n";

        result = result + String.format("%-9s", "Total");
        for (int i = 0; i < 24; i++) {
            result = result + String.format("%-3d", getTotalBikes(i));
        }
        result = result + "\n";

        return result;
    }
}
