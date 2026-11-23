# Bike Sharing System

A Java console program that simulates one day of a bike-sharing network.
Stations hold four types of bikes, availability is tracked hour by hour
(0–23), riders rent and return bikes between stations, and each trip is
priced by bike type, duration and the rider's membership. Trip revenue is
credited to the stations involved.

The program runs a fixed demo scenario and prints station inventory tables,
trip receipts, rider summaries and a station's revenue. It takes no input.

## Features

- **Hourly inventory per station:** every station tracks how many bikes of
  each type are available at each hour of the day. Renting a bike lowers the
  count from the rental hour onward; returning one raises it from the return
  hour onward.
- **Four bike types:** Standard, E-Bike, Kids and Tandem, each with its own
  hourly rate.
- **Rentals that respect availability:** a rental is refused if a bike of
  the requested type is not available at the start station from that hour
  to the end of the day, or if the bike type is not recognised. Counts
  never go below zero, even when rentals are entered out of time order.
- **One-way and round trips:** a bike can be returned to any station.
- **Hour checks:** trips must start at an hour from 0 to 23 and end at or
  after their start hour, no later than 23.
- **Membership pricing:** Student, Senior and Kids members pay a reduced
  rate; Adult members pay full price.
- **Station revenue:** a round trip's fare goes to its station; a one-way
  trip's fare is split equally between the start and end stations.
- **Rider history:** each rider keeps up to 30 trips and can print their
  completed trips and total amount due.
- **Inventory table:** a station prints its hourly inventory as a table with
  one row per bike type and a total row.

## Requirements

- A Java Development Kit (JDK), version 8 or newer. No external libraries or
  build tools are needed.
- Tested with JDK 24.0.2. The sources also compile with `--release 8`.

Check your installation with:

```bash
java -version
javac -version
```

## Build and run

From the project root:

```bash
javac -d out src/bikesharing/*.java
java -cp out bikesharing.BikeSharingSystem
```

Compiled classes go into `out/`, which is ignored by Git.

## Project structure

```
bike-sharing-system/
├── README.md
├── .gitignore
└── src/
    └── bikesharing/
        ├── BikeSharingSystem.java
        ├── Station.java
        ├── BikeInventory.java
        ├── Rider.java
        └── Trip.java
```

| Class               | Responsibility |
|---------------------|----------------|
| `BikeSharingSystem` | Entry point (`main`). Creates five stations and two riders, then runs the demo scenario and prints the results. |
| `Station`           | A named station with one `BikeInventory` per bike type. Checks bikes out and in, reports availability and hourly totals, keeps the station's revenue, and prints the inventory table. |
| `BikeInventory`     | Hourly counts (24 values) for a single bike type at a single station. Adds or removes bikes from a given hour to the end of the day. |
| `Rider`             | A rider with an ID, a membership type and up to 30 trips. Starts and ends trips, works out the membership price multiplier, and totals the amount due. |
| `Trip`              | One rental. Takes the bike out of the start station, returns it to the end station, calculates the fare, and credits the revenue to the station(s). |

## How it works

### Bike types and hourly rates

| Bike type | Rate per hour |
|-----------|---------------|
| Standard  | $1.00         |
| E-Bike    | $5.00         |
| Kids      | $0.50         |
| Tandem    | $3.00         |

Bike type names are case-sensitive and must match exactly (for example
`"E-Bike"`, not `"ebike"`).

### Station inventory

A `Station` is created with a starting count for each bike type, and that
count applies to all 24 hours. Times are whole hours from 0 to 23.

- **Renting** a bike at hour *h* removes one bike from hour *h* through 23.
  The rental is refused if the count is 0 at hour *h* or at any later hour.
  Because a trip's return hour is not known when it starts, this
  conservative check stops a station from handing out a bike that is already
  booked from a later hour.
- **Returning** a bike at hour *h* adds one bike from hour *h* through 23.

So after a Standard bike is taken from Concordia at 5:00 and returned to
McGill at 10:00, Concordia shows one fewer Standard bike from hour 5 onward
and McGill shows one more from hour 10 onward.

### Trips

`Rider.startTrip(bikeType, station, hour)` creates a trip and tries to take a
bike from the station; it returns `false` if the start hour is outside 0–23,
no bike is available, or the rider already has 30 trips.
`Rider.endTrip(station, hour)` ends the rider's most recent unfinished trip
and returns a one-line receipt, or `null` if the rider has no trip in
progress. An end hour earlier than the start hour or later than 23 throws an
`IllegalArgumentException` and leaves the trip in progress, so no negative
fare is charged and no bike goes missing.

### Pricing and membership discounts

```
fare = (end hour − start hour) × hourly rate × membership multiplier
```

| Membership | Multiplier | Discount |
|------------|------------|----------|
| Adult      | 1.0        | none     |
| Student    | 0.8        | 20%      |
| Senior     | 0.6        | 40%      |
| Kids       | 0.5        | 50%      |

Any other membership name is charged the Adult (full) price. A trip that
starts and ends in the same hour costs $0.00.

Example: a Student riding a Tandem from 2:00 to 14:00 pays
12 × $3.00 × 0.8 = **$28.80**.

### Station revenue

- Round trip (same start and end station): the full fare goes to that station.
- One-way trip: the fare is split 50/50 between the start and end stations.

In the demo, UdeM earns the full $15.00 from an E-Bike round trip plus half
of a $28.80 Tandem trip that ends there, for **$29.40**.

## Demo scenario and output

All five stations (Concordia, McGill, UdeM, UQAM, ETS) start with 5 bikes of
each type. The demo:

1. Prints Concordia's starting inventory.
2. Rider 1 (Adult) rides a Standard bike from Concordia (5:00) to McGill
   (10:00), then an E-Bike round trip at UdeM (12:00–15:00).
3. Prints Rider 1's summary and McGill's inventory.
4. Rider 2 (Student) rides a Tandem from UQAM (2:00) to UdeM (14:00).
5. Prints Rider 2's summary, UdeM's inventory and UdeM's revenue.

Output:

```
=== Bike Inventory of Concordia Station ===

Hour     0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23
         ------------------------------------------------------------------------
Standard 5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5
E-Bike   5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5
Kids     5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5
Tandem   5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5
Total    20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20

[Start: Concordia at 5:00, End: McGill at 10:00, Bike: Standard, Amount Due: 5.00]
[Start: UdeM at 12:00, End: UdeM at 15:00, Bike: E-Bike, Amount Due: 15.00]
ID: 1, Membership: Adult
Trip 1: [Start: Concordia at 5:00, End: McGill at 10:00, Bike: Standard, Amount Due: 5.00]
Trip 2: [Start: UdeM at 12:00, End: UdeM at 15:00, Bike: E-Bike, Amount Due: 15.00]
Total amount due: 20.00

=== Bike Inventory of McGill Station ===

Hour     0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23
         ------------------------------------------------------------------------
Standard 5  5  5  5  5  5  5  5  5  5  6  6  6  6  6  6  6  6  6  6  6  6  6  6
E-Bike   5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5
Kids     5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5
Tandem   5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5
Total    20 20 20 20 20 20 20 20 20 20 21 21 21 21 21 21 21 21 21 21 21 21 21 21

[Start: UQAM at 2:00, End: UdeM at 14:00, Bike: Tandem, Amount Due: 28.80]
ID: 2, Membership: Student
Trip 1: [Start: UQAM at 2:00, End: UdeM at 14:00, Bike: Tandem, Amount Due: 28.80]
Total amount due: 28.80

=== Bike Inventory of UdeM Station ===

Hour     0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23
         ------------------------------------------------------------------------
Standard 5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5
E-Bike   5  5  5  5  5  5  5  5  5  5  5  5  4  4  4  5  5  5  5  5  5  5  5  5
Kids     5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5  5
Tandem   5  5  5  5  5  5  5  5  5  5  5  5  5  5  6  6  6  6  6  6  6  6  6  6
Total    20 20 20 20 20 20 20 20 20 20 20 20 19 19 20 21 21 21 21 21 21 21 21 21

The revenue of UdeM Station is 29.40
```

## Current limitations

- **Fixed scenario:** stations, riders and trips are hard-coded in
  `BikeSharingSystem.run()`; there is no user input or menu. The ETS station
  is created but not used by the demo.
- **Single day, whole hours:** times are integers 0–23. There are no minutes,
  dates or multi-day trips.
- **Conservative availability:** because the return hour is unknown when a
  trip starts, a rental is refused if any later hour has no bike of that
  type, even if the new trip would have ended before then.
- **String-based types:** bike types and memberships are plain strings. An
  unknown bike type is refused at rental time; an unknown membership is
  silently charged full price.
- **Fixed trip history:** each rider can store at most 30 trips, and
  `startTrip` returns `false` once the history is full.
- **No persistence:** everything is kept in memory and lost when the program
  ends.
- **Station equality:** `Station.equals` compares stations by revenue only
  and `hashCode` is not overridden, so stations should not be used as keys in
  hash-based collections.

## Author

Yusuf Almahoozi
