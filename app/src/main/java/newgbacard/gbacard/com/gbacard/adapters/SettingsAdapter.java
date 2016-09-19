package newgbacard.gbacard.com.gbacard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import newgbacard.gbacard.com.gbacard.R;
import newgbacard.gbacard.com.gbacard.models.Settings;

/**
 * Created by Williamz on 19-Sep-16.
 */
public class SettingsAdapter  extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {
    private List<Settings> settingsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView settingTitle;
        public ImageView settingIcon;

        public MyViewHolder(View view) {
            super(view);
            settingTitle = (TextView) view.findViewById(R.id.setting_title);
            settingIcon = (ImageView) view.findViewById(R.id.setting_icon);
        }
    }


    public SettingsAdapter(List<Settings> settingsList) {
        this.settingsList = settingsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_menu_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Settings settings = settingsList.get(position);
        holder.settingTitle.setText(settings.getSettingTitle());
        holder.settingIcon.setImageDrawable(settings.getSettingIcon());
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }
}
