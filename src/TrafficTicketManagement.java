import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Application for managing traffic tickets
 */
public class TrafficTicketManagement {


    /** Keyboard input for any method in this class */
    protected static Scanner keyboard = new Scanner(System.in);


    /** Hashmaps for principal data to track */
    protected static HashMap<String, Driver> drivers = new HashMap<>();
    protected static HashMap<String, Vehicle> vehicles = new HashMap<>();
    protected static HashMap<Integer, Violation> violations = new HashMap<>();
    protected static HashMap<Integer, TrafficTicket> trafficTickets = new HashMap<>();


    /**
     * TrafficTicketManagement
     */
    public static void main(String[] args) throws ParseException, IOException {
        // Load the data from storage to the hashmaps -- method is in FileOperations class
        FileOperations.loadData();
        mostFrequentViolation();

        // Run the menu -- method is in MenuOperations class
        MenuOperations.menu();
    }  // method main


    /**
     * Write a ticket. Method checks if driver is already in database. If so,
     * the existing data are used; otherwise method asks for more information
     * about the driver. Same for vehicle.
     *
     * @throws ParseException
     * @throws IOException
     */
    public static void writeTicket() throws ParseException, IOException {
        // obtain drivers license
        String licenseNumber = AuxiliaryOperations.citedDriver();
        // obtain plate #
        String licensePlate = AuxiliaryOperations.citedPlate();
        // Obtain violation code
        int violationCode = AuxiliaryOperations.citedViolation();
        //take address
        String address = AuxiliaryOperations.citedAddress();
        // take date
        Date ticketDate = AuxiliaryOperations.citedDate();
        // ticket number
        int ticketNumber = FileOperations.maxTicketNumber++;
        // Create ticket object
        TrafficTicket trafficTicket = new TrafficTicket(ticketNumber,
                licenseNumber, licensePlate,
                violationCode,
                address,
                ticketDate);
        // Write tickets file
        FileOperations.writeToFile(FileOperations.TRAFFIC_TICKETS, trafficTicket);
    }


    /** Inactive method - do not implement */
    public static void editTicket() {}  // method editTicket


    /** Inactive method - do not implement */
    public static void searchForTicket() {}  // method searchForTicket


    /**
     * Looks up a driver by license # and reports findings
     */
    public static void searchForDriver() {
        // Get driver info
        System.out.printf("\nEnter a driver's license number: ");
        String driverLicenseNumber = keyboard.next();
        Driver driver = drivers.get(driverLicenseNumber);
        // Driver may not have a record
        if (driver == null) {
            System.out.printf("\nThere is no record for driver license number %s\n",
                    driver.driverLicenseNumber);
        } else {
            System.out.printf("\n%s belongs to %s %s",
                    driver.driverLicenseNumber, driver.firstName, driver.lastName);
            // Header for ticket report
            System.out.printf("\n\n\tTicket date and time\t\t\tLocation\t\t\t\tPlate\t\tViolation");
            // Counter for how many tickets this driver has
            int ticketCounter = 0;
            // Search every ticket in the database but look for the ones matching the entered driver license number
            for (Map.Entry<Integer,TrafficTicket> trafficTicketEntry: trafficTickets.entrySet()) {
                // Get the value V of the <K,V> pair stored in the trafficTickets hashmap;
                // Remember it's a trafficTicket object
                TrafficTicket trafficTicket = trafficTicketEntry.getValue();
                // Is this a ticket for the driver we are looking for?
                if (trafficTicket.driverLicenseNumber.equals(driver.driverLicenseNumber)) {
                    // This is a ticket for the driver we are looking at.
                    // Pull the strings we want to print in the report and pad them for left-flush.
                    String licensePlate = AuxiliaryOperations.pad(trafficTicket.licensePlate, FileOperations.licensePlateMaxLength);
                    String date = trafficTicket.date.toString();
                    String address = AuxiliaryOperations.pad(trafficTicket.address, FileOperations.addressMaxLength);
                    String violation = AuxiliaryOperations.pad(violations.get(trafficTicket.violationCode).violationDescription, FileOperations.violationDescriptionMaxLength);
                    ticketCounter++;
                    System.out.printf("\n\t%s\t%s\t\t%s\t%s", date, address, licensePlate, violation);
                }
            }
            if (ticketCounter == 0) {
                System.out.printf("\nNo tickets found.");
            }
            System.out.printf("\n");
        }
    }  // method searchForDriver

    public static void mostFrequentViolation() {
        // first check if there are any violations
        if (violations == null) {
            System.out.println("\nThere are no violations within the system! \n");
        } else {
            // create new hash map with the elements of violations hashmap as keys and their occurrence as values
            HashMap<Integer, Integer> violationCountMap = new HashMap<Integer, Integer>();
            // insert the violation codes from trafficTickets in the new hashmap
            for (Map.Entry<Integer, TrafficTicket> trafficTicketEntry : trafficTickets.entrySet()) {
                // create new trafficticket object
                TrafficTicket violationCodeCurrent = trafficTicketEntry.getValue();
                // the current key
                int i = violationCodeCurrent.violationCode;
                if (violationCountMap.containsKey(i)) {
                    // if the map already has this key, add 1 to its value
                    violationCountMap.put(i, violationCountMap.get(i) + 1);
                } else {
                    // if the map does not have this key, put it in and set value to 1
                    violationCountMap.put(i, 1);
                }
            }
            // which element shows up the most
            int element = 0;
            // the frequency that element shows up
            int frequency = 1;
            // create entry set of the violationCountMap
            Set<Map.Entry<Integer, Integer>> entrySet = violationCountMap.entrySet();
            // iterate through each (k, v) value in the entrySet
            for (Map.Entry<Integer, Integer> entry : entrySet) {
                // if the value is greater than the frequency
                if(entry.getValue() > frequency) {
                    // change element to the most frequent element so far
                    element = entry.getKey();
                    // change frequency to the amount of times element has occurred
                    frequency = entry.getValue();
                }
            }
            // printing which element is most frequent and its frequency
            System.out.println("The most frequent violation is number: " + element +
                    "\nIt appears this many times: " + frequency);
        }
    } // method mostFrequentViolation

    /** Inactive method - do not implement unless working on OPTION A*/
    public static void searchForPlate() {}  // method searchForPlate


    /** Inactive method - do not implement */
    public static void searchForViolation() {}  // method searchForViolation



}  // class TrafficTicketManagement
