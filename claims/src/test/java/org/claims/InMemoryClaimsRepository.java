package org.claims;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class InMemoryClaimsRepository implements ClaimsRepository{

    private final URL initialDataUrl;
    private final Set<String> processedClaims = new HashSet<>();

    public InMemoryClaimsRepository(URL initialDataUrl) {
        this.initialDataUrl = initialDataUrl;

    }

    @Override
    public List<Claim> getAllUnprocessedClaims() {

        Path path;
        try {
            path = Paths.get(Objects.requireNonNull(initialDataUrl).toURI());
        } catch (URISyntaxException e) { throw new RuntimeException(e);}

        try (Stream<String> lines = Files.lines(path)) {
                return lines.map(line -> Arrays.asList(line.split(",")))
                        .skip(1)
                        .map(this::parse)
                        .filter(claim -> !processedClaims.contains(claim.id()))
                        .toList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }

    private Claim parse(List<String> record){
        return new Claim(record.get(0),
                record.get(1),
                BigDecimal.valueOf(Double.parseDouble(record.get(2))),
                LocalDate.parse(record.get(3)),
                record.get(4));
    }

    @Override
    public void setClaimAsProcessed(String claimId) {
        processedClaims.add(claimId);
    }
}
