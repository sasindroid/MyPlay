package com.sasi.giffgaffplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sasikumarlakshmanan on 14/10/15.
 */
public class LayoutAdapter extends BaseAdapter {

    Context context;
    boolean isBorder = false;
    int menuRes = 0;

    public LayoutAdapter(Context context, boolean isBorder) {
        this.context = context;
        this.isBorder = isBorder;
    }

    public void notifyDataSetChanged(boolean isBorder, int menuRes) {
        this.isBorder = isBorder;
        this.menuRes = menuRes;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.feature_layout, null);
            viewHolder.imageView2 = (ImageView) view.findViewById(R.id.imageView2);
            viewHolder.textView2 = (TextView) view.findViewById(R.id.textView2);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        try {
            viewHolder.imageView2.setImageResource(myResID[position]);
        } catch (Exception e) {
            viewHolder.imageView2.setImageResource(myResID[0]);
        }

        try {
            viewHolder.textView2.setText(myText[position]);
        } catch (Exception e) {
            viewHolder.textView2.setText("More?");
        }

        if(isBorder) {
            switch (menuRes) {
                case R.id.action_border:
                    view.setBackgroundResource(R.drawable.myround_ripple_square);
                    break;
                case R.id.action_border_round:
                    view.setBackgroundResource(R.drawable.myround);
                    break;
                case R.id.action_border_ripple:
                    view.setBackgroundResource(R.drawable.custom_bg);
                    break;
                case R.id.action_border_ripple_round:
                    view.setBackgroundResource(R.drawable.myround_ripple2);
                    break;
                default:
                    view.setBackgroundResource(0);
            }
        }
        else {
            view.setBackgroundResource(0);
        }

        return view;
    }


    private class ViewHolder {
        private ImageView imageView2;
        private TextView textView2;
    }


    private Integer[] myResID = {R.drawable.account_tap, R.drawable.topup_tap,
            R.drawable.payback_tap, R.drawable.community_tap, R.drawable.contacts_tap, R.drawable.help};

    private String[] myText = {"my account", "Ways to buy", "my payback", "Community", "Contacts", "Need help?", "More"};

}
