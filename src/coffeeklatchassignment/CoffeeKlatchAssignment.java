package coffeeklatchassignment;

import coffeeklatchassignment.Other.Beans;
import coffeeklatchassignment.Other.Sizes;
import coffeeklatchassignment.Other.advanced;
import java.util.Scanner;
import static coffeeklatchassignment.Verification.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    static CoffeeMachine coffeemachine;
    static CoffeeCup cup;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        coffeemachine = new CoffeeMachine();
        //get the user's name
        System.out.println(ANSI.FG_BLUE + "What's your name?" + ANSI.RESET);
        String name = scan.nextLine();
        System.out.println(ANSI.FG_DARK_GREEN + "Welcome, "+name);
        //display the cup sizes
        System.out.println("\n-------------------------------------------------");
        System.out.println("|                     Sizes                     |");
        for (int i = 0; i < Sizes.values().length; i++) {
            String changedText = (i + 1) + ": " + Sizes.values()[i];
            System.out.print("|  " + changedText);
            for (int z = 0; z < (45 - changedText.length()); z++) {
                System.out.print(" ");
            }
            System.out.print("|\n");
        }
        System.out.println("-------------------------------------------------");

        //prompt the user to enter a cup size
        System.out.println(ANSI.RESET + "\n" + ANSI.FG_BLUE + "What size of cup would you like?" + ANSI.RESET);
        boolean valid = false;
        while (!valid) {
            int size = Integer.parseInt(getValidatedNumber("Enter a size: ", false));
            if (size < 1 || size > Sizes.values().length) {
                valid = false;
                System.out.println(ANSI.FG_RED + "Unknown size. Please select one from the list above." + ANSI.RESET);
            } else {
                cup = new CoffeeCup(name, size);
                System.out.println(ANSI.FG_DARK_GREEN + "Selected size \"" + cup.getSize() + "\"" + ANSI.RESET);
                valid = true;
            }
        }
        //dump the coffee machine
        coffeemachine.dumpWithoutMessage();
        boolean exit = false;
        while (!exit) {
            System.out.println("\n-------------------------------------------------");
            System.out.println("|                    Commands                   |");
            System.out.println("|   B: Add Beans to Machine                     |");
            System.out.println("|   G: Grind Beans in Machine                   |");
            System.out.println("|   W: Add Water to Machine                     |");
            System.out.println("|   S: Set the Strength of the Coffee           |");
            System.out.println("|                                               |");
            System.out.println("|   F: Fill a Cup from the Machine              |");
            System.out.println("|   D: Drink the Cup                            |");
            System.out.println("|   C: Create a new Cup                         |");
            System.out.println("|                                               |");
            System.out.println("|   A: Advanced Commands                        |");
            System.out.println("|   E: Exit                                     |");
            System.out.println("-------------------------------------------------");
            System.out.print(ANSI.FG_BLUE + "Enter your command: ");

            String command = scan.nextLine();
            if (command.equalsIgnoreCase("W")) {
                coffeemachine.addWater();
            } else if (command.equalsIgnoreCase("B")) {
                coffeemachine.addBeans();
            } else if (command.equalsIgnoreCase("G")) {
                coffeemachine.grindBeans();
            } else if (command.equalsIgnoreCase("S")) {
                System.out.print("Strength: ");
                coffeemachine.setStrength(scan.nextLine());
            } else if (command.equalsIgnoreCase("F")) {
                coffeemachine.brew(cup);
            } else if (command.equalsIgnoreCase("D")) {
                cup.drink();
            } else if (command.equalsIgnoreCase("A")) {
                //I got bored so I tried reflection. This is way more inefficient than just some if-else statements but performance isn't an issue with this task. It also lets me add new methods for the user to use with 1 line, so I guess that's good?
                System.out.println("  Advanced commands: ");
                Iterator<Entry<String, Method>> it = advancedCommands.iterator();
                //String currentClassContainer = "";
                while (it.hasNext()) {
                    Entry<String, Method> pair = it.next();
                    System.out.println("     " + pair.getKey());
                }
                System.out.print(ANSI.FG_BLUE + "Enter an advanced command: ");
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
                        String paramMessage = ANSI.FG_BLUE + selected.getAnnotation(advanced.class).messages()[i];
                        if (paramType.equals(long.class) || paramType.equals(Long.class)) {
                            params[i] = Long.parseLong(Verification.getValidatedNumber(paramMessage, false));
                        } else if (paramType.equals(double.class) || paramType.equals(Double.class)) {
                            params[i] = Double.parseDouble(Verification.getValidatedNumber(paramMessage, true));
                        } else if (paramType.equals(int.class) || paramType.equals(Integer.class)) {
                            params[i] = Integer.parseInt(Verification.getValidatedNumber(paramMessage, true));
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
                        selected.invoke((selected.getDeclaringClass().equals(CoffeeCup.class) ? cup : coffeemachine), params);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(CoffeeKlatchAssignment.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println(ANSI.FG_RED + "Unknown command." + ANSI.RESET);
                }
            } else if (command.equalsIgnoreCase("E")) {
                exit = true;
            } else {
                System.out.println(ANSI.FG_RED + "Unknown command. Please select one from the list above." + ANSI.RESET);
            }
        }
        scan.close();
    }
}
