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
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

public class AppDbHelper {
    AppDatabase mAppDatabase;
    AppPreferencesHelper appPreferencesHelper;

    @Inject
    public AppDbHelper(AppDatabase appDatabase, AppPreferencesHelper appPreferencesHelper) {
        this.mAppDatabase = appDatabase;
        this.appPreferencesHelper = appPreferencesHelper;
    }


    public Observable<Boolean> insertBeneficiary(final BeneficiaryEntity beneficiaryEntity) {
        return Observable.fromCallable(() -> {
            mAppDatabase.beneficiaryDao().insert(beneficiaryEntity);
            return true;
        });
    }

    public Observable<Boolean> insertPregnant(final PregnantEntity pregnantEntity) {
        return Observable.fromCallable(() -> {
            mAppDatabase.pregnantDao().insert(pregnantEntity);
            return true;
        });
    }

    public Observable<Boolean> insertChild(final ChildEntity childEntity) {
        return Observable.fromCallable(() -> {
            mAppDatabase.childDao().insert(childEntity);
            return true;
        });
    }


    public Completable insertPwMonitor(PWMonitorEntity pwMonitorEntity) {

        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mAppDatabase.pwMonitorDao().insert(pwMonitorEntity);
            }
        });
    }

    public Completable insertLmMonitor(LMMonitorEntity lmMonitorEntity) {

        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mAppDatabase.lmMonitorDao().insert(lmMonitorEntity);
            }
        });
    }


    public List<BefModel> getBefModels() {
        return mAppDatabase.AppDao().befModels();
    }

}
