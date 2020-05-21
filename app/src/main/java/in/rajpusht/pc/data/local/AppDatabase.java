package in.rajpusht.pc.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import in.rajpusht.pc.data.local.dao.AssignedLocationDao;
import in.rajpusht.pc.data.local.entity.AssignedLocationEntity;

@Database(entities = {AssignedLocationEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AssignedLocationDao assignedLocationDao();
}