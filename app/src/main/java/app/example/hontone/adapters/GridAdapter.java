package app.example.hontone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import app.example.hontone.R;

/**
 * グリッド画面用のアダプタークラス
 */
public class GridAdapter extends BaseAdapter {

    private Context mContext;

    // TODO: 仮にベタ書き
    private String[] mBooks = {"http://ecx.images-amazon.com/images/I/51a%2BeyWtFaL._SL150_.jpg", "http://ecx.images-amazon.com/images/I/51p9hgkvLCL._SL500_PIsitb-sticker-arrow-big,TopRight,35,-73_SL150_OU09_.jpg", "http://ecx.images-amazon.com/images/I/51mIeX-d0vL._SL150_.jpg", "http://ecx.images-amazon.com/images/I/51wBoV4HHnL._SL150_.jpg", "http://ecx.images-amazon.com/images/I/51Ok-N1G1RL._SS120_RO10,1,201,225,243,255,255,255,15_.jpg", "http://ecx.images-amazon.com/images/I/51td5vbzFZL._SS120_RO10,1,201,225,243,255,255,255,15_.jpg", "http://ecx.images-amazon.com/images/I/413Eny8KQsL._SS120_RO10,1,201,225,243,255,255,255,15_.jpg"};

    public GridAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mBooks.length;
    }

    @Override
    public Object getItem(int position) {
        return mBooks[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.grid_item_book, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.grid_image_book);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.image.setImageResource(android.R.drawable.ic_btn_speak_now);

        return convertView;
    }

    private static class ViewHolder {
        ImageView image;
    }
}
