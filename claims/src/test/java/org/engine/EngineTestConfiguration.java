package org.engine;

import org.claims.Claim;
import org.claims.ClaimsOrderingService;
import org.claims.InMemoryClaimsOrderingService;
import org.claims.SampleFromFile;
import org.resources.InMemoryResourcesService;
import org.resources.ResourcesService;
import org.rules.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class EngineTestConfiguration {

    public static Engine getEngine(){
        return new Engine(orderingService(), approvalService());
    }

    private static ClaimsOrderingService orderingService() {
        ClaimsOrderingService service = new InMemoryClaimsOrderingService(comparatorsByType());
        service.addClaims(SampleFromFile.withDefaultData());
        return service;
    }

    private static Map<String, Comparator<Claim>> comparatorsByType(){
        return Map.of(ClaimType.MEDICAL.name(), Comparator.comparing(Claim::id),
                ClaimType.PROPERTY.name(), Comparator.comparing(Claim::id),
                ClaimType.VEHICLE.name(), Comparator.comparing(Claim::id));
    }

    private static List<UpdatablePredicate> examplePredicate(){
        return List.of(new ExamplePredicate(), new NoDuplicateIdPredicate(new InMemoryResourcesService()));
    }

    private static ApprovalService approvalService(){
        return new ApprovalService(examplePredicate());
    }

}
