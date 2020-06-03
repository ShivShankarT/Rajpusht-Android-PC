package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

import in.rajpusht.pc.model.DataStatus;


@Entity(tableName = ChildEntity.TABLE)
public class ChildEntity {
    public static final String TABLE = "child";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long childId;
    private String stage;
    private String subStage;
    private int childOrder;
    private Date dob;
    private long motherId;
    private String deliveryPlace;
    private int deliveryHome;
    private String createdAt;
    private String updatedAt;
    private DataStatus dataStatus;
    private boolean isNew;
    private int age;


    @Ignore
    private List<LMMonitorEntity> lmMonitorEntities;


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

    public int getChildOrder() {
        return childOrder;
    }

    public void setChildOrder(int childOrder) {
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

    public int getDeliveryHome() {
        return deliveryHome;
    }

    public void setDeliveryHome(int deliveryHome) {
        this.deliveryHome = deliveryHome;
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

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}