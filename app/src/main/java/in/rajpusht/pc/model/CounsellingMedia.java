package in.rajpusht.pc.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
    public static String counsellingSubstage = "PW1";
    public static long counsellingPregId = 0;
    public static Date counsellingPregLmp = null;
    public static boolean isTesting = false;
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

    public static List<CounsellingMedia> counsellingMediaData(String subStage) {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();

        //Section suppose to be included in various stages:
        //
        //PW1 - PMVY
        //PW2 - Weight Gain/IFA/PMVY
        //PM3 - Weight Gain/IFA/PMVY
        //PW4 - Weight Gain/IFA/Birth Preparedness/PMVY
        //LM1 - IFA/Exclusive Breastfeeding/Immunization/Diet Diversity/PMVY
        //LM2 - IFA/Exclusive Breastfeeding/Immunization/Diet Diversity/PMVY
        //MY1 - Diet Diversity
        //MY2 - Diet Diversity
        //MY3 - Diet Diversity

        if (subStage.equals("PW1")) {
            counsellingMedia.addAll(pmmvy());

        } else if (subStage.equals("PW2") || subStage.equals("PW3")) {
            counsellingMedia.addAll(wg());
            counsellingMedia.addAll(ifa());
            counsellingMedia.addAll(pmmvy());
        } else if (subStage.equals("PW4")) {
            counsellingMedia.addAll(wg());
            counsellingMedia.addAll(ifa());
            counsellingMedia.addAll(prefBirth());
            counsellingMedia.addAll(pmmvy());
        } else if (subStage.contains("LM")) {
            counsellingMedia.addAll(ifa());
            counsellingMedia.addAll(ebf());
            counsellingMedia.addAll(immutation());
            counsellingMedia.addAll(dietDi());
            counsellingMedia.addAll(pmmvy());
        } else if (subStage.contains("MY")) {
            counsellingMedia.addAll(dietDi());
        }


        return counsellingMedia;
    }


    public static List<CounsellingMedia> immutation() {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();
        counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/exclusive breast feeding/Gfx07_t.webp", "file:///android_asset/img/exclusive breast feeding/Gfx07.webp")));

        return counsellingMedia;
    }


    public static List<CounsellingMedia> dietDi() {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();
        counsellingMedia.add(new CounsellingMedia(29, Arrays.asList("file:///android_asset/img/exclusive breast feeding/Gfx08_t.webp", "file:///android_asset/img/exclusive breast feeding/Gfx08.webp")));
        counsellingMedia.add(new CounsellingMedia(25, Arrays.asList("file:///android_asset/img/Mother Of 4-6 month children/Gfx10_t.webp", "file:///android_asset/img/Mother Of 4-6 month children/Gfx10.webp")));

        return counsellingMedia;
    }


    public static List<CounsellingMedia> pmmvy() {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();
        counsellingMedia.add(new CounsellingMedia(9, Arrays.asList("file:///android_asset/img/General message/Gfx09_t.webp", "file:///android_asset/img/General message/Gfx09.webp")));
        counsellingMedia.add(new CounsellingMedia(13, Arrays.asList("file:///android_asset/img/General message/Gfx10c_t.webp", "file:///android_asset/img/General message/Gfx10c.webp")));
        counsellingMedia.add(new CounsellingMedia(14, Arrays.asList("file:///android_asset/img/General message/Gfx10d_t.webp", "file:///android_asset/img/General message/Gfx10d.webp")));
        counsellingMedia.add(new CounsellingMedia(10, Arrays.asList("file:///android_asset/img/General message/Gfx10_t.webp", "file:///android_asset/img/General message/Gfx10.webp")));
        counsellingMedia.add(new CounsellingMedia(11, Arrays.asList("file:///android_asset/img/General message/Gfx10a_t.webp", "file:///android_asset/img/General message/Gfx10a.webp")));
        counsellingMedia.add(new CounsellingMedia(12, Arrays.asList("file:///android_asset/img/General message/Gfx10b_t.webp", "file:///android_asset/img/General message/Gfx10b.webp")));

        return counsellingMedia;
    }


    public static List<CounsellingMedia> wg() {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();

        counsellingMedia.add(new CounsellingMedia(1, Arrays.asList(
                "file:///android_asset/counseling/wg/t/4.webp",
                "file:///android_asset/counseling/wg/0.1.webp")));
        counsellingMedia.add(new CounsellingMedia(2, Arrays.asList(
                "file:///android_asset/counseling/wg/t/4.webp",
                "file:///android_asset/counseling/wg/5.webp")));

        counsellingMedia.add(new CounsellingMedia(3, CounsellingMedia.GRAPH_MEDIA));
        counsellingMedia.add(new CounsellingMedia(1, Arrays.asList(
                "file:///android_asset/counseling/wg/t/1.webp",
                "file:///android_asset/counseling/wg/2.webp")));
        counsellingMedia.add(new CounsellingMedia(1, Arrays.asList(
                "file:///android_asset/counseling/wg/t/1.webp",
                "file:///android_asset/counseling/wg/1.webp")));
        counsellingMedia.add(new CounsellingMedia(1, Arrays.asList(
                "file:///android_asset/counseling/wg/t/2.webp",
                "file:///android_asset/counseling/wg/3.webp")));
        counsellingMedia.add(new CounsellingMedia(13, Arrays.asList(R.raw.film + ""), VIDEO_MEDIA));
        counsellingMedia.add(new CounsellingMedia(6, Arrays.asList(
                "file:///android_asset/counseling/wg/t/5.webp",
                "file:///android_asset/counseling/wg/6.webp")));
        counsellingMedia.add(new CounsellingMedia(7, Arrays.asList(
                "file:///android_asset/counseling/wg/t/5.webp",
                "file:///android_asset/counseling/wg/7.webp")));
        counsellingMedia.add(new CounsellingMedia(8,
                Arrays.asList(
                        "file:///android_asset/counseling/an/1.webp",
                        "file:///android_asset/counseling/an/2.webp",
                        "file:///android_asset/counseling/an/3.webp",
                        "file:///android_asset/counseling/an/4.webp",
                        "file:///android_asset/counseling/an/5.webp",
                        "file:///android_asset/counseling/an/6.webp"
                )));

        counsellingMedia.add(new CounsellingMedia(9, Arrays.asList(
                "file:///android_asset/counseling/wg/t/5.webp",
                "file:///android_asset/counseling/wg/8.webp")));
        counsellingMedia.add(new CounsellingMedia(10, Arrays.asList(
                "file:///android_asset/counseling/wg/t/5.webp",
                "file:///android_asset/counseling/wg/9.webp")));
        counsellingMedia.add(new CounsellingMedia(11, Arrays.asList(
                "file:///android_asset/counseling/wg/t/5.webp",
                "file:///android_asset/counseling/wg/10.webp")));
        counsellingMedia.add(new CounsellingMedia(12, Arrays.asList(
                "file:///android_asset/counseling/wg/t/3.webp",
                "file:///android_asset/counseling/wg/4.webp")));

        return counsellingMedia;
    }

    public static List<CounsellingMedia> ifa() {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();
        counsellingMedia.add(new CounsellingMedia(13, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/6.webp",
                "file:///android_asset/counseling/ifa/1.webp")));
        counsellingMedia.add(new CounsellingMedia(14, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/7.webp",
                "file:///android_asset/counseling/ifa/6.webp")));
        counsellingMedia.add(new CounsellingMedia(15, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/7.webp",
                "file:///android_asset/counseling/ifa/7.webp")));
        counsellingMedia.add(new CounsellingMedia(16, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/7.webp",
                "file:///android_asset/counseling/ifa/2.webp")));
        counsellingMedia.add(new CounsellingMedia(17, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/7.webp",
                "file:///android_asset/counseling/ifa/5.webp")));
        counsellingMedia.add(new CounsellingMedia(18, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/7.webp",
                "file:///android_asset/counseling/ifa/3.webp")));
        counsellingMedia.add(new CounsellingMedia(19, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/7.webp",
                "file:///android_asset/counseling/ifa/4.webp")));
        counsellingMedia.add(new CounsellingMedia(20, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/12.webp",
                "file:///android_asset/counseling/ifa/16.webp")));
        counsellingMedia.add(new CounsellingMedia(21, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/9.webp",
                "file:///android_asset/counseling/ifa/12.webp")));//1
        counsellingMedia.add(new CounsellingMedia(22, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/9.webp",
                "file:///android_asset/counseling/ifa/14.webp")));
        counsellingMedia.add(new CounsellingMedia(23, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/9.webp",
                "file:///android_asset/counseling/ifa/13.webp")));
        counsellingMedia.add(new CounsellingMedia(24, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/9.webp",
                "file:///android_asset/counseling/ifa/8.webp")));
        counsellingMedia.add(new CounsellingMedia(25, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/9.webp",
                "file:///android_asset/counseling/ifa/11.webp")));
        counsellingMedia.add(new CounsellingMedia(18, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/7.webp",
                "file:///android_asset/counseling/ifa/3.webp")));
        counsellingMedia.add(new CounsellingMedia(27, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/9.webp",
                "file:///android_asset/counseling/ifa/10.webp")));
        counsellingMedia.add(new CounsellingMedia(28, Arrays.asList(
                "file:///android_asset/counseling/ifa/t/11.webp",
                "file:///android_asset/counseling/ifa/15.webp")));//


        return counsellingMedia;
    }

    public static List<CounsellingMedia> mchildren() {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();
        counsellingMedia.add(new CounsellingMedia(24, Arrays.asList("file:///android_asset/img/Mother Of 4-6 month children/Gfx09_t.webp", "file:///android_asset/img/Mother Of 4-6 month children/Gfx09.webp")));
        counsellingMedia.add(new CounsellingMedia(25, Arrays.asList("file:///android_asset/img/Mother Of 4-6 month children/Gfx10_t.webp", "file:///android_asset/img/Mother Of 4-6 month children/Gfx10.webp")));

        return counsellingMedia;
    }

    public static List<CounsellingMedia> ebf() {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();
        counsellingMedia.add(new CounsellingMedia(27, Arrays.asList("file:///android_asset/img/exclusive breast feeding/Gfx06_t.webp", "file:///android_asset/img/exclusive breast feeding/Gfx06.webp")));
        counsellingMedia.add(new CounsellingMedia(26, Arrays.asList("file:///android_asset/img/exclusive breast feeding/Gfx05_t.webp", "file:///android_asset/img/exclusive breast feeding/Gfx05.webp")));
/*        counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/exclusive breast feeding/Gfx07_t.webp", "file:///android_asset/img/exclusive breast feeding/Gfx07.webp")));
        counsellingMedia.add(new CounsellingMedia(29, Arrays.asList("file:///android_asset/img/exclusive breast feeding/Gfx08_t.webp", "file:///android_asset/img/exclusive breast feeding/Gfx08.webp")))*/
        ;
        return counsellingMedia;
    }

    public static List<CounsellingMedia> prefBirth() {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();

        counsellingMedia.add(new CounsellingMedia(43, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx01_t.webp", "file:///android_asset/img/preparing for birth/Gfx01.webp")));
        counsellingMedia.add(new CounsellingMedia(44, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx02_t.webp", "file:///android_asset/img/preparing for birth/Gfx02.webp")));
        counsellingMedia.add(new CounsellingMedia(46, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx04_t.webp", "file:///android_asset/img/preparing for birth/Gfx04.webp")));
        counsellingMedia.add(new CounsellingMedia(45, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx03_t.webp", "file:///android_asset/img/preparing for birth/gfx03_low.webp")));
        counsellingMedia.add(new CounsellingMedia(26, Arrays.asList("file:///android_asset/img/exclusive breast feeding/Gfx05_t.webp", "file:///android_asset/img/exclusive breast feeding/Gfx05.webp")));



        return counsellingMedia;
    }


    public static List<CounsellingMedia> counsellingMediaData() {

        return counsellingMediaData(counsellingSubstage);
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

    @Override
    public String toString() {
        return "CounsellingMedia{" +
                "id=" + id +
                ", mediaImage=" + mediaImage +
                ", type=" + type +
                '}';
    }
}

