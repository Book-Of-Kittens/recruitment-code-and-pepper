package org.claims;

import org.resources.IncomingClaimsService;
import org.rules.UpdatablePredicate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryWaitListService implements WaitListService {

    private final List<UpdatablePredicate> consumePredicate;
    private final Comparator<Claim> orderSource;
    private final IncomingClaimsService incomingClaimsService; /* who should be calling who? */

    private final List<Claim> waitList = new LinkedList<>(); // TODO: choose structure

    private final HashMap<String, Claim> currentlyProcessed = new HashMap<>(); // TODO: thread safety!

    private final List<Claim> postponed = new LinkedList<>();

    public InMemoryWaitListService(List<UpdatablePredicate> consumePredicate, Comparator<Claim> orderSource, IncomingClaimsService incomingClaimsService) {
        this.consumePredicate = consumePredicate;
        this.orderSource = orderSource;
        this.incomingClaimsService = incomingClaimsService;
    }

    @Override
    public Claim getClaim() {
        Claim claim = waitList.getFirst();
        if (null == claim) return null;
        waitList.removeFirst(); /* TODO: probably queue */
        currentlyProcessed.put(claim.id(), claim);
        return claim;
    }

    @Override
    public void postponeClaim(Claim claim) {
        currentlyProcessed.remove(claim.id());
        postponed.add(claim);
    }

    @Override
    public void removeClaim(Claim claim) {
        currentlyProcessed.remove(claim.id());
    }

}
