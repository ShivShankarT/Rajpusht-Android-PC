package in.rajpusht.pc.model;

public enum DataStatus {

    OLD(0), NEW(1), EDIT(2);
    public final Integer value;

    DataStatus(Integer value) {
        this.value = value;
    }
}
