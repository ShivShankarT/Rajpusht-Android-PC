package in.rajpusht.pc.data.local.db.entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = AssignedLocationEntity.TABLE_NAME)
public class AssignedLocationEntity {
    public static final String TABLE_NAME = "assigned_location";

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String distCode;
    private String projectCode;
    private String sectorCode;
    private String villageCode;
    private String villageEng;
    private String villageHindi;
    private String awcCode;
    private String surveyorName;
    private String surveyorId;
    private String sectorName;
    private String projectName;
    private String awcHindi;
    private String awcEng;
    private String login;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDistCode() {
        return distCode;
    }

    public void setDistCode(String distCode) {
        this.distCode = distCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

    public String getVillageEng() {
        return villageEng;
    }

    public void setVillageEng(String villageEng) {
        this.villageEng = villageEng;
    }

    public String getVillageHindi() {
        return villageHindi;
    }

    public void setVillageHindi(String villageHindi) {
        this.villageHindi = villageHindi;
    }

    public String getAwcCode() {
        return awcCode;
    }

    public void setAwcCode(String awcCode) {
        this.awcCode = awcCode;
    }

    public String getSurveyorName() {
        return surveyorName;
    }

    public void setSurveyorName(String surveyorName) {
        this.surveyorName = surveyorName;
    }

    public String getSurveyorId() {
        return surveyorId;
    }

    public void setSurveyorId(String surveyorId) {
        this.surveyorId = surveyorId;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAwcHindi() {
        return awcHindi;
    }

    public void setAwcHindi(String awcHindi) {
        this.awcHindi = awcHindi;
    }

    public String getAwcEng() {
        return awcEng;
    }

    public void setAwcEng(String awcEng) {
        this.awcEng = awcEng;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
