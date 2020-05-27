package in.rajpusht.pc.data.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import in.rajpusht.pc.data.local.db.dao.AppDao;
import in.rajpusht.pc.data.local.db.dao.AssignedLocationDao;
import in.rajpusht.pc.data.local.db.dao.BeneficiaryDao;
import in.rajpusht.pc.data.local.db.dao.ChildDao;
import in.rajpusht.pc.data.local.db.dao.LMMonitorDao;
import in.rajpusht.pc.data.local.db.dao.PWMonitorDao;
import in.rajpusht.pc.data.local.db.dao.PregnantDao;
import in.rajpusht.pc.data.local.db.entity.AssignedLocationEntity;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;

@Database(entities = {AssignedLocationEntity.class,
        BeneficiaryEntity.class,
        PregnantEntity.class,
        ChildEntity.class,
        PWMonitorEntity.class,
        LMMonitorEntity.class
}, version = 2)
@TypeConverters(value = {DbDataConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract AssignedLocationDao assignedLocationDao();

    public abstract BeneficiaryDao beneficiaryDao();

    public abstract ChildDao childDao();

    public abstract PregnantDao pregnantDao();

    public abstract LMMonitorDao lmMonitorDao();

    public abstract PWMonitorDao pwMonitorDao();

    public abstract AppDao AppDao();
}