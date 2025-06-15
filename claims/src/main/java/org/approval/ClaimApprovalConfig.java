package org.approval;

import org.events.ClaimEventsBus;
import org.resources.ResourcesService;
import org.rules.BusinessRulesConfiguration;

public class ClaimApprovalConfig {

    public static ClaimApprovalService getClaimApprovalService(ResourcesService resourcesService, ClaimEventsBus events) {

        return new ClaimApprovalService(
                BusinessRulesConfiguration.commonApprovalPredicate(resourcesService),
                events);
    }
}
