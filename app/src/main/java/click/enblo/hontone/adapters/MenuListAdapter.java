package click.enblo.hontone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import click.enblo.hontone.R;
import click.enblo.hontone.dto.MenuDto;

/**
 * メニューアダプター
 */
public class MenuListAdapter extends ArrayAdapter<MenuDto> {

    private Context mContext;
    private int mLayoutId;
    private List<MenuDto> mMenus;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public MenuListAdapter(Context context, int resource, List<MenuDto> objects) {
        super(context, resource, objects);

        mContext = context;
        mLayoutId = resource;
        mMenus = objects;
    }

    /**
     * メニューを生成する。
     */
    public static List<MenuDto> createMenuItems(Context context) {

        List<MenuDto> menus = new ArrayList<>();

        // Settings
        MenuDto settings = new MenuDto();
        settings.setId(R.drawable.ic_action_action_settings);
        settings.setName(context.getString(R.string.action_settings));
        menus.add(settings);

        // ヘルプ
        MenuDto help = new MenuDto();
        help.setId(R.drawable.ic_action_help);
        help.setName(context.getString(R.string.action_help));
        menus.add(help);

        // このアプリについて
        MenuDto about = new MenuDto();
        about.setId(R.drawable.ic_action_info);
        about.setName(context.getString(R.string.action_about));
        menus.add(about);

        return menus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(mLayoutId, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.menu_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.menu_image);
        textView.setText(mMenus.get(position).getName());
        imageView.setImageResource(mMenus.get(position).getId());

        return rowView;
    }
}
