package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

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
    private Integer pmmvyInstallment;
    private Integer igmpyInstallment;
    private Integer jsyInstallment;
    private Integer rajshriInstallment;
    private List<String> collectedDataSubStage;
    private DataStatus dataStatus;
    private String createdAt;
    private String updatedAt;


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

    public Integer getPmmvyInstallment() {
        return pmmvyInstallment;
    }

    public void setPmmvyInstallment(Integer pmmvyInstallment) {
        this.pmmvyInstallment = pmmvyInstallment;
    }

    public Integer getIgmpyInstallment() {
        return igmpyInstallment;
    }

    public void setIgmpyInstallment(Integer igmpyInstallment) {
        this.igmpyInstallment = igmpyInstallment;
    }

    public Integer getJsyInstallment() {
        return jsyInstallment;
    }

    public void setJsyInstallment(Integer jsyInstallment) {
        this.jsyInstallment = jsyInstallment;
    }

    public Integer getRajshriInstallment() {
        return rajshriInstallment;
    }

    public void setRajshriInstallment(Integer rajshriInstallment) {
        this.rajshriInstallment = rajshriInstallment;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getCollectedDataSubStage() {
        return collectedDataSubStage;
    }

    public void setCollectedDataSubStage(List<String> collectedDataSubStage) {
        this.collectedDataSubStage = collectedDataSubStage;
    }
}
