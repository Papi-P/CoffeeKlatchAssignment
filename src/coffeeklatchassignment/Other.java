package coffeeklatchassignment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 @author Daniel Allen
 14-Oct-2019
 */
public class Other {

    /**
     * Bean types and their caffeine content in percent mass.
     *
     */
    public enum Beans {

        ARABICA(0.012, 0, "Arabica"),
        ROBUSTA(0.022, 0, "Robusta");

        Beans(double mgOfCaffeine, double strength, String formattedName) {
            this.caffeineContent = mgOfCaffeine;
            this.formattedName = formattedName;
        }
        private double caffeineContent;
        private String formattedName;
        private double strength;

        double getContent() {
            return this.caffeineContent;
        }

        String getName() {
            return this.formattedName;
        }

        @Override
        public String toString() {
            return this.formattedName;
        }

        double getStrength() {
            return this.strength;
        }
    }

    /**
     * Cup sizes.
     */
    public enum Sizes {

        SMALL(0.15),
        MEDIUM(0.30),
        LARGE(0.50),
        THIS_IS_TOO_MUCH(0.80),
        PLEASE_NO_MORE(1.00),
        YOU_NEED_TO_STOP(1.50),
        YOU_HAVE_A_PROBLEM(2.00),
        INFINITE(Double.POSITIVE_INFINITY);

        Sizes(double volume) {
            this.volume = volume;
        }
        private final double volume;

        public double getVolume() {
            return this.volume;
        }

        @Override
        public String toString() {
            return capitalizeFully(this.name().toLowerCase(), "_", " ");
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
    public @interface advanced {

        String[] messages() default {};
    }

    /**
     * Capitalizes each word as defined by a delimiter in a String.
     * <br><b>Note:</b> This will preserve case, such that
     * <span style="color: green">thIs is SOme tEXt</span>
     * will become <span style="color: green">ThIs Is SOme TEXt</span>
     *
     * @param in String to capitalize
     * @param delimiter Delimiter to separate words
     * @param joiner String to reconnect words.
     * @return Capitalized String
     */
    public static String capitalizeFully(String in, String delimiter, String joiner) {
        //create a stream from the input, split it by the delimiter, capitalize each index, then join them using the delimiter.
        return Stream.of(in.split(delimiter)).map((e) -> {
            return Character.toUpperCase(e.charAt(0)) + e.substring(1);
        }).collect(Collectors.joining(joiner));
    }

    //<editor-fold desc="Roman Numerals">
    //Too lazy to write the method myself since I have to do research on Roman numerals, so the following is code by Ben-Hur Langoni Junior on stackoverflow. See https://stackoverflow.com/questions/12967896/converting-integers-to-roman-numerals-java.
    private final static TreeMap<Integer, String> numeralMap = new TreeMap<Integer, String>();

    static {

        numeralMap.put(1000, "M");
        numeralMap.put(900, "CM");
        numeralMap.put(500, "D");
        numeralMap.put(400, "CD");
        numeralMap.put(100, "C");
        numeralMap.put(90, "XC");
        numeralMap.put(50, "L");
        numeralMap.put(40, "XL");
        numeralMap.put(10, "X");
        numeralMap.put(9, "IX");
        numeralMap.put(5, "V");
        numeralMap.put(4, "IV");
        numeralMap.put(1, "I");

    }

    /**
     * Converts a number to a Roman numeral
     * @param number Number to convert
     * @return A Roman numeral representing the number.
     */
    public final static String toRoman(int number) {
        int l = numeralMap.floorKey(number);
        if (number == l) {
            return numeralMap.get(number);
        }
        return numeralMap.get(l) + toRoman(number - l);
    }
    //</editor-fold>

    /**
     * Adds padding to either side of a String, centering it given a total width for the String to be.
     * @param str String to center
     * @param size Final size of the String
     * @return Centered String
     */
    public static String center(String str, int size){
        //returns the input if it's null or the size is less than or equal to the input's length
        if(str == null || size <= str.length()){
            return str;
        }
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < (size-str.length())/2; i++){
            sb.append(' ');
        }
        sb.append(str);
        for(int i = 0; i < (size - str.length())/2; i++){
            sb.append(' ');
        }
        return sb.toString();
    }
}
