package coffeeklatchassignment;

import static coffeeklatchassignment.Other.*;
import java.util.Scanner;
import static coffeeklatchassignment.Verification.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Daniel Allen
 */
public class CoffeeKlatchAssignment {

    //open a scanner
    static Scanner scan = new Scanner(System.in);

    //map advanced commands in a sortedset to keep classes together and to sort them alphabetically.
    final static SortedSet<Entry<String, Method>> advancedCommands = new TreeSet<>(new Comparator<Entry<String, Method>>() {
        @Override
        public int compare(Entry<String, Method> t, Entry<String, Method> t1) {
            return t.getKey().compareTo(t1.getKey());
        }
    });

    static {
        //add all methods from the CoffeeMachine and CoffeeCup classes that contain the "advanced" annotation to the set
        for (Method m : CoffeeMachine.class.getMethods()) {
            if (m.isAnnotationPresent(advanced.class)) {
                advancedCommands.add(new AbstractMap.SimpleEntry<>(m.getDeclaringClass().getSimpleName() + "#" + m.getName() + "(" + Stream.of(m.getParameterTypes()).map(e -> e.toString()).collect(Collectors.joining(", ")) + ")", m));
            }
        }
        for (Method m : CoffeeCup.class.getMethods()) {
            if (m.isAnnotationPresent(advanced.class)) {
                advancedCommands.add(new AbstractMap.SimpleEntry<>(m.getDeclaringClass().getSimpleName() + "#" + m.getName() + "(" + Stream.of(m.getParameterTypes()).map(e -> e.toString()).collect(Collectors.joining(", ")) + ")", m));
            }
        }
    }
    static CoffeeMachine coffeeMachine;
    static CoffeeCup cup;

    //create a log file and writer
    static File logFile = new File((System.getProperty("user.dir") + "\\log.txt"));
    static FileWriter fw = null;

    static {
        try {
            fw = new FileWriter(logFile);
        } catch (IOException ex) {

        }
    }

    /**
     * Outputs text to a log file for debugging purposes.
     * @param toLog Text to output
     */
    public static void log(String toLog) {
        try {
            fw.write(toLog);
        } catch (IOException ex) {

        }
    }

    //keep track of time for debugging.
    static long startTime = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //load the start time
        startTime = System.currentTimeMillis();

        //define the CoffeeCup and CoffeeMachine
        coffeeMachine = new CoffeeMachine();
        CoffeeCup cup = new CoffeeCup();

        //keep track of whether to exit the program or not
        boolean exit = false;

        //store decimalformats
        DecimalFormat df = new DecimalFormat("0.##");
        DecimalFormat timeFormat = new DecimalFormat("00");
        DecimalFormat oneDecimal = new DecimalFormat("0.0");
        DecimalFormat noDecimals = new DecimalFormat("0");

        //welcome the user
        System.out.println("***********************************\n"
                + "*  Willkommen beim Kaffeeklatsch  *\n"
                + "***********************************");
        //loop until a user powers off the machine
        while (!exit) {
            //get the user's name
            System.out.println(ANSI.FG_BLUE + "What's your name?" + ANSI.RESET);
            String name = scan.nextLine().trim();
            if (name.isEmpty()) {
                //if the user's name was empty, replace it with the profile name from the system
                name = capitalizeFully(System.getProperty("user.name"), " ", " ");
            }
            System.out.println(ANSI.FG_DARK_GREEN + "Welcome, " + name + ANSI.RESET);

            //ask what strength of coffee the user wants.
            System.out.println(ANSI.FG_BLUE + "What strength would you like your coffee?" + ANSI.RESET);
            coffeeMachine.setStrength(scan.nextLine());

            //display the cup sizes
            System.out.println("\n-------------------------------------------------");
            System.out.println("|                     Sizes                     |");
            for (int i = 0; i < Sizes.values().length; i++) {
                System.out.format("|     " + (i + 1) + ": %1$-26s%2$8s     |%n", Sizes.values()[i], noDecimals.format(Sizes.values()[i].getVolume() * 1000) + "mL");
            }
            System.out.println("-------------------------------------------------");

            //prompt the user to enter a cup size
            System.out.println(ANSI.RESET + "\n" + ANSI.FG_BLUE + "What size of cup would you like?" + ANSI.RESET);
            boolean valid = false;
            while (!valid) {
                int size = (int) getValidatedNumber("Enter a size: ", false, false, false);
                if (size < 1 || size > Sizes.values().length) {
                    valid = false;
                    System.out.println(ANSI.FG_RED + "Unknown size. Please select one from the list above." + ANSI.RESET);
                } else {
                    cup = new CoffeeCup(name, size);
                    System.out.println(ANSI.FG_DARK_GREEN + "Selected size \"" + cup.getSize() + "\"" + ANSI.RESET);
                    valid = true;
                }
            }
            //keep track of if a user is logged in
            boolean hasUser = true;

            while (hasUser) {
                System.out.println("-------------------------------------------------");
                String format = "%1$-10s%2$-10s%3$-10s%4$-10s%5$-10s\n";
                System.out.println("Cup Size: " + cup.getSize());
                System.out.println(ANSI.RESET + "Coffee in cup: " + df.format(cup.getLitres() * 1000) + "mL" + ANSI.BOLD + "/" + ANSI.RESET + df.format(cup.getSize().getVolume() * 1000) + "mL");
                //<editor-fold defaultstate="collapsed" desc="Header Labels for Coffee Machine Data">
                String hasBeanLabel = "Beans",
                        isGroundLabel = "Ground",
                        waterLabel = "Water",
                        brewedLabel = "Brewed",
                        strengthLabel = "Strength";
                //print the header labels
                System.out.format(format, center(hasBeanLabel, 9), center(isGroundLabel, 9), center(waterLabel, 9), center(brewedLabel, 9), center(strengthLabel, Math.max(coffeeMachine.getStrength().length(), 9)));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Body Labels for Coffee Machine Data">
                String formattedBeans = coffeeMachine.hasBeans()
                        ? Symbols.CHECK_MARK
                        : Symbols.X_MARK,
                        formattedBrewed = coffeeMachine.isBrewed()
                        ? Symbols.CHECK_MARK
                        : Symbols.X_MARK,
                        formattedGround = coffeeMachine.hasBeans()
                        ? coffeeMachine.isGround()
                        ? Symbols.CHECK_MARK
                        : Symbols.X_MARK
                        : "-",
                        formattedWater = oneDecimal.format(coffeeMachine.getWater()) + "L";
                //print the data labels
                System.out.format(format,
                        (coffeeMachine.hasBeans() ? ANSI.FG_DARK_GREEN : ANSI.FG_RED) + center(formattedBeans, 9) + ANSI.RESET,
                        (coffeeMachine.hasBeans() ? coffeeMachine.isGround() ? ANSI.FG_DARK_GREEN : ANSI.FG_RED : "") + center(formattedGround, 9) + ANSI.RESET,
                        "  " + (coffeeMachine.hasWater() ? ANSI.FG_DARK_GREEN : ANSI.FG_RED) + center(formattedWater, 9) + ANSI.RESET,
                        (coffeeMachine.isBrewed() ? ANSI.FG_DARK_GREEN : ANSI.FG_RED) + center(formattedBrewed, 9) + ANSI.RESET,
                        "  " + center(coffeeMachine.getStrength(), Math.max(coffeeMachine.getStrength().length(), 9)));
                //</editor-fold>

                //display the menu
                System.out.println("-------------------------------------------------");
                System.out.println("|                    Commands                   |");
                System.out.println("|   S: Set the Strength of the Coffee           |");
                System.out.println("|   C: Add Coffee Beans to Machine              |");
                System.out.println("|   G: Grind Beans in Machine                   |");
                System.out.println("|   W: Add Water to Machine                     |");
                System.out.println("|   B: Brew the Coffee                          |");
                System.out.println("|                                               |");
                System.out.println("|   P: Pour a Cup from the Machine              |");
                System.out.println("|   D: Drink the Cup                            |");
                System.out.println("|                                               |");
                System.out.println("|   A: Advanced Commands                        |");
                System.out.println("|                                               |");
                System.out.println("|   N: New User                                 |");
                System.out.println("|   O: Turn off machine                         |");
                System.out.println("-------------------------------------------------");
                System.out.print(ANSI.FG_BLUE + "Enter your command: " + ANSI.RESET);

                //get the user's command
                String command = scan.nextLine();

                //log the command and time
                long secs = (System.currentTimeMillis()-startTime)/1000;
                long minutes = 0;
                while(secs > 60){
                    minutes++;
                    secs-=60;
                }
                log(timeFormat.format(minutes) + ":" + timeFormat.format(secs)+ " - "+command+"\n");


                //handle the command via the appropriate method
                if (command.equalsIgnoreCase("W")) {
                    coffeeMachine.addWater();
                } else if (command.equalsIgnoreCase("C")) {
                    coffeeMachine.addBeans();
                } else if (command.equalsIgnoreCase("G")) {
                    coffeeMachine.grindBeans();
                } else if (command.equalsIgnoreCase("S")) {
                    System.out.print("Strength: ");
                    coffeeMachine.setStrength(scan.nextLine());
                } else if (command.equalsIgnoreCase("B")) {
                    coffeeMachine.brew();
                } else if (command.equalsIgnoreCase("P")) {
                    coffeeMachine.pour(cup);
                } else if (command.equalsIgnoreCase("D")) {
                    cup.drink();
                } else if (command.equalsIgnoreCase("A")) {
                    advancedCommands();
                } else if (command.equalsIgnoreCase("N")) {

                    //stop the innermost loop to get a new user from the outer loop
                    hasUser = false;

                    //print a goodbye message
                    System.out.println(ANSI.FG_DARK_GREEN + "Please come again :)" + ANSI.RESET);

                    //wait a bit so the user can read the message
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {

                    }

                    //add spacing for the next user
                    System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                } else if (command.equalsIgnoreCase("O")) {
                    hasUser = false;
                    exit = true;
                } else {
                    //if the command is not recognized, print an error message
                    System.out.println(ANSI.FG_RED + "Unknown command. Please select one from the list above." + ANSI.RESET);
                }
            }
        }
        System.out.print("\nShutting down");
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(333);
                System.out.print(".");
            } catch (InterruptedException ex) {
                log(ex.getMessage());
            }
        }
        try {
            scan.close();
            fw.close();
            //pause for effect
            Thread.sleep(1000);
        } catch (InterruptedException | IOException ex) {
            log(ex.getMessage());
        }
    }

    /**
     * This lets the user use more specific commands than provided in the menu
     * using reflection.
     */
    public static void advancedCommands() {
        /*I got bored and wanted to overcomplicate the program.
         This is way less efficient than just some if-else statements, but
         performance isn't important in this program. It also lets me add new
         methods for the user to use with 1 line, so I guess that's good
         */
        System.out.println("  Advanced commands: ");
        Iterator<Entry<String, Method>> it = advancedCommands.iterator();
        while (it.hasNext()) {
            Entry<String, Method> pair = it.next();
            System.out.println("     " + pair.getKey());
        }
        System.out.print(ANSI.FG_BLUE + "Enter an advanced command: " + ANSI.RESET);

        //I know user input was supposed to only be called in the main() method but it was really hard to follow inside it so I made this a method. If it makes a difference, just imagine this was copy-pasted where this method was invoked.
        String advancedCmd = scan.nextLine().trim();
        boolean contains = false;
        Method selected = null;
        Iterator<Entry<String, Method>> containsCheckingIterator = advancedCommands.iterator();
        while (containsCheckingIterator.hasNext() && !contains) {
            Entry<String, Method> pair = containsCheckingIterator.next();
            contains = pair.getKey().trim().equals(advancedCmd);
            if (contains) {
                selected = pair.getValue();
            }
        }
        if (contains && selected != null) {
            Object[] params = new Object[selected.getParameterCount()];
            for (int i = 0; i < selected.getParameterCount(); i++) {
                Class<?> paramType = selected.getParameterTypes()[i];
                if (i >= selected.getAnnotation(advanced.class).messages().length) {
                    throw new ArrayIndexOutOfBoundsException(ANSI.FG_RED + "Incorrect number of messages for the parameters in " + paramType.toString() + ". Should have " + selected.getParameterCount() + " messages, only has " + selected.getAnnotation(advanced.class).messages().length + ANSI.RESET);
                }
                String paramMessage = ANSI.FG_BLUE + selected.getAnnotation(advanced.class).messages()[i] + ANSI.RESET;
                if (paramType.equals(long.class) || paramType.equals(Long.class)) {
                    params[i] = (long) Verification.getValidatedNumber(paramMessage, false, true, true);
                } else if (paramType.equals(double.class) || paramType.equals(Double.class)) {
                    params[i] = Verification.getValidatedNumber(paramMessage, true, true, true);
                } else if (paramType.equals(int.class) || paramType.equals(Integer.class)) {
                    params[i] = (int) Verification.getValidatedNumber(paramMessage, true, true, true);
                } else if (paramType.equals(boolean.class) || paramType.equals(Boolean.class)) {
                    params[i] = Boolean.parseBoolean(getValidatedBoolean(paramMessage, false));
                } else if (paramType.equals(String.class)) {
                    System.out.print(paramMessage);
                    params[i] = scan.nextLine();
                } else if (paramType.equals(Beans.class)) {
                    System.out.print(paramMessage);
                    params[i] = scan.nextLine();
                }
            }
            try {
                selected.invoke((selected.getDeclaringClass().equals(CoffeeCup.class) ? cup : coffeeMachine), params);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                log(ex.getMessage());
            }
        } else {
            System.out.println(ANSI.FG_RED + "Unknown command." + ANSI.RESET);
        }
    }
}
