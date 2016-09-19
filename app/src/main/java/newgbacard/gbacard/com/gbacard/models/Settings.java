package newgbacard.gbacard.com.gbacard.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Williamz on 19-Sep-16.
 */
public class Settings {
    private String settingTitle;
    private Drawable settingIcon;

    public Settings() {
    }

    public Settings(String settingTitle, Drawable settingIcon) {
        this.settingTitle = settingTitle;
        this.settingIcon = settingIcon;
    }

    public Drawable getSettingIcon() {
        return settingIcon;
    }

    public void setSettingIcon(Drawable settingIcon) {
        this.settingIcon = settingIcon;
    }

    public String getSettingTitle() {
        return settingTitle;
    }

    public void setSettingTitle(String settingTitle) {
        this.settingTitle = settingTitle;
    }
}
