package in.rajpusht.pc.data;

import javax.inject.Inject;

import in.rajpusht.pc.data.local.AppDatabase;

public class DataRepository {

    @Inject
    public DataRepository(AppDatabase appDatabase) {
    }
}
