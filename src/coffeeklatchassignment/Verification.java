package coffeeklatchassignment;

import java.util.Scanner;

/*
    @author Daniel Allen
    14-Oct-2019
*/
public class Verification {
    public static double getValidatedNumber(String tooltip, boolean allowDecimals, boolean allowNegative, boolean allowInfinity) {
        //to verify user's input to prevent errors
        String userInput = "";
        Scanner scan = new Scanner(System.in);
        boolean valid = false;
        while (!valid) {
            System.out.print(tooltip);
            userInput = scan.nextLine();
            valid = isNumbersOnly(userInput, allowDecimals, allowNegative, allowInfinity, false);
            if (!valid) {
                System.out.println(ANSI.FG_RED + "Please use only numbers!\n" + ANSI.RESET);
            }
        }
        if(userInput.matches("[0-9.-]+$")){
            return Double.parseDouble(userInput);
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }
    public static boolean isNumbersOnly(String input, boolean allowDecimals, boolean allowNegative, boolean allowInfinity, boolean allowEmpty){
        if(input == null) {
            return false;
        }
        if(!allowEmpty && input.isEmpty()) {
            return false;
        }
        if(allowInfinity && (input.equalsIgnoreCase("Infinite") || input.equalsIgnoreCase("Infinity") || input.equalsIgnoreCase("∞"))){
            return true;
        }
        if(allowDecimals) {
            if(allowNegative) {
                return input.matches("[0-9.-]+$");
            }
            return input.matches("[0-9.]+$");
        } else {
            if(allowNegative) {
                return input.matches("[0-9-]+$");
            }
            return input.matches("[0-9]+$");
        }
    }
    public static String getValidatedBoolean(String tooltip, boolean allowNull) {
        //to verify user's input to prevent errors
        String userInput = "";
        Scanner scan = new Scanner(System.in);
        boolean valid = false;
        while (!valid) {
            userInput = scan.nextLine();
            if(userInput.equalsIgnoreCase("true") || userInput.equalsIgnoreCase("false") || (userInput.equalsIgnoreCase("null") && allowNull)){
                valid = true;
            } else {
                if(allowNull) {
                    System.out.println(ANSI.FG_RED + "Please use only 'true', 'false', or 'null'!" + ANSI.RESET);
                } else {
                    System.out.println(ANSI.FG_RED + "Please use only 'true' or 'false'!" + ANSI.RESET);
                }
            }
        }
        return userInput;
    }
}