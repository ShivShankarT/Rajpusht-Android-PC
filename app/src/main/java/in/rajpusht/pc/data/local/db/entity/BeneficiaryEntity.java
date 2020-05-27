package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import in.rajpusht.pc.model.DataStatus;


@Entity(tableName = BeneficiaryEntity.TABLE)
public class BeneficiaryEntity {
    public static final String TABLE = "beneficiary";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long beneficiaryId;
    private String name;
    private String husbandName;
    private String mobileNo;
    private String husbandMobNo;
    private String stage;
    private String subStage;
    private Date DOB;
    private int childCount;
    private Integer age;
    private String caste;
    private String economic;
    private String pctsId;
    private String bahamashahId;
    private String counselingProv;
    private int counselingSms;
    private Integer pmmvyInstallmentCt;
    private Integer igmpyInstallmentCt;
    private Integer jsyInstallmentCt;
    private Integer rajshriInstallmentCt;
    private DataStatus dataStatus;
    @Ignore
    private String currentSubStage;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getHusbandMobNo() {
        return husbandMobNo;
    }

    public void setHusbandMobNo(String husbandMobNo) {
        this.husbandMobNo = husbandMobNo;
    }


    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getSubStage() {
        return subStage;
    }

    public void setSubStage(String subStage) {
        this.subStage = subStage;
    }

    public String getCurrentSubStage() {
        return currentSubStage;
    }

    public void setCurrentSubStage(String currentSubStage) {
        this.currentSubStage = currentSubStage;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date dob) {
        this.DOB = dob;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getEconomic() {
        return economic;
    }

    public void setEconomic(String economic) {
        this.economic = economic;
    }

    public String getPctsId() {
        return pctsId;
    }

    public void setPctsId(String pctsId) {
        this.pctsId = pctsId;
    }

    public String getBahamashahId() {
        return bahamashahId;
    }

    public void setBahamashahId(String bahamashahId) {
        this.bahamashahId = bahamashahId;
    }

    public String getCounselingProv() {
        return counselingProv;
    }

    public void setCounselingProv(String counselingProv) {
        this.counselingProv = counselingProv;
    }

    public int getCounselingSms() {
        return counselingSms;
    }

    public void setCounselingSms(int counselingSms) {
        this.counselingSms = counselingSms;
    }

    public long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public DataStatus getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(DataStatus dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Integer getPmmvyInstallmentCt() {
        return pmmvyInstallmentCt;
    }

    public void setPmmvyInstallmentCt(Integer pmmvyInstallmentCt) {
        this.pmmvyInstallmentCt = pmmvyInstallmentCt;
    }

    public Integer getIgmpyInstallmentCt() {
        return igmpyInstallmentCt;
    }

    public void setIgmpyInstallmentCt(Integer igmpyInstallmentCt) {
        this.igmpyInstallmentCt = igmpyInstallmentCt;
    }

    public Integer getJsyInstallmentCt() {
        return jsyInstallmentCt;
    }

    public void setJsyInstallmentCt(Integer jsyInstallmentCt) {
        this.jsyInstallmentCt = jsyInstallmentCt;
    }

    public Integer getRajshriInstallmentCt() {
        return rajshriInstallmentCt;
    }

    public void setRajshriInstallmentCt(Integer rajshriInstallmentCt) {
        this.rajshriInstallmentCt = rajshriInstallmentCt;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }
}
