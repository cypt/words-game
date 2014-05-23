package so.pretty.cam_memory;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isp on 16.05.2014.
 */
public class Game implements Parcelable {

    private List<String> words;

    private int time;

    private Mode mode;

    private boolean randomTextParams;

    public Game(List<String> words, int time, Mode mode, boolean randomTextParams) {
        this.words = words;
        this.time = time;
        this.mode = mode;
        this.randomTextParams = randomTextParams;
    }

    public Game() {

    }

    public List<String> getWords() {
        return words;
    }

    public int getTime() {
        return time;
    }

    public Mode getMode() {
        return mode;
    }

    public boolean isRandomTextParams() {
        return randomTextParams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(words);
        dest.writeInt(time);
        dest.writeInt(randomTextParams ? 1 : 0);
        dest.writeSerializable(mode);
    }

    public static Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            ArrayList words = new ArrayList();
            source.readList(words, ClassLoader.getSystemClassLoader());
            int time = source.readInt();
            boolean randomTextParams = 1 == source.readInt();
            Mode mode = (Mode) source.readSerializable();

            return new Game(words, time, mode, randomTextParams);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public void setWords(List<String> words) {
        this.words = words;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setRandomTextParams(boolean randomTextParams) {
        this.randomTextParams = randomTextParams;
    }
}
