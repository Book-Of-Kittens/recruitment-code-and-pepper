package org.claims;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class SampleFromFile {

    public static final String DEFAULT_FILE = "/samples/claims.csv";

    public static List<Claim> withDefaultData() {
        URL samples = SampleFromFile.class.getResource(DEFAULT_FILE);
        return getClaims(samples);
    }

    private static List<Claim> getClaims(URL initialDataUrl) {

        Path path;
        try {
            path = Paths.get(Objects.requireNonNull(initialDataUrl).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try (Stream<String> lines = Files.lines(path)) {
            return lines.map(line -> Arrays.asList(line.split(",")))
                    .skip(1)
                    .map(SampleFromFile::parse)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static Claim parse(List<String> record) {
        return new Claim(record.get(0),
                ClaimType.valueOf(record.get(1)),
                BigDecimal.valueOf(Double.parseDouble(record.get(2))),
                LocalDate.parse(record.get(3)),
                ComplexityLevel.valueOf(record.get(4)),
                null);
    }

}
