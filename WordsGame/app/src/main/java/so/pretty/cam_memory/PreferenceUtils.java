package so.pretty.cam_memory;

import android.content.Context;

/**
 * Created by isp on 17.05.2014.
 */
public class PreferenceUtils {
    private static final String OPTION_WORDS_COUNT = "OPTION_WORDS_COUNT";
    public static final String OPTION_TIME = "OPTION_TIME";
    public static final String OPTIONS_DIFFICULT_LEVEL = "OPTIONS_DIFFICULT";

    public static Level getDifficultLevel(final Context context) {
        return Level.ADVANCED;
    }

    public static Mode getMode(final Context context) {
        return Mode.SHOW_ALL;
    }

    public static void setMode(final Context context, final Mode mode) {

    }

    public static void setDifficultLevel(final Context context, final Level level) {

    }
}
