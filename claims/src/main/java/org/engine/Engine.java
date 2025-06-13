package org.engine;

import org.claims.Claim;
import org.claims.ClaimsRepository;
import org.progress.ProgressRepository;

import java.util.ArrayList;
import java.util.List;

public class Engine {

    private final ClaimsRepository claimsRepository;
    private final ProgressRepository progressRepository;


    public Engine(ClaimsRepository claimsRepository, ProgressRepository progressRepository) {
        this.claimsRepository = claimsRepository;
        this.progressRepository = progressRepository;
    }

    public static List<Claim> processDay(){
        List<Claim> processedClaims = new ArrayList<>();
        return processedClaims;
    }
}
