package com.pluralsight;

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
                    System.out.println("Goodbye!");
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
        LocalDate date = null;
        String description = null;
        LocalTime time = null;
        String vendor = null;
        double amount = 0;
        boolean running = true;
        while (running) {
            try {
                System.out.println("Enter the date (yyyy-MM-dd):  ");
                String dateInput = scanner.nextLine();
                date = LocalDate.parse(dateInput, DATE_FORMATTER);
                System.out.println("Enter the time (HH:mm:ss) ");
                String timeInput = scanner.nextLine();
                System.out.println("Enter a deposit description: ");
                description = scanner.nextLine();
                time = LocalTime.parse(timeInput, TIME_FORMATTER);
                System.out.println("Enter the vendor: ");
                vendor = scanner.nextLine();
                System.out.println("Enter your deposit amount: ");
                amount = scanner.nextDouble();
                scanner.nextLine();
                running = false;

            } catch (Exception e) {
                System.out.println("Incorrect format, Please try again!");
                running = true;
            }
        }


        try {
            if (amount > 0) {
                Transaction deposit = new Transaction(date, time, description, vendor, amount);
                transactions.add(deposit);
                BufferedWriter bw = new BufferedWriter(new FileWriter("transactions.csv", true));

                String formattedTrans = String.format("%s|%s|%s|%s|%.2f", deposit.getDate(), deposit.getTime(),
                        deposit.getDescription(), deposit.getVendor(), deposit.getAmount());
                bw.write(formattedTrans);
                bw.newLine();
                bw.close();

            }


        } catch (Exception e) {
            System.out.println("Invalid amount format. Please enter a valid number.");

        }

    }

    private static void addPayment(Scanner scanner) {
        boolean running = true;
        LocalDate date = null;
        String description = null;
        LocalTime time = null;
        String vendor = null;
        double amount = 0;
        while (running) {
            try {
                System.out.println("Enter the date (yyyy-MM-dd):  ");
                String dateInput = scanner.nextLine();
                date = LocalDate.parse(dateInput, DATE_FORMATTER);
                System.out.println("Enter the time (HH:mm:ss) ");
                String timeInput = scanner.nextLine();
                time = LocalTime.parse(timeInput, TIME_FORMATTER);
                System.out.println("Enter a payment description: ");
                description = scanner.nextLine();
                System.out.println("Enter the vendor: ");
                vendor = scanner.nextLine();
                System.out.println("Enter your payment amount: ");
                double amountInput = scanner.nextDouble();
                amount = amountInput * -1;
                scanner.nextLine();
                running = false;

            } catch (Exception e) {
                System.out.println("Invalid format, please try again!");
            }
        }

        try {

            if (amount < 0) {
                Transaction payment = new Transaction(date, time, description, vendor, amount);
                transactions.add(payment);
                BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true));

                String formattedTrans = String.format("%s|%s|%s|%s|%.2f", payment.getDate(), payment.getTime(),
                        payment.getDescription(), payment.getVendor(), payment.getAmount());
                bw.write(formattedTrans);
                bw.newLine();
                bw.close();
            }

        } catch (Exception e) {
            System.out.println("Invalid amount format. Please enter a valid number.");
        }
    }


    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("S) Show Stats");
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
                case "S":
                    displayStatistics();
                    break;
                case "H":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid options");
                    break;
            }
        }
    }

    private static void displayLedger() {
        System.out.println("Ledger: ");
        System.out.printf("%-12s|%-12s|%-25s|%-12s%n", "Date", "Time", "Vendor", "Amount");
        for (Transaction transaction : transactions) {

            String fs = String.format("%-12s|%-12s|%-25s|$%-12f", transaction.getDate(), transaction.getTime(), transaction.getVendor(), transaction.getAmount());
            System.out.println(fs);
        }
    }

    private static void displayDeposits() {
        System.out.println("Deposit: ");
        System.out.println(String.format("%-12s|%-12s|%-25s|%-12s", "Date", "Time", "Vendor", "Amount"));
        for (Transaction deposit : transactions) {
            if (deposit.getAmount() > 0) {

                String fs = String.format("%-12s|%-12s|%-25s|$%-12f", deposit.getDate(), deposit.getTime(), deposit.getVendor(), deposit.getAmount());
                System.out.println(fs);
            }
        }
    }

    private static void displayPayments() {
        System.out.println("Payment: ");
        System.out.printf("%-12s|%-12s|%-25s|%-12s", "Date", "Time", "Vendor", "Amount");
        for (Transaction payment : transactions) {
            if (payment.getAmount() < 0) {
                String fs = String.format("%-12s|%-12s|%-25s|$%-12f", payment.getDate(), payment.getTime(), payment.getVendor(), payment.getAmount());
                System.out.println(fs);
            }
        }
    }

    private static void displayStatistics() {
        double totalDeposits = transactions.stream()
                .filter(transaction -> transaction.getAmount() > 0)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalPayments = transactions.stream()
                .filter(transaction -> transaction.getAmount() < 0)
                .mapToDouble(Transaction::getAmount)
                .sum();

        long depositCount = transactions.stream()
                .filter(transaction -> transaction.getAmount() > 0)
                .count();

        long paymentCount = transactions.stream()
                .filter(transaction -> transaction.getAmount() < 0)
                .count();

        double averageDeposit = totalDeposits / depositCount;
        double averagePayment = totalPayments / paymentCount;

        double balance = totalDeposits + totalPayments;

        System.out.println("=== Transaction Statistics ===");
        System.out.println("Total Deposits: $" + totalDeposits);
        System.out.println("Total Payments: $" + totalPayments);
        System.out.println("Average Deposit: $" + averageDeposit);
        System.out.println("Average Payment: $" + averagePayment);
        System.out.println("Overall Balance: $" + balance);
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
                    LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());
                    filterTransactionsByDate(startDate, endDate);
                    break;
                // Generate a report for all transactions within the current month,
                case "2":
                    LocalDate lastMonth = LocalDate.now().minusMonths(1);
                    LocalDate startDate1 = lastMonth.withDayOfMonth(1);
                    LocalDate endDate1 = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
                    filterTransactionsByDate(startDate1, endDate1);
                    break;
                // Generate a report for all transactions within the previous month,
                case "3":
                    LocalDate today1 = LocalDate.now();
                    LocalDate startOfYear = today1.withDayOfYear(1);
                    LocalDate endOfYear = today1.withDayOfYear(today1.lengthOfYear());
                    filterTransactionsByDate(startOfYear, endOfYear);
                    break;
                // Generate a report for all transactions within the current year,

                case "4":
                    LocalDate lastYear = LocalDate.now().minusYears(1);
                    LocalDate startOfLastYear = lastYear.withDayOfYear(1);
                    LocalDate endOfLastYear = lastYear.withDayOfYear(lastYear.lengthOfYear());
                    filterTransactionsByDate(startOfLastYear, endOfLastYear);
                    break;
                // Generate a report for all transactions within the previous year,
                case "5":
                    System.out.println("Enter the vendor: ");
                    String vendor = scanner.nextLine();
                    filterTransactionsByVendor(vendor);
                    break;
                // Prompt the user to enter a vendor name, then generate a report for all transactions

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
                String formattedTrans = String.format("%-12s|%-12s|%-30s|%-30s|$%-12f", transaction.getDate(), transaction.getTime(),
                        transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                System.out.println(formattedTrans);
                foundTransactions = true;
            }
        }
        if (!foundTransactions) {
            System.out.println("No results, please try again!");

        }

    }

    private static void filterTransactionsByVendor(String vendor) {
        boolean foundVendor = false;
        System.out.printf("%-12s|%-12s|%-30s|%-30s|%-12s%n", "Date", "Time", "Description", "Vendor", "Amount");
        for (Transaction transaction : transactions) {
            if (vendor.equals(transaction.getVendor())) {
                String formattedTrans = String.format("%-12s|%-12s|%-30s|%-30s|$%-12f", transaction.getDate(), transaction.getTime(),
                        transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                System.out.println(formattedTrans);
                foundVendor = true;

            }
        }
        if (!foundVendor) {
            System.out.println("No vendors with that name found, please try again!");

        }

    }
}