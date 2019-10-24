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
    private String strength = "Regular";
    private int strengthModifier = 0;
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

    public String getStrength(){
        String output = this.strength;
        if(this.strengthModifier != 0){
            if(this.strengthModifier < 0){
                for(int i = 0; i < Math.abs(this.strengthModifier); i++){
                    output += "-";
                }
            } else {
                for(int i = 0; i < this.strengthModifier; i++){
                    output += "+";
                }
            }
        }
        return output;
    }

    /**
     * Grind the beans for the coffee
     */
    @advanced
    public void grindBeans() {
        if(ground == true) {
            System.out.println(ANSI.FG_RED + "You've already ground the beans!" + ANSI.RESET);
            return;
        }
        if (hasBeans()) {
            System.out.println(ANSI.FG_DARK_GREEN + "Grinding beans for " + strength + " coffee." + ANSI.RESET);
            this.ground = true;
        } else {
            System.out.println(ANSI.FG_RED + "There are no beans to grind!" + ANSI.RESET);
        }
    }

    public boolean isGround(){
        return this.ground;
    }

    /**
     * Brew the coffee into given cup c
     *
     * @param c The cup of coffee to be filled
     */
    public void brew(CoffeeCup c) {
        boolean valid = true;
        if(c == null){
            System.out.println(ANSI.FG_RED + "There is no cup!" + ANSI.RESET);
            valid = false;
        }
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

        if (valid) {
            System.out.println(ANSI.FG_DARK_GREEN + "Brewing " + strength + " coffee." + ANSI.RESET);
            volume = c.fill(this.volume);
            if(this.volume > 0){
                System.out.println("You have " + this.volume + " litres left.");
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
        if(this.volume > maxVolume) {
            this.volume = maxVolume;
        }
        if(this.volume == maxVolume) {
            System.out.println(ANSI.FG_RED + "The machine is already full!");
        } else {
            this.volume = maxVolume;
            if(pre) {
                strengthModifier--;
            }
            System.out.println(ANSI.FG_DARK_GREEN + "Added Water" + ANSI.RESET);
        }
    }

    public boolean hasWater(){
        return this.volume > 0;
    }
    public double getWater(){
        return this.volume;
    }

    /**
     * Dumps the pot to remove all water, coffee, and beans.
     */
    @advanced
    public void dump(){
        this.volume = 0;
        this.beanCount = 0;
        this.autoFill = false;
        this.strengthModifier = 0;
        this.ground = false;
        System.out.println(ANSI.FG_DARK_GREEN + "Dumped the coffee" + ANSI.RESET);
    }

    public void dumpWithoutMessage(){
        this.volume = 0;
        this.beanCount = 0;
        this.autoFill = false;
        this.strengthModifier = 0;
        this.ground = false;
    }

    /**
     * Add Beans to the Machine
     */

    public void addBeans() {
        System.out.println(ANSI.FG_DARK_GREEN + "Added " + beanType.getName() + " Beans." + ANSI.RESET);
        if(hasBeans()) {
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
        autoFill = false;
        if (count < 0) {
            System.out.println(ANSI.FG_RED + "You can't remove beans!" + ANSI.RESET);
            return;
        }
        if (beanType.getContent() * count < 5000) {
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
