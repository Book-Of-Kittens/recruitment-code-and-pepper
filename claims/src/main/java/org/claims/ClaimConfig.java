package org.claims;

import org.events.ClaimEventsQueue;
import org.resources.ResourcesService;
import org.rules.BusinessRulesConfiguration;

public class ClaimConfig {

    public static ClaimApprovalService getClaimApprovalService(ResourcesService resourcesService, ClaimEventsQueue claimEventsQueue) {

        return new ClaimApprovalService(
                BusinessRulesConfiguration.commonApprovalPredicate(resourcesService),
                claimEventsQueue);
    }        // TODO: active management of the number of services?


}
