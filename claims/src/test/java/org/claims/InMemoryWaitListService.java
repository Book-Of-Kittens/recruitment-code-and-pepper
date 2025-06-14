package org.claims;

import org.events.ClaimEventsQueue;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class InMemoryWaitListService implements WaitListService {


    // TODO: to be considered: compound predicate with possible custom logic instead of the list. currently  unclear which lists are OR and which are AND.
    private final List<Predicate<Claim>> consumePredicate;
    private final Comparator<Claim> orderSource;/* TODO: list order! */

    private final List<Claim> waitList = new LinkedList<>(); // TODO: choose structure
    private final HashMap<String, Claim> currentlyProcessed = new HashMap<>(); // TODO: thread safety!
    private final List<Claim> postponed = new LinkedList<>();

    public InMemoryWaitListService(ClaimEventsQueue events, List<Predicate<Claim>> consumePredicate, Comparator<Claim> orderSource) {
        this.consumePredicate = consumePredicate;
        this.orderSource = orderSource;
        events.subscribe(this::onEvent);
    }

    @Override
    public Claim getClaimForProcessing() {
        if (waitList.isEmpty()) return null;
        Claim claim = waitList.getFirst();
        waitList.removeFirst(); /* TODO: list order! */
        currentlyProcessed.put(claim.id(), claim);
        return claim;
    }

    @Override
    public void reingestPostponed() {
        postponed.forEach(this::tryConsume);
    }

    @Override
    public int getPriority() {
        return waitList.size(); /* might be also something configured */
    }


    @Override
    public boolean hasClaimsToProcess() {
        return !waitList.isEmpty();
    }

    // EVENTS:

    private void onEvent(ClaimUpdatedEvent event) {
        if (EventType.APPROVED == event.eventType()) removeClaimIfProcessing(event.claim());
        if (EventType.APPROVAL_POSTPONED == event.eventType()) postponeProcessedClaim(event.claim());
        if (EventType.NEW == event.eventType()) tryConsume(event.claim()); // remember that all wait list get access to the same events!
    }

    private void postponeProcessedClaim(Claim claim) {
        if (isClaimProcessed(claim)) postponeClaim(claim);
    }

    private void removeClaimIfProcessing(Claim claim) {
        if (isClaimProcessed(claim)) removeClaim(claim);
    }

    private void tryConsume(Claim claim) {
        boolean shouldConsume = consumePredicate.stream().anyMatch(predicate -> predicate.test(claim));
        if (!shouldConsume) return;
        waitList.add(claim);
    }

    private void postponeClaim(Claim claim) {
        currentlyProcessed.remove(claim.id());
        postponed.add(claim);
    }

    private void removeClaim(Claim claim) {
        currentlyProcessed.remove(claim.id());
    }

    private boolean isClaimProcessed(Claim claim) {
        return currentlyProcessed.containsKey(claim.id());
    }

}
