package coffeeklatchassignment;

import java.util.Scanner;

/*
    @author Daniel Allen
    14-Oct-2019
*/
public class Verification {
    public static String getValidatedNumber(String tooltip, boolean allowDecimals) {
        //to verify user's input to prevent errors
        String userInput = "";
        Scanner scan = new Scanner(System.in);
        boolean valid = false;
        while (!valid) {
            System.out.print(tooltip);
            userInput = scan.nextLine();
            valid = isNumbersOnly(userInput, allowDecimals, false);
            if (!valid) {
                System.out.println(ANSI.FG_RED + "Please use only numbers!\n" + ANSI.RESET);
            }
        }
        return userInput;
    }
    public static boolean isNumbersOnly(String input, boolean allowDecimals, boolean allowEmpty){
        if(input == null) {
            return false;
        }
        if(!allowEmpty && input.isEmpty()) {
            return false;
        }
        if(allowDecimals) {
            return !input.matches("[^0-9.]+$");
        } else {
            return !input.matches("[^0-9]+$");
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