package org.engine;

import org.claims.*;
import org.resources.InMemoryResourcesService;
import org.rules.ApprovalService;
import org.rules.ExamplePredicate;
import org.rules.NoDuplicateIdPredicate;
import org.rules.UpdatablePredicate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class EngineTestConfiguration {

    public static ClaimProcessingService getEngine() {
        return new ClaimProcessingService(orderingService(), approvalService());
    }

    private static ClaimsOrderingService orderingService() {
        ClaimsOrderingService service = new InMemoryClaimsOrderingService(comparatorsByType());
        service.addClaims(SampleFromFile.withDefaultData());
        return service;
    }

    private static Map<ClaimType, Comparator<Claim>> comparatorsByType() {
        return Map.of(ClaimType.MEDICAL, Comparator.comparing(Claim::id),
                ClaimType.PROPERTY, Comparator.comparing(Claim::id),
                ClaimType.VEHICLE, Comparator.comparing(Claim::id));
    }

    private static List<UpdatablePredicate> examplePredicate() {
        return List.of(new ExamplePredicate(), new NoDuplicateIdPredicate(new InMemoryResourcesService()));
    }

    private static ApprovalService approvalService() {
        return new ApprovalService(examplePredicate());
    }

}
