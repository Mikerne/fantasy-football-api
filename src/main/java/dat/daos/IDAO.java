package dat.daos;

import java.util.List;

public interface IDAO<T, I> {

    T read(Integer integer);
    List<T> readAll();
    T create(T t);
    T update(I i, T t);
    void delete(I i);
    boolean validatePrimaryKey(I i);

}
