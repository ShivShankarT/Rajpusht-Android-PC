package in.rajpusht.pc.data;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.util.ArrayList;
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
import in.rajpusht.pc.model.ApiResponse;
import in.rajpusht.pc.model.BefModel;
import in.rajpusht.pc.model.Tuple;
import in.rajpusht.pc.utils.AppDateTimeUtils;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import static in.rajpusht.pc.utils.JsonUtils.getDouble;
import static in.rajpusht.pc.utils.JsonUtils.getInt;
import static in.rajpusht.pc.utils.JsonUtils.getString;

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

    public Observable<Boolean> updateBeneficiary(final BeneficiaryEntity beneficiaryEntity) {

        return appDbHelper.updateBeneficiary(beneficiaryEntity);
    }

    public Observable<Boolean> updatePregnant(final PregnantEntity pregnantEntity) {
        return appDbHelper.updatePregnant(pregnantEntity);
    }

    public Observable<Boolean> updateChild(final ChildEntity childEntity) {
        return appDbHelper.updateChild(childEntity);
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

    public List<BefModel> getBefModels() {

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

    public Single<ApiResponse<JsonObject>> bulkdownload() {

        return appApiHelper.bulkDownload().doOnSuccess(jsonObjectbulkdata -> {
            if (jsonObjectbulkdata != null && jsonObjectbulkdata.isStatus()) {
                JsonObject jsonObject = jsonObjectbulkdata.getData();

                try {

                    JsonArray beneficiarylist = jsonObject.getAsJsonArray("beneficiary");
                    JsonArray pregnantlist = jsonObject.getAsJsonArray("pregnant");
                    JsonArray pw_monitorlist = jsonObject.getAsJsonArray("pw_monitor");
                    JsonArray childlist = jsonObject.getAsJsonArray("child");
                    JsonArray lm_monitorlist = jsonObject.getAsJsonArray("lm_monitor");

                    List<BeneficiaryEntity> beneficiaryModels = new ArrayList<>();
                    List<PregnantEntity> pregnantModels = new ArrayList<>();
                    List<PWMonitorEntity> pwMonitorModels = new ArrayList<>();
                    List<ChildEntity> childModel = new ArrayList<>();
                    List<LMMonitorEntity> lmMonitorModel = new ArrayList<>();

                    for (int i = 0; i < beneficiarylist.size(); i++) {

                        JsonObject object =  beneficiarylist.get(i).getAsJsonObject();

                        BeneficiaryEntity bmodel = new BeneficiaryEntity();

                        //String awc_code = getString(object,"awc_code").getAsString();//
                        String awc_code=getString(object,"awc_code");
                        
                        String name = getString(object,"name");
                        int age = getInt(object,"age");
                        String dob = getString(object,"dob");
                        String stage = getString(object,"stage");
                        String sub_stage = getString(object,"sub_stage");
                        int no_of_child = getInt(object,"no_of_child");
                        Integer pmmvy_installment = getInt(object,"pmmvy_installment");
                        Integer igmpy_installment = getInt(object,"igmpy_installment");
                        Integer jsy_installment = getInt(object,"jsy_installment");
                        Integer rajshri_installment = getInt(object,"rajshri_installment");
                        String husband_name = getString(object,"husband_name");
                        String mobile_no = getString(object,"mobile_no");
                        String husband_mobile_no = getString(object,"husband_mobile_no");
                        String caste = getString(object,"caste");
                        String economic_status = getString(object,"economic_status");
                        String pcts_id = getString(object,"pcts_id");
                        String bahamashah_id = getString(object,"bahamashah_id");
                        String is_counseling_provided = getString(object,"is_counseling_provided");
                        Integer counseling_sms = getInt(object,"counseling_sms");
                        int created_by = getInt(object,"created_by");

                        bmodel.setAwcCode(awc_code);
                        bmodel.setName(name);
                        bmodel.setAge(age);
                        bmodel.setDob(AppDateTimeUtils.convertDateFromServer(dob));
                        bmodel.setStage(stage);
                        bmodel.setSubStage(sub_stage);
                        bmodel.setChildCount(no_of_child);
                        bmodel.setPmmvyInstallment(pmmvy_installment);
                        bmodel.setIgmpyInstallment(igmpy_installment);
                        bmodel.setJsyInstallment(jsy_installment);
                        bmodel.setRajshriInstallment(rajshri_installment);
                        bmodel.setHusbandName(husband_name);
                        bmodel.setHusbandMobNo(husband_mobile_no);
                        bmodel.setMobileNo(mobile_no);
                        bmodel.setCaste(caste);
                        bmodel.setEconomic(economic_status);
                        bmodel.setPctsId(pcts_id);
                        bmodel.setBahamashahId(bahamashah_id);
                        bmodel.setCounselingProv(is_counseling_provided);
                        bmodel.setCounselingSms(counseling_sms);
                        bmodel.setCreatedBy(created_by);

                        beneficiaryModels.add(bmodel);


                    }

                    for (int i = 0; i < pregnantlist.size(); i++) {
                        JsonObject object = (JsonObject) pregnantlist.get(i);

                        PregnantEntity pmodel = new PregnantEntity();

                        String lmp_date = getString(object,"lmp_date");
                        int beneficiary_id = getInt(object,"beneficiary_id");

                        pmodel.setLmpDate(AppDateTimeUtils.convertDateFromServer(lmp_date));
                        pmodel.setBeneficiaryId(beneficiary_id);

                        pregnantModels.add(pmodel);

                    }

                    for (int i = 0; i < pw_monitorlist.size(); i++) {
                        JsonObject object = (JsonObject) pw_monitorlist.get(i);

                        PWMonitorEntity pwmodel = new PWMonitorEntity();
                        int pregnancy_id = getInt(object,"pregnancy_id");
                        String stage = getString(object,"stage");
                        String sub_stage = getString(object,"sub_stage");
                        int anc_count = getInt(object,"anc_count");
                        String last_anc = getString(object,"last_anc");
                        Double last_weight_in_mamta = getDouble(object,"last_weight_in_mamta");
                        String last_weight_check_date = getString(object,"last_weight_check_date");
                        Double current_weight = getDouble(object,"current_weight");
                        Integer pmmvy_installment = getInt(object,"pmmvy_installment");
                        Integer igmpy_installment = getInt(object,"igmpy_installment");
                        Integer jsy_installment = getInt(object,"jsy_installment");
                        Integer rajshri_installment = getInt(object,"rajshri_installment");
                        int created_by = getInt(object,"created_by");

                        pwmodel.setPregnancyId(pregnancy_id);
                        pwmodel.setStage(stage);
                        pwmodel.setSubStage(sub_stage);
                        pwmodel.setAncCount(anc_count);
                        pwmodel.setLastAnc(AppDateTimeUtils.convertDateFromServer(last_anc));
                        pwmodel.setLastWeightInMamta(last_weight_in_mamta);
                        pwmodel.setLastWeightCheckDate(AppDateTimeUtils.convertDateFromServer(last_weight_check_date));
                        pwmodel.setCurrentWeight(current_weight);
                        pwmodel.setPmmvyInstallment(pmmvy_installment);
                        pwmodel.setIgmpyInstallment(igmpy_installment);
                        pwmodel.setJsyInstallment(jsy_installment);
                        pwmodel.setRajshriInstallment(rajshri_installment);
                        pwmodel.setCreatedBy(created_by);

                        pwMonitorModels.add(pwmodel);


                    }

                    for (int i = 0; i < childlist.size(); i++) {
                        JsonObject object = (JsonObject) childlist.get(i);
                        ChildEntity cmodel = new ChildEntity();

                        String dob = getString(object,"dob");
                        Integer age = getInt(object,"age");
                        String stage = getString(object,"stage");
                        String sub_stage = getString(object,"sub_stage");
                        int mother_id = getInt(object,"mother_id");
                        Integer delivery_home = getInt(object,"delivery_home");
                        String delivery_place = getString(object,"delivery_place");
                        int child_order = getInt(object,"child_order");

                        cmodel.setDob(AppDateTimeUtils.convertDateFromServer(dob));
                        cmodel.setAge(age);
                        cmodel.setStage(stage);
                        cmodel.setSubStage(sub_stage);
                        cmodel.setMotherId(mother_id);
                        cmodel.setDeliveryHome(delivery_home);
                        cmodel.setDeliveryPlace(delivery_place);
                        cmodel.setChildOrder(child_order);

                        childModel.add(cmodel);


                    }

                    for (int i = 0; i < lm_monitorlist.size(); i++) {
                        JsonObject object = (JsonObject) lm_monitorlist.get(i);

                        int child_id = getInt(object,"child_id");
                        String is_first_immunization_complete = getString(object,"is_first_immunization_complete");
                        Double last_muac = getDouble(object,"last_muac");
                        String last_muac_check_date = getString(object,"last_muac_check_date");
                        Double current_muac = getDouble(object,"current_muac");
                        Double birth_weight = getDouble(object,"birth_weight");
                        Double child_weight = getDouble(object,"child_weight");
                        Integer pmmvy_installment = getInt(object,"pmmvy_installment");
                        Integer igmpy_installment = getInt(object,"igmpy_installment");
                        Integer jsy_installment = getInt(object,"jsy_installment");
                        Integer rajshri_installment = getInt(object,"rajshri_installment");
                        int created_by = getInt(object,"created_by");
                        String stage = getString(object,"stage");
                        String sub_stage = getString(object,"sub_stage");

                        LMMonitorEntity lmmodel = new LMMonitorEntity();
                        lmmodel.setChildId(child_id);
                        lmmodel.setIsFirstImmunizationComplete(is_first_immunization_complete);
                        lmmodel.setLastMuac(last_muac);
                        lmmodel.setLastMuacCheckDate(AppDateTimeUtils.convertDateFromServer(last_muac_check_date));
                        lmmodel.setCurrentMuac(current_muac);
                        lmmodel.setBirthWeight(birth_weight);
                        lmmodel.setChildWeight(child_weight);
                        lmmodel.setPmmvyInstallment(pmmvy_installment);
                        lmmodel.setIgmpyInstallment(igmpy_installment);
                        lmmodel.setJsyInstallment(jsy_installment);
                        lmmodel.setRajshriInstallment(rajshri_installment);
                        lmmodel.setCreatedBy(created_by);
                        lmmodel.setStage(stage);
                        lmmodel.setSubStage(sub_stage);

                        lmMonitorModel.add(lmmodel);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void bulkupload() {


        JsonObject mainJsonObject = new JsonObject();

        JsonArray beneficiaryArray = new JsonArray();
        JsonArray pregnantArray = new JsonArray();
        JsonArray pwMonitorArray = new JsonArray();
        JsonArray childArray = new JsonArray();
        JsonArray lmMonitorArray = new JsonArray();


        for (BeneficiaryEntity beneficiaryEntity : appDbHelper.getBeneficiaryNotSyncData()) {
            try {
                beneficiaryArray.add(getBeneficiaryJson(beneficiaryEntity));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        ;

        for (PregnantEntity pregnantEntity : appDbHelper.getPregnantNotSyncData()) {
            pregnantArray.add(getPregnantJson(pregnantEntity));

        }
        ;

        for (PWMonitorEntity pwMonitorEntity : appDbHelper.getPWNotSyncData()) {
            pwMonitorArray.add(getPwJson(pwMonitorEntity));
        }
        ;

        for (ChildEntity childEntity : appDbHelper.getChildNotSyncData()) {
            childArray.add(getChildJson(childEntity));
        }
        ;

        for (LMMonitorEntity lmMonitorEntity : appDbHelper.getLMNotSyncData()) {
            lmMonitorArray.add(getLmJson(lmMonitorEntity));
        }
        ;

        mainJsonObject.add("beneficiary", beneficiaryArray);
        mainJsonObject.add("pregnant", pregnantArray);
        mainJsonObject.add("pw_monitor", pwMonitorArray);
        mainJsonObject.add("child", childArray);
        mainJsonObject.add("lm_monitor", lmMonitorArray);

        appApiHelper.bulkUpload(mainJsonObject);
    }

    private JsonObject getLmJson(LMMonitorEntity lmMonitorEntity) {
        JsonObject lmObject = new JsonObject();


        lmObject.addProperty("child_id", lmMonitorEntity.getChildId());
        lmObject.addProperty("is_first_immunization_complete", lmMonitorEntity.getIsFirstImmunizationComplete());
        lmObject.addProperty("last_muac", lmMonitorEntity.getLastMuac());
        lmObject.addProperty("last_muac_check_date", AppDateTimeUtils.convertServerDate(lmMonitorEntity.getLastMuacCheckDate()));
        lmObject.addProperty("current_muac", lmMonitorEntity.getCurrentMuac());
        lmObject.addProperty("birth_weight", lmMonitorEntity.getBirthWeight());
        lmObject.addProperty("child_weight", lmMonitorEntity.getChildWeight());
        lmObject.addProperty("pmmvy_installment", lmMonitorEntity.getPmmvyInstallment());
        lmObject.addProperty("igmpy_installment", lmMonitorEntity.getIgmpyInstallment());
        lmObject.addProperty("jsy_installment", lmMonitorEntity.getJsyInstallment());
        lmObject.addProperty("rajshri_installment", lmMonitorEntity.getRajshriInstallment());
        lmObject.addProperty("created_by", lmMonitorEntity.getCreatedBy());
        lmObject.addProperty("stage", lmMonitorEntity.getStage());
        lmObject.addProperty("sub_stage", lmMonitorEntity.getSubStage());


        return lmObject;
    }

    private JsonObject getChildJson(ChildEntity childEntity) {
        JsonObject childobject = new JsonObject();

        childobject.addProperty("dob", AppDateTimeUtils.convertServerDate(childEntity.getDob()));
        childobject.addProperty("age", childEntity.getAge());
        childobject.addProperty("stage", childEntity.getSubStage());
        childobject.addProperty("sub_stage", childEntity.getSubStage());
        childobject.addProperty("mother_id", childEntity.getMotherId());
        childobject.addProperty("delivery_home", childEntity.getDeliveryHome());
        childobject.addProperty("delivery_place", childEntity.getDeliveryPlace());
        childobject.addProperty("child_order", childEntity.getChildOrder());


        return childobject;
    }

    private JsonObject getPwJson(PWMonitorEntity pwMonitorEntity) {
        JsonObject pwobject = new JsonObject();
        pwobject.addProperty("pregnancy_id", pwMonitorEntity.getPregnancyId());
        pwobject.addProperty("stage", pwMonitorEntity.getStage());
        pwobject.addProperty("sub_stage", pwMonitorEntity.getSubStage());
        pwobject.addProperty("anc_count", pwMonitorEntity.getAncCount());
        pwobject.addProperty("last_anc", AppDateTimeUtils.convertServerDate(pwMonitorEntity.getLastAnc()));
        pwobject.addProperty("last_weight_in_mamta", pwMonitorEntity.getLastWeightInMamta());
        pwobject.addProperty("last_weight_check_date", AppDateTimeUtils.convertServerDate(pwMonitorEntity.getLastWeightCheckDate()));
        pwobject.addProperty("current_weight", pwMonitorEntity.getCurrentWeight());
        pwobject.addProperty("pmmvy_installment", pwMonitorEntity.getPmmvyInstallment());
        pwobject.addProperty("igmpy_installment", pwMonitorEntity.getIgmpyInstallment());
        pwobject.addProperty("jsy_installment", pwMonitorEntity.getJsyInstallment());
        pwobject.addProperty("rajshri_installment", pwMonitorEntity.getRajshriInstallment());
        pwobject.addProperty("created_by", pwMonitorEntity.getCreatedBy());


        return pwobject;
    }

    private JsonObject getPregnantJson(PregnantEntity pregnantEntity) {
        JsonObject pregnantobject = new JsonObject();

        pregnantobject.addProperty("lmp_date", AppDateTimeUtils.convertServerDate(pregnantEntity.getLmpDate()));
        pregnantobject.addProperty("beneficiary_id", pregnantEntity.getBeneficiaryId());

        return pregnantobject;
    }

    private JsonObject getBeneficiaryJson(BeneficiaryEntity beneficiaryEntity) throws JSONException {
        JsonObject beneficiaryobject = new JsonObject();

        beneficiaryobject.addProperty("awc_code", beneficiaryEntity.getAwcCode());
        beneficiaryobject.addProperty("name", beneficiaryEntity.getName());
        beneficiaryobject.addProperty("age", beneficiaryEntity.getAge());
        beneficiaryobject.addProperty("dob", AppDateTimeUtils.convertServerDate(beneficiaryEntity.getDob()));
        beneficiaryobject.addProperty("stage", beneficiaryEntity.getStage());
        beneficiaryobject.addProperty("sub_stage", beneficiaryEntity.getSubStage());
        beneficiaryobject.addProperty("no_of_child", beneficiaryEntity.getChildCount());
        beneficiaryobject.addProperty("pmmvy_installment", beneficiaryEntity.getPmmvyInstallment());
        beneficiaryobject.addProperty("igmpy_installment", beneficiaryEntity.getIgmpyInstallment());
        beneficiaryobject.addProperty("jsy_installment", beneficiaryEntity.getJsyInstallment());
        beneficiaryobject.addProperty("rajshri_installment", beneficiaryEntity.getRajshriInstallment());
        beneficiaryobject.addProperty("husband_father_name", beneficiaryEntity.getHusbandName());
        beneficiaryobject.addProperty("mobile_no", beneficiaryEntity.getMobileNo());
        beneficiaryobject.addProperty("husband_mobile_no", beneficiaryEntity.getHusbandMobNo());
        beneficiaryobject.addProperty("caste", beneficiaryEntity.getCaste());
        beneficiaryobject.addProperty("economic_status", beneficiaryEntity.getEconomic());
        beneficiaryobject.addProperty("pcts_id", beneficiaryEntity.getPctsId());
        beneficiaryobject.addProperty("bahamashah_or_ack_id", beneficiaryEntity.getBahamashahId());
        beneficiaryobject.addProperty("is_counseling_prov", beneficiaryEntity.getCounselingProv());
        beneficiaryobject.addProperty("counseling_sms", beneficiaryEntity.getCounselingSms());
        beneficiaryobject.addProperty("created_by", beneficiaryEntity.getCreatedBy());
        return beneficiaryobject;
    }


}
