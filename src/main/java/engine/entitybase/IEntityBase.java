package engine.entitybase;

import java.util.List;

public interface IEntityBase<T> {

    void add(T t);
    void addAll(T[] t);
    void addAll(List<T> t);

    List<T> getAllWithName(String name);
    List<T> getAllWithId(long id);

    void remove(T object);
    void removeAll();

}
