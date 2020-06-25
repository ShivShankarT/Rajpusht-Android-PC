package in.rajpusht.pc.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;

@Dao
abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(T entity);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<T> entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insertAllCompletable(List<T> entity);
    @Update
    public abstract void update(T entity);

    @Delete
    public abstract void delete(T entity);
}