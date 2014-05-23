package so.pretty.cam_memory;

import android.support.v4.app.Fragment;

/**
 * Created by isp on 17.05.2014.
 */
public class CheckScreenFragment extends Fragment {
    public static Fragment newInstance(MainActivity mainActivity) {
        return Fragment.instantiate(mainActivity, CheckScreenFragment.class.getName());
    }
}
