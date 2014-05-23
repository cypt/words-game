package so.pretty.cam_memory;

/**
 * Created by isp on 16.05.2014.
 */
public interface WordGameManager {

    void showSettingsScreen();

    void startNewGame(Game game);

    void showCompareScreen();

    void showResultScreen();

    Game getGame();
}
