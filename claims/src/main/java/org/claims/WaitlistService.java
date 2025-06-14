package org.claims;

import org.resources.IncomingClaimsService;
import org.rules.UpdatablePredicate;

import java.util.Comparator;
import java.util.List;

public class WaitlistService {

    private final List<UpdatablePredicate> consumePredicate;
    private final Comparator<Claim> orderSource;
    private final IncomingClaimsService incomingClaimsService;

    public WaitlistService(List<UpdatablePredicate> consumePredicate, Comparator<Claim> orderSource, IncomingClaimsService incomingClaimsService) {
        this.consumePredicate = consumePredicate;
        this.orderSource = orderSource;
        this.incomingClaimsService = incomingClaimsService;
    }
}
