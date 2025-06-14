package org.resources;

import org.claims.Claim;
import org.claims.SampleFromFile;

import java.util.List;

public class InMemoryIncomingClaimsService implements IncomingClaimsService {
    public List<Claim> getAll() {
        return SampleFromFile.withDefaultData();
    }
}
