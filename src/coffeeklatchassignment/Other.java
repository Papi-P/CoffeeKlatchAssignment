package coffeeklatchassignment;

import com.sun.xml.internal.ws.util.StringUtils;
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
            return capitalizeFully(this.name().toLowerCase(), "_");
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
     * @return Capitalized String
     */
    public static String capitalizeFully(String in, String delimiter) {
        //I don't know where StringUtils.capitalizeFully went so I made this to replace it.
        return Stream.of(in.split(delimiter)).map((e) -> {
            return StringUtils.capitalize(e);
        }).collect(Collectors.joining(" "));
    }

    
    //Too lazy to write the method myself since I have to do a bit of research, so the following is code by Ben-Hur Langoni Junior on stackoverflow. See https://stackoverflow.com/questions/12967896/converting-integers-to-roman-numerals-java.
    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public final static String toRoman(int number) {
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }
    
    public static String center(String str, int size){
        if(str == null || size <= 0 || size < str.length()){
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
