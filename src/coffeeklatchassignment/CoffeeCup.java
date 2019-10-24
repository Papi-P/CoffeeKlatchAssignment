package coffeeklatchassignment;

import coffeeklatchassignment.Other.Sizes;
import coffeeklatchassignment.Other.advanced;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author gordon.payne
 * @author Daniel Allen
 */
public class CoffeeCup {

    /**
     * Stores a Map of all CoffeeCups, indexed by their name.
     */
    private static ArrayList<CoffeeCup> allCups = new ArrayList<>();

    /**
     * Formats numbers printed, mainly to replace "Infinity" with "∞" but also to round numbers to 2 (or fewer) decimal places.
     */
    private static DecimalFormat df = new DecimalFormat("0.##");

    private double litres; // How full is this cup?
    private final Sizes size; // What size is this cup?
    private String name; // Who's cup is this?

    /**
     * Creates a CoffeeCup with a name and size. Sizes:
     * <ol>
     * <li>Small</li>
     * <li>Medium</li>
     * <li>Large</li>
     * <li>This is too much</li>
     * <li>Please no more</li>
     * <li>You need to stop</li>
     * <li>You have a problem</li>
     * <li>Infinite</li>
     * </ol>
     *
     * @param name Name to put on the cup
     * @param size Size of cup ranging from <code>1-n</code>, where
     * <code>n</code> is the number of elements in
     * {@link coffeeklatchassignment.Other.Sizes}
     */
    public CoffeeCup(String name, int size) {
        this.name = name;
        this.size = Sizes.values()[Math.max(Math.min(Sizes.values().length - 1, size - 1), 0)];
        init();
    }

    /**
     * Creates a CoffeeCup with a name and size. Sizes:
     * <ol>
     * <li>Small</li>
     * <li>Medium</li>
     * <li>Large</li>
     * <li>This is too much</li>
     * <li>Please no more</li>
     * <li>You need to stop</li>
     * <li>You have a problem</li>
     * <li>Infinite</li>
     * </ol>
     *
     * @param name Name to put on the cup
     * @param size Size of cup. String must match a size in
     * {@link coffeeklatchassignment.Other.Sizes}, case-insensitive.
     */
    public CoffeeCup(String name, String size) {
        this.name = name;
        this.size = Sizes.valueOf(size);
        init();
    }

    /**
     * Creates a CoffeeCup with a name and size.
     *
     * @param name Name to put on the cup
     * @param size Size of cup. Size must be from
     * {@link coffeeklatchassignment.Other.Sizes}.
     */
    public CoffeeCup(String name, Sizes size) {
        this.size = size;
        this.name = name;
        init();
    }

    /**
     * Creates a medium-sized CoffeeCup with a name.
     *
     * @param name Name to put on the cup
     */
    public CoffeeCup(String name) {
        this.name = name;
        this.size = Sizes.MEDIUM;
        init();
    }

    /**
     * Creates a medium CoffeeCup named "Coffee Cup".
     */
    public CoffeeCup() {
        this.name = "Coffee Cup";
        this.size = Sizes.MEDIUM;
        init();
    }

    /**
     * Initializes the cup. This modifies the name to prevent multiple users
     * from using the same one.
     */
    private void init() {
        //keep track of if the name is unique, the current index of an iteration, and the original name of this cup.
        boolean isUnique = false;
        int index = 1;
        String originalName = this.getName();
        while (!isUnique) {
            //if the list contains the current name, set the current name to the original name succeeded by "the " and the index as a roman numeral, and repeat. If not, end the loop and add the cup.
            if (listContainsName(this.getName())) {
                index++;
                this.name = originalName + " the " + Other.toRoman(index);
            } else {
                isUnique = true;
            }
        }
        allCups.add(this);
    }

    /**
     * Check if a cup exists with the given name.
     *
     * @param name The name to check for
     * @return true if a cup has that name.
     */
    public boolean listContainsName(String name) {
        for (CoffeeCup cc : allCups) {
            if (cc.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a cup with a given name.
     *
     * @param name The name of the cup
     * @return the cup with the given name, or <code>null</code> if it doesn't
     * exist.
     */
    public static CoffeeCup getCupFromName(String name) {
        for (CoffeeCup cc : allCups) {
            if (cc.getName().equals(name)) {
                return cc;
            }
        }
        return null;
    }

    /**
     * Returns the volume remaining in this drink
     *
     * @return the volume in litres
     */
    public double getLitres() {
        return this.litres;
    }

    /**
     * Returns the name of this cup.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Fill this cup to the top. Use
     * {@link coffeeklatchassignment.CoffeeCup#fill(double)} instead, unless you
     * want to magically fill the cup and not worry about the amount.
     *
     */
    @advanced
    public void fill() {
        this.litres = this.size.getVolume();
        System.out.println("You've filled the cup to " + df.format(this.litres) + "L!");
    }


    /**
     * Fill this cup either to the top or until the amount provided is empty and
     * returns the volume left over.
     *
     * @param amount
     * @return
     */
    @advanced(messages = {"Amount to fill in litres: "})
    public double fill(double amount) {
        double curVolume = this.litres;
        double maxVolume = this.size.getVolume();
        if (curVolume > maxVolume) {
            this.litres = maxVolume;
            System.out.println("The cup overflowed!");
            return amount;
        }
        if (curVolume == maxVolume) {
            System.out.println("The cup was already full!");
            return amount;
        }
        if (curVolume < maxVolume) {
            double difference = maxVolume - curVolume;
            if (amount >= difference) {
                this.litres = maxVolume;
                System.out.println("You've filled the cup to " + df.format(this.litres) + "L!");
                return amount - difference;
            } else {
                this.litres += amount;
                System.out.println("You've added " + df.format(amount) + "L to this cup. You need another " + df.format(this.size.getVolume() - this.litres) + "L to fill it.");
                return 0;
            }
        }
        return 0;
    }

    /**
     * Drink this cup entirely
     *
     * @return true if the coffee was drunk
     */
    @advanced
    public boolean drink() {
        if (this.litres > 0) {
            System.out.println(ANSI.FG_DARK_GREEN + "You glug the coffee in " + this.getName() + " down." + ANSI.RESET);
            this.litres = 0;
            return true;
        } else {
            System.out.println(ANSI.FG_RED + "You sip " + this.getName() + " furiously, but only suck air." + ANSI.RESET);
            return false;
        }

    }

    /**
     * Returns the size of this cup.
     * @return the size of this cup from {@link coffeeklatchassignment.Other.Sizes}
     */
    public Sizes getSize() {
        return this.size;
    }
}
