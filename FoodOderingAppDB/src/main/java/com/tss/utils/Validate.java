package com.tss.utils;

import java.util.Scanner;

public class Validate {
    static Scanner scanner = new Scanner(System.in);

    public static boolean validateBoolean() {
        boolean temp;
        while (true) {
            if (scanner.hasNextBoolean()) {
                temp = scanner.nextBoolean();
                scanner.nextLine();
                return temp;
            }
            System.out.print("Enter valid value:");
            scanner.nextLine();
        }
    }

    public static int validatePositiveInt() {
        while (true) {
            if (scanner.hasNextInt()) {
                int temp = scanner.nextInt();
                scanner.nextLine();
                if (temp >= 0) {
                    return temp;
                }
                System.out.print("Enter Positive Number: ");
            } else {
                System.out.print("Enter valid number: ");
                scanner.nextLine();
            }
        }
    }

    public static int validatePositiveIntNonZero() {
        while (true) {
            if (scanner.hasNextInt()) {
                int temp = scanner.nextInt();
                scanner.nextLine();
                if (temp > 0) {
                    return temp;
                }
                System.out.print("Enter Positive Number: ");
            } else {
                System.out.print("Enter valid number: ");
                scanner.nextLine();
            }
        }
    }

    public static double validatePositiveDouble() {
        while (true) {
            if (scanner.hasNextDouble()) {
                double temp = scanner.nextDouble();
                scanner.nextLine();
                if (temp >= 0) {
                    return temp;
                }
                System.out.print("Enter positive number: ");
            } else {
                System.out.print("Enter valid number: ");
                scanner.nextLine();
            }
        }
    }

    public static long validatePositiveLong() {
        while (true) {
            if (scanner.hasNextLong()) {
                long temp = scanner.nextLong();
                scanner.nextLine();
                if (temp >= 0) {
                    return temp;
                }
                System.out.print("Enter Positive Number: ");
            } else {
                System.out.print("Enter valid number: ");
                scanner.nextLine();
            }
        }
    }

    public static String validateCharOnlyString() {
        while (true) {
            String temp = scanner.nextLine();
            if (temp.matches("[a-zA-Z ]+")) {
                return temp;
            }
            System.out.print("Enter Characters Only: ");
        }
    }

    public static String validateNonEmptyString() {
        while (true) {
            String temp = scanner.nextLine();
            if (!temp.isEmpty()) {
                return temp;
            }
            System.out.print("Input cannot be empty. Please enter again: ");
        }
    }

    public static boolean validateYesNo() {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.print("Invalid input! Enter 'y' or 'n': ");
            }
        }
    }

    public static String validateCharAndNumberOnlyString() {
        while (true) {
            String temp = scanner.nextLine();
            if (temp.matches("[0-9a-zA-Z ]+")) {
                return temp;
            }
            System.out.print("Enter Characters and Numbers Only: ");
        }
    }

    public static String validatePassword() {
        while (true) {
            String password = scanner.nextLine().trim();
            if (password.length() >= 8) {
                return password;
            }
            System.out.print("Password must be at least 8 characters long: ");
        }
    }

    public static String validateEmail() {
        while (true) {
            String email = scanner.nextLine().trim();

            if (email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                return email;
            }

            System.out.print("Invalid email. Enter again: ");
        }
    }

    public static int validateIntLimit(int limit) {

        while (true) {

            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();

                if (value >= 0 && value <= limit) {
                    return value;
                }
                System.out.print("Enter a number between 0 and " + limit + " : ");

            } else {
                System.out.print("Enter a valid number: ");
                scanner.nextLine();
            }
        }
    }

    public static long validatePhoneNumber() {

        while (true) {

            String input = scanner.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.print("It must contain digits only: ");
                continue;
            }

            if (input.length() != 10) {
                System.out.print("It must be exactly 10 digits: ");
                continue;
            }

            char firstDigit = input.charAt(0);
            if (firstDigit < '6' || firstDigit > '9') {
                System.out.print("It must start with 6, 7, 8 or 9: ");
                continue;
            }

            return Long.parseLong(input);
        }
    }
}