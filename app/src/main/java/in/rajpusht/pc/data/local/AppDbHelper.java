package in.rajpusht.pc.data.local;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import in.rajpusht.pc.data.local.db.AppDatabase;
import in.rajpusht.pc.data.local.db.entity.AssignedLocationEntity;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.model.BefRel;
import in.rajpusht.pc.model.DataStatus;
import in.rajpusht.pc.model.Quintet;
import in.rajpusht.pc.model.Tuple;
import in.rajpusht.pc.utils.AppDateTimeUtils;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

public class AppDbHelper {
    private AppDatabase mAppDatabase;
    private AppPreferencesHelper appPreferencesHelper;

    @Inject
    public AppDbHelper(AppDatabase appDatabase, AppPreferencesHelper appPreferencesHelper) {
        this.mAppDatabase = appDatabase;
        this.appPreferencesHelper = appPreferencesHelper;
    }

    public Completable deleteAndInsertAssignedLocation(List<AssignedLocationEntity> assignedLocationEntities) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mAppDatabase.assignedLocationDao().deleteAll();
                mAppDatabase.assignedLocationDao().insertAll(assignedLocationEntities);
            }
        });
    }

    public Completable insertAndDeleteBenfData(Quintet<List<BeneficiaryEntity>, List<PregnantEntity>, List<ChildEntity>, List<PWMonitorEntity>, List<LMMonitorEntity>> quintet) {

        return Completable.fromAction(() -> {
            mAppDatabase.beneficiaryDao().deleteAll();
            mAppDatabase.pregnantDao().deleteAll();
            mAppDatabase.childDao().deleteAll();
            mAppDatabase.pwMonitorDao().deleteAll();
            mAppDatabase.lmMonitorDao().deleteAll();
            mAppDatabase.beneficiaryDao().insertAll(quintet.getT1());
            mAppDatabase.pregnantDao().insertAll(quintet.getT2());
            mAppDatabase.childDao().insertAll(quintet.getT3());
            mAppDatabase.pwMonitorDao().insertAll(quintet.getT4());
            mAppDatabase.lmMonitorDao().insertAll(quintet.getT5());

        });


    }

    public Maybe<List<AssignedLocationEntity>> getAllAssignedLocation() {
        return mAppDatabase.assignedLocationDao().getAllAssignedLocation();
    }

    public Observable<Boolean> insertOrUpdateBeneficiary(final BeneficiaryEntity beneficiaryEntity) {
        return Observable.fromCallable(() -> {
            if (beneficiaryEntity.getId() == 0) {
                if (beneficiaryEntity.getDataStatus() == null)
                    beneficiaryEntity.setDataStatus(DataStatus.NEW);
                beneficiaryEntity.setCreatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                beneficiaryEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                beneficiaryEntity.setCreatedBy(appPreferencesHelper.getCurrentUserId());
                mAppDatabase.beneficiaryDao().insert(beneficiaryEntity);
            } else {
                if (beneficiaryEntity.getDataStatus() == null || beneficiaryEntity.getDataStatus() == DataStatus.OLD)
                    beneficiaryEntity.setDataStatus(DataStatus.EDIT);
                beneficiaryEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                mAppDatabase.beneficiaryDao().update(beneficiaryEntity);
            }
            return true;
        });
    }


    public Observable<Boolean> insertOrUpdatePregnant(final PregnantEntity pregnantEntity) {
        return Observable.fromCallable(() -> {
            if (pregnantEntity.getId() == 0) {
                if (pregnantEntity.getDataStatus() == null)
                    pregnantEntity.setDataStatus(DataStatus.NEW);
                pregnantEntity.setCreatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                pregnantEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                mAppDatabase.pregnantDao().insert(pregnantEntity);
            } else {
                if (pregnantEntity.getDataStatus() == null || pregnantEntity.getDataStatus() == DataStatus.OLD)
                    pregnantEntity.setDataStatus(DataStatus.EDIT);
                pregnantEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                mAppDatabase.pregnantDao().update(pregnantEntity);
            }
            return true;
        });
    }


    public Observable<Boolean> insertOrUpdateChild(final ChildEntity childEntity) {
        return Observable.fromCallable(() -> {
            if (childEntity.getId() == 0) {
                if (childEntity.getDataStatus() == null)
                    childEntity.setDataStatus(DataStatus.NEW);
                childEntity.setCreatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                childEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                mAppDatabase.childDao().insert(childEntity);
            } else {
                if (childEntity.getDataStatus() == null || childEntity.getDataStatus() == DataStatus.OLD)
                    childEntity.setDataStatus(DataStatus.EDIT);
                childEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                mAppDatabase.childDao().update(childEntity);
            }
            return true;
        });
    }


    //-------------------pwMonitor-----------------------

    public Maybe<PWMonitorEntity> pwMonitorByID(long id) {
        return mAppDatabase.pwMonitorDao().pwMonitorByID(id);
    }


    public Completable insertOrUpdatePwMonitor(PWMonitorEntity pwMonitorEntity) {

        return Completable.fromAction(() -> {
            if (pwMonitorEntity.getId() == 0) {
                if (pwMonitorEntity.getDataStatus() == null)
                    pwMonitorEntity.setDataStatus(DataStatus.NEW);
                pwMonitorEntity.setCreatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                pwMonitorEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                pwMonitorEntity.setCreatedBy(appPreferencesHelper.getCurrentUserId());
                mAppDatabase.pwMonitorDao().insert(pwMonitorEntity);
            } else {
                if (pwMonitorEntity.getDataStatus() == null || pwMonitorEntity.getDataStatus() == DataStatus.OLD)
                    pwMonitorEntity.setDataStatus(DataStatus.EDIT);
                pwMonitorEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                mAppDatabase.pwMonitorDao().update(pwMonitorEntity);
            }
        });
    }


    //-------------------LmMonitor-----------------------

    public Maybe<LMMonitorEntity> lmMonitorById(long id) {

        return mAppDatabase.lmMonitorDao().lmMonitorById(id);
    }

    // update based on primary key
    public Completable insertOrUpdateLmMonitor(LMMonitorEntity lmMonitorEntity) {

        return Completable.fromAction(() -> {
            if (lmMonitorEntity.getId() == 0) {
                if (lmMonitorEntity.getDataStatus() == null)
                    lmMonitorEntity.setDataStatus(DataStatus.NEW);
                lmMonitorEntity.setCreatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                lmMonitorEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                lmMonitorEntity.setCreatedBy(appPreferencesHelper.getCurrentUserId());
                mAppDatabase.lmMonitorDao().insert(lmMonitorEntity);
            } else {
                if (lmMonitorEntity.getDataStatus() == null || lmMonitorEntity.getDataStatus() == DataStatus.OLD)
                    lmMonitorEntity.setDataStatus(DataStatus.EDIT);
                lmMonitorEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                mAppDatabase.lmMonitorDao().update(lmMonitorEntity);
            }
        });
    }


    public List<BefModel> getBefModels() {
        return mAppDatabase.AppDao().befModels();
    }

    private Tuple<BeneficiaryEntity, PregnantEntity, ChildEntity> getBeneficiaryDatah(long beneficiaryId) {
        BeneficiaryEntity q = mAppDatabase.beneficiaryDao().getBeneficiariesById(beneficiaryId);
        List<PregnantEntity> s = mAppDatabase.pregnantDao().getPregnantById(beneficiaryId);
        List<ChildEntity> r = mAppDatabase.childDao().childEntities(beneficiaryId);

        PregnantEntity pregnantEntity = s.isEmpty() ? null : s.get(0);
        ChildEntity childEntity = r.isEmpty() ? null : r.get(0);
        return new Tuple<>(q, pregnantEntity, childEntity);
    }

    public Single<Tuple<BeneficiaryEntity, PregnantEntity, ChildEntity>> getBeneficiaryData(long beneficiaryId) {

        return Single.fromCallable(() -> getBeneficiaryDatah(beneficiaryId));
    }

    public Single<BeneficiaryEntity> getBeneficiary(long beneficiaryId) {

        return Single.fromCallable(() -> mAppDatabase.beneficiaryDao().getBeneficiariesById(beneficiaryId));
    }



    // upload data sync

    public Maybe<List<BefRel>> getNotSyncBenfData() {
        return mAppDatabase.AppDao().befRels();
    }


}
