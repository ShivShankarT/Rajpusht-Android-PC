package in.rajpusht.pc.data.local;

import java.util.List;

import javax.inject.Inject;

import in.rajpusht.pc.data.local.db.AppDatabase;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.model.DataStatus;
import in.rajpusht.pc.model.Tuple;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Action;

public class AppDbHelper {
    AppDatabase mAppDatabase;
    AppPreferencesHelper appPreferencesHelper;

    @Inject
    public AppDbHelper(AppDatabase appDatabase, AppPreferencesHelper appPreferencesHelper) {
        this.mAppDatabase = appDatabase;
        this.appPreferencesHelper = appPreferencesHelper;
    }


    public Observable<Boolean> insertOrUpdateBeneficiary(final BeneficiaryEntity beneficiaryEntity) {
        return Observable.fromCallable(() -> {
            if (beneficiaryEntity.getId() == 0)
                mAppDatabase.beneficiaryDao().insert(beneficiaryEntity);
            else
                mAppDatabase.beneficiaryDao().update(beneficiaryEntity);
            return true;
        });
    }

    public Observable<Boolean> updateBeneficiary(final BeneficiaryEntity beneficiaryEntity) {
        return Observable.fromCallable(() -> {
            mAppDatabase.beneficiaryDao().update(beneficiaryEntity);
            return true;
        });
    }

    public Observable<Boolean> insertOrUpdatePregnant(final PregnantEntity pregnantEntity) {
        return Observable.fromCallable(() -> {
            if (pregnantEntity.getId() == 0)
                mAppDatabase.pregnantDao().insert(pregnantEntity);
            else
                mAppDatabase.pregnantDao().update(pregnantEntity);
            return true;
        });
    }

    public Observable<Boolean> updatePregnant(final PregnantEntity pregnantEntity) {
        return Observable.fromCallable(() -> {
            mAppDatabase.pregnantDao().update(pregnantEntity);
            return true;
        });
    }

    public Observable<Boolean> insertOrUpdateChild(final ChildEntity childEntity) {
        return Observable.fromCallable(() -> {
            if (childEntity.getId() == 0)
                mAppDatabase.childDao().insert(childEntity);
            else
                mAppDatabase.childDao().update(childEntity);
            return true;
        });
    }

    public Observable<Boolean> updateChild(final ChildEntity childEntity) {
        return Observable.fromCallable(() -> {
            mAppDatabase.childDao().update(childEntity);
            return true;
        });
    }

    //-------------------pwMonitor-----------------------

    public Maybe<PWMonitorEntity> pwMonitorByID(long id) {
        return mAppDatabase.pwMonitorDao().pwMonitorByID(id);
    }


    public Completable insertOrUpdatePwMonitor(PWMonitorEntity pwMonitorEntity) {

        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (pwMonitorEntity.getId() == 0)
                    mAppDatabase.pwMonitorDao().insert(pwMonitorEntity);
                else
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

        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                if (lmMonitorEntity.getId() == 0)
                    mAppDatabase.lmMonitorDao().insert(lmMonitorEntity);
                else
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



}
