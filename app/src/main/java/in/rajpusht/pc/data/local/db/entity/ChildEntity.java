package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

import in.rajpusht.pc.model.DataStatus;


@Entity(tableName = ChildEntity.TABLE,indices = { @Index(value="childId"), @Index(value="motherId") })
public class ChildEntity {
    public static final String TABLE = "child";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long childId;
    private String childSex;
    private String stage;
    private String subStage;
    private Integer childOrder;
    private Date dob;
    private long motherId;
    private String deliveryPlace;
    private Integer deliveryHome;
    private String isActive;
    private String createdAt;
    private String updatedAt;
    private DataStatus dataStatus;
    private Integer age;
    private String isFirstImmunizationComplete;
    private Double birthWeight;
    private String pctsChildId;
    private Integer birthWeightSource;
    private Long opdipd;
    @Ignore
    private List<LMMonitorEntity> lmMonitorEntities;

    public ChildEntity() {
    }

    public long getChildId() {
        return childId;
    }

    public void setChildId(long childId) {
        this.childId = childId;
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

    public Integer getChildOrder() {
        return childOrder;
    }

    public void setChildOrder(Integer childOrder) {
        this.childOrder = childOrder;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public long getMotherId() {
        return motherId;
    }

    public void setMotherId(long motherId) {
        this.motherId = motherId;
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DataStatus getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(DataStatus dataStatus) {
        this.dataStatus = dataStatus;
    }

    public List<LMMonitorEntity> getLmMonitorEntities() {
        return lmMonitorEntities;
    }

    public void setLmMonitorEntities(List<LMMonitorEntity> lmMonitorEntities) {
        this.lmMonitorEntities = lmMonitorEntities;
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

    public Integer getDeliveryHome() {
        return deliveryHome;
    }

    public void setDeliveryHome(Integer deliveryHome) {
        this.deliveryHome = deliveryHome;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsFirstImmunizationComplete() {
        return isFirstImmunizationComplete;
    }

    public void setIsFirstImmunizationComplete(String isFirstImmunizationComplete) {
        this.isFirstImmunizationComplete = isFirstImmunizationComplete;
    }

    public Double getBirthWeight() {
        return birthWeight;
    }

    public void setBirthWeight(Double birthWeight) {
        this.birthWeight = birthWeight;
    }

    public String getPctsChildId() {
        return pctsChildId;
    }

    public void setPctsChildId(String pctsChildId) {
        this.pctsChildId = pctsChildId;
    }

    public Integer getBirthWeightSource() {
        return birthWeightSource;
    }

    public void setBirthWeightSource(Integer birthWeightSource) {
        this.birthWeightSource = birthWeightSource;
    }

    public Long getOpdipd() {
        return opdipd;
    }

    public void setOpdipd(Long opdipd) {
        this.opdipd = opdipd;
    }

    public String getChildSex() {
        return childSex;
    }

    public void setChildSex(String childSex) {
        this.childSex = childSex;
    }
}