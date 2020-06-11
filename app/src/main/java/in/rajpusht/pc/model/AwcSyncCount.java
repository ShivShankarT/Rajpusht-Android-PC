package in.rajpusht.pc.model;

public class AwcSyncCount {

    private String awcCode;
    private String awcEnglishName;
    private String isMother;
    private Integer count;
    private DataStatus dataStatus;


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

    public String getIsMother() {
        return isMother;
    }

    public void setIsMother(String isMother) {
        this.isMother = isMother;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public DataStatus getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(DataStatus dataStatus) {
        this.dataStatus = dataStatus;
    }
}
