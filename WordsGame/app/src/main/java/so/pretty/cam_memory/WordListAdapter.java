package so.pretty.cam_memory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isp on 17.05.2014.
 */
public class WordListAdapter extends BaseAdapter {

    private ArrayList<WordParameters> wordParameterses;
    private final List<String> words;
    private boolean randomTextParams;

    public static class WordParameters {
        int color;
        float size;

        public static WordParameters getRandom() {
            WordParameters wordParameters = new WordParameters();
            wordParameters.size = (float) (15 + Math.random() * 10);
            return wordParameters;
        }
    }

    public WordListAdapter(List<String> words, boolean randomTextParams) {
        this.words = words;
        this.randomTextParams = randomTextParams;
        if (randomTextParams) {
            wordParameterses = new ArrayList<WordParameters>();
        }
    }


    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int position) {
        return words.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_word, null);
        }

        TextView textView = (TextView) convertView;
        if (randomTextParams) {
            textView.setTextSize((float) (15 + Math.random() * 10));
        } else {
            textView.setTextSize(18);
        }

        textView.setText(words.get(position));

        return convertView;
    }
}
