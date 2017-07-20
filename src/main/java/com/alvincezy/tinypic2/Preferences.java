package com.alvincezy.tinypic2;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

/**
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 2.0.1, 7/20/2017
 * @since 2.0
 */
@State(name = "TinyPicPreferences", storages = @Storage(value = "$APP_CONFIG$/TinyPic2.xml"))
public class Preferences implements PersistentStateComponent<Preferences> {

    private String apiKey;

    public static Preferences getInstance() {
        return ServiceManager.getService(Preferences.class);
    }

    @Nullable
    @Override
    public Preferences getState() {
        return this;
    }

    @Override
    public void loadState(Preferences preferences) {
        XmlSerializerUtil.copyBean(preferences, this);
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String key) {
        apiKey = key;
    }
}
