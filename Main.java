
import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Car class
class Car {
    private int carId;
    private String brand;
    private String model;
    private double rentalPricePerDay;
    private boolean available;

    public Car(int carId, String brand, String model, double rentalPricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.rentalPricePerDay = rentalPricePerDay;
        this.available = true;
    }

    public int getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void rent() {
        available = false;
    }

    public void returnCar() {
        available = true;
    }

    public double calculatePrice(int days) {
        return rentalPricePerDay * days;
    }
}

// Customer class
class Customer {
    private int customerId;
    private String name;
    private String contact;

    public Customer(int customerId, String name, String contact) {
        this.customerId = customerId;
        this.name = name;
        this.contact = contact;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}

// Rental class with date support
class Rental {
    private Car car;
    private Customer customer;
    private LocalDate startDate;
    private LocalDate endDate;

    public Rental(Car car, Customer customer, LocalDate startDate, LocalDate endDate) {
        this.car = car;
        this.customer = customer;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public long getRentalDays() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }
}

// CarRentalSystem main class
public class Main {
    private static List<Car> cars = new ArrayList<>();
    private static List<Rental> rentals = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int customerCounter = 100;

    public static void main(String[] args) {
        initializeCars();
        menu();
    }

    public static void initializeCars() {
        cars.add(new Car(1, "Toyota", "Camry", 45.0));
        cars.add(new Car(2, "Honda", "Civic", 40.0));
        cars.add(new Car(3, "Ford", "Focus", 42.0));
    }

    public static void menu() {
        int choice;
        do {
            System.out.println("\n=== Car Rental System ===");
            System.out.println("1. View Available Cars");
            System.out.println("2. Rent a Car");
            System.out.println("3. View Rental Records");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewAvailableCars();
                    break;
                case 2:
                    rentCar();
                    break;
                case 3:
                    viewRentals();
                    break;
                case 4:
                    System.out.println("Thank you for using the Car Rental System!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 4);
    }

    public static void viewAvailableCars() {
        System.out.println("\nAvailable Cars:");
        for (Car car : cars) {
            if (car.isAvailable()) {
                System.out.printf("ID: %d | %s %s | Price/Day: $%.2f\n",
                        car.getCarId(), car.getBrand(), car.getModel(), car.getRentalPricePerDay());
            }
        }
    }

    public static void rentCar() {
        viewAvailableCars();
        System.out.print("\nEnter Car ID to rent: ");
        int carId = scanner.nextInt();
        scanner.nextLine();

        Car selectedCar = null;
        for (Car car : cars) {
            if (car.getCarId() == carId && car.isAvailable()) {
                selectedCar = car;
                break;
            }
        }

        if (selectedCar == null) {
            System.out.println("Invalid car selection or car not available.");
            return;
        }

        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter contact number: ");
        String contact = scanner.nextLine();

        System.out.print("Enter rental start date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter rental end date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        long rentalDays = ChronoUnit.DAYS.between(startDate, endDate);
        if (rentalDays <= 0) {
            System.out.println("Invalid rental period. End date must be after start date.");
            return;
        }

        Customer newCustomer = new Customer(customerCounter++, name, contact);
        Rental newRental = new Rental(selectedCar, newCustomer, startDate, endDate);
        selectedCar.rent();
        rentals.add(newRental);

        System.out.println("\n== Rental Information ==");
        System.out.println("Customer ID: " + newCustomer.getCustomerId());
        System.out.println("Customer Name: " + newCustomer.getName());
        System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
        System.out.println("Rental From: " + startDate);
        System.out.println("Rental To: " + endDate);
        System.out.println("Rental Days: " + rentalDays);
        System.out.printf("Total Price: $%.2f%n", selectedCar.calculatePrice((int) rentalDays));
    }

    public static void viewRentals() {
        System.out.println("\nRental Records:");
        for (Rental rental : rentals) {
            System.out.printf("Customer: %s | Car: %s %s | From: %s | To: %s | Days: %d\n",
                    rental.getCustomer().getName(),
                    rental.getCar().getBrand(),
                    rental.getCar().getModel(),
                    rental.getStartDate(),
                    rental.getEndDate(),
                    rental.getRentalDays());
        }
    }
}
