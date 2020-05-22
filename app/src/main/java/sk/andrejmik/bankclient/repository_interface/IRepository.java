package sk.andrejmik.bankclient.repository_interface;

import android.database.Observable;

import java.util.List;

import sk.andrejmik.bankclient.objects.BaseEntity;

public interface IRepository<T extends BaseEntity>
{
    Observable<T> get(Object id);
    
    Observable<List<T>> getAll();
    
    void save(T data);
    
    void delete(Object id);
}