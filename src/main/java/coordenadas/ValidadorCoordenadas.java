package coordenadas;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidadorCoordenadas {

    public static void main(String[] args) {
        System.out.println(esCoordenadaValidaGM(" -40°25.00'3°42.19'")); // true
        System.out.println(esCoordenadaValidaGMS(" 38° 18' 42.1934\" 34° 34' 59.34\"")); // true
        System.out.println(esCoordenadaValidaGD(" 180° -8.70°")); // true
        System.out.println(esCoordenadaValidaGMS("40° 25' 34\" 3° 42' 23\"")); // ture
        System.out.println(esCoordenadaValidaGD("-40° 3°")); // true
        System.out.println(esCoordenadaValidaGD("180.0000° 90.0000°"));   // true
        System.out.println(esCoordenadaValidaGD("-180.0000° -90.0000°")); // true
        System.out.println(esCoordenadaValidaGM("40.416775' N,-3.703790' W")); // false
        System.out.println(esCoordenadaValidaGD("181.0000° 0.000°"));     // false
        System.out.println(esCoordenadaValidaGD("0.0000 91.0000"));    // false
        System.out.println(esCoordenadaValidaGM("abc,def"));            // false
        System.out.println(esCoordenadaValidaGM("45.0000"));           // false
        System.out.println(esCoordenadaValidaGM("45.0000,90.0000,10")); // false
    }

    public static boolean esCoordenadaValidaGM(String valor) {
        String regex = "^\\s*(([-]?0?[0-8][0-9]|90)°\\s*([0-5]?[0-9](\\.\\d+))?'\\s*)\\s*(([+-]?0{0,2}[0-9]|0?[0-9][0-9]|1[0-7][0-9]|180)°\\s*([0-5]?[0-9](\\.\\d+)?)'\\s*?)$";
        String input = "40º 25.12' N, 3º 42.198' W";

        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(valor);

        if (matcher.matches()) {
            System.out.println("Valid coordinates Grados!");
            System.out.println("Latitude: " + matcher.group(1)); // Group 1: Latitude
            System.out.println("Longitude: " + matcher.group(4)); // Group 4: Longitude
            return true;
        } else {
            System.out.println("Invalid coordinates.");
            return false;
        }
    }

    public static boolean esCoordenadaValidaGMS(String valor) {
        String regex = "^\\s*(([-]?(180|1[0-7][0-9]|[0-9]?[0-9]))°\\s*([0-5]?[0-9])'\\s*([0-5]?[0-9](\\.\\d+)?)\\\"\\s*?)\\s*((([-]?(90|[0-8]?[0-9]))°\\s*([0-5]?[0-9])'\\s*([0-5]?[0-9](\\.\\d+)?)\\\"\\s?))\\s*$";
        String input = "38º 18' 42.1934\" 34º 34' 59.34\"";

        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(valor);

        if (matcher.matches()) {
            System.out.println("Valid coordinates Grados Minutos Segundos!");
            System.out.println("Latitude: " + matcher.group(1)); // Group 1: Latitude
            System.out.println("Longitude: " + matcher.group(6)); // Group 4: Longitude
            return true;
        } else {
            System.out.println("Invalid coordinates.");
            return false;
        }
    }

    public static boolean esCoordenadaValidaGD(String valor) {
        String regex = "^\\s*([-+]?((1[0-7][0-9](\\.\\d+)?|180(\\.0+)?|[0-9]?[0-9](\\.\\d+)?)))[°]\\s*([-+]?([0-8]?[0-9](\\.\\d+)?|90(\\.0+)?)[°])\\s*?$";

        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(valor);

        if (matcher.matches()) {
            System.out.println("Valid coordinates Grados Decimales!");
            System.out.println("Latitude: " + matcher.group(1)); // Group 1: Latitude
            System.out.println("Longitude: " + matcher.group(7)); // Group 4: Longitude
            return true;
        } else {
            System.out.println("Invalid coordinates.");
            return false;
        }
    }

}