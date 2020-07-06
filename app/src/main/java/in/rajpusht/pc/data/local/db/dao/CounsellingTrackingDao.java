package in.rajpusht.pc.data.local.db.dao;

import android.util.Log;
import android.util.Pair;

import androidx.collection.LongSparseArray;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import in.rajpusht.pc.data.local.db.entity.CounselingTrackingEntity;
import in.rajpusht.pc.data.local.db.entity.CounselingTrackingEntity;
import io.reactivex.Single;

@Dao
public abstract class CounsellingTrackingDao extends BaseDao<CounselingTrackingEntity> {
    @Query("Delete FROM counseling_tracking")
    public abstract void deleteAll();

    @Query("Select * from counseling_tracking where id=:id")
    public abstract Single<CounselingTrackingEntity> getCounselingTrackingById(long id);

    @Query("Select * from counseling_tracking ")
    public abstract Single<List<CounselingTrackingEntity>> getCounselingTrackingList();

    public Single<CounselingTrackingEntity> insertAndReturn(CounselingTrackingEntity counselingTrackingEntity) {
        return insertSingle(counselingTrackingEntity).flatMap(this::getCounselingTrackingById);
    }

    // first pw and l
    public Single<Pair<LongSparseArray<List<CounselingTrackingEntity>>, LongSparseArray<List<CounselingTrackingEntity>>>> counselingTrackingListPairForm() {

        return getCounselingTrackingList().map(counselingTrackingEntities -> {

            LongSparseArray<List<CounselingTrackingEntity>> pw = new LongSparseArray<>();
            LongSparseArray<List<CounselingTrackingEntity>> lm = new LongSparseArray<>();
            Pair<LongSparseArray<List<CounselingTrackingEntity>>, LongSparseArray<List<CounselingTrackingEntity>>> pair = new Pair<>(pw, lm);
            for (CounselingTrackingEntity counselingTrackingEntity : counselingTrackingEntities) {
                List<CounselingTrackingEntity> form;
                if (counselingTrackingEntity.isPwType()) {
                    form = pw.get(counselingTrackingEntity.getFormId(), new ArrayList<>());
                    pw.put(counselingTrackingEntity.getFormId(), form);
                } else {
                    form = lm.get(counselingTrackingEntity.getFormId(), new ArrayList<>());
                    lm.put(counselingTrackingEntity.getFormId(), form);
                }
                form.add(counselingTrackingEntity);

            }

            return pair;


        });

    }
}
