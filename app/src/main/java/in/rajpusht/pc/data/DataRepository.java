package in.rajpusht.pc.data;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import in.rajpusht.pc.custom.ui.DropDownModel;
import in.rajpusht.pc.data.local.AppDbHelper;
import in.rajpusht.pc.data.local.db.entity.AssignedLocationEntity;
import in.rajpusht.pc.data.local.db.entity.BeneficiaryEntity;
import in.rajpusht.pc.data.local.db.entity.ChildEntity;
import in.rajpusht.pc.data.local.db.entity.CounselingTrackingEntity;
import in.rajpusht.pc.data.local.db.entity.InstitutionPlaceEntity;
import in.rajpusht.pc.data.local.db.entity.LMMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.LocationEntity;
import in.rajpusht.pc.data.local.db.entity.PWMonitorEntity;
import in.rajpusht.pc.data.local.db.entity.PregnantEntity;
import in.rajpusht.pc.data.local.pref.AppPreferencesHelper;
import in.rajpusht.pc.data.remote.AppApiHelper;
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.model.AwcSyncCount;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.model.BeneficiaryJoin;
import in.rajpusht.pc.model.BeneficiaryWithChild;
import in.rajpusht.pc.model.ProfileDetail;
import in.rajpusht.pc.model.Tuple;
import in.rajpusht.pc.utils.AppDateTimeUtils;
import in.rajpusht.pc.utils.JsonParser;
import in.rajpusht.pc.utils.event_bus.EventBusLiveData;
import in.rajpusht.pc.utils.event_bus.MessageEvent;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import kotlin.Triple;
import timber.log.Timber;

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

    public Observable<Boolean> insertOrUpdatePregnant(PregnantEntity lmMonitorEntity) {
        return appDbHelper.insertOrUpdatePregnant(lmMonitorEntity);
    }

    public Observable<Boolean> insertOrUpdateChild(final ChildEntity childEntity) {
        return appDbHelper.insertOrUpdateChild(childEntity);
    }


    public Maybe<PWMonitorEntity> pwMonitorByID(long id) {

        return appDbHelper.pwMonitorByID(id);
    }

    public Single<List<PWMonitorEntity>> pwMonitorData(long pid) {
        return appDbHelper.pwMonitorData(pid);
    }


    public Completable insertOrUpdatePwMonitor(PWMonitorEntity pwMonitorEntity) {

        return appDbHelper.insertOrUpdatePwMonitor(pwMonitorEntity);
    }

    public Maybe<LMMonitorEntity> lmMonitorById(long id) {

        return appDbHelper.lmMonitorById(id);
    }

    public Single<List<LMMonitorEntity>> lmMonitorsByChildId(long childId) {

        return appDbHelper.lmMonitorsByChildId(childId);
    }

    public Completable insertOrUpdateLmMonitor(LMMonitorEntity lmMonitorEntity) {
        return appDbHelper.insertOrUpdateLmMonitor(lmMonitorEntity);
    }

    public Single<CounselingTrackingEntity> insertOrUpdateCounsellingTracking(CounselingTrackingEntity counselingTrackingEntity) {
        return appDbHelper.insertOrUpdateCounsellingTracking(counselingTrackingEntity);
    }

    public Single<Long> insertBeneficiaryLocation(LocationEntity locationEntity) {
        return appDbHelper.insertBeneficiaryLocation(locationEntity);
    }


    public LiveData<List<BefModel>> getBefModels() {

        return appDbHelper.getBefModels();
    }

    public LiveData<List<BefModel>> otherWomenBefModels() {

        return appDbHelper.otherWomenBefModels();
    }

    public Single<BeneficiaryJoin> getBeneficiaryData(long beneficiaryId) {
        return appDbHelper.getBeneficiaryJoinData(beneficiaryId);
    }

    public Single<BeneficiaryWithChild> getBeneficiaryChildDataById(long beneficiaryId) {
        return appDbHelper.getBeneficiaryChildDataById(beneficiaryId);
    }

    public Single<BeneficiaryJoin> getBeneficiaryJoinDataFromPregnancyId(long pregnancyId) {

        return appDbHelper.getBeneficiaryJoinDataFromPregnancyId(pregnancyId);
    }

    public Single<BeneficiaryJoin> getBeneficiaryJoinDataFromChildId(long childID) {

        return appDbHelper.getBeneficiaryJoinDataFromChildId(childID);
    }

    public Single<BeneficiaryEntity> getBeneficiary(long beneficiaryId) {
        return appDbHelper.getBeneficiary(beneficiaryId);
    }

    public Maybe<ChildEntity> getChild(long childId) {

        return appDbHelper.getChild(childId);
    }


    public Single<ApiResponse<List<InstitutionPlaceEntity>>> fetchInsertFacilityData() {

        return appApiHelper.fetchInstitutionPlaceEntity()
                .flatMap(listApiResponse -> {
                    if (listApiResponse.isStatus())
                        return appDbHelper.insertInstitutionPlace(listApiResponse.getData()).toSingleDefault(listApiResponse);
                    else
                        return Single.error(new Exception());
                });


    }

    public Single<List<DropDownModel>> getInstitutionLocation(String type) {

        return appDbHelper.getInstitutionLocation(type);
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

    public Single<ApiResponse<JsonObject>> resendOtp() {
        return appApiHelper.resendOtp();
    }

    public Single<ApiResponse<JsonElement>> changePassword(String oldPassword,
                                                           String newPassword) {
        return appApiHelper.changePassword(oldPassword, newPassword);
    }

    public Single<ApiResponse<JsonObject>> forgotPassword(String email) {
        return appApiHelper.forgotPassword(email).doOnSuccess(jsonObjectApiResponse -> {
            if (jsonObjectApiResponse != null && jsonObjectApiResponse.isStatus()) {
                JsonObject data = jsonObjectApiResponse.getData();
                String token = data.get("token").getAsString();
                JsonElement otpjsonElement = data.get("reset_otp");
                String otp = otpjsonElement != null && !otpjsonElement.isJsonNull() ? otpjsonElement.getAsString() : null;
                appPreferencesHelper.setAccessToken(token);
                appPreferencesHelper.putString("otp", otp);
            }
        });
    }

    public Single<ApiResponse<JsonElement>> setPassword(String resetOtp, String newPassword) {
        return appApiHelper.setPassword(resetOtp, newPassword);
    }

    public Single<ApiResponse<JsonObject>> bulkUpload(JsonArray jsonArray) {
        return appApiHelper.bulkUpload(jsonArray).doAfterSuccess(new Consumer<ApiResponse<JsonObject>>() {
            @Override
            public void accept(ApiResponse<JsonObject> jsonObjectApiResponse) throws Exception {
                putPrefString(AppPreferencesHelper.PREF_LAST_SYNC, AppDateTimeUtils.convertLocalDateTime(new Date()));
                EventBusLiveData.postMessage(MessageEvent.getMessageEvent(MessageEvent.MessageEventType.SYNC_SUCCESS));
                Timber.i("Sync upload Success");

            }
        }).flatMap(new Function<ApiResponse<JsonObject>, SingleSource<? extends ApiResponse<JsonObject>>>() {
            @Override
            public SingleSource<? extends ApiResponse<JsonObject>> apply(ApiResponse<JsonObject> jsonObjectApiResponse) throws Exception {
                if (jsonObjectApiResponse.isStatus())
                    return appDbHelper.deleteAllBeneficiaryData().toSingleDefault(jsonObjectApiResponse);//if success delete all data
                else
                    return Single.just(jsonObjectApiResponse);
            }
        });
    }

    public Single<ApiResponse<JsonObject>> uploadDataToServer() {

        return Single.zip(appDbHelper.getNotSyncBenfData(), appDbHelper.counselingTrackingListPairForm(), appDbHelper.getBeneficiaryLocation(), Tuple::new)
                .map(JsonParser::convertBenfUploadJson).flatMap(jsonElements -> {
                    if (jsonElements.size() > 0)
                        return bulkUpload(jsonElements);
                    else {
                        ApiResponse<JsonObject> jsonObjectApiResponse = new ApiResponse<>();
                        jsonObjectApiResponse.setInternalErrorCode(ApiResponse.NO_DATA_SYNC);
                        jsonObjectApiResponse.setStatus(false);
                        jsonObjectApiResponse.setInternalError(true);
                        return Single.just(jsonObjectApiResponse);
                    }
                });
    }

    public Observable<List<AwcSyncCount>> awcViceSyncData() {
        return appDbHelper.awcViceSyncData();
    }

    public Single<Triple<Integer, Integer, String>> appConfigVersion() {
        return appApiHelper.appConfigVersion().doOnSuccess(new Consumer<Triple<Integer, Integer, String>>() {
            @Override
            public void accept(Triple<Integer, Integer, String> integerIntegerStringTriple) throws Exception {
                if (integerIntegerStringTriple != null) {
                    appPreferencesHelper.putInt(AppPreferencesHelper.PREF_MIN_VERSION, integerIntegerStringTriple.getFirst());
                    appPreferencesHelper.putInt(AppPreferencesHelper.PREF_CURRENT_VERSION, integerIntegerStringTriple.getSecond());
                    appPreferencesHelper.putString(AppPreferencesHelper.PREF_DRIVE_URL, integerIntegerStringTriple.getThird());
                    appPreferencesHelper.putString(AppPreferencesHelper.PREF_LAST_APPCONFIG_FTIME, String.valueOf(new Date().getTime()));
                }


            }
        });
    }


    public Single<ApiResponse<ProfileDetail>> profileDetail() {
        return appApiHelper.profileDetail();
    }

    public Single<ApiResponse<JsonElement>> profileUpdate(String fName, String lName, String mob) {

        return appApiHelper.profileUpdate(fName, lName, mob).doOnSuccess(new Consumer<ApiResponse<JsonElement>>() {
            @Override
            public void accept(ApiResponse<JsonElement> jsonElementApiResponse) throws Exception {
                if (jsonElementApiResponse.isStatus()) {
                    appPreferencesHelper.putString(AppPreferencesHelper.PREF_FIRST_NAME, fName);
                    appPreferencesHelper.putString(AppPreferencesHelper.PREF_LAST_NAME, lName);
                    appPreferencesHelper.setCurrentUserName(fName + " " + lName);
                }
            }
        });
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
                    appPreferencesHelper.setCurrentUserName(data.getFirstName() + " " + data.getLastName());
                    appPreferencesHelper.putString(AppPreferencesHelper.PREF_FIRST_NAME, data.getFirstName());
                    appPreferencesHelper.putString(AppPreferencesHelper.PREF_LAST_NAME, data.getLastName());
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

    public String getString(@StringRes int stringId) {
        return appPreferencesHelper.getStringRes(stringId);
    }

    @SuppressWarnings("rawtypes")
    public Single<ApiResponse<JsonElement>> logoutApiReq() {
        return appApiHelper.logout();
    }

    public void logout() {
        appPreferencesHelper.logout();
    }

    public void putPrefString(String key, String value) {

        appPreferencesHelper.putString(key, value);
    }

    public String getPrefString(String key) {

        return appPreferencesHelper.getString(key);
    }

    public int getPrefInt(String key) {

        return appPreferencesHelper.getInt(key);
    }

}
