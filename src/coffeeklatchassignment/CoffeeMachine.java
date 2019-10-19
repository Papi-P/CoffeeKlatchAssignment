/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coffeeklatchassignment;

import coffeeklatchassignment.Other.Beans;
import coffeeklatchassignment.Other.advanced;

/**
 *
 * @author gordon.payne
 * @author Daniel Allen
 */
public class CoffeeMachine {

    // The strength of the coffee.
    private String strength = "Killer Intense";
    private double caffieneContentInMg = 0;

    //keep track of how much volume is left in litres.
    private double maxVolume = 2.0;
    private double volume = 0.0;

    //BEANS BEANS BEANS
    private Beans beanType = Beans.ARABICA;
    private int beanCount = 0;
    private boolean ground = false;
    private boolean autoFill = false;

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
     * Grind the beans for the coffee
     */
    @advanced
    public void grindBeans() {
        if(ground == true) {
            System.out.println("You've already ground the beans!");
            return;
        }
        if (hasBeans()) {
            System.out.println("Grinding beans for " + strength + " coffee.");
            this.ground = true;
        } else {
            System.out.println("There are no beans to grind!");
        }
    }

    /**
     * Brew the coffee into given cup c
     *
     * @param c The cup of coffee to be filled
     */
    public void brew(CoffeeCup c) {
        boolean valid = true;
        if(c == null){
            System.out.println(ANSI.FG_RED + "There is no cup!");
            valid = false;
        }
        if (this.volume <= 0) {
            System.out.println(ANSI.FG_RED + "You forgot to add water!");
            valid = false;
        }

        if (!hasBeans()) {
            System.out.println(ANSI.FG_RED + "You forgot to add the beans!");
            valid = false;
        } else if (!this.ground) {
            System.out.println(ANSI.FG_RED + "You forgot to grind the beans!");
            valid = false;
        }

        if (valid) {
            System.out.println(ANSI.FG_DARK_GREEN + "Brewing " + strength + " coffee.");
            volume = c.fill(this.volume);
            if(this.volume > 0){
                System.out.println("You have " + this.volume + " litres left.");
            } else {
                this.dumpWithoutMessage();
                System.out.println(ANSI.FG_RED + "You have no more coffee!");
            }
        }
    }

    /**
     * Add water to the machine reservoir
     */
    @advanced()
    public void addWater() {
        this.volume = maxVolume;
        System.out.println("Added Water");
    }

    /**
     * Dumps the pot to remove all water, coffee, and beans.
     */
    @advanced
    public void dump(){
        this.volume = 0;
        this.beanCount = 0;
        this.autoFill = false;
        System.out.println("Dumped the coffee");
    }

    public void dumpWithoutMessage(){
        this.volume = 0;
        this.beanCount = 0;
        this.autoFill = false;
    }

    /**
     * Add Beans to the Machine
     */

    public void addBeans() {
        System.out.println("Adding " + beanType.getName() + " Beans");
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
        autoFill = false;
        if (count < 0) {
            System.out.println("You can't remove beans!");
            return;
        }
        if (beanType.getContent() * count + this.caffieneContentInMg < 5000) {
            if (count == 0) {
                System.out.println(ANSI.FG_DARK_GREEN + "Did not add any " + beanType.getName() + " beans.");
            } else {
                System.out.println(ANSI.FG_DARK_GREEN + "Adding " + count + " " + beanType.getName() + " Bean" + (count == 1 ? "." : "s."));
            }
        } else {
            System.out.println(ANSI.FG_MAGENTA + "You've decided to add more than the lethal dose of caffeine. Please, get some help.");
        }
        beanCount += count;
        if (count > 0) {
            ground = false;
        }
    }

    public boolean hasBeans() {
        return this.beanCount > 0 || this.autoFill;
    }

    public void setBeanType(Beans beanType) {
        this.beanType = beanType;
    }

    @advanced(messages = {"Enter maximum capacity in litres: "})
    public void setCapacity(double litres){
        this.maxVolume = litres;
    }
}
