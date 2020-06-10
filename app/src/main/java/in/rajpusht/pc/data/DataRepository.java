package in.rajpusht.pc.data;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import javax.inject.Inject;

import in.rajpusht.pc.data.local.AppDbHelper;
import in.rajpusht.pc.data.local.db.entity.AssignedLocationEntity;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.data.remote.AppApiHelper;
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.model.ProfileDetail;
import in.rajpusht.pc.model.Tuple;
import in.rajpusht.pc.utils.JsonParser;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

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

    public Single<List<Boolean>> insertOrUpdateBeneficiaryData(BeneficiaryEntity beneficiaryEntity, @Nullable ChildEntity childEntity, @Nullable PregnantEntity pregnantEntity) {

        Observable<Boolean> ob = null;
        if (childEntity != null && pregnantEntity != null)
            ob = Observable.merge(appDbHelper.insertOrUpdateBeneficiary(beneficiaryEntity), appDbHelper.insertOrUpdatePregnant(pregnantEntity), appDbHelper.insertOrUpdateChild(childEntity));
        else if (pregnantEntity != null)
            ob = Observable.merge(appDbHelper.insertOrUpdateBeneficiary(beneficiaryEntity), appDbHelper.insertOrUpdatePregnant(pregnantEntity));

        else if (childEntity != null)
            ob = Observable.merge(appDbHelper.insertOrUpdateBeneficiary(beneficiaryEntity), appDbHelper.insertOrUpdateChild(childEntity));

        return ob.toList();
    }


    public boolean getLogin() {
        return appPreferencesHelper.getLogin();
    }

    public void setLogin(boolean login) {
        appPreferencesHelper.setLogin(login);
    }

    public String getSelectedAwcCode() {
        return appPreferencesHelper.getSelectedAwcCode();
    }

    public Maybe<List<AssignedLocationEntity>> getAllAssignedLocation() {
        return appDbHelper.getAllAssignedLocation();
    }

    public Observable<Boolean> insertOrUpdateBeneficiary(final BeneficiaryEntity beneficiaryEntity) {
        return appDbHelper.insertOrUpdateBeneficiary(beneficiaryEntity);
    }

    public Observable<Boolean> insertOrUpdateChild(final ChildEntity childEntity) {
        return appDbHelper.insertOrUpdateChild(childEntity);
    }


    public Maybe<PWMonitorEntity> pwMonitorByID(long id) {

        return appDbHelper.pwMonitorByID(id);
    }

    public Completable insertOrUpdatePwMonitor(PWMonitorEntity pwMonitorEntity) {

        return appDbHelper.insertOrUpdatePwMonitor(pwMonitorEntity);
    }

    public Maybe<LMMonitorEntity> lmMonitorById(long id) {

        return appDbHelper.lmMonitorById(id);
    }

    public Completable insertOrUpdateLmMonitor(LMMonitorEntity lmMonitorEntity) {
        return appDbHelper.insertOrUpdateLmMonitor(lmMonitorEntity);
    }

    public LiveData<List<BefModel>> getBefModels() {

        return appDbHelper.getBefModels();
    }

    public Single<Tuple<BeneficiaryEntity, PregnantEntity, ChildEntity>> getBeneficiaryData(long beneficiaryId) {
        return appDbHelper.getBeneficiaryData(beneficiaryId);
    }

    public Single<BeneficiaryEntity> getBeneficiary(long beneficiaryId) {
        return appDbHelper.getBeneficiary(beneficiaryId);
    }


    public Single<ApiResponse<JsonObject>> login(String email, String password) {
        return appApiHelper.login(email, password).doOnSuccess(jsonObjectApiResponse -> {
            if (jsonObjectApiResponse != null && jsonObjectApiResponse.isStatus()) {
                JsonObject data = jsonObjectApiResponse.getData();
                String token = data.get("token").getAsString();
                JsonElement otpjsonElement = data.get("otp");
                String otp = otpjsonElement != null && !otpjsonElement.isJsonNull() ? otpjsonElement.getAsString() : null;
                appPreferencesHelper.setAccessToken(token);
                appPreferencesHelper.putString("otp", otp);
            }
        });
    }

    public Single<ApiResponse<JsonElement>> verifyOtp(String otp) {
        return appApiHelper.verifyOtp(otp);
    }

    public Single<ApiResponse<JsonObject>> bulkUpload(JsonArray jsonArray) {
        return appApiHelper.bulkUpload(jsonArray);
    }

    public Single<ApiResponse<JsonObject>> uploadDataToServer() {
        return appDbHelper.getNotSyncBenfData()
                .map(JsonParser::convertBenfUploadJson).flatMapSingle(jsonElements -> appApiHelper.bulkUpload(jsonElements));
    }


    public Single<ApiResponse<ProfileDetail>> profileDetail() {
        return appApiHelper.profileDetail();
    }

    public Completable profileAssignedLocation() {

        return appApiHelper
                .profileDetail()
                .flatMapCompletable(profileDetailApiResponse -> {

                    if (profileDetailApiResponse == null || !profileDetailApiResponse.isStatus())
                        return Completable.error(new Exception("no data"));
                    ProfileDetail data = profileDetailApiResponse.getData();
                    appPreferencesHelper.setCurrentUserId(Long.valueOf(data.getPcId()));
                    appPreferencesHelper.setCurrentUserEmail(data.getEmail());
                    appPreferencesHelper.setCurrentUserMob(String.valueOf(data.getMobile()));
                    appPreferencesHelper.setCurrentUserName(data.getFirstName() +" "+ data.getLastName());
                    return appDbHelper.deleteAndInsertAssignedLocation(data.getAssignedLocations());
                });
    }

    public Completable bulkdownloadandInsert() {
        return appApiHelper.bulkDownload().flatMapCompletable(new Function<ApiResponse<JsonObject>, CompletableSource>() {
            @Override
            public CompletableSource apply(ApiResponse<JsonObject> jsonObjectApiResponse) throws Exception {
                if (jsonObjectApiResponse.isStatus())
                    return appDbHelper.insertAndDeleteBenfData(JsonParser.parseBenfData(jsonObjectApiResponse));
                else
                    return Completable.error(new Exception(jsonObjectApiResponse.getMessage()));
            }
        });
    }

    public Completable profileAndBulkDownload() {
        //return  profileAssignedLocation().andThen( bulkdownloadandInsert());
        return Completable.concatArray(profileAssignedLocation(), bulkdownloadandInsert());
    }


    public void logout() {
        appPreferencesHelper.logout();
    }
}
