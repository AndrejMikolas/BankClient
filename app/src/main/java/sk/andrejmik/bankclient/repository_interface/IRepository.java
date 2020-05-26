package sk.andrejmik.bankclient.repository_interface;

import java.util.List;

import io.reactivex.Observable;
import sk.andrejmik.bankclient.objects.BaseEntity;

public interface IRepository<T extends BaseEntity>
{
    Observable<T> get(Object id);
    
    Observable<List<T>> getAll();
    
    Observable<T> save(T data);
    
    void delete(Object id);
}
