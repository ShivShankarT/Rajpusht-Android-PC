package in.rajpusht.pc.data;

import androidx.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import in.rajpusht.pc.data.local.AppDbHelper;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.data.remote.AppApiHelper;
import in.rajpusht.pc.model.BefModel;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class DataRepository {

    private AppDbHelper appDbHelper;
    private AppPreferencesHelper appPreferencesHelper;
    private AppApiHelper appApiHelper;

    @Inject
    public DataRepository(AppDbHelper appDbHelper, AppPreferencesHelper appPreferencesHelper, AppApiHelper appApiHelper) {
        this.appDbHelper = appDbHelper;
        this.appPreferencesHelper = appPreferencesHelper;
        this.appApiHelper = appApiHelper;
    }

    public Single<List<Boolean>> insertBeneficiaryData(BeneficiaryEntity beneficiaryEntity, @Nullable ChildEntity childEntity, @Nullable PregnantEntity pregnantEntity) {

        Observable<Boolean> ob = null;
        if (childEntity != null && pregnantEntity != null)
            ob = Observable.merge(appDbHelper.insertBeneficiary(beneficiaryEntity), appDbHelper.insertPregnant(pregnantEntity), appDbHelper.insertChild(childEntity));
        else if (pregnantEntity != null)
            ob = Observable.merge(appDbHelper.insertBeneficiary(beneficiaryEntity), appDbHelper.insertPregnant(pregnantEntity));

        else if (childEntity != null)
            ob = Observable.merge(appDbHelper.insertBeneficiary(beneficiaryEntity), appDbHelper.insertChild(childEntity));

        return ob.toList();
    }


    public Completable insertPwMonitor(PWMonitorEntity pwMonitorEntity) {
        return appDbHelper.insertPwMonitor(pwMonitorEntity);
    }

    public Completable insertLmMonitor(LMMonitorEntity lmMonitorEntity) {
        return appDbHelper.insertLmMonitor(lmMonitorEntity);
    }

    public  List<BefModel> getBefModels(){

        return  appDbHelper.getBefModels();
    }


}
