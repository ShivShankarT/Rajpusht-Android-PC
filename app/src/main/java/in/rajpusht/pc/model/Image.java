package in.rajpusht.pc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
    private long imageId;
    private String imageSm;
    private String imageLg;
    private long displayOrder;
    private String caption;

    public Image() {
    }

    protected Image(Parcel in) {
        imageId = in.readLong();
        imageSm = in.readString();
        imageLg = in.readString();
        displayOrder = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(imageId);
        dest.writeString(imageSm);
        dest.writeString(imageLg);
        dest.writeLong(displayOrder);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public String getImageSm() {
        return imageSm;
    }

    public void setImageSm(String imageSm) {
        this.imageSm = imageSm;
    }


    public String getImageLg() {
        return imageLg;
    }

    public void setImageLg(String imageLg) {
        this.imageLg = imageLg;
    }


    public long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
