package so.pretty.cam_memory;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by isp on 16.05.2014.
 */
public class SettingsFragment extends Fragment {

    @InjectView(R.id.mode)
    protected RadioGroup modeGroup;

    @InjectView(R.id.difficult_level)
    protected RadioGroup difficultLevelGroup;

    public static Fragment newInstance(final Context context) {
        return Fragment.instantiate(context, SettingsFragment.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.inject(this, getActivity());

        if (savedInstanceState != null) {
            final FragmentActivity activity = getActivity();

            Level difficultLevel = PreferenceUtils.getDifficultLevel(activity);
            switch (difficultLevel) {
                case NEWBIE:
                    difficultLevelGroup.check(R.id.newbie);
                    break;
                case ADVANCED:
                    difficultLevelGroup.check(R.id.advanced);
                    break;
                case CAESAR:
                    difficultLevelGroup.check(R.id.caesar);
                    break;
                case SHERLOCK:
                    difficultLevelGroup.check(R.id.sherlock);
                    break;
            }

            difficultLevelGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.newbie:
                            PreferenceUtils.setDifficultLevel(activity, Level.NEWBIE);
                            return;
                        case R.id.advanced:
                            PreferenceUtils.setDifficultLevel(activity, Level.ADVANCED);
                            return;
                        case R.id.caesar:
                            PreferenceUtils.setDifficultLevel(activity, Level.CAESAR);
                            return;
                        case R.id.sherlock:
                            PreferenceUtils.setDifficultLevel(activity, Level.SHERLOCK);
                            return;
                    }
                }
            });

            Mode mode = PreferenceUtils.getMode(activity);
            switch (mode) {
                case SHOW_ALL:
                    modeGroup.check(R.id.all);
                    break;
                case SHOW_ONE_BY_ONE:
                    modeGroup.check(R.id.one_by_one);
                    break;
            }

            modeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.one_by_one:
                            PreferenceUtils.setMode(activity, Mode.SHOW_ONE_BY_ONE);
                            break;
                        case R.id.all:
                            PreferenceUtils.setMode(activity, Mode.SHOW_ALL);
                            break;
                    }
                }
            });
        }

        getView().findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(view);
            }
        });
    }

    public void start(View view) {
        Context applicationContext = getActivity().getApplicationContext();
        Mode mode = PreferenceUtils.getMode(applicationContext);
        Level level = PreferenceUtils.getDifficultLevel(applicationContext);
        Game game = new Game();

        game.setMode(mode);
        if (level == Level.NEWBIE) {
            game.setTime(120);
            game.setRandomTextParams(false);
            game.setWords(DatabaseHelper.getRandomWords(applicationContext, 20));
        } else if (level == Level.ADVANCED) {
            game.setTime(120);
            game.setRandomTextParams(false);
            game.setWords(DatabaseHelper.getRandomWords(applicationContext, 20));
        } else if (level == Level.CAESAR) {
            game.setTime(120);
            game.setRandomTextParams(false);
            game.setWords(DatabaseHelper.getRandomWords(applicationContext, 20));
        } else if (level == Level.SHERLOCK) {
            game.setTime(120);
            game.setRandomTextParams(false);
            game.setWords(DatabaseHelper.getRandomWords(applicationContext, 20));
        }

        ((WordGameOwner)getActivity()).getWordGameManager().startNewGame(game);
    }
}
