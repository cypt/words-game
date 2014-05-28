package so.pretty.cam_memory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by oisupov on 5/25/2014.
 */
public class AnswersAdapter extends BaseAdapter {

    private List<String> wrongWords = new ArrayList<String>();
    private List<String> rightWords = new ArrayList<String>();

    public AnswersAdapter() {
    }

    public boolean addWrongWord(String s) {
        return wrongWords.add(s);
    }

    public boolean addRightWord(String s) {
        return rightWords.add(s);
    }

    @Override
    public int getCount() {
        return wrongWords.size() + rightWords.size();
    }

    @Override
    public Object getItem(int i) {
        return i < rightWords.size() ? rightWords.get(i) : wrongWords.get(i - rightWords.size()) ;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();

        if (view == null) {
            view = View.inflate(context, R.layout.item_answer, null);
        }

        String word = (String) getItem(i);

        TextView textView = (TextView) view;

        if (i < rightWords.size()) {
            textView.setTextColor(context.getResources().getColor(R.color.right_answer));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.wrong_answer));
        }

        textView.setText(word);

        return view;
    }

    public List<String> getWrongWords() {
        return wrongWords;
    }

    public List<String> getRightWords() {
        return rightWords;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
