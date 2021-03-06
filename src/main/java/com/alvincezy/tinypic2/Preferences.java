package com.alvincezy.tinypic2;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.1, 2018/8/18
 * @since 1.0
 */
@State(name = "TinyPicPreferences", storages = @Storage(value = "$APP_CONFIG$/TinyPic2.xml"))
public class Preferences implements PersistentStateComponent<Preferences> {

    private String apiKey;
    private boolean backupBeforeTinify;

    public static Preferences getInstance() {
        return ServiceManager.getService(Preferences.class);
    }

    @Nullable
    @Override
    public Preferences getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull Preferences preferences) {
        XmlSerializerUtil.copyBean(preferences, this);
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String key) {
        apiKey = key;
    }

    public boolean isBackupBeforeTinify() {
        return backupBeforeTinify;
    }

    public void setBackupBeforeTinify(boolean backupBeforeTinify) {
        this.backupBeforeTinify = backupBeforeTinify;
    }
}
