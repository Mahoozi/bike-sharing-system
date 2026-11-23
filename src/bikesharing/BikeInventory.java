/*
 * Bike Sharing System
 * Author: Yusuf Almahoozi
 *
 * BikeInventory tracks how many bikes of one type a station has during each
 * hour of a single day (hours 0-23). Taking a bike out at a given hour lowers
 * the count for that hour and every later hour; returning a bike raises the
 * count from the return hour onward. The array therefore shows, hour by
 * hour, how availability changes as trips start and end.
 */
package bikesharing;

public class BikeInventory {

    // ---------------------------------------------------------
    // Fields
    // ---------------------------------------------------------
    private String bikeType; // Bike type this inventory tracks (e.g. "Standard")
    private int[]  bikeNum;  // Number of bikes available at each hour (index = hour)

    // ---------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------
    // Creates the inventory for one bike type, starting every hour
    // of the day with the same number of bikes.
    public BikeInventory(String bikeType, int num) {
        this.bikeType = bikeType;
        this.bikeNum  = new int[24];

        for (int i = 0; i < bikeNum.length; i++) {
            bikeNum[i] = num;
        }
    }

    // ---------------------------------------------------------
    // Accessors
    // ---------------------------------------------------------
    // Returns the number of bikes available at the given hour.
    public int getNumOfBikes(int time) {
        return bikeNum[time];
    }

    // Returns the bike type this inventory tracks.
    public String getBikeType() {
        return bikeType;
    }

    // ---------------------------------------------------------
    // Adding and removing bikes
    // ---------------------------------------------------------
    // Adds bikes at the given hour and every hour after it.
    public void addBikes(int time, int num) {
        for (int i = time; i < bikeNum.length; i++) {
            bikeNum[i] = bikeNum[i] + num;
        }
    }

    // Removes bikes at the given hour and every hour after it.
    // Returns false, and changes nothing, if fewer than num bikes are
    // available at that hour or at any later hour. Checking the later
    // hours matters when bikes have already been taken from a later
    // hour: removing more would drive those counts below zero.
    public boolean removeBikes(int time, int num) {
        int fewest = bikeNum[time];

        for (int i = time + 1; i < bikeNum.length; i++) {
            fewest = Math.min(fewest, bikeNum[i]);
        }

        if (fewest < num)
            return false;

        for (int i = time; i < bikeNum.length; i++) {
            bikeNum[i] = bikeNum[i] - num;
        }

        return true;
    }

    // ---------------------------------------------------------
    // toString
    // ---------------------------------------------------------
    // Returns the bike type followed by the 24 hourly counts,
    // separated by spaces.
    @Override
    public String toString() {
        String result = bikeType;

        for (int i = 0; i < bikeNum.length; i++) {
            result = result + " " + bikeNum[i];
        }

        return result;
    }
}
