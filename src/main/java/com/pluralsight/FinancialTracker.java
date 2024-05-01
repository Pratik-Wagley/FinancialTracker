package com.pluralsight;

import javax.sound.sampled.Line;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class FinancialTracker {

    private static final ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);


        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\\|");
                if (tokens.length == 5) {
                    LocalDate date = LocalDate.parse(tokens[0], DATE_FORMATTER);
                    LocalTime time = LocalTime.parse(tokens[1], TIME_FORMATTER);
                    String description = tokens[2];
                    String vendor = tokens[3];
                    double price = Double.parseDouble(tokens[4]);
                    transactions.add(new Transaction(date, time, description, vendor, price));
                }

            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    private static void addDeposit(Scanner scanner) {
        System.out.println("Enter the date (yyyy-MM-dd):  ");
        String dateInput = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER);
        System.out.println("Enter the time (HH:mm:ss) ");
        String timeInput = scanner.nextLine();
        System.out.println("Enter a deposit description: ");
        String description = scanner.nextLine();
        LocalTime time = LocalTime.parse(timeInput, TIME_FORMATTER);
        System.out.println("Enter the vendor: ");
        String vendor = scanner.nextLine();
        System.out.println("Enter your deposit amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();


        try {
            if (amount > 0) {
                Transaction deposit = new Transaction(date, time, description, vendor, amount);
                transactions.add(deposit);
                BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true));
                String line;
                for (Transaction transaction : transactions) {
                    String formattedTrans = String.format("%s|%s|%s|%s|%.2f", transaction.getDate(), transaction.getTime(),
                            transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                    bw.write(formattedTrans);
                    bw.newLine();


                }
            }

            //buffer write into csv file
            //write to the file if line = null
        } catch (Exception e) {
            System.out.println("Invalid amount format. Please enter a valid number.");
        }


        // This method should prompt the user to enter the date, time, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Deposit` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
    }

    private static void addPayment(Scanner scanner) {
        System.out.println("Enter the date (yyyy-MM-dd):  ");
        String dateInput = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER);
        System.out.println("Enter the time (HH:mm:ss) ");
        String timeInput = scanner.nextLine();
        LocalTime time = LocalTime.parse(timeInput, TIME_FORMATTER);
        System.out.println("Enter a payment description: ");
        String description = scanner.nextLine();
        System.out.println("Enter the vendor: ");
        String vendor = scanner.nextLine();
        System.out.println("Enter your payment amount: ");
        double amountInput = scanner.nextDouble();
        double amount = amountInput * -1;

        scanner.nextLine();


        try {

            if (amount < 0) {
                Transaction payment = new Transaction(date, time, description, vendor, amount);
                transactions.add(payment);
                BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true));
                String line;
                for (Transaction transaction : transactions) {
                    String formattedTrans = String.format("%s|%s|%s|%s|%.2f", transaction.getDate(), transaction.getTime(),
                            transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                    bw.write(formattedTrans);
                    bw.newLine();


                }
            }

        } catch (Exception e) {
            System.out.println("Invalid amount format. Please enter a valid number.");
        }
        // This method should prompt the user to enter the date, time, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Payment` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        System.out.println("Ledger: ");
        for (Transaction transaction : transactions) {

            System.out.println(transaction);
        }
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, type, and amount.
    }

    private static void displayDeposits() {
        System.out.println("Deposit: ");
        for (Transaction deposit : transactions) {
            System.out.println(deposit);
            // This method should display a table of all deposits in the `transactions` ArrayList.
            // The table should have columns for date, time, vendor, and amount.
        }
    }

    private static void displayPayments() {
        System.out.println("Payment: ");
        for (Transaction payment : transactions) {
            System.out.println(payment);
            // This method should display a table of all payments in the `transactions` ArrayList.
            // The table should have columns for date, time, vendor, and amount.
        }
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    LocalDate today = LocalDate.now();
                    LocalDate startDate = today.withDayOfMonth(1);
                    LocalDate endDate = LocalDate.now();
                    filterTransactionsByDate(startDate, endDate);
                    break;
                // Generate a report for all transactions within the current month,
                // including the date, vendor, and amount for each transaction.
                case "2":
                    LocalDate lastMonth = LocalDate.now().minusMonths(1);
                    LocalDate startDate1 = lastMonth.withDayOfMonth(1);
                    LocalDate endDate1 = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
                    filterTransactionsByDate(startDate1, endDate1);
                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.
                case "3":
                    LocalDate today1 = LocalDate.now();
                    LocalDate startOfYear = today1.withDayOfYear(1);
                    LocalDate endOfYear = today1.withDayOfYear(today1.lengthOfYear());
                    filterTransactionsByDate(startOfYear, endOfYear);
                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.

                case "4":
                    LocalDate lastYear = LocalDate.now().minusYears(1);
                    LocalDate startOfLastYear = lastYear.withDayOfYear(1);
                    LocalDate endOfLastYear = lastYear.withDayOfYear(lastYear.lengthOfYear());

                    filterTransactionsByDate(startOfLastYear, endOfLastYear);
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                case "5":
                    System.out.println("Enter the vendor: ");
                    String vendor = scanner.nextLine();
                    filterTransactionsByVendor(vendor);
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, vendor, and amount for each transaction.
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        boolean foundTransactions = false;
        for (Transaction transaction : transactions) {
            boolean afterDate = !transaction.getDate().isBefore(startDate);
            boolean beforeDate = !transaction.getDate().isAfter(endDate);

            if (afterDate && beforeDate) {
                double positiveNum = Math.abs(transaction.getAmount());
                String formattedTrans = String.format("%s|%s|%s|%s|%.2f", transaction.getDate(), transaction.getTime(),
                        transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                System.out.println(formattedTrans);
                foundTransactions = true;
            }
            if (!foundTransactions) {
                System.out.println("No results, please try again!");
            }

        }

        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {
        boolean foundVendor = false;
        for (Transaction transaction : transactions) {
            if (vendor.equals(transaction.getVendor())) {
                String formattedTrans = String.format("%s|%s|%s|%s|%.2f", transaction.getDate(), transaction.getTime(),
                        transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                System.out.println(formattedTrans);
                foundVendor = true;
            } if (!foundVendor){
                System.out.println("No vendors with that name found, please try again!");
            }
        }


        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }
}