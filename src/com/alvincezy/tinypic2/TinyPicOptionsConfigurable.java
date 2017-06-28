package com.alvincezy.tinypic2;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.alvincezy.tinypic2.ui.PreferencesPanel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * TinyPic 选项配置
 * <p/>
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 2.0, 6/28/2017
 * @since 2.0
 */
public class TinyPicOptionsConfigurable implements SearchableConfigurable, Disposable {

    public static void showSettingsDialog(Project project) {
        if (project != null) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, TinyPicOptionsConfigurable.class);
        }
    }

    private Preferences mPrefs;
    private PreferencesPanel mPanel;

    public TinyPicOptionsConfigurable() {
        mPrefs = Preferences.getInstance();
    }

    @NotNull
    @Override
    public String getId() {
        return "me.alvince.plugin.tinypic2";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "TinyPic 2";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mPanel = new PreferencesPanel();
        return mPanel.create(mPrefs);
    }

    @Override
    public boolean isModified() {
        return mPanel.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        mPanel.apply();
    }

    @Override
    public void reset() {
        mPanel.reset();
    }

    @Override
    public void dispose() {
        mPanel = null;
    }
}
