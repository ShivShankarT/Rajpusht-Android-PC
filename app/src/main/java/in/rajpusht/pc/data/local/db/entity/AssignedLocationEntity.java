package in.rajpusht.pc.data.local.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = AssignedLocationEntity.TABLE_NAME , indices = { @Index(value="awcCode"), @Index(value="sectorCode"),})
public class AssignedLocationEntity {
    public static final String TABLE_NAME = "assigned_location";

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    @SerializedName("projectCode")
    @Expose
    private String projectCode;
    @SerializedName("projectEnglishName")
    @Expose
    private String projectEnglishName;
    @SerializedName("projectHindiName")
    @Expose
    private String projectHindiName;
    @SerializedName("districtCode")
    @Expose
    private String districtCode;
    @SerializedName("districtEnglishName")
    @Expose
    private String districtEnglishName;
    @SerializedName("districtHindiName")
    @Expose
    private String districtHindiName;
    @SerializedName("awcCode")
    @Expose
    private String awcCode;
    @SerializedName("awcEnglishName")
    @Expose
    private String awcEnglishName;
    @SerializedName("awcHindiName")
    @Expose
    private String awcHindiName;
    @SerializedName("sectorCode")
    @Expose
    private String sectorCode;
    @SerializedName("sectorName")
    @Expose
    private String sectorName;
    @SerializedName("sectorHindiName")
    @Expose
    private String sectorHindiName;

    @Ignore
    private Integer pwCount;
    @Ignore
    private Integer lmCount;
    @Ignore
    private Integer myCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectEnglishName() {
        return projectEnglishName;
    }

    public void setProjectEnglishName(String projectEnglishName) {
        this.projectEnglishName = projectEnglishName;
    }

    public String getProjectHindiName() {
        return projectHindiName;
    }

    public void setProjectHindiName(String projectHindiName) {
        this.projectHindiName = projectHindiName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictEnglishName() {
        return districtEnglishName;
    }

    public void setDistrictEnglishName(String districtEnglishName) {
        this.districtEnglishName = districtEnglishName;
    }

    public String getDistrictHindiName() {
        return districtHindiName;
    }

    public void setDistrictHindiName(String districtHindiName) {
        this.districtHindiName = districtHindiName;
    }

    public String getAwcCode() {
        return awcCode;
    }

    public void setAwcCode(String awcCode) {
        this.awcCode = awcCode;
    }

    public String getAwcEnglishName() {
        return awcEnglishName;
    }

    public void setAwcEnglishName(String awcEnglishName) {
        this.awcEnglishName = awcEnglishName;
    }

    public String getAwcHindiName() {
        return awcHindiName;
    }

    public void setAwcHindiName(String awcHindiName) {
        this.awcHindiName = awcHindiName;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getSectorHindiName() {
        return sectorHindiName;
    }

    public void setSectorHindiName(String sectorHindiName) {
        this.sectorHindiName = sectorHindiName;
    }

    public Integer getPwCount() {
        return pwCount;
    }

    public void setPwCount(Integer pwCount) {
        this.pwCount = pwCount;
    }

    public Integer getLmCount() {
        return lmCount;
    }

    public void setLmCount(Integer lmCount) {
        this.lmCount = lmCount;
    }

    public Integer getMyCount() {
        return myCount;
    }

    public void setMyCount(Integer myCount) {
        this.myCount = myCount;
    }
}
