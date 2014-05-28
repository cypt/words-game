package so.pretty.cam_memory;


import android.support.v4.app.FragmentTransaction;

/**
 * Created by isp on 16.05.2014.
 */
public class WordGameManagerImpl implements WordGameManager {

    private MainActivity mainActivity;

    private Game game;

    public WordGameManagerImpl(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private FragmentTransaction beginTransaction() {
        return mainActivity.getSupportFragmentManager().beginTransaction().addToBackStack(null);
    }

    @Override
    public void showSettingsScreen() {
        beginTransaction().replace(R.id.container, SettingsFragment.newInstance(mainActivity)).commit();
    }

    @Override
    public void startNewGame(final Game game) {
        this.game = game;
        if (game.getMode() == Mode.SHOW_ALL) {
            beginTransaction().replace(R.id.container, WordListFragment.newInstance(mainActivity)).commit();
        } else {
            beginTransaction().replace(R.id.container, WordOneByOne.newInstance(mainActivity)).commit();
        }
    }

    @Override
    public void showCompareScreen() {
        beginTransaction().replace(R.id.container, CheckScreenFragment.newInstance(mainActivity)).commit();
    }

    @Override
    public void showResultScreen() {
        beginTransaction().replace(R.id.container, ResultFragment.newInstance(mainActivity)).commit();
    }

    @Override
    public Game getGame() {
        return game;
    }
}
