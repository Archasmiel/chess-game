package engine.entity.entitybase;

import engine.entity.basic.Identifiable;
import engine.entity.basic.Nameable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityBase<T extends Nameable & Identifiable> implements IEntityBase<T> {

    private final List<T> entities;

    public EntityBase() {
        entities = new ArrayList<>();
    }

    @Override
    public void add(T t) {
        if (t.getId() == 0) {
            t.setId(RandomIds.generate());
        }
        entities.add(t);
    }

    @Override
    public void addAll(T[] t) {
        addAll(Arrays.asList(t));
    }

    @Override
    public void addAll(List<T> t) {
        t.forEach(this::add);
    }

    @Override
    public List<T> getAllWithName(String name) {
        return entities.stream()
                .filter(e -> e.getName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<T> getAllWithId(long id) {
        return entities.stream()
                .filter(e -> e.getId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public void remove(T object) {
        if (entities.contains(object)) {
            entities.remove(object);
        } else {
            throw new IllegalStateException("Not found object with id " + object.getId());
        }
        RandomIds.remove(object.getId());
    }

    @Override
    public void removeAll() {
        entities.clear();
    }
}
