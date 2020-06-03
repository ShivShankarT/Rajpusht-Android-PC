package in.rajpusht.pc.model;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import in.rajpusht.pc.data.DataRepository;
import in.rajpusht.pc.data.remote.ApiService;
import in.rajpusht.pc.data.remote.AppApiHelper;
import io.reactivex.Single;
import retrofit2.Call;

public class BulkDownload {
    @Inject
    DataRepository dataRepository;
    Single<ApiResponse<JsonObject>> jsonobject;
    JsonObject data;

    public BulkDownload(){
     jsonobject=dataRepository.bulkdownload();


    }


}
