package org.claims;

import org.resources.IncomingClaimsService;
import org.resources.ResourcesService;
import org.rules.BusinessRulesConfiguration;

import java.util.List;

public class SplitResponsibilityConfig {
    public static ClaimApprovalService getClaimApprovalService(ResourcesService resourcesService) {

        return new ClaimApprovalService(
                BusinessRulesConfiguration.approvalPredicates(resourcesService),
                BusinessRulesConfiguration.all());
    }        // TODO: active management of the number of services?

    public static List<WaitlistService> getWaitListServices(IncomingClaimsService incomingClaimsService) {

        return List.of(
                getWaitlistService(ClaimType.MEDICAL, incomingClaimsService),
                getWaitlistService(ClaimType.VEHICLE, incomingClaimsService),
                getWaitlistService(ClaimType.PROPERTY, incomingClaimsService)
        );
    }

    private static WaitlistService getWaitlistService(ClaimType claimType,
                                                      IncomingClaimsService incomingClaimsService) {
        BusinessRulesConfiguration.comparatorsByType.get(claimType);
        BusinessRulesConfiguration.ofType(claimType);

        return new WaitlistService(
                BusinessRulesConfiguration.ofType(claimType),
                BusinessRulesConfiguration.comparatorsByType.get(claimType),
                incomingClaimsService);
    }
}
