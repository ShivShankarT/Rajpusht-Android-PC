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
    public static final int GRAPH_HEIGHT_MEDIA = 4;
    public static final int VIDEO_MEDIA = 3;
    public static String counsellingSubstage = "PW1";
    public static long counsellingPregId = 0;
    public static Date counsellingPregLmp = null;
    public static long counsellingChildId = 0;
    public static long counsellingChildIAgeInDay = 0;
    public static long counsellingFormId = 0;
    public static boolean isPmmvyReg = false;


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

        if (subStage.equals("PW1")) {
            counsellingMedia.addAll(pmmvy(subStage));

        } else if (subStage.equals("PW2")) {
            counsellingMedia.addAll(wg());
            counsellingMedia.addAll(ifa());
            counsellingMedia.addAll(pmmvy(subStage));
        } else if (subStage.equals("PW3")) {
            counsellingMedia.addAll(wg());
            counsellingMedia.addAll(ifa());
            counsellingMedia.addAll(pmmvy(subStage));
            counsellingMedia.addAll(prefBirth(subStage));
        } else if (subStage.equals("PW4")) {
            counsellingMedia.addAll(wg());
            counsellingMedia.addAll(ifa());
            counsellingMedia.addAll(prefBirth(subStage));
            counsellingMedia.addAll(pmmvy(subStage));
        } else if (subStage.contains("LM")) {
            counsellingMedia.addAll(ifa());
            counsellingMedia.addAll(ebf());
            counsellingMedia.addAll(immutation(subStage));
            counsellingMedia.addAll(dietDi(subStage));
            counsellingMedia.addAll(pmmvy(subStage));
            counsellingMedia.addAll(childGrowth(subStage));
        } else if (subStage.equals("MY1")) {
            counsellingMedia.addAll(dietDi(subStage));
            counsellingMedia.addAll(immutation(subStage));
            counsellingMedia.addAll(childGrowth(subStage));
        } else if (subStage.equals("MY2")) {
            counsellingMedia.addAll(dietDi(subStage));
            counsellingMedia.addAll(childGrowth(subStage));
        } else if (subStage.equals("MY3")) {
            counsellingMedia.addAll(dietDi(subStage));
            counsellingMedia.addAll(childGrowth(subStage));
        } else if (subStage.equals("all")) {//testing

            counsellingMedia.addAll(wg());
            counsellingMedia.addAll(ifa());
            counsellingMedia.addAll(prefBirth(""));
            counsellingMedia.addAll(pmmvy("PW1"));
            counsellingMedia.addAll(ebf());
            counsellingMedia.addAll(immutation("LM1"));
            //----  immutation
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/immune/Gfx05_GFX 01_t1.webp",
                    "file:///android_asset/img/immune/Gfx05_GFX 01.webp")));
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/immune/Gfx06_GFX 01_t1.webp",
                    "file:///android_asset/img/immune/Gfx06_GFX 01.webp")));
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/immune/Gfx07_GFX 01_t1.webp",
                    "file:///android_asset/img/immune/Gfx07_GFX 01.webp")));
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/immune/Gfx08_GFX 01_t1.webp",
                    "file:///android_asset/img/immune/Gfx08_GFX 01.webp")));
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/immune/Gfx09_GFX 01_t1.webp",
                    "file:///android_asset/img/immune/Gfx09_GFX 01.webp")));
            counsellingMedia.addAll(dietDi("MY1"));
            //counsellingMedia.addAll(childGrowth(subStage));

            counsellingMedia.add(new CounsellingMedia(312, CounsellingMedia.GRAPH_MEDIA));
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/childgrowth/Gfx10_GFX 01_t1.webp", "file:///android_asset/img/childgrowth/Gfx10_GFX 01.webp")));
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/childgrowth/Gfx11_GFX 01_t1.webp", "file:///android_asset/img/childgrowth/Gfx11_GFX 01.webp")));
            counsellingMedia.add(new CounsellingMedia(32, CounsellingMedia.GRAPH_HEIGHT_MEDIA));
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/childgrowth/Gfx12_GFX 01_t1.webp", "file:///android_asset/img/childgrowth/Gfx12_GFX 01.webp")));
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/childgrowth/1_t.webp", "file:///android_asset/img/childgrowth/1.webp")));
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/childgrowth/2_t.webp", "file:///android_asset/img/childgrowth/2.webp")));
            counsellingMedia.add(new CounsellingMedia(222, Arrays.asList("file:///android_asset/img/childgrowth/4_t.webp", "file:///android_asset/img/childgrowth/4.webp")));
            counsellingMedia.add(new CounsellingMedia(123, Arrays.asList("file:///android_asset/img/childgrowth/5_t.webp", "file:///android_asset/img/childgrowth/5.webp")));


        }
        if (subStage.contains("MY")) {
            counsellingMedia.add(new CounsellingMedia(312, CounsellingMedia.GRAPH_MEDIA));
            counsellingMedia.add(new CounsellingMedia(32, CounsellingMedia.GRAPH_HEIGHT_MEDIA));
        }


        return counsellingMedia;
    }


    public static List<CounsellingMedia> immutation(String subStage) {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();


        if (subStage.equals("PW4") || subStage.equals("LM1"))
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/immune/Gfx05_GFX 01_t1.webp",
                    "file:///android_asset/img/immune/Gfx05_GFX 01.webp")));
        if (subStage.equals("PW4") || (subStage.equals("LM1") && counsellingChildIAgeInDay <= 42))
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/immune/Gfx06_GFX 01_t1.webp",
                    "file:///android_asset/img/immune/Gfx06_GFX 01.webp")));
        if (subStage.equals("PW4") || (subStage.equals("LM1") && counsellingChildIAgeInDay <= 70))
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/immune/Gfx07_GFX 01_t1.webp",
                    "file:///android_asset/img/immune/Gfx07_GFX 01.webp")));
        if (subStage.equals("LM1") || (subStage.equals("LM2") && counsellingChildIAgeInDay <= 98))
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/immune/Gfx08_GFX 01_t1.webp",
                    "file:///android_asset/img/immune/Gfx08_GFX 01.webp")));
        if (subStage.equals("MY1"))
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/immune/Gfx09_GFX 01_t1.webp",
                    "file:///android_asset/img/immune/Gfx09_GFX 01.webp")));


        return counsellingMedia;
    }

    public static List<CounsellingMedia> childGrowth(String subStage) {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();


        counsellingMedia.add(new CounsellingMedia(312, CounsellingMedia.GRAPH_MEDIA));
        counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/childgrowth/Gfx10_GFX 01_t1.webp", "file:///android_asset/img/childgrowth/Gfx10_GFX 01.webp")));
        counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/childgrowth/Gfx11_GFX 01_t1.webp", "file:///android_asset/img/childgrowth/Gfx11_GFX 01.webp")));
        counsellingMedia.add(new CounsellingMedia(32, CounsellingMedia.GRAPH_HEIGHT_MEDIA));
        counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/childgrowth/Gfx12_GFX 01_t1.webp", "file:///android_asset/img/childgrowth/Gfx12_GFX 01.webp")));
        if (subStage.equals("LM1"))
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/childgrowth/1_t.webp", "file:///android_asset/img/childgrowth/1.webp")));
        if (subStage.equals("LM2"))
            counsellingMedia.add(new CounsellingMedia(28, Arrays.asList("file:///android_asset/img/childgrowth/2_t.webp", "file:///android_asset/img/childgrowth/2.webp")));

        if (subStage.equals("MY1"))
            counsellingMedia.add(new CounsellingMedia(222, Arrays.asList("file:///android_asset/img/childgrowth/4_t.webp", "file:///android_asset/img/childgrowth/4.webp")));
        if (subStage.equals("MY2") || subStage.equals("MY3"))
            counsellingMedia.add(new CounsellingMedia(123, Arrays.asList("file:///android_asset/img/childgrowth/5_t.webp", "file:///android_asset/img/childgrowth/5.webp")));


        return counsellingMedia;
    }


    public static List<CounsellingMedia> dietDi(String subStage) {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();
        counsellingMedia.add(new CounsellingMedia(29, Arrays.asList("file:///android_asset/img/exclusive breast feeding/Gfx08_t.webp", "file:///android_asset/img/exclusive breast feeding/Gfx08.webp")));
        if (subStage.equals("LM2") || subStage.equals("MY1"))
            counsellingMedia.add(new CounsellingMedia(25, Arrays.asList("file:///android_asset/img/Mother Of 4-6 month children/Gfx10_t.webp", "file:///android_asset/img/Mother Of 4-6 month children/Gfx10.webp")));

        return counsellingMedia;
    }


    public static List<CounsellingMedia> pmmvy(String subStage) {

        List<CounsellingMedia> counsellingMedia = new ArrayList<>();

        if (!isPmmvyReg) {
            counsellingMedia.add(new CounsellingMedia(9, Arrays.asList("file:///android_asset/img/General message/Gfx09_t.webp", "file:///android_asset/img/General message/Gfx09.webp")));
            counsellingMedia.add(new CounsellingMedia(13, Arrays.asList("file:///android_asset/img/General message/Gfx10c_t.webp", "file:///android_asset/img/General message/Gfx10c.webp")));
            counsellingMedia.add(new CounsellingMedia(14, Arrays.asList("file:///android_asset/img/General message/Gfx10d_t.webp", "file:///android_asset/img/General message/Gfx10d.webp")));
        }
        if (!isPmmvyReg || (subStage.contentEquals("PW1") || subStage.contentEquals("PW2")))
            counsellingMedia.add(new CounsellingMedia(10, Arrays.asList("file:///android_asset/img/General message/Gfx10_t.webp", "file:///android_asset/img/General message/Gfx10.webp")));
        if (!isPmmvyReg || (subStage.contentEquals("PW1") || subStage.contentEquals("PW2") || subStage.contentEquals("PW3") || subStage.contentEquals("PW34")))
            counsellingMedia.add(new CounsellingMedia(11, Arrays.asList("file:///android_asset/img/General message/Gfx10a_t.webp", "file:///android_asset/img/General message/Gfx10a.webp")));
        if (!isPmmvyReg || (subStage.contentEquals("PW1") || subStage.contentEquals("PW2") || subStage.contentEquals("PW3") || subStage.contentEquals("PW34") || subStage.contains("LM")))
            counsellingMedia.add(new CounsellingMedia(12, Arrays.asList("file:///android_asset/img/General message/Gfx10b_t.webp", "file:///android_asset/img/General message/Gfx10b.webp")));

        return counsellingMedia;
    }


    public static List<CounsellingMedia> wg() {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();

        counsellingMedia.add(new CounsellingMedia(0, Arrays.asList("file:///android_asset/img/weight gain/Gfx0.1_t.webp", "file:///android_asset/img/weight gain/Gfx0.1.webp")));
        counsellingMedia.add(new CounsellingMedia(5, Arrays.asList("file:///android_asset/img/weight gain/Gfx04_t.webp", "file:///android_asset/img/weight gain/Gfx04.webp")));
        counsellingMedia.add(new CounsellingMedia(3, CounsellingMedia.GRAPH_MEDIA));
        counsellingMedia.add(new CounsellingMedia(2, Arrays.asList("file:///android_asset/img/weight gain/Gfx01b_t.webp", "file:///android_asset/img/weight gain/Gfx01b.webp")));
        counsellingMedia.add(new CounsellingMedia(1, Arrays.asList("file:///android_asset/img/weight gain/Gfx01a_t.webp", "file:///android_asset/img/weight gain/Gfx01a.webp")));
        counsellingMedia.add(new CounsellingMedia(3, Arrays.asList("file:///android_asset/img/weight gain/Gfx02_t.webp", "file:///android_asset/img/weight gain/Gfx02.webp")));
        counsellingMedia.add(new CounsellingMedia(13, Arrays.asList(R.raw.film + ""), VIDEO_MEDIA));
        counsellingMedia.add(new CounsellingMedia(6, Arrays.asList("file:///android_asset/img/weight gain/Gfx05_t.webp", "file:///android_asset/img/weight gain/Gfx05.webp")));
        counsellingMedia.add(new CounsellingMedia(7, Arrays.asList("file:///android_asset/img/weight gain/Gfx05a_t.webp", "file:///android_asset/img/weight gain/Gfx05a.webp")));
        counsellingMedia.add(new CounsellingMedia(8,
                Arrays.asList(
                        "file:///android_asset/counseling/an/1.webp",
                        "file:///android_asset/counseling/an/2.webp",
                        "file:///android_asset/counseling/an/3.webp",
                        "file:///android_asset/counseling/an/4.webp",
                        "file:///android_asset/counseling/an/5.webp",
                        "file:///android_asset/counseling/an/6.webp"
                )));
        counsellingMedia.add(new CounsellingMedia(8, Arrays.asList("file:///android_asset/img/weight gain/Gfx05b_t.webp", "file:///android_asset/img/weight gain/Gfx05b.webp")));
        counsellingMedia.add(new CounsellingMedia(9, Arrays.asList("file:///android_asset/img/weight gain/Gfx05c_t.webp", "file:///android_asset/img/weight gain/Gfx05c.webp")));
        counsellingMedia.add(new CounsellingMedia(10, Arrays.asList("file:///android_asset/img/weight gain/Gfx05d_t.webp", "file:///android_asset/img/weight gain/Gfx05d.webp")));
        counsellingMedia.add(new CounsellingMedia(4, Arrays.asList("file:///android_asset/img/weight gain/Gfx03_t.webp", "file:///android_asset/img/weight gain/Gfx03.webp")));

        return counsellingMedia;
    }

    public static List<CounsellingMedia> ifa() {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();


        counsellingMedia.add(new CounsellingMedia(1, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx06_t.webp", "file:///android_asset/img/ifa_calcium/Gfx06.webp")));
        counsellingMedia.add(new CounsellingMedia(6, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx08_t.webp", "file:///android_asset/img/ifa_calcium/Gfx08.webp")));
        counsellingMedia.add(new CounsellingMedia(7, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx08a_t.webp", "file:///android_asset/img/ifa_calcium/Gfx08a.webp")));
        counsellingMedia.add(new CounsellingMedia(2, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx07_t.webp", "file:///android_asset/img/ifa_calcium/Gfx07.webp")));
        counsellingMedia.add(new CounsellingMedia(5, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx07c_t.webp", "file:///android_asset/img/ifa_calcium/Gfx07c.webp")));
        counsellingMedia.add(new CounsellingMedia(3, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx07a_t.webp", "file:///android_asset/img/ifa_calcium/Gfx07a.webp")));
        counsellingMedia.add(new CounsellingMedia(4, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx07b_t.webp", "file:///android_asset/img/ifa_calcium/Gfx07b.webp")));
        counsellingMedia.add(new CounsellingMedia(16, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx12_t.webp", "file:///android_asset/img/ifa_calcium/Gfx12.webp")));
        counsellingMedia.add(new CounsellingMedia(12, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx10_t.webp", "file:///android_asset/img/ifa_calcium/Gfx10.webp")));
        counsellingMedia.add(new CounsellingMedia(13, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx10a_t.webp", "file:///android_asset/img/ifa_calcium/Gfx10a.webp")));
        counsellingMedia.add(new CounsellingMedia(14, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx10b_t.webp", "file:///android_asset/img/ifa_calcium/Gfx10b.webp")));
        counsellingMedia.add(new CounsellingMedia(8, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx09_t.webp", "file:///android_asset/img/ifa_calcium/Gfx09.webp")));
        counsellingMedia.add(new CounsellingMedia(11, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx09c_t.webp", "file:///android_asset/img/ifa_calcium/Gfx09c.webp")));
        counsellingMedia.add(new CounsellingMedia(9, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx09a_t.webp", "file:///android_asset/img/ifa_calcium/Gfx09a.webp")));
        counsellingMedia.add(new CounsellingMedia(10, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx09b_t.webp", "file:///android_asset/img/ifa_calcium/Gfx09b.webp")));
        counsellingMedia.add(new CounsellingMedia(15, Arrays.asList("file:///android_asset/img/ifa_calcium/Gfx11_t.webp", "file:///android_asset/img/ifa_calcium/Gfx11.webp")));


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

        return counsellingMedia;
    }

    public static List<CounsellingMedia> prefBirth(String subStage) {
        List<CounsellingMedia> counsellingMedia = new ArrayList<>();


        counsellingMedia.add(new CounsellingMedia(43, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx01_t.webp", "file:///android_asset/img/preparing for birth/Gfx01.webp")));
        counsellingMedia.add(new CounsellingMedia(44, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx02_t.webp", "file:///android_asset/img/preparing for birth/Gfx02.webp")));
        counsellingMedia.add(new CounsellingMedia(46, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx04_t.webp", "file:///android_asset/img/preparing for birth/Gfx04.webp")));
        counsellingMedia.add(new CounsellingMedia(45, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx02_GFX 01_t1.webp", "file:///android_asset/img/preparing for birth/Gfx02_GFX 01.webp")));

        counsellingMedia.add(new CounsellingMedia(45, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx01_GFX 01_t1.webp",
                "file:///android_asset/img/preparing for birth/Gfx01_GFX 01.webp")));

        counsellingMedia.add(new CounsellingMedia(45, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx02_GFX 01_t1.webp", "file:///android_asset/img/preparing for birth/Gfx02_GFX 01.webp")));


        counsellingMedia.add(new CounsellingMedia(45, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx03_GFX 01_t1.webp",
                "file:///android_asset/img/preparing for birth/Gfx03_GFX 01.webp")));
        counsellingMedia.add(new CounsellingMedia(45, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx04_GFX 01_t1.webp",
                "file:///android_asset/img/preparing for birth/Gfx04_GFX 01.webp")));
        counsellingMedia.add(new CounsellingMedia(45, Arrays.asList("file:///android_asset/img/preparing for birth/Gfx05a_GFX 01_t1.webp",
                "file:///android_asset/img/preparing for birth/Gfx05a_GFX 01.webp")));


        // counsellingMedia.add(new CounsellingMedia(26, Arrays.asList("file:///android_asset/img/exclusive breast feeding/Gfx05_t.webp", "file:///android_asset/img/exclusive breast feeding/Gfx05.webp")));


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

