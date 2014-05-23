package so.pretty.cam_memory;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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

    public static final String ARGUMENT_WORD_COUNT = "WORD_COUNT";
    public static final int DELTA_TIME = 300;

    @InjectView(R.id.word_list)
    ListView wordList;

    private volatile TextView timer;

    private Handler handler;
    private Runnable r;
    private int pauseTime;
    private int remaining;

    public WordListFragment newInstance(final Context context, final int wordCount) {
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_WORD_COUNT, wordCount);
        return (WordListFragment) Fragment.instantiate(context, WordListFragment.class.getName(), args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_word_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, getActivity());
        final Game game = ((WordGameOwner) getActivity()).getWordGameManager().getGame();
        wordList.setAdapter(new WordListAdapter(game.getWords(), game.isRandomTextParams()));
        handler = new Handler();
        this.pauseTime = game.getTime();
    }

    @Override
    public void onResume() {
        super.onResume();
        final long startTime = System.currentTimeMillis();
        r = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long elapsed = currentTime - startTime;
                remaining = (int) (pauseTime - elapsed);
                if (remaining < 0) {
                    showNextScreen();
                    return;
                }
                int minutes = remaining / 60;
                int seconds = remaining % 60;
                timer.setText(String.format("{} : {}", minutes, seconds));
                handler.postDelayed(this, DELTA_TIME);
            }
        };
        handler.postDelayed(r, DELTA_TIME);
    }

    private void showNextScreen() {
        handler.removeCallbacks(r);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(r);
        pauseTime = remaining;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.word_list_menu, menu);
        MenuItem timerMenuItem = menu.findItem(R.id.timer);

        this.timer = (TextView) timerMenuItem.getActionView().findViewById(R.id.timer);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static Fragment newInstance(final Context context) {
        return Fragment.instantiate(context, WordListFragment.class.getName());
    }
}
