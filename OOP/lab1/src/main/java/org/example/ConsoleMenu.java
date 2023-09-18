package org.example;

import org.example.entities.Customer;
import org.example.entities.HomeTariff;
import org.example.entities.MobileTariff;
import org.example.filters.HomeTariffFilter;
import org.example.filters.MobileTariffFilter;
import org.example.services.CustomerService;
import org.example.services.HomeTariffService;
import org.example.services.MobileTariffService;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ConsoleMenu {
    private CustomerService customerService;
    private HomeTariffService homeTariffService;
    private MobileTariffService mobileTariffService;
    private Scanner scanner;

    public ConsoleMenu() {
        //this.disableLogging();
        this.customerService = new CustomerService();
        this.homeTariffService = new HomeTariffService();
        this.mobileTariffService = new MobileTariffService();
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("===== Main Menu =====");
            System.out.println("1. Customer Operations");
            System.out.println("2. Home Tariff Operations");
            System.out.println("3. Mobile Tariff Operations");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = this.scanner.nextInt();
            this.scanner.nextLine();

            switch (choice) {
                case 1 -> this.customerMenu();
                case 2 -> this.homeTariffMenu();
                case 3 -> this.mobileTariffMenu();
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private void customerMenu() {
        int choice;
        do {
            System.out.println("===== Customer Menu =====");
            System.out.println("1. Find Customer by ID");
            System.out.println("2. Find All Customers");
            System.out.println("3. Count All Customers");
            System.out.println("4. Save Customer");
            System.out.println("5. Update Customer");
            System.out.println("6. Delete Customer");
            System.out.println("7. Add Mobile Tariff to Customer");
            System.out.println("8. Remove Mobile Tariff from Customer");
            System.out.println("9. Add Home Tariff to Customer");
            System.out.println("10. Remove Home Tariff from Customer");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = this.scanner.nextInt();
            this.scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter customer ID: ");
                    long customerId = this.scanner.nextLong();
                    Customer customer = this.customerService.findById(customerId);
                    if (customer != null) {
                        System.out.println("Customer found: " + customer.getName());
                    } else {
                        System.out.println("Customer not found.");
                    }
                }
                case 2 -> {
                    List<Customer> customers = this.customerService.findAll();
                    if (customers.isEmpty()) {
                        System.out.println("No customers");
                    } else {
                        for (Customer c : customers) {
                            System.out.println(c);
                        }
                    }
                }
                case 3 -> {
                    Long customerCount = this.customerService.countAll();
                    System.out.println("Total number of customers: " + customerCount);
                }
                case 4 -> {
                    System.out.println("Enter customer name:");
                    String name = this.scanner.nextLine();
                    if (name != null) {
                        this.customerService.saveCustomer(new Customer(name));
                    }
                    else {
                        System.out.println("Invalid input. Please try again.");
                    }
                }
                case 5 -> {
                    System.out.print("Enter customer ID to update: ");
                    long updateCustomerId = this.scanner.nextLong();
                    Customer updatedCustomer = this.customerService.findById(updateCustomerId);
                    if (updatedCustomer != null) {
                        System.out.println(updatedCustomer);
                        System.out.println("Enter updated customer name:");
                        String name = this.scanner.nextLine();
                        if (name != null) {
                            updatedCustomer.setName(name);
                            this.customerService.updateCustomer(updatedCustomer);
                        }
                        else {
                            System.out.println("Invalid input. Please try again.");
                        }
                    } else {
                        System.out.println("Customer not found.");
                    }
                }
                case 6 -> {
                    System.out.print("Enter customer ID to delete: ");
                    long deleteCustomerId = this.scanner.nextLong();
                    Customer deleteCustomer = this.customerService.findById(deleteCustomerId);
                    if (deleteCustomer != null) {
                        this.customerService.deleteCustomer(deleteCustomer);
                        System.out.println("Customer deleted.");
                    } else {
                        System.out.println("Customer not found.");
                    }
                }
                case 7 -> {
                    System.out.print("Enter customer ID to add mobile tariff: ");
                    long customerId = this.scanner.nextLong();
                    Customer customer = this.customerService.findById(customerId);
                    if (customer != null) {
                        System.out.print("Enter mobile tariff ID to add: ");
                        long mobileTariffId = this.scanner.nextLong();
                        MobileTariff mobileTariff = this.mobileTariffService.findById(mobileTariffId);
                        if(mobileTariff != null) {
                            this.customerService.addMobileTariff(customer, mobileTariff);
                        } else {
                            System.out.println("Mobile tariff not found.");
                        }
                    } else {
                        System.out.println("Customer not found.");
                    }
                }
                case 8 -> {
                    System.out.print("Enter customer ID to delete mobile tariff: ");
                    long customerId = this.scanner.nextLong();
                    Customer customer = this.customerService.findById(customerId);
                    if (customer != null) {
                        System.out.print("Enter mobile tariff ID to delete: ");
                        long mobileTariffId = this.scanner.nextLong();
                        MobileTariff mobileTariff = this.mobileTariffService.findById(mobileTariffId);
                        if(mobileTariff != null) {
                            this.customerService.removeMobileTariff(customer, mobileTariff);
                        } else {
                            System.out.println("Mobile tariff not found.");
                        }
                    } else {
                        System.out.println("Customer not found.");
                    }
                }
                case 9 -> {
                    System.out.print("Enter customer ID to add home tariff: ");
                    long customerId = this.scanner.nextLong();
                    Customer customer = this.customerService.findById(customerId);
                    if (customer != null) {
                        System.out.print("Enter home tariff ID to add: ");
                        long homeTariffId = this.scanner.nextLong();
                        HomeTariff homeTariff = this.homeTariffService.findById(homeTariffId);
                        if(homeTariff != null) {
                            this.customerService.addHomeTariff(customer, homeTariff);
                        } else {
                            System.out.println("Home tariff not found.");
                        }
                    } else {
                        System.out.println("Customer not found.");
                    }
                }
                case 10 -> {
                    System.out.print("Enter customer ID to remove home tariff: ");
                    long customerId = this.scanner.nextLong();
                    Customer customer = this.customerService.findById(customerId);
                    if (customer != null) {
                        System.out.print("Enter home tariff ID to remove: ");
                        long homeTariffId = this.scanner.nextLong();
                        HomeTariff homeTariff = this.homeTariffService.findById(homeTariffId);
                        if(homeTariff != null) {
                            this.customerService.removeHomeTariff(customer, homeTariff);
                        } else {
                            System.out.println("Home tariff not found.");
                        }
                    } else {
                        System.out.println("Customer not found.");
                    }
                }
                case 0 -> {
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }


    private void mobileTariffMenu() {
        int choice;
        do {
            System.out.println("===== Mobile Tariff Menu =====");
            System.out.println("1. Find Mobile Tariff by ID");
            System.out.println("2. Find All Mobile Tariffs");
            System.out.println("3. Sort Mobile Tariffs by Price");
            System.out.println("4. Count All Mobile Tariffs");
            System.out.println("5. Save Mobile Tariff");
            System.out.println("6. Update Mobile Tariff");
            System.out.println("7. Delete Mobile Tariff");
            System.out.println("8. Find Mobile Tariffs by Filter");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = this.scanner.nextInt();
            this.scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter mobile tariff ID: ");
                    long mobileTariffId = this.scanner.nextLong();
                    MobileTariff mobileTariff = this.mobileTariffService.findById(mobileTariffId);
                    if (mobileTariff != null) {
                        System.out.println(mobileTariff);
                    } else {
                        System.out.println("Mobile Tariff not found.");
                    }
                }
                case 2 -> {
                    List<MobileTariff> mobileTariffs = this.mobileTariffService.findAll();
                    for (MobileTariff mt : mobileTariffs) {
                        System.out.println(mt);
                    }
                }
                case 3 -> {
                    List<MobileTariff> sortedMobileTariffs = this.mobileTariffService.sortByPrice();
                    for (MobileTariff mt : sortedMobileTariffs) {
                        System.out.println(mt);
                    }
                }
                case 4 -> {
                    Long mobileTariffCount = this.mobileTariffService.countAll();
                    System.out.println("Total number of mobile tariffs: " + mobileTariffCount);
                }
                case 5 -> {
                    System.out.println("Enter updated mobile tariff details... (skip if no updates)");

                    String name = this.getStringInput("Enter mobile tariff name:");
                    Integer pricePerMonth = this.getIntegerInput("Enter mobile tariff price per month:");
                    Integer minutes = this.getIntegerInput("Enter mobile tariff minutes per month:");
                    Integer sms = this.getIntegerInput("Enter mobile tariff SMS per month:");

                    if(name != null && pricePerMonth != null && minutes != null && sms != null) {
                        this.mobileTariffService.saveMobileTariff(new MobileTariff(name, pricePerMonth, minutes, sms));
                    }

                    else{
                        System.out.println("Invalid input. Please try again.");
                    }
                }
                case 6 -> {
                    System.out.print("Enter mobile tariff ID to update: ");
                    long updateMobileTariffId = this.scanner.nextLong();
                    MobileTariff updatedMobileTariff = this.mobileTariffService.findById(updateMobileTariffId);
                    if (updatedMobileTariff != null) {
                        System.out.println("Enter updated mobile tariff details... (skip if no updates)");

                        String name = this.getStringInput("Enter mobile tariff new name:");
                        Integer pricePerMonth = this.getIntegerInput("Enter mobile tariff new price per month:");
                        Integer minutes = this.getIntegerInput("Enter mobile tariff new minutes per month:");
                        Integer sms = this.getIntegerInput("Enter mobile tariff new SMS per month:");

                        if (name != null) {
                            updatedMobileTariff.setName(name);
                        }
                        if (pricePerMonth != null) {
                            updatedMobileTariff.setPricePerMonth(pricePerMonth);
                        }
                        if (minutes != null) {
                            updatedMobileTariff.setMinutes(minutes);
                        }
                        if (sms != null) {
                            updatedMobileTariff.setSms(sms);
                        }

                        this.mobileTariffService.updateMobileTariff(updatedMobileTariff);
                    } else {
                        System.out.println("Mobile Tariff not found.");
                    }
                }
                case 7 -> {
                    System.out.print("Enter mobile tariff ID to delete: ");
                    long deleteMobileTariffId = this.scanner.nextLong();
                    MobileTariff deleteMobileTariff = this.mobileTariffService.findById(deleteMobileTariffId);
                    if (deleteMobileTariff != null) {
                        this.mobileTariffService.deleteMobileTariff(deleteMobileTariff);
                        System.out.println("Mobile Tariff deleted.");
                    } else {
                        System.out.println("Mobile Tariff not found.");
                    }
                }
                case 8 -> {
                    MobileTariffFilter filter = new MobileTariffFilter();

                    Integer minPrice = this.getIntegerInput("Enter minimal price:");
                    Integer maxPrice = this.getIntegerInput("Enter maximum price:");
                    Integer minMinutes = this.getIntegerInput("Enter minimal minutes:");
                    Integer maxMinutes = this.getIntegerInput("Enter maximum minutes:");
                    Integer minSms = this.getIntegerInput("Enter minimum SMS:");
                    Integer maxSms = this.getIntegerInput("Enter maximum SMS:");

                    if (minPrice != null) {
                        filter.setMinPricePerMonth(minPrice);
                    }
                    if (maxPrice != null) {
                        filter.setMaxPricePerMonth(maxPrice);
                    }
                    if (minMinutes != null) {
                        filter.setMinMinutes(minMinutes);
                    }
                    if (maxMinutes != null) {
                        filter.setMaxMinutes(maxMinutes);
                    }
                    if (minSms != null) {
                        filter.setMinSms(minSms);
                    }
                    if (maxSms != null) {
                        filter.setMaxSms(maxSms);
                    }

                    List<MobileTariff> mobileTariffs = this.mobileTariffService.findByFilter(filter);
                    if (mobileTariffs.isEmpty()) {
                        System.out.println("No mobile tariffs within such a range");
                    } else {
                        for (MobileTariff m : mobileTariffs) {
                            System.out.println(m);
                        }
                    }
                }
                case 0 -> {
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }while (choice != 0);
    }

    private void homeTariffMenu() {
        int choice;
        do {
            System.out.println("===== Home Tariff Menu =====");
            System.out.println("1. Find Home Tariff by ID");
            System.out.println("2. Find All Home Tariffs");
            System.out.println("3. Sort Home Tariffs by Price");
            System.out.println("4. Count All Home Tariffs");
            System.out.println("5. Save Home Tariff");
            System.out.println("6. Update Home Tariff");
            System.out.println("7. Delete Home Tariff");
            System.out.println("8. Find Home Tariffs by Filter");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = this.scanner.nextInt();
            this.scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter home tariff ID: ");
                    long homeTariffId = this.scanner.nextLong();
                    HomeTariff homeTariff = this.homeTariffService.findById(homeTariffId);
                    if (homeTariff != null) {
                        System.out.println(homeTariff);
                    } else {
                        System.out.println("Home Tariff not found.");
                    }
                }
                case 2 -> {
                    List<HomeTariff> homeTariffs = this.homeTariffService.findAll();
                    for (HomeTariff ht : homeTariffs) {
                        System.out.println(ht);
                    }
                }
                case 3 -> {
                    List<HomeTariff> sortedHomeTariffs = this.homeTariffService.sortByPrice();
                    for (HomeTariff ht : sortedHomeTariffs) {
                        System.out.println(ht);
                    }
                }
                case 4 -> {
                    Long homeTariffCount = this.homeTariffService.countAll();
                    System.out.println("Total number of home tariffs: " + homeTariffCount);
                }
                case 5 -> {
                    System.out.println("Enter updated mobile tariff details... (skip if no updates)");

                    String name = this.getStringInput("Enter home tariff name:");
                    Integer pricePerMonth = this.getIntegerInput("Enter home tariff price per month:");
                    Integer dataAllowance = this.getIntegerInput("Enter home tariff data allowance per month:");
                    Integer speedMbps = this.getIntegerInput("Enter home tariff speed mbps per month:");

                    if(name != null && pricePerMonth != null && dataAllowance != null && speedMbps != null) {
                        this.homeTariffService.saveHomeTariff(
                                new HomeTariff(name, pricePerMonth, dataAllowance, speedMbps));
                    }

                    else{
                        System.out.println("Invalid input. Please try again.");
                    }
                }
                case 6 -> {
                    System.out.print("Enter home tariff ID to update: ");
                    long updateHomeTariffId = this.scanner.nextLong();
                    HomeTariff updatedHomeTariff = this.homeTariffService.findById(updateHomeTariffId);
                    if (updatedHomeTariff != null) {
                        System.out.println("Enter updated home tariff details... (skip if no updates)");

                        String name = this.getStringInput("Enter home tariff new name:");
                        Integer pricePerMonth = this.getIntegerInput("Enter home tariff new price per month:");
                        Integer dataAllowance = this.getIntegerInput("Enter home tariff new data allowance per month:");
                        Integer speedMbps = this.getIntegerInput("Enter home tariff new speed mbps per month:");

                        if (name != null) {
                            updatedHomeTariff.setName(name);
                        }
                        if (pricePerMonth != null) {
                            updatedHomeTariff.setPricePerMonth(pricePerMonth);
                        }
                        if (dataAllowance != null) {
                            updatedHomeTariff.setDataAllowanceMb(dataAllowance);
                        }
                        if (speedMbps != null) {
                            updatedHomeTariff.setSpeedMbps(speedMbps);
                        }

                        this.homeTariffService.updateHomeTariff(updatedHomeTariff);
                    } else {
                        System.out.println("Home Tariff not found.");
                    }
                }
                case 7 -> {
                    System.out.print("Enter home tariff ID to delete: ");
                    long deleteHomeTariffId = this.scanner.nextLong();
                    HomeTariff deleteHomeTariff = this.homeTariffService.findById(deleteHomeTariffId);
                    if (deleteHomeTariff != null) {
                        this.homeTariffService.deleteHomeTariff(deleteHomeTariff);
                        System.out.println("Home Tariff deleted.");
                    } else {
                        System.out.println("Home Tariff not found.");
                    }
                }
                case 8 -> {
                    HomeTariffFilter filter = new HomeTariffFilter();

                    Integer minPrice = this.getIntegerInput("Enter minimal price:");
                    Integer maxPrice = this.getIntegerInput("Enter maximum price:");
                    Integer minDataAllowanceMb = this.getIntegerInput("Enter minimal data allowance (MB):");
                    Integer maxDataAllowanceMb = this.getIntegerInput("Enter maximum data allowance (MB):");
                    Integer minSpeedMbps = this.getIntegerInput("Enter minimal speed (Mbps):");
                    Integer maxSpeedMbps = this.getIntegerInput("Enter maximum speed (Mbps):");

                    if (minPrice != null) {
                        filter.setMinPricePerMonth(minPrice);
                    }
                    if (maxPrice != null) {
                        filter.setMaxPricePerMonth(maxPrice);
                    }
                    if (minDataAllowanceMb != null) {
                        filter.setMinDataAllowanceMb(minDataAllowanceMb);
                    }
                    if (maxDataAllowanceMb != null) {
                        filter.setMaxDataAllowanceMb(maxDataAllowanceMb);
                    }
                    if (minSpeedMbps != null) {
                        filter.setMinSpeedMbps(minSpeedMbps);
                    }
                    if (maxSpeedMbps != null) {
                        filter.setMaxSpeedMbps(maxSpeedMbps);
                    }

                    List<HomeTariff> homeTariffs = this.homeTariffService.findByFilter(filter);
                    if (homeTariffs.isEmpty()) {
                        System.out.println("No home tariffs within such a range");
                    } else {
                        for (HomeTariff ht : homeTariffs) {
                            System.out.println(ht);
                        }
                    }
                }
                case 0 -> {
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private String getStringInput(String message) {
        System.out.print(message);
        String input = this.scanner.nextLine().trim();
        return input.isEmpty() ? null : input;
    }

    private Long getLongInput(String message) {
        while (true) {
            System.out.print(message);
            String input = this.scanner.nextLine();
            if (input.isEmpty()) {
                return null; // Input is empty, return null to indicate "skip"
            }
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private Integer getIntegerInput(String message) {
        while (true) {
            System.out.print(message);
            String input = this.scanner.nextLine();
            if (input.isEmpty()) {
                return null; // Input is empty, return null to indicate "skip"
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private void disableLogging() {
        LogManager logManager = LogManager.getLogManager();
        Logger logger = logManager.getLogger("");
        logger.setLevel(Level.SEVERE); //could be Level.OFF
    }

    public static void main(String[] args) {
        ConsoleMenu menu = new ConsoleMenu();
        menu.displayMenu();
    }
}

