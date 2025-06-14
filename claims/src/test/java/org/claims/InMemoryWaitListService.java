package org.claims;

import org.resources.IncomingClaimsService;
import org.rules.UpdatablePredicate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryWaitListService implements WaitListService {

    // TODO: to be considered: compound predicate with possible custom logic instead of the list.
    private final List<UpdatablePredicate> consumePredicate; // TODO: unclear which lists are OR and which are AND
    private final Comparator<Claim> orderSource;
    private final IncomingClaimsService incomingClaimsService; /* who should be calling who? */

    private final List<Claim> waitList = new LinkedList<>(); // TODO: choose structure

    private final HashMap<String, Claim> currentlyProcessed = new HashMap<>(); // TODO: thread safety!

    private final List<Claim> postponed = new LinkedList<>();

    // TODO: where should the logic for updating resources for predicates be? Who should initialize this update? Predicate resource update service?
    /* TODO: do this with a common event based queue? */
    public InMemoryWaitListService(List<UpdatablePredicate> consumePredicate, Comparator<Claim> orderSource, IncomingClaimsService incomingClaimsService) {
        this.consumePredicate = consumePredicate;
        this.orderSource = orderSource;
        this.incomingClaimsService = incomingClaimsService;
    }

    @Override
    public Claim getClaimForProcessing() {
        if (waitList.isEmpty()) return null;
        Claim claim = waitList.getFirst();
        waitList.removeFirst(); /* TODO: probably queue */
        currentlyProcessed.put(claim.id(), claim);
        return claim;
    }

    @Override
    public void postponeClaim(Claim claim) { /* TODO: set data about postpone */
        currentlyProcessed.remove(claim.id());
        postponed.add(claim);
    }

    @Override
    public void removeClaim(Claim claim) {
        currentlyProcessed.remove(claim.id());
    }

    @Override
    public void ingestIncoming() {
        /* TODO: ...*/
        List<Claim> claims = incomingClaimsService.getAll();
        claims.forEach(c -> tryConsume(c));
    }

    @Override
    public boolean tryConsume(Claim claim) {
        boolean shouldConsume = consumePredicate.stream().anyMatch(predicate -> predicate.predicate().test(claim));
        if (!shouldConsume) return false;
        waitList.add(claim);
        return true;
    }

    @Override
    public void reingestPostponed() {
        postponed.stream().forEach(c -> tryConsume(c));
        /* TODO: triggers and behaviour to be considered in the future */
    }

    @Override
    public int getPriority() {
        return waitList.size(); /* might be also something configured */
    }

    @Override
    public boolean isClaimProcessed(Claim claim) {
        return currentlyProcessed.containsKey(claim.id()); /* TODO: better update with event */
    }

}
