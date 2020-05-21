package in.rajpusht.pc.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(T entity);

    @Update
    abstract void update(T entity);

    @Delete
    abstract void delete(T entity);
}