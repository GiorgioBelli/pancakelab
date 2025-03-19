package org.pancakelab.model;

import org.pancakelab.model.pancakes.Pancake;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private final UUID id;
    private final int building;
    private final int room;
    private final List<Pancake> pancakes;

    protected Order(UUID id, int building, int room) {
        this.id = id;
        this.building = building;
        this.room = room;
        this.pancakes = new ArrayList<>();
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

    public List<Pancake> getPancakes() {
        return pancakes;
    }

    public Order addPancake(Pancake pancake) {
        this.pancakes.add(pancake);
        return this;
    }

    public Order removePancakesByDescription(String description, int count) {
        if (count <=0 ){
            return this;
        }
        final AtomicInteger removedCount = new AtomicInteger(0);
        this.getPancakes()
                .removeIf(pancake -> pancake.description().equals(description) && removedCount.getAndIncrement() < count);
        return this;
    }

    public List<String> view() {
        return this.getPancakes().stream()
                .map(Pancake::description)
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
}
