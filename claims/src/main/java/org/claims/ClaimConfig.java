package org.claims;

import org.resources.ResourcesService;
import org.rules.BusinessRulesConfiguration;

public class ClaimConfig {
    public static ClaimApprovalService getClaimApprovalService(ResourcesService resourcesService) {

        return new ClaimApprovalService(
                BusinessRulesConfiguration.approvalPredicates(resourcesService),
                BusinessRulesConfiguration.all());
    }        // TODO: active management of the number of services?


}
