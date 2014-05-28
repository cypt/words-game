package so.pretty.cam_memory;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by oisupov on 5/23/2014.
 */
public class SplashScreenFragment extends DialogFragment {

    private Subscription subscribe;

    public static Fragment newInstance(final Context context) {
        return Fragment.instantiate(context, SplashScreenFragment.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Context context = getActivity().getApplicationContext();

        if (!DatabaseHelper.isEmpty(context)) {
            dismiss();
        }

        subscribe = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                DatabaseHelper.init(context);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                ((MainActivity) getActivity()).getWordGameManager().showSettingsScreen();
                dismiss();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(SplashScreenFragment.class.getName(), Log.getStackTraceString(e));
            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

}
