package uk.gov.justice.services.core.error;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ExceptionHashGenerator {

    private static final String MD_5_ALGORITHM = "MD5";

    public String createHashFrom(final String rawString) {
        final byte[] rawStringBytes = rawString.getBytes(UTF_8);

        try {
            final MessageDigest messageDigest = MessageDigest.getInstance(MD_5_ALGORITHM);
            messageDigest.reset();
            messageDigest.update(rawStringBytes);
            final byte[] digest = messageDigest.digest();
            final BigInteger bigInt = new BigInteger(1, digest);

            return bigInt.toString(16);
        } catch (final NoSuchAlgorithmException e) {
            throw new ExceptionHashingException(MD_5_ALGORITHM + " algorithm not found", e);
        }
    }
}
