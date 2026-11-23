/*
 * Bike Sharing System
 * Author: Yusuf Almahoozi
 *
 * BikeSharingSystem is the program's entry point. It sets up five stations,
 * each starting with 5 bikes of every type, creates two riders, and runs a
 * fixed scenario that prints station inventories, trip receipts, rider
 * summaries and a station's revenue.
 */
package bikesharing;

public class BikeSharingSystem {

    private Station concordia;
    private Station mcgill;
    private Station udem;
    private Station uqam;
    private Station ets;

    // Creates the five stations used by the demo.
    public BikeSharingSystem() {
        this.concordia = new Station("Concordia", 5, 5, 5, 5);
        this.mcgill    = new Station("McGill",    5, 5, 5, 5);
        this.udem      = new Station("UdeM",      5, 5, 5, 5);
        this.uqam      = new Station("UQAM",      5, 5, 5, 5);
        this.ets       = new Station("ETS",       5, 5, 5, 5);
    }

    // Runs the demo scenario.
    public void run() {
        // Starting inventory of Concordia station
        System.out.println(concordia);

        // Two riders with different memberships
        Rider rider1 = new Rider(1, "Adult");
        Rider rider2 = new Rider(2, "Student");

        // Rider 1, trip 1: Standard bike, Concordia 5:00 -> McGill 10:00
        rider1.startTrip("Standard", concordia, 5);
        System.out.println(rider1.endTrip(mcgill, 10));

        // Rider 1, trip 2: E-Bike, UdeM 12:00 -> UdeM 15:00 (round trip)
        rider1.startTrip("E-Bike", udem, 12);
        System.out.println(rider1.endTrip(udem, 15));

        // Rider 1 summary
        System.out.println(rider1);

        // McGill inventory: the Standard bike returned at 10:00 appears from that hour on
        System.out.println();
        System.out.println(mcgill);

        // Rider 2, trip 1: Tandem, UQAM 2:00 -> UdeM 14:00, with the Student discount
        rider2.startTrip("Tandem", uqam, 2);
        System.out.println(rider2.endTrip(udem, 14));

        // Rider 2 summary
        System.out.println(rider2);

        // UdeM inventory: shows the E-Bike rental and the Tandem return
        System.out.println();
        System.out.println(udem);

        // UdeM revenue: the full E-Bike round trip plus half of the Tandem trip
        System.out.println("The revenue of " + udem.getName() + " Station is "
                + String.format("%.2f", udem.getRevenue()));
    }

    public static void main(String[] args) {
        BikeSharingSystem system = new BikeSharingSystem();
        system.run();
    }
}
