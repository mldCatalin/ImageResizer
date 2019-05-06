import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ImageValidator {

    private Pattern pattern;

    private static final String IMAGE_PATTERN =
            "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    ImageValidator() {
        pattern = Pattern.compile(IMAGE_PATTERN);
    }

    /**
     * Validate image with regular expression
     *
     * @param image image for validation
     *
     * @return true valid image, false invalid image
     */
    boolean validate(final String image) {

        Matcher matcher = pattern.matcher(image);
        return matcher.matches();
    }
}
