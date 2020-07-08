package in.rajpusht.pc.data.local;

import android.util.Pair;

import androidx.collection.LongSparseArray;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import in.rajpusht.pc.custom.ui.DropDownModel;
import in.rajpusht.pc.data.local.db.AppDatabase;
import in.rajpusht.pc.data.local.db.entity.AssignedLocationEntity;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.CounselingTrackingEntity;
import in.rajpusht.pc.data.local.db.entity.InstitutionPlaceEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.model.AwcStageCount;
import in.rajpusht.pc.model.AwcSyncCount;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.model.BeneficiaryJoin;
import in.rajpusht.pc.model.BeneficiaryWithChild;
import in.rajpusht.pc.model.BeneficiaryWithRelation;
import in.rajpusht.pc.model.DataStatus;
import in.rajpusht.pc.model.Quintet;
import in.rajpusht.pc.utils.AppDateTimeUtils;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;

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

    public Completable deleteAllBeneficiaryData() {

        return Completable.fromAction(() -> {
            mAppDatabase.beneficiaryDao().deleteAll();
            mAppDatabase.pregnantDao().deleteAll();
            mAppDatabase.childDao().deleteAll();
            mAppDatabase.pwMonitorDao().deleteAll();
            mAppDatabase.lmMonitorDao().deleteAll();
            mAppDatabase.counsellingTrackingDao().deleteAll();
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
        return mAppDatabase.assignedLocationDao().getAllAssignedLocation().zipWith(mAppDatabase.assignedLocationDao().awcStageCount(), new BiFunction<List<AssignedLocationEntity>, List<AwcStageCount>, List<AssignedLocationEntity>>() {
            @Override
            public List<AssignedLocationEntity> apply(List<AssignedLocationEntity> assignedLocationEntities, List<AwcStageCount> awcStageCounts) throws Exception {
                //todo break
                for (AssignedLocationEntity assignedLocationEntity : assignedLocationEntities) {
                    for (AwcStageCount awcStageCount : awcStageCounts) {
                        if (assignedLocationEntity.getAwcCode().equals(awcStageCount.getAwcCode())) {
                            if ("PW".equals(awcStageCount.getStage())) {
                                assignedLocationEntity.setPwCount(awcStageCount.getCount());
                            } else if ("LM".equals(awcStageCount.getStage())) {
                                assignedLocationEntity.setLmCount(awcStageCount.getCount());
                            } else if ("MY".equals(awcStageCount.getStage())) {
                                assignedLocationEntity.setMyCount(awcStageCount.getCount());
                            }
                        }
                    }

                }


                return assignedLocationEntities;
            }
        });
    }

    public Observable<Boolean> insertOrUpdateBeneficiary(final BeneficiaryEntity beneficiaryEntity) {
        return Observable.fromCallable(() -> {
            if (beneficiaryEntity.getId() == 0) {
                if (beneficiaryEntity.getDataStatus() == null)
                    beneficiaryEntity.setDataStatus(DataStatus.NEW);
                beneficiaryEntity.setCreatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                beneficiaryEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                beneficiaryEntity.setCreatedBy(appPreferencesHelper.getCurrentUserId());
                beneficiaryEntity.setUuid(UUID.randomUUID().toString());
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
                pregnantEntity.setUuid(UUID.randomUUID().toString());
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
                childEntity.setUuid(UUID.randomUUID().toString());
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

    public Single<List<PWMonitorEntity>> pwMonitorData(long pid) {
        return mAppDatabase.pwMonitorDao().pwMonitor(pid);
    }


    public Completable insertOrUpdatePwMonitor(PWMonitorEntity pwMonitorEntity) {

        return Completable.fromAction(() -> {
            if (pwMonitorEntity.getDataStatus() == null || pwMonitorEntity.getDataStatus() == DataStatus.NEW) {
                if (pwMonitorEntity.getDataStatus() == null)
                    pwMonitorEntity.setDataStatus(DataStatus.NEW);
                pwMonitorEntity.setCreatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                pwMonitorEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                pwMonitorEntity.setCreatedBy(appPreferencesHelper.getCurrentUserId());
                pwMonitorEntity.setUuid(UUID.randomUUID().toString());
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

    public Single<List<LMMonitorEntity>> lmMonitorsByChildId(long childId) {

        return mAppDatabase.lmMonitorDao().lmMonitorList(childId);
    }

    // update based on primary key
    public Completable insertOrUpdateLmMonitor(LMMonitorEntity lmMonitorEntity) {

        return Completable.fromAction(() -> {
            if (lmMonitorEntity.getDataStatus() == null || lmMonitorEntity.getDataStatus() == DataStatus.NEW) {
                if (lmMonitorEntity.getDataStatus() == null)
                    lmMonitorEntity.setDataStatus(DataStatus.NEW);
                lmMonitorEntity.setCreatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                lmMonitorEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                lmMonitorEntity.setCreatedBy(appPreferencesHelper.getCurrentUserId());
                lmMonitorEntity.setUuid(UUID.randomUUID().toString());
                mAppDatabase.lmMonitorDao().insert(lmMonitorEntity);
            } else {
                if (lmMonitorEntity.getDataStatus() == null || lmMonitorEntity.getDataStatus() == DataStatus.OLD)
                    lmMonitorEntity.setDataStatus(DataStatus.EDIT);
                lmMonitorEntity.setUpdatedAt(AppDateTimeUtils.convertServerTimeStampDate(new Date()));
                mAppDatabase.lmMonitorDao().update(lmMonitorEntity);
            }
        });
    }

    // update based on primary key
    public Single<CounselingTrackingEntity> insertOrUpdateCounsellingTracking(CounselingTrackingEntity counselingTrackingEntity) {

        if (counselingTrackingEntity.getId() == 0) {
            counselingTrackingEntity.setStartTime( new Date());
            counselingTrackingEntity.setLastKnowUpdateTime( new Date());
            counselingTrackingEntity.setUuid(UUID.randomUUID().toString());
            return mAppDatabase.counsellingTrackingDao().insertAndReturn(counselingTrackingEntity);
        } else {

            return mAppDatabase.counsellingTrackingDao().updateViaCompletable(counselingTrackingEntity).toSingleDefault(counselingTrackingEntity);
        }

    }

    public Single<Pair<LongSparseArray<List<CounselingTrackingEntity>>, LongSparseArray<List<CounselingTrackingEntity>>>> counselingTrackingListPairForm() {

        return mAppDatabase.counsellingTrackingDao().counselingTrackingListPairForm();
    }


    public LiveData<List<BefModel>> getBefModels() {
        return mAppDatabase.AppDao().befModels(appPreferencesHelper.getSelectedAwcCode());
    }

    public LiveData<List<BefModel>> otherWomenBefModels() {
        return mAppDatabase.AppDao().otherWomenBefModels(appPreferencesHelper.getSelectedAwcCode());
    }


    public Single<BeneficiaryJoin> getBeneficiaryJoinDataFromPregnancyId(long pregnancyId) {
        return Single.fromCallable(() -> {
            PregnantEntity pregnantEntity = mAppDatabase.pregnantDao().getPregnantByPregnancyId(pregnancyId);
            BeneficiaryEntity beneficiaryEntity = mAppDatabase.beneficiaryDao().getBeneficiariesById(pregnantEntity.getBeneficiaryId());
            List<ChildEntity> childEntities = mAppDatabase.childDao().childEntities(beneficiaryEntity.getBeneficiaryId());
            ChildEntity childEntity = childEntities.isEmpty() ? null : childEntities.get(0);
            BeneficiaryJoin beneficiaryJoin = new BeneficiaryJoin();
            beneficiaryJoin.setBeneficiaryEntity(beneficiaryEntity);
            beneficiaryJoin.setChildEntity(childEntity);
            beneficiaryJoin.setPregnantEntity(pregnantEntity);
            return beneficiaryJoin;
        });
    }

    public Single<BeneficiaryJoin> getBeneficiaryJoinDataFromChildId(long childID) {
        return Single.fromCallable(() -> {
            ChildEntity childEntity = mAppDatabase.childDao().childEntityId(childID);
            BeneficiaryEntity beneficiaryEntity = mAppDatabase.beneficiaryDao().getBeneficiariesById(childEntity.getMotherId());
            PregnantEntity pregnantEntity = mAppDatabase.pregnantDao().getPregnantByPregnancyId(childEntity.getMotherId());
            BeneficiaryJoin beneficiaryJoin = new BeneficiaryJoin();
            beneficiaryJoin.setBeneficiaryEntity(beneficiaryEntity);
            beneficiaryJoin.setChildEntity(childEntity);
            beneficiaryJoin.setPregnantEntity(pregnantEntity);
            return beneficiaryJoin;
        });
    }

    public Single<BeneficiaryJoin> getBeneficiaryJoinData(long beneficiaryId) {
        return mAppDatabase.beneficiaryDao().getBeneficiaryDataById(beneficiaryId);
    }

    public Single<BeneficiaryWithChild> getBeneficiaryChildDataById(long beneficiaryId) {
        return mAppDatabase.beneficiaryDao().getBeneficiaryChildDataById(beneficiaryId);
    }

    public Single<BeneficiaryEntity> getBeneficiary(long beneficiaryId) {

        return Single.fromCallable(() -> mAppDatabase.beneficiaryDao().getBeneficiariesById(beneficiaryId));
    }

    public Maybe<ChildEntity> getChild(long childId) {

        return mAppDatabase.childDao().childEntity(childId);
    }


    // upload data sync

    public Single<List<BeneficiaryWithRelation>> getNotSyncBenfData() {
        return mAppDatabase.AppDao().getAllNotSyncBeneficiaryWithRelation();
    }


    public Observable<List<AwcSyncCount>> awcViceSyncData() {
        return mAppDatabase.AppDao().awcViceSyncData();
    }

    //institution

    public Single<List<DropDownModel>> getInstitutionLocation(String type) {
        return mAppDatabase.institutionPlaceDao().getInstitutionLocation(type);
    }

    public Single<Long> getInstitutionPlaceCount() {

        return mAppDatabase.institutionPlaceDao().getInstitutionPlaceCount();
    }

    public Completable insertInstitutionPlace(List<InstitutionPlaceEntity> placeEntities) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mAppDatabase.institutionPlaceDao().deleteAll();
                mAppDatabase.institutionPlaceDao().insertAll(placeEntities);
            }
        });
    }


}
