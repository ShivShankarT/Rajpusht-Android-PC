package in.rajpusht.pc.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import in.rajpusht.pc.R;

public class CounsellingMedia implements Parcelable {
    public static final Creator<CounsellingMedia> CREATOR = new Creator<CounsellingMedia>() {
        @Override
        public CounsellingMedia createFromParcel(Parcel in) {
            return new CounsellingMedia(in);
        }

        @Override
        public CounsellingMedia[] newArray(int size) {
            return new CounsellingMedia[size];
        }
    };
    public static final int IMAGE_MEDIA = 1;
    public static final int GRAPH_MEDIA = 2;
    public static final int VIDEO_MEDIA = 3;


    private int id;
    private List<String> mediaImage;
    private int type = IMAGE_MEDIA;//single , multiple, graph


    public CounsellingMedia(int id, List<String> mediaImage, int type) {
        this.id = id;
        this.mediaImage = mediaImage;
        this.type = type;
    }

    public CounsellingMedia(int id, List<String> mediaImage) {
        this.id = id;
        this.mediaImage = mediaImage;
    }

    public CounsellingMedia(int id, int type) {
        this.id = id;
        this.mediaImage = Collections.emptyList();
        this.type = type;
    }

    protected CounsellingMedia(Parcel in) {
        id = in.readInt();
        mediaImage = in.createStringArrayList();
        type = in.readInt();
    }


    public static List<CounsellingMedia> counsellingMediaData() {

        List<CounsellingMedia> counsellingMedia = new ArrayList<>();

        counsellingMedia.add(new CounsellingMedia(1, Collections.singletonList("file:///android_asset/counseling/wg/0.1.webp")));
        counsellingMedia.add(new CounsellingMedia(2, Collections.singletonList("file:///android_asset/counseling/wg/5.webp")));
        counsellingMedia.add(new CounsellingMedia(3, CounsellingMedia.GRAPH_MEDIA));
        counsellingMedia.add(new CounsellingMedia(1, Collections.singletonList("file:///android_asset/counseling/wg/2.webp")));
        counsellingMedia.add(new CounsellingMedia(1, Collections.singletonList("file:///android_asset/counseling/wg/1.webp")));
        counsellingMedia.add(new CounsellingMedia(1, Collections.singletonList("file:///android_asset/counseling/wg/3.webp")));
        counsellingMedia.add(new CounsellingMedia(13, Collections.singletonList(R.raw.film + ""), VIDEO_MEDIA));
        counsellingMedia.add(new CounsellingMedia(6, Collections.singletonList("file:///android_asset/counseling/wg/6.webp")));
        counsellingMedia.add(new CounsellingMedia(7, Collections.singletonList("file:///android_asset/counseling/wg/7.webp")));
        counsellingMedia.add(new CounsellingMedia(8,
                Arrays.asList(
                        "file:///android_asset/counseling/an/1.webp",
                        "file:///android_asset/counseling/an/2.webp",
                        "file:///android_asset/counseling/an/3.webp",
                        "file:///android_asset/counseling/an/4.webp",
                        "file:///android_asset/counseling/an/5.webp",
                        "file:///android_asset/counseling/an/6.webp"
                )));

        counsellingMedia.add(new CounsellingMedia(9, Collections.singletonList("file:///android_asset/counseling/wg/8.webp")));
        counsellingMedia.add(new CounsellingMedia(10, Collections.singletonList("file:///android_asset/counseling/wg/9.webp")));
        counsellingMedia.add(new CounsellingMedia(11, Collections.singletonList("file:///android_asset/counseling/wg/10.webp")));
        counsellingMedia.add(new CounsellingMedia(12, Collections.singletonList("file:///android_asset/counseling/wg/4.webp")));
        counsellingMedia.add(new CounsellingMedia(13, Collections.singletonList("file:///android_asset/counseling/ifa/1.webp")));
        counsellingMedia.add(new CounsellingMedia(14, Collections.singletonList("file:///android_asset/counseling/ifa/6.webp")));
        counsellingMedia.add(new CounsellingMedia(15, Collections.singletonList("file:///android_asset/counseling/ifa/7.webp")));
        counsellingMedia.add(new CounsellingMedia(16, Collections.singletonList("file:///android_asset/counseling/ifa/2.webp")));
        counsellingMedia.add(new CounsellingMedia(17, Collections.singletonList("file:///android_asset/counseling/ifa/5.webp")));
        counsellingMedia.add(new CounsellingMedia(18, Collections.singletonList("file:///android_asset/counseling/ifa/3.webp")));
        counsellingMedia.add(new CounsellingMedia(19, Collections.singletonList("file:///android_asset/counseling/ifa/4.webp")));
        counsellingMedia.add(new CounsellingMedia(20, Collections.singletonList("file:///android_asset/counseling/ifa/16.webp")));
        counsellingMedia.add(new CounsellingMedia(21, Collections.singletonList("file:///android_asset/counseling/ifa/12.webp")));//1
        counsellingMedia.add(new CounsellingMedia(22, Collections.singletonList("file:///android_asset/counseling/ifa/14.webp")));
        counsellingMedia.add(new CounsellingMedia(23, Collections.singletonList("file:///android_asset/counseling/ifa/13.webp")));
        counsellingMedia.add(new CounsellingMedia(24, Collections.singletonList("file:///android_asset/counseling/ifa/8.webp")));
        counsellingMedia.add(new CounsellingMedia(25, Collections.singletonList("file:///android_asset/counseling/ifa/11.webp")));
        counsellingMedia.add(new CounsellingMedia(18, Collections.singletonList("file:///android_asset/counseling/ifa/3.webp")));
        counsellingMedia.add(new CounsellingMedia(27, Collections.singletonList("file:///android_asset/counseling/ifa/10.webp")));
        counsellingMedia.add(new CounsellingMedia(28, Collections.singletonList("file:///android_asset/counseling/ifa/15.webp")));//




/*
// anain
        counsellingMedia.add(new CounsellingMedia(1, Collections.singletonList("file:///android_asset/pw/1.webp")));
        counsellingMedia.add(new CounsellingMedia(2, Collections.singletonList("file:///android_asset/pw/2.webp")));
        counsellingMedia.add(new CounsellingMedia(3, CounsellingMedia.GRAPH_MEDIA));

        counsellingMedia.add(new CounsellingMedia(3, Collections.singletonList("file:///android_asset/pw/3.webp")));
        counsellingMedia.add(new CounsellingMedia(4, Collections.singletonList("file:///android_asset/pw/4.webp")));
        counsellingMedia.add(new CounsellingMedia(5, Collections.singletonList("file:///android_asset/pw/5.webp")));

        counsellingMedia.add(new CounsellingMedia(13, Collections.singletonList(R.raw.dinosaur + ""), VIDEO_MEDIA));

        counsellingMedia.add(new CounsellingMedia(6, Collections.singletonList("file:///android_asset/pw/6.webp")));
        counsellingMedia.add(new CounsellingMedia(7, Collections.singletonList("file:///android_asset/pw/7.webp")));

        counsellingMedia.add(new CounsellingMedia(8,
                Arrays.asList(
                        "file:///android_asset/pw/8.1.webp",
                        "file:///android_asset/pw/8.2.webp",
                        "file:///android_asset/pw/8.3.webp",
                        "file:///android_asset/pw/8.4.webp",
                        "file:///android_asset/pw/8.5.webp",
                        "file:///android_asset/pw/8.6.webp"
                )));
        //  counsellingMedia.add(new CounsellingMedia(8, Collections.singletonList("file:///android_asset/pw/8.webp")));
        counsellingMedia.add(new CounsellingMedia(9, Collections.singletonList("file:///android_asset/pw/9.webp")));
        counsellingMedia.add(new CounsellingMedia(10, Collections.singletonList("file:///android_asset/pw/10.webp")));
        counsellingMedia.add(new CounsellingMedia(11, Collections.singletonList("file:///android_asset/pw/11.webp")));
        counsellingMedia.add(new CounsellingMedia(12, Collections.singletonList("file:///android_asset/pw/12.webp")));
        counsellingMedia.add(new CounsellingMedia(13, Collections.singletonList("file:///android_asset/pw/13.webp")));
        counsellingMedia.add(new CounsellingMedia(14, Collections.singletonList("file:///android_asset/pw/14.webp")));
        counsellingMedia.add(new CounsellingMedia(15, Collections.singletonList("file:///android_asset/pw/15.webp")));
        counsellingMedia.add(new CounsellingMedia(16, Collections.singletonList("file:///android_asset/pw/16.webp")));
        counsellingMedia.add(new CounsellingMedia(17, Collections.singletonList("file:///android_asset/pw/17.webp")));
        counsellingMedia.add(new CounsellingMedia(18, Collections.singletonList("file:///android_asset/pw/18.webp")));
        counsellingMedia.add(new CounsellingMedia(19, Collections.singletonList("file:///android_asset/pw/19.webp")));
        counsellingMedia.add(new CounsellingMedia(20, Collections.singletonList("file:///android_asset/pw/20.webp")));
        counsellingMedia.add(new CounsellingMedia(21, Collections.singletonList("file:///android_asset/pw/21.webp")));
        counsellingMedia.add(new CounsellingMedia(22, Collections.singletonList("file:///android_asset/pw/22.webp")));
        counsellingMedia.add(new CounsellingMedia(23, Collections.singletonList("file:///android_asset/pw/23.webp")));
        counsellingMedia.add(new CounsellingMedia(24, Collections.singletonList("file:///android_asset/pw/24.webp")));
        counsellingMedia.add(new CounsellingMedia(25, Collections.singletonList("file:///android_asset/pw/25.webp")));
        counsellingMedia.add(new CounsellingMedia(26, Collections.singletonList("file:///android_asset/pw/26.webp")));
        counsellingMedia.add(new CounsellingMedia(27, Collections.singletonList("file:///android_asset/pw/27.webp")));
        counsellingMedia.add(new CounsellingMedia(28, Collections.singletonList("file:///android_asset/pw/28.webp")));
*/


        return counsellingMedia;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeStringList(mediaImage);
        dest.writeInt(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public List<String> getMediaImage() {
        return mediaImage;
    }

    public void setMediaImage(List<String> mediaImage) {
        this.mediaImage = mediaImage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

