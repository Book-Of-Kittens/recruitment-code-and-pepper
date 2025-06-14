package org.waitList;

import org.claims.Claim;
import org.events.ClaimEventsBus;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;

import java.util.*;
import java.util.function.Predicate;

public class InMemoryWaitListService implements WaitListService {

    private final Predicate<Claim> consumePredicate;

    private final PriorityQueue<Claim> waitList;
    private final HashMap<String, Claim> currentlyProcessed = new HashMap<>(); // TODO: thread safety!
    private final List<Claim> postponed = new LinkedList<>();

    public InMemoryWaitListService(ClaimEventsBus events,
                                   Predicate<Claim> consumePredicate,
                                   Comparator<Claim> orderSource) {
        this.waitList = new PriorityQueue<>(20, orderSource);
        this.consumePredicate = consumePredicate;
        events.subscribe(this::onEvent);
    }

    @Override
    public Claim getClaimForProcessing() {
        if (waitList.isEmpty()) return null;
        Claim claim = waitList.poll();
        currentlyProcessed.put(claim.id(), claim);
        return claim;
    }

    @Override
    public void placePostponedBackOnTheWaitList() {
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
        if (consumePredicate.test(claim)) waitList.add(claim);
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
