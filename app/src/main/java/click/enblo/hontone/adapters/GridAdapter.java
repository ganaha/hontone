package click.enblo.hontone.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import click.enblo.hontone.R;
import click.enblo.hontone.dto.ItemDto;

/**
 * グリッド画面用のアダプタークラス
 */
public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private List<ItemDto> mBooks;
    private DisplayImageOptions options;

    public GridAdapter(Context context, List<ItemDto> books) {
        mContext = context;
        mBooks = books;

        // Univ
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        // 並び替え
        Collections.sort(mBooks, new Comparator<ItemDto>() {
            @Override
            public int compare(ItemDto lhs, ItemDto rhs) {
                if (lhs == null || lhs.registered == null || rhs == null || rhs.registered == null) {
                    return 0;
                }
                return rhs.registered.compareTo(lhs.registered);
            }
        });
    }

    @Override
    public int getCount() {
        return mBooks.size();
    }

    @Override
    public Object getItem(int position) {
        return mBooks.get(position);
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
            viewHolder.mark = (ImageView) convertView.findViewById(R.id.grid_mark_uploaded);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ItemDto dto = mBooks.get(position);
        ImageLoader.getInstance().displayImage(dto.largeImageUrl, viewHolder.image, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        // アップロード済みマーク
        if (dto.kintoneId != null && dto.kintoneId.length() != 0) {
            // kintone IDがある場合、マークを表示
            viewHolder.mark.setVisibility(View.VISIBLE);
        } else {
            // kintone IDがない場合、マークを非表示
            viewHolder.mark.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void remove(int position) {
        mBooks.remove(position);
    }

    private static class ViewHolder {
        ImageView image;
        ImageView mark;
    }
}
