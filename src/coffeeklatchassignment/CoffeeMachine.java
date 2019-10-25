/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coffeeklatchassignment;

import coffeeklatchassignment.Other.Beans;
import coffeeklatchassignment.Other.advanced;
import java.text.DecimalFormat;

/**
 *
 * @author gordon.payne
 * @author Daniel Allen
 */
public class CoffeeMachine {

    // The strength of the coffee.
    private String strength = "Regular";
    private int strengthModifier = 0;

    //keep track of how much volume is left in litres.
    private double maxVolume = 2.0;
    private double volume = 0.0;

    //keep track of whether the coffee is brewed or not.
    private boolean brewed = false;

    //BEANS BEANS BEANS
    private Beans beanType = Beans.ARABICA;
    private int beanCount = 0;
    private boolean ground = false;
    private boolean autoFill = false;

    private static DecimalFormat df = new DecimalFormat("0.##");

    /**
     * Set the strength of the Coffee to s; affects the fineness of the grind.
     * "Weak", "Regular", "Strong" are the usual options for s, but you can try
     * others.
     *
     * @param s Text Description of Strength
     */
    public void setStrength(String s) {
        strength = s;
    }

    /**
     * Returns the Strength of this coffee as a String.
     *
     * @return the Strength of this coffee.
     */
    public String getStrength() {
        final StringBuilder sb = new StringBuilder(this.strength);
        if (this.strengthModifier != 0) {
            if (this.strengthModifier < 0) {
                for (int i = 0; i < Math.abs(this.strengthModifier); i++) {
                    sb.append('-');
                }
            } else {
                for (int i = 0; i < this.strengthModifier; i++) {
                    sb.append('+');
                }
            }
        }
        return sb.toString();
    }

    /**
     * Grind the beans for the coffee
     */
    @advanced
    public void grindBeans() {
        if (ground == true) {
            System.out.println(ANSI.FG_RED + "You've already ground the beans!" + ANSI.RESET);
        } else if (hasBeans()) {
            System.out.println(ANSI.FG_DARK_GREEN + "Grinding beans for " + strength + " coffee." + ANSI.RESET);
            this.ground = true;
        } else {
            System.out.println(ANSI.FG_RED + "There are no beans to grind!" + ANSI.RESET);
        }
    }

    /**
     * Returns if the beans has been ground yet.
     *
     * @return true if it has.
     */
    public boolean isGround() {
        return this.ground;
    }

    /**
     * Returns if the coffee has been brewed yet.
     *
     * @return true if it has.
     */
    public boolean isBrewed() {
        return this.brewed;
    }

    /**
     * Brews the coffee. Requires that water has been added and the beans have been added and ground.
     *
     * @return whether the coffee was successfully brewed or not.
     */
    @advanced
    public boolean brew() {
        boolean valid = true;
        if (this.isBrewed()) {
            System.out.println(ANSI.FG_RED + "The coffee has already been brewed!" + ANSI.RESET);
            valid = false;
        } else {
            if (this.volume <= 0) {
                System.out.println(ANSI.FG_RED + "You forgot to add water!" + ANSI.RESET);
                valid = false;
            }

            if (!hasBeans()) {
                System.out.println(ANSI.FG_RED + "You forgot to add beans!" + ANSI.RESET);
                valid = false;
            } else if (!this.ground) {
                System.out.println(ANSI.FG_RED + "You forgot to grind the beans!" + ANSI.RESET);
                valid = false;
            }
        }

        if (valid) {
            System.out.println(ANSI.FG_DARK_GREEN + "Brewing " + strength + " coffee." + ANSI.RESET);
            this.brewed = true;
        }
        return valid;
    }

    /**
     * Brew the coffee into given cup c
     *
     * @param c The cup of coffee to be filled
     */
    public void pour(CoffeeCup c) {
        boolean valid = true;
        if (c == null) {
            System.out.println(ANSI.FG_RED + "There is no cup!" + ANSI.RESET);
            valid = false;
        }
        if (!isBrewed()) {
            System.out.println(ANSI.FG_RED + "You need to brew the coffee before you can pour it!" + ANSI.RESET);
            valid = false;
        }

        if (valid) {
            System.out.println(ANSI.FG_DARK_GREEN + "Pouring " + strength + " coffee into coffee cup " + c.getName() + ANSI.RESET + ANSI.FG_DARK_GREEN + "." + ANSI.RESET);
            volume = c.fill(this.volume);
            if (this.volume > 0) {
                System.out.println(ANSI.FG_DARK_GREEN + "You have " + df.format(this.volume) + " litres left." + ANSI.RESET);
            } else {
                this.dumpWithoutMessage();
                System.out.println(ANSI.FG_RED + "You have no more coffee!" + ANSI.RESET);
            }
        }
    }

    /**
     * Add water to the machine reservoir
     */
    @advanced()
    public void addWater() {
        boolean pre = hasWater();
        if (this.volume > maxVolume) {
            this.volume = maxVolume;
        }
        if (this.volume == maxVolume) {
            System.out.println(ANSI.FG_RED + "The machine is already full!");
        } else {
            this.volume = maxVolume;
            if (pre) {
                strengthModifier--;
            }
            System.out.println(ANSI.FG_DARK_GREEN + "Added Water" + ANSI.RESET);
        }
    }

    /**
     * Returns whether this machine has water.
     * @return true if this machine's volume is over 0;
     */
    public boolean hasWater() {
        return this.volume > 0;
    }

    /**
     * Returns the volume of this machine's water.
     * @return the volume of water in mL.
     */
    public double getWater() {
        return this.volume;
    }

    /**
     * Dumps the pot to remove all water, coffee, and beans, resetting it to as
     * if it had not been used.
     */
    @advanced
    public void dump() {
        dumpWithoutMessage();
        System.out.println(ANSI.FG_DARK_GREEN + "Dumped the coffee" + ANSI.RESET);
    }

    /**
     * Dumps the pot to remove all water, coffee, and beans, resetting it to as
     * if it had not been used.
     */
    public void dumpWithoutMessage() {
        this.volume = 0;
        this.beanCount = 0;
        this.autoFill = false;
        this.strengthModifier = 0;
        this.ground = false;
        this.brewed = false;
    }

    /**
     * Add Beans to the Machine
     */
    public void addBeans() {
        System.out.println(ANSI.FG_DARK_GREEN + "Added " + beanType.getName() + " Beans." + ANSI.RESET);
        if (hasBeans()) {
            strengthModifier++;
        }
        autoFill = true;
        ground = false;
    }

    /**
     * Add Beans to the Machine
     *
     * @param count Number of beans to add.
     */
    @advanced(messages = {"How many beans to add: "})
    public void addBeans(long count) {
        //turn off autofill since the user now determined how many beans to add.
        autoFill = false;

        //prevent the user from adding a negative value.
        if (count < 0) {
            System.out.println(ANSI.FG_RED + "You can't remove beans!" + ANSI.RESET);
        } else if (beanType.getContent() * count < 5000) {
            if (count == 0) {
                System.out.println(ANSI.FG_DARK_GREEN + "Did not add any " + beanType.getName() + " beans." + ANSI.RESET);
            } else {
                System.out.println(ANSI.FG_DARK_GREEN + "Adding " + count + " " + beanType.getName() + " Bean" + (count == 1 ? "." : "s.") + ANSI.RESET);
            }
        } else {
            System.out.println(ANSI.BOLD + "You've decided to add more than the lethal dose of caffeine. Please, get some help." + ANSI.RESET);
        }
        beanCount += count;
        if (count > 0) {
            ground = false;
        }
    }

    /**
     *
     * @return true if this has more than 0 beans, or autofill is enabled.
     */
    public boolean hasBeans() {
        return this.beanCount > 0 || this.autoFill;
    }

    /**
     *
     * @param beanType
     */
    public void setBeanType(Beans beanType) {
        this.beanType = beanType;
    }

    /**
     * Sets the maximum capacity of this coffee machine.
     *
     * @param litres
     */
    @advanced(messages = {"Enter maximum capacity in litres: "})
    public void setCapacity(double litres) {
        this.maxVolume = litres;
    }
}
