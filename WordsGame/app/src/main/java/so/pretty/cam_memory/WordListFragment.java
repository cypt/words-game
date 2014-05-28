package so.pretty.cam_memory;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by isp on 16.05.2014.
 */
public class WordListFragment extends Fragment {

    public static final long DELTA_TIME = 240;

    @InjectView(R.id.word_list)
    ListView wordList;

    private volatile TextView timer;

    private Handler handler;
    private Runnable r;
    private int pauseTime;
    private int remaining;
    private View timerContainer;
    private TextView timerSub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_word_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        ActionBar supportActionBar = activity.getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowCustomEnabled(true);
        supportActionBar.setCustomView(R.layout.action_bar_timer);

        View customView = supportActionBar.getCustomView();
        timer = (TextView) customView.findViewById(R.id.timer);
        timerContainer = customView.findViewById(R.id.timer_container);
        timerSub = (TextView) customView.findViewById(R.id.timer_sub);

        timerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer.getVisibility() == View.VISIBLE) {
                    timer.setVisibility(View.GONE);
                    timerSub.setText("Показать таймер");
                } else {
                    timer.setVisibility(View.VISIBLE);
                    timerSub.setText("Скрыть");
                }
            }
        });

        ButterKnife.inject(this, getActivity());

        final Game game = ((WordGameOwner) getActivity()).getWordGameManager().getGame();
        Log.d("Game", game.getWords().toString());

        wordList.setAdapter(new WordListAdapter(game.getWords(), game.isRandomTextParams()));

        handler = new Handler();

        this.pauseTime = game.getTime() * 1000;
    }

    @Override
    public void onResume() {
        super.onResume();
        final long startTime = System.currentTimeMillis();
        r = new Runnable() {
            @Override
            public void run() {
                Log.d(WordListFragment.class.getName(), "Tick!");
                long currentTime = System.currentTimeMillis();
                long elapsed = currentTime - startTime;
                remaining = (int) (pauseTime - elapsed);
                if (remaining < 0) {
                    showNextScreen();
                    return;
                }
                int minutes = remaining / 60000;
                int seconds = (remaining / 1000) % 60;
                timer.setText(String.format("%s:%s", minutes, seconds));
                handler.postDelayed(r, DELTA_TIME);
            }
        };
        handler.post(r);
    }

    private void showNextScreen() {
        Log.d(WordListFragment.class.getName(), "showNextScreen");
        handler.removeCallbacks(r);
        WordGameManager wordGameManager = ((MainActivity) getActivity()).getWordGameManager();
        wordGameManager.showCompareScreen();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(r);
        pauseTime = remaining;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.word_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean b = super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else {
            showNextScreen();
        }
        return b;
    }

    public static Fragment newInstance(final Context context) {
        return Fragment.instantiate(context, WordListFragment.class.getName());
    }
}
