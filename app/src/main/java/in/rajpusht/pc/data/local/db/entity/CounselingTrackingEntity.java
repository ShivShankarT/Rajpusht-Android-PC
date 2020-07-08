package in.rajpusht.pc.data.local.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = CounselingTrackingEntity.TABLE_NAME, indices = @Index(value = {"formId"}))
public class CounselingTrackingEntity {
    public static final String TABLE_NAME = "counseling_tracking";

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long formId;
    private boolean isPwType;
    private Date startTime;
    private Date lastKnowUpdateTime;
    private boolean isCompleted;
    private String uuid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    public boolean isPwType() {
        return isPwType;
    }

    public void setPwType(boolean pwType) {
        isPwType = pwType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public Date getLastKnowUpdateTime() {
        return lastKnowUpdateTime;
    }

    public void setLastKnowUpdateTime(Date lastKnowUpdateTime) {
        this.lastKnowUpdateTime = lastKnowUpdateTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
