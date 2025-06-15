package org.waitList;

import org.claims.Claim;
import org.events.ClaimEventsBus;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

public class InMemoryWaitListService implements WaitListService {

    private final Predicate<Claim> consumePredicate;

    private final PriorityQueue<Claim> newClaimsQueue;
    private final HashMap<String, Claim> currentlyProcessed = new HashMap<>();
    private final AbstractQueue<Claim> postponed = new ConcurrentLinkedQueue<>();

    /* TODO: thread safety. */

    public InMemoryWaitListService(ClaimEventsBus events,
                                   Predicate<Claim> consumePredicate,
                                   Comparator<Claim> orderSource) {
        this.newClaimsQueue = new PriorityQueue<>(20, orderSource);
        this.consumePredicate = consumePredicate;
        events.subscribe(this::onEvent);
    }

    @Override
    public Claim getClaimForProcessing() {
        if (newClaimsQueue.isEmpty()) return null;
        Claim claim = newClaimsQueue.poll();
        currentlyProcessed.put(claim.id(), claim);
        return claim;
    }

    @Override
    public void placePostponedBackOnTheWaitList() {

        /* TODO:
        as per document content:
        Należy przemyśleć, jak rozwiązać odkładanie i ponowne rozpatrywanie roszczeń, które nie zmieściły się w dziennym limicie.
        and also:
        Roszczenia, które nie mogą zostać przetworzone danego dnia (np. przekraczają budżet lub limit HIGH), należy odłożyć do kolejnego dnia, zachowując ich priorytety.

         */

        Iterator<Claim> iterator = postponed.iterator();
        while (iterator.hasNext()) {
            tryConsume(iterator.next());
            iterator.remove();
        }
    }

    @Override
    public int getPriority() {
        return newClaimsQueue.size(); /* might be also something configured */
    }

    @Override
    public boolean hasClaimsToProcess() {
        return !newClaimsQueue.isEmpty();
    }

    // EVENTS:

    private void onEvent(ClaimUpdatedEvent event) {
        if (EventType.APPROVED == event.eventType()) removeClaimIfProcessing(event.claim());
        if (EventType.POSTPONED == event.eventType()) postponeProcessedClaim(event.claim());
        if (EventType.NEW == event.eventType()) tryConsume(event.claim()); // remember that all wait list get access to the same events!
    }

    private void postponeProcessedClaim(Claim claim) {
        if (isClaimProcessed(claim)) postponeClaim(claim);
    }

    private void removeClaimIfProcessing(Claim claim) {
        if (isClaimProcessed(claim)) removeClaim(claim);
    }

    private void tryConsume(Claim claim) {
        if (consumePredicate.test(claim)) newClaimsQueue.add(claim);
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
