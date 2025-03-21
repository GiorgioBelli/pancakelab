package org.pancakelab.model;

import org.pancakelab.model.pancakes.PancakeRecipe;
import org.pancakelab.model.state.CreatedState;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private final UUID id;
    private final int building;
    private final int room;
    private final List<PancakeRecipe> pancakes;
    private OrderState state;

    protected Order(UUID id, int building, int room) {
        this.id = id;
        this.building = building;
        this.room = room;
        this.pancakes = new ArrayList<>();
        this.state = new CreatedState();
    }

    public Order(int building, int room) {
        this(UUID.randomUUID(), building, room);
    }

    public UUID getId() {
        return id;
    }

    public int getBuilding() {
        return building;
    }

    public int getRoom() {
        return room;
    }

    public synchronized List<PancakeRecipe> getPancakes() {
        return pancakes;
    }

    public synchronized Order addPancake(PancakeRecipe pancake) {
        this.pancakes.add(pancake);
        return this;
    }

    public synchronized void removePancakesByDescription(String description, int count) {
        if (count <=0 ){
            return;
        }
        final AtomicInteger removedCount = new AtomicInteger(0);
        this.getPancakes()
                .removeIf(pancake -> pancake.description().equals(description) && removedCount.getAndIncrement() < count);
    }

    public List<String> view() {
        return this.getPancakes().stream()
                .map(PancakeRecipe::description)
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public synchronized OrderState getState() {
        return state;
    }

    public synchronized void setState(OrderState state) {
        this.state = state;
    }

    public synchronized StateTransitionResult cancel() {
        return state.cancel(this);
    }

    public synchronized StateTransitionResult complete() {
        return state.complete(this);
    }

    public synchronized StateTransitionResult prepare() {
        return state.prepare(this);
    }

    public synchronized StateTransitionResult deliver() {
        return state.deliver(this);
    }
}
