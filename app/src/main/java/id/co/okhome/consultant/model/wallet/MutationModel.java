package id.co.okhome.consultant.model.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Map;

public class MutationModel implements Parcelable{
    public double amount;
    public String date, refId, type, transactionId;
    public Map<String, String> title;

    protected MutationModel(Parcel in) {
        amount = in.readDouble();
        date = in.readString();
        refId = in.readString();
        type = in.readString();
        transactionId = in.readString();
    }

    public static final Creator<MutationModel> CREATOR = new Creator<MutationModel>() {
        @Override
        public MutationModel createFromParcel(Parcel in) {
            return new MutationModel(in);
        }

        @Override
        public MutationModel[] newArray(int size) {
            return new MutationModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(amount);
        parcel.writeString(date);
        parcel.writeString(refId);
        parcel.writeString(type);
        parcel.writeString(transactionId);
    }
}
