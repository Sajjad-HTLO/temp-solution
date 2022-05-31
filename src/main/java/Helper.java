import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Helper {

    private final static String FILE_PREFIX = "resources/";

    public static boolean isValidFile(String path) {
        try {
            return Files.exists(Paths.get(FILE_PREFIX + path));
            // Check the file is valid
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }

    public static int getPricePerSquareMeter(String priceStr, int squareMeters) {
        NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
        float value = 0;
        try {
            value = format.parse(priceStr).floatValue() / squareMeters;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new BigDecimal(value)
                .multiply(new BigDecimal("1000"))
                .intValue();
    }
}
