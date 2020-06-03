package in.rajpusht.pc.data;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

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
import in.rajpusht.pc.model.BeneficiaryModel;
import in.rajpusht.pc.model.ChildModel;
import in.rajpusht.pc.model.LmMonitorModel;
import in.rajpusht.pc.model.PregnantModel;
import in.rajpusht.pc.model.PwMonitorModel;
import in.rajpusht.pc.model.Tuple;
import io.reactivex.Completable;
import io.reactivex.Maybe;
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
                    JsonArray childlist=jsonObject.getAsJsonArray("child");
                    JsonArray lm_monitorlist = jsonObject.getAsJsonArray("lm_monitor");

                    Log.i("beneficiarylist", "bulkdownload: "+beneficiarylist);
                    Log.i("pregnanlist", "bulkdownload: "+pregnantlist);
                    Log.i("pwmonitorlist", "bulkdownload: "+pw_monitorlist);
                    Log.i("childlist", "bulkdownload: "+childlist);
                    Log.i("lmmonitorlist", "bulkdownload: "+lm_monitorlist);


                    List<BeneficiaryModel> beneficiaryModels = new ArrayList<>();
                    List<PregnantModel> pregnantModels=new ArrayList<>();
                    List<PwMonitorModel> pwMonitorModels=new ArrayList<>();
                    List<ChildModel> childModel=new ArrayList<>();
                    List<LmMonitorModel> lmMonitorModel=new ArrayList<>();

                    for (int i = 0; i < beneficiarylist.size(); i++) {

                        JsonObject object = (JsonObject) beneficiarylist.get(i);

                        BeneficiaryModel bmodel = new BeneficiaryModel();

                        String awc_code = object.get("awc_code").toString();
                        String name = object.get("name").toString();
                        int age = object.get("age").getAsInt();
                        String dob = object.get("dob").toString();
                        String stage = object.get("stage").toString();
                        String sub_stage = object.get("sub_stage").toString();
                        int no_of_child = object.get("no_of_child").getAsInt();
                        int pmmvy_installment = object.get("pmmvy_installment").getAsInt();
                        int igmpy_installment = object.get("igmpy_installment").getAsInt();
                        int jsy_installment = object.get("jsy_installment").getAsInt();
                        int rajshri_installment = object.get("rajshri_installment").getAsInt();
                        String husband_name = object.get("husband_name").toString();
                        int mobile_no = object.get("mobile_no").getAsInt();
                        int husband_mobile_no = object.get("husband_mobile_no").getAsInt();
                        String caste = object.get("caste").toString();
                        String economic_status = object.get("economic_status").toString();
                        String pcts_id = object.get("pcts_id").toString();
                        String bahamashah_id = object.get("bahamashah_id").toString();
                        String is_counseling_provided = object.get("is_counseling_provided").toString();
                        String counseling_sms = object.get("counseling_sms").toString();
                        int created_by = object.get("created_by").getAsInt();

                        bmodel.setAwc_code(awc_code);
                        bmodel.setName(name);
                        bmodel.setAge(age);
                        bmodel.setDob(dob);
                        bmodel.setStage(stage);
                        bmodel.setSub_stage(sub_stage);
                        bmodel.setNo_of_child(no_of_child);
                        bmodel.setPmmvy_installment(pmmvy_installment);
                        bmodel.setIgmpy_installment(igmpy_installment);
                        bmodel.setJsy_installment(jsy_installment);
                        bmodel.setRajshri_installment(rajshri_installment);
                        bmodel.setHusband_name(husband_name);
                        bmodel.setHusband_mobile_no(husband_mobile_no);
                        bmodel.setMobile_no(mobile_no);
                        bmodel.setCaste(caste);
                        bmodel.setEconomic_status(economic_status);
                        bmodel.setPcts_id(pcts_id);
                        bmodel.setBahamashah_id(bahamashah_id);
                        bmodel.setIs_counseling_provided(is_counseling_provided);
                        bmodel.setCounseling_sms(counseling_sms);
                        bmodel.setCreated_by(created_by);

                        beneficiaryModels.add(bmodel);


                    }

                    for(int i=0;i<pregnantlist.size();i++){
                        JsonObject object= (JsonObject) pregnantlist.get(i);

                        PregnantModel pmodel=new PregnantModel();

                         String lmp_date=object.get("lmp_date").toString();
                         int beneficiary_id=object.get("beneficiary_id").getAsInt();

                        pmodel.setLmp_date(lmp_date);
                        pmodel.setBeneficiary_id(beneficiary_id);

                         pregnantModels.add(pmodel);
                        
                    }

                    for(int i=0;i<pw_monitorlist.size();i++){
                        JsonObject object= (JsonObject) pw_monitorlist.get(i);

                        PwMonitorModel pwmodel=new PwMonitorModel();

                         int pregnancy_id=object.get("pregnancy_id").getAsInt();
                         String stage=object.get("stage").toString();
                         String sub_stage=object.get("sub_stage").toString();
                         int anc_count=object.get("anc_count").getAsInt();
                         String last_anc=object.get("last_anc").toString();
                         int last_weight_in_mamta=object.get("last_weight_in_mamta").getAsInt();
                         String last_weight_check_date=object.get("last_weight_check_date").toString();
                         String current_weight=object.get("current_weight").toString();
                         int pmmvy_installment=object.get("pmmvy_installment").getAsInt();
                         int igmpy_installment=object.get("igmpy_installment").getAsInt();
                         int jsy_installment=object.get("jsy_installment").getAsInt();
                         int rajshri_installment=object.get("rajshri_installment").getAsInt();
                         int created_by=object.get("created_by").getAsInt();

                         pwmodel.setPregnancy_id(pregnancy_id);
                         pwmodel.setStage(stage);
                         pwmodel.setSub_stage(sub_stage);
                         pwmodel.setAnc_count(anc_count);
                         pwmodel.setLast_anc(last_anc);
                         pwmodel.setLast_weight_in_mamta(last_weight_in_mamta);
                         pwmodel.setLast_weight_check_date(last_weight_check_date);
                         pwmodel.setCurrent_weight(current_weight);
                         pwmodel.setPmmvy_installment(pmmvy_installment);
                         pwmodel.setIgmpy_installment(igmpy_installment);
                         pwmodel.setJsy_installment(jsy_installment);
                         pwmodel.setRajshri_installment(rajshri_installment);
                         pwmodel.setCreated_by(created_by);

                        pwMonitorModels.add(pwmodel);


                    }

                    for(int i=0;i<childlist.size();i++){
                        JsonObject object= (JsonObject) childlist.get(i);
                        ChildModel cmodel=new ChildModel();

                         String dob=object.get("dob").toString();
                         int age=object.get("age").getAsInt();
                         String stage=object.get("stage").toString();
                         String sub_stage=object.get("sub_stage").toString();
                         int mother_id=object.get("mother_id").getAsInt();
                         String delivery_home=object.get("delivery_home").toString();
                         String delivery_place=object.get("delivery_place").toString();
                         int child_order=object.get("child_order").getAsInt();

                         cmodel.setDob(dob);
                         cmodel.setAge(age);
                         cmodel.setStage(stage);
                         cmodel.setSub_stage(sub_stage);
                         cmodel.setMother_id(mother_id);
                         cmodel.setDelivery_home(delivery_home);
                         cmodel.setDelivery_place(delivery_place);
                         cmodel.setChild_order(child_order);

                         childModel.add(cmodel);


                    }

                    for(int i=0;i<lm_monitorlist.size();i++){
                        JsonObject object= (JsonObject) lm_monitorlist.get(i);
                        LmMonitorModel lmmodel=new LmMonitorModel();
                         int child_id=object.get("child_id").getAsInt();
                         String is_first_immunization_complete=object.get("is_first_immunization_complete").toString();
                         double last_muac=object.get("last_muac").getAsInt();
                         String last_muac_check_date=object.get("last_muac_check_date").toString();
                         double current_muac=object.get("current_muac").getAsInt();
                         double birth_weight=object.get("birth_weight").getAsInt();
                         double child_weight=object.get("child_weight").getAsInt();
                         int pmmvy_installment=object.get("pmmvy_installment").getAsInt();
                         int igmpy_installment=object.get("igmpy_installment").getAsInt();
                         int jsy_installment=object.get("jsy_installment").getAsInt();
                         int rajshri_installment=object.get("rajshri_installment").getAsInt();
                         int created_by=object.get("created_by").getAsInt();
                         String stage=object.get("stage").toString();
                         String sub_stage=object.get("sub_stage").toString();

                         lmmodel.setChild_id(child_id);
                         lmmodel.setIs_first_immunization_complete(is_first_immunization_complete);
                         lmmodel.setLast_muac(last_muac);
                         lmmodel.setLast_muac_check_date(last_muac_check_date);
                         lmmodel.setCurrent_muac(current_muac);
                         lmmodel.setBirth_weight(birth_weight);
                         lmmodel.setChild_weight(child_weight);
                         lmmodel.setPmmvy_installment(pmmvy_installment);
                         lmmodel.setIgmpy_installment(igmpy_installment);
                         lmmodel.setJsy_installment(jsy_installment);
                         lmmodel.setRajshri_installment(rajshri_installment);
                         lmmodel.setCreated_by(created_by);
                         lmmodel.setStage(stage);
                         lmmodel.setSub_stage(sub_stage);

                         lmMonitorModel.add(lmmodel);



                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    public void bulkupload(long beneficiaryid,String motherid, String childid,String pgwomenid){


        JsonObject mainJsonObject = new JsonObject();

        JsonArray beneficiaryArray = new JsonArray();
        JsonArray pregnantArray = new JsonArray();
        JsonArray pwMonitorArray = new JsonArray();
        JsonArray childArray = new JsonArray();
        JsonArray lmMonitorArray = new JsonArray();


        for(BeneficiaryEntity beneficiaryEntity : appDbHelper.getBeneficiaryNotSyncData()){
            try {
                beneficiaryArray.add(getBeneficiaryJson(beneficiaryEntity));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        };

        for(PregnantEntity pregnantEntity : appDbHelper.getPregnantNotSyncData()){
            pregnantArray.add(getPregnantJson(pregnantEntity));

        };

        for(PWMonitorEntity pwMonitorEntity : appDbHelper.getPWNotSyncData()){
            pwMonitorArray.add(getPwJson(pwMonitorEntity));
        };

        for(ChildEntity childEntity : appDbHelper.getChildNotSyncData()){
            childArray.add(getChildJson(childEntity));
        };

        for(LMMonitorEntity lmMonitorEntity : appDbHelper.getLMNotSyncData()){
            lmMonitorArray.add(getLmJson(lmMonitorEntity));
        };

        mainJsonObject.add("beneficiary",beneficiaryArray);
        mainJsonObject.add("pregnant",pregnantArray);
        mainJsonObject.add("pw_monitor",pwMonitorArray);
        mainJsonObject.add("child",childArray);
        mainJsonObject.add("lm_monitor",lmMonitorArray);

        appApiHelper.bulkUpload(mainJsonObject);
    }

    private JsonObject getLmJson(LMMonitorEntity lmMonitorEntity) {
        JsonObject lmObject=new JsonObject();


        lmObject.addProperty("child_id",lmMonitorEntity.getChildId());
        lmObject.addProperty("is_first_immunization_complete",lmMonitorEntity.getIsFirstImmunizationComplete());
        lmObject.addProperty("last_muac",lmMonitorEntity.getLastMuac());
        lmObject.addProperty("last_muac_check_date",lmMonitorEntity.getLastMuacCheckDate().toString());
        lmObject.addProperty("current_muac",lmMonitorEntity.getCurrentMuac());
        lmObject.addProperty("birth_weight",lmMonitorEntity.getBirthWeight());
        lmObject.addProperty("child_weight",lmMonitorEntity.getChildWeight());
        lmObject.addProperty("pmmvy_installment",lmMonitorEntity.getPmmvyInstallment());
        lmObject.addProperty("igmpy_installment",lmMonitorEntity.getIgmpyInstallment());
        lmObject.addProperty("jsy_installment",lmMonitorEntity.getJsyInstallment());
        lmObject.addProperty("rajshri_installment",lmMonitorEntity.getRajshriInstallment());
        lmObject.addProperty("created_by",lmMonitorEntity.getCreatedBy());
        lmObject.addProperty("stage",lmMonitorEntity.getStage());
        lmObject.addProperty("sub_stage",lmMonitorEntity.getSubStage());


        return lmObject;
    }

    private JsonObject getChildJson(ChildEntity childEntity) {
        JsonObject childobject=new JsonObject();

        childobject.addProperty("dob",childEntity.getDob().toString());
        childobject.addProperty("age",childEntity.getAge());
        childobject.addProperty("stage",childEntity.getSubStage());
        childobject.addProperty("sub_stage",childEntity.getSubStage());
        childobject.addProperty("mother_id",childEntity.getMotherId());
        childobject.addProperty("delivery_home",childEntity.getDeliveryHome());
        childobject.addProperty("delivery_place",childEntity.getDeliveryPlace());
        childobject.addProperty("child_order",childEntity.getChildOrder());


        return childobject;
    }

    private JsonObject getPwJson(PWMonitorEntity pwMonitorEntity) {
        JsonObject pwobject=new JsonObject();
        pwobject.addProperty("pregnancy_id",pwMonitorEntity.getPregnancyId());
        pwobject.addProperty("stage",pwMonitorEntity.getStage());
        pwobject.addProperty("sub_stage",pwMonitorEntity.getSubStage());
        pwobject.addProperty("anc_count",pwMonitorEntity.getAncCount());
        pwobject.addProperty("last_anc",pwMonitorEntity.getLastAnc().toString());
        pwobject.addProperty("last_weight_in_mamta",pwMonitorEntity.getLastWeightInMamta());
        pwobject.addProperty("last_weight_check_date",pwMonitorEntity.getLastWeightCheckDate().toString());
        pwobject.addProperty("current_weight",pwMonitorEntity.getCurrentWeight());
        pwobject.addProperty("pmmvy_installment",pwMonitorEntity.getPmmvyInstallment());
        pwobject.addProperty("igmpy_installment",pwMonitorEntity.getIgmpyInstallment());
        pwobject.addProperty("jsy_installment",pwMonitorEntity.getJsyInstallment());
        pwobject.addProperty("rajshri_installment",pwMonitorEntity.getRajshriInstallment());
        pwobject.addProperty("created_by",pwMonitorEntity.getCreatedBy());



        return pwobject;
    }

    private JsonObject getPregnantJson(PregnantEntity pregnantEntity) {
        JsonObject pregnantobject=new JsonObject();

        pregnantobject.addProperty("lmp_date",pregnantEntity.getLmpDate().toString());
        pregnantobject.addProperty("beneficiary_id",pregnantEntity.getBeneficiaryId());

        return pregnantobject;
    }

    private JsonObject getBeneficiaryJson(BeneficiaryEntity beneficiaryEntity) throws JSONException {
        JsonObject beneficiaryobject = new JsonObject();

        beneficiaryobject.addProperty("awc_code", beneficiaryEntity.getAwcCode());
        beneficiaryobject.addProperty("name", beneficiaryEntity.getName());
        beneficiaryobject.addProperty("age", beneficiaryEntity.getAge());
        beneficiaryobject.addProperty("dob", beneficiaryEntity.getDob().toString());
        beneficiaryobject.addProperty("stage", beneficiaryEntity.getStage());
        beneficiaryobject.addProperty("sub_stage", beneficiaryEntity.getSubStage());
        beneficiaryobject.addProperty("no_of_child", beneficiaryEntity.getChildCount());
        beneficiaryobject.addProperty("pmmvy_installment", beneficiaryEntity.getPmmvyInstallment());
        beneficiaryobject.addProperty("igmpy_installment", beneficiaryEntity.getIgmpyInstallment());
        beneficiaryobject.addProperty("jsy_installment", beneficiaryEntity.getJsyInstallment());
        beneficiaryobject.addProperty("rajshri_installment", beneficiaryEntity.getRajshriInstallment());
        beneficiaryobject.addProperty("husband_father_name", beneficiaryEntity.getHusbandName());
        beneficiaryobject.addProperty("mobile_no", beneficiaryEntity.getMobileNo());
        beneficiaryobject.addProperty("husband_mobile_no",beneficiaryEntity.getHusbandMobNo());
        beneficiaryobject.addProperty("caste", beneficiaryEntity.getCaste());
        beneficiaryobject.addProperty("economic_status", beneficiaryEntity.getEconomic());
        beneficiaryobject.addProperty("pcts_id", beneficiaryEntity.getPctsId());
        beneficiaryobject.addProperty("bahamashah_or_ack_id", beneficiaryEntity.getBahamashahId());
        beneficiaryobject.addProperty("is_counseling_prov", beneficiaryEntity.getCounselingProv());
        beneficiaryobject.addProperty("counseling_sms", beneficiaryEntity.getCounselingSms());
        beneficiaryobject.addProperty("created_by",beneficiaryEntity.getCreatedBy());
        return beneficiaryobject;
    }


}
