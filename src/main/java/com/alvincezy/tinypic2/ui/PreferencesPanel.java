package com.alvincezy.tinypic2.ui;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.labels.ActionLink;
import com.intellij.ui.components.labels.LinkLabel;
import com.alvincezy.tinypic2.Constants;
import com.alvincezy.tinypic2.Preferences;
import com.alvincezy.tinypic2.util.ComponentUtil;
import com.alvincezy.tinypic2.util.StringUtil;

import javax.swing.*;

/**
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 2.0, 6/28/2017
 * @since 2.0
 */
public class PreferencesPanel {

    private JPanel mTopPanel;
    private JPanel mApiKeySettingsPanel;
    private JTextField mApiKeyField;
    private LinkLabel mGetApiKeyLink;

    private Preferences mPrefs;

    public JComponent create(Preferences prefs) {
        mPrefs = prefs;
        setApiKeyPanel();
        return mTopPanel;
    }

    public boolean isModified() {
        String apiKeyInput = ComponentUtil.getInputText(mApiKeyField);
        return StringUtil.isNotEmpty(apiKeyInput) && !StringUtil.equals(apiKeyInput, mPrefs.getApiKey());
    }

    public void apply() {
        mPrefs.setApiKey(ComponentUtil.getInputText(mApiKeyField));
    }

    public void reset() {
        mApiKeyField.setText(mPrefs.getApiKey());
    }

    private void createUIComponents() {
        mGetApiKeyLink = new ActionLink("", new AnAction() {
            @Override
            public void actionPerformed(AnActionEvent anActionEvent) {
                BrowserUtil.browse(Constants.LINK_TINY_PNG_DEVELOPER);
            }
        });
    }

    private void setApiKeyPanel() {
        mApiKeySettingsPanel.setBorder(IdeBorderFactory.createTitledBorder("TinyPng"));
    }
}
