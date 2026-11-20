// Roll Number: 202510403
import java.util.Scanner;

public class ATMSimulation {

    public static void main(String[] args) {
        final int CORRECT_PIN = 1234;
        final int MAX_ATTEMPTS = 3;
        final int MAX_TRANSACTIONS = 50;

        int attempts = 0;
        boolean isAuthenticated = false;

        double balance = 99.00;
        String[] transactionHistory = new String[MAX_TRANSACTIONS];
        int transactionCount = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=================================");
            System.out.println("   Welcome to Smart Bank ATM");
            System.out.println("=================================\n");

            // PIN verification loop
            while (attempts < MAX_ATTEMPTS) {
                System.out.print("Please enter your 4-digit PIN: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Numbers only, please. Try again.\n");
                    scanner.next();
                    continue;
                }

                int enteredPin = scanner.nextInt();

                if (enteredPin == CORRECT_PIN) {
                    isAuthenticated = true;
                    System.out.println("\nAccess granted! Welcome back.\n");
                    break;
                } else {
                    attempts++;
                    int remaining = MAX_ATTEMPTS - attempts;
                    if (remaining > 0) {
                        System.out.println("Wrong PIN. You have " + remaining + " attempt(s) left.\n");
                    } else {
                        System.out.println("Wrong PIN. No attempts left.");
                    }
                }
            }

            if (!isAuthenticated) {
                System.out.println("\nToo many failed attempts. Your card has been temporarily locked.");
                return;
            }

            // Main ATM menu loop
            int choice = 0;
            while (choice != 5) {
                printMenu();

                if (!scanner.hasNextInt()) {
                    System.out.println("Please enter a valid menu number (1-5).\n");
                    scanner.next();
                    continue;
                }

                choice = scanner.nextInt();
                System.out.println();

                switch (choice) {
                    case 1 -> System.out.printf("Available Balance: $%.2f%n%n", balance);

                    case 2 -> {
                        System.out.print("How much would you like to deposit? $");
                        if (!scanner.hasNextDouble()) {
                            System.out.println("Invalid input. Deposit cancelled.\n");
                            scanner.next();
                            break;
                        }

                        double depositAmount = scanner.nextDouble();
                        if (depositAmount > 0) {
                            balance += depositAmount;
                            recordTransaction(transactionHistory, transactionCount,
                                    String.format("Deposit  | +$%.2f | Balance: $%.2f", depositAmount, balance));
                            if (transactionCount < MAX_TRANSACTIONS) {
                                transactionCount++;
                            }
                            System.out.printf("Done! $%.2f added. New balance: $%.2f%n%n", depositAmount, balance);
                        } else {
                            System.out.println("Please enter an amount greater than $0.00.\n");
                        }
                    }

                    case 3 -> {
                        System.out.print("How much would you like to withdraw? $");
                        if (!scanner.hasNextDouble()) {
                            System.out.println("Invalid input. Withdrawal cancelled.\n");
                            scanner.next();
                            break;
                        }

                        double withdrawAmount = scanner.nextDouble();
                        if (withdrawAmount <= 0) {
                            System.out.println("Please enter a valid positive amount.\n");
                        } else if (withdrawAmount > balance) {
                            System.out.printf("Insufficient funds. Your current balance is $%.2f.%n%n", balance);
                        } else {
                            balance -= withdrawAmount;
                            recordTransaction(transactionHistory, transactionCount,
                                    String.format("Withdraw | -$%.2f | Balance: $%.2f", withdrawAmount, balance));
                            if (transactionCount < MAX_TRANSACTIONS) {
                                transactionCount++;
                            }
                            System.out.printf("Please take your cash ($%.2f). Remaining balance: $%.2f%n%n",
                                    withdrawAmount, balance);
                        }
                    }

                    case 4 -> {
                        System.out.println("---------- Activity Log ----------");
                        if (transactionCount == 0) {
                            System.out.println("No activity recorded in this session yet.");
                        } else {
                            for (int i = 0; i < transactionCount; i++) {
                                System.out.println((i + 1) + ". " + transactionHistory[i]);
                            }
                        }
                        System.out.println("----------------------------------\n");
                    }

                    case 5 -> System.out.println("Session ended. Have a great day!");

                    default -> System.out.println("Unknown option (" + choice + "). Please pick from 1 to 5.\n");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("=== Main Menu ===");
        System.out.println("1. View Balance");
        System.out.println("2. Deposit Cash");
        System.out.println("3. Withdraw Cash");
        System.out.println("4. Activity Log");
        System.out.println("5. Exit");
        System.out.print("Select an option: ");
    }

    private static void recordTransaction(String[] history, int index, String entry) {
        if (index < history.length) {
            history[index] = entry;
        }
    }
}