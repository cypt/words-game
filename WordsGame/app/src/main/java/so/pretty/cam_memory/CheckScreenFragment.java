package so.pretty.cam_memory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by isp on 17.05.2014.
 */
public class CheckScreenFragment extends Fragment {

    private AnswersAdapter answersAdapter;

    @InjectView(R.id.voice_input)
    ImageButton voiceInput;

    @InjectView(R.id.accept)
    ImageButton accept;

    @InjectView(R.id.input)
    EditText input;

    @InjectView(R.id.answers)
    GridView answers;

    TextView status;

    private Dialog recognizeDialog;

    private class EditTextRecognitionListener extends DefaultRecognitionListener {

        private EditText editText;

        @Override
        public void onReadyForSpeech(Bundle params) {
            super.onReadyForSpeech(params);
            recognizeDialog.show();
        }

        @Override
        public void onEndOfSpeech() {
            super.onEndOfSpeech();
            recognizeDialog.dismiss();
        }

        private EditTextRecognitionListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onError(int error) {

        }

        @Override
        public void onResults(Bundle results) {
            final ArrayList<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (result.size() > 0) {
                editText.setText(result.get(0));
            }
        }
    }

    public static Fragment newInstance(MainActivity mainActivity) {
        return Fragment.instantiate(mainActivity, CheckScreenFragment.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setHasOptionsMenu(true);

        ActionBar supportActionBar = ((MainActivity) getActivity()).getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowCustomEnabled(true);
        supportActionBar.setCustomView(R.layout.action_bar_compare);

        status = (TextView) supportActionBar.getCustomView().findViewById(R.id.status);

        return inflater.inflate(R.layout.fragment_check_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.inject(this, view);

        answersAdapter = new AnswersAdapter();
        answers.setAdapter(answersAdapter);

        updateStatus();

        Context applicationContext = getActivity().getApplicationContext();
        recognizeDialog = new AlertDialog.Builder(getActivity()).setTitle("Speak").create();

        final SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext);

        final Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru")
                .putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName())
                .putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        voiceInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechRecognizer.setRecognitionListener(new EditTextRecognitionListener(input));
                speechRecognizer.startListening(recognizerIntent);
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWord(input.getText().toString());
            }
        });

        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode()) {
                    addWord(input.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void addWord(final String word) {

        if (TextUtils.isEmpty(word)) {
            return;
        }

        Game game = ((MainActivity) getActivity()).getWordGameManager().getGame();

        if (game.getWords().contains(word)) {
            answersAdapter.addRightWord(word);
        } else {
            answersAdapter.addWrongWord(word);
        }

        answersAdapter.notifyDataSetChanged();

        input.setText("");

        updateStatus();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.word_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        WordGameManager wordGameManager = ((MainActivity) getActivity()).getWordGameManager();

        if (item.getItemId() == android.R.id.home) {
            wordGameManager.showSettingsScreen();
            return true;
        } else {
            showResult();
        }

        return false;
    }

    private void updateStatus() {
        WordGameManager wordGameManager = ((MainActivity) getActivity()).getWordGameManager();

        int rightAnswers = answersAdapter.getRightWords().size();
        int totalWords = wordGameManager.getGame().getWords().size();

        status.setText(String.format("%s/%s", rightAnswers, totalWords));
        if (rightAnswers == totalWords) {
            showResult();
        }
    }

    private void showResult() {
        WordGameManager wordGameManager = ((MainActivity) getActivity()).getWordGameManager();
        wordGameManager.showResultScreen();
    }
}
