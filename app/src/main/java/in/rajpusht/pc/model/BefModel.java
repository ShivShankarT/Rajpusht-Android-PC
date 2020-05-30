package in.rajpusht.pc.model;

import java.util.Date;
import java.util.List;

public class BefModel {
    private Long beneficiaryId;
    private String name;
    private Date dob;
    private String stage;
    private String subStage;
    private String motherId;
    private String husbandName;
    private String motherName;
    private long pregnancyId;
    private String pctsId;
    private Date lmpDate;
    private String currentSubStage;
    private List<String> collectedDataSubStage;

    public Long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }


    public long getPregnancyId() {
        return pregnancyId;
    }

    public void setPregnancyId(long pregnancyId) {
        this.pregnancyId = pregnancyId;
    }

    public String getCurrentSubStage() {
        return currentSubStage;
    }

    public void setCurrentSubStage(String currentSubStage) {
        this.currentSubStage = currentSubStage;
    }

    public Date getLmpDate() {
        return lmpDate;
    }

    public void setLmpDate(Date lmpDate) {
        this.lmpDate = lmpDate;
    }

    public String getSubStage() {
        return subStage;
    }

    public void setSubStage(String subStage) {
        this.subStage = subStage;
    }

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    public String getPctsId() {
        return pctsId;
    }

    public void setPctsId(String pctsId) {
        this.pctsId = pctsId;
    }

    public List<String> getCollectedDataSubStage() {
        return collectedDataSubStage;
    }

    public void setCollectedDataSubStage(List<String> collectedDataSubStage) {
        this.collectedDataSubStage = collectedDataSubStage;
    }
}
