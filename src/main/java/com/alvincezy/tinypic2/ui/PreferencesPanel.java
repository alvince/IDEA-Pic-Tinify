package com.alvincezy.tinypic2.ui;

import com.alvincezy.tinypic2.Constants;
import com.alvincezy.tinypic2.Preferences;
import com.alvincezy.tinypic2.util.ComponentUtil;
import com.alvincezy.tinypic2.util.StringUtil;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.labels.ActionLink;
import com.intellij.ui.components.labels.LinkLabel;

import javax.swing.*;

/**
 * Created by alvince on 2017/6/28.
 *
 * @author alvince.zy@gmail.com
 * @version 1.1.1, 2018/8/20
 * @since 1.0
 */
public class PreferencesPanel {

    private JPanel mTopPanel;
    private JPanel mApiKeySettingsPanel;
    private JTextField mApiKeyField;
    private LinkLabel mGetApiKeyLink;
    private JPanel mOptionsPanel;
    private JCheckBox mOptionBackupCheck;

    private Preferences mPrefs;

    public JComponent create(Preferences prefs) {
        mPrefs = prefs;
        setApiKeyPanel();
        return mTopPanel;
    }

    public boolean isModified() {
        String apiKeyInput = ComponentUtil.getInputText(mApiKeyField);
        if (!StringUtil.equals(apiKeyInput, mPrefs.getApiKey())) {
            return true;
        }

        boolean optionBackup = mOptionBackupCheck.isSelected();
        return mPrefs.isBackupBeforeTinify() != optionBackup;
    }

    public void apply() {
        mPrefs.setApiKey(ComponentUtil.getInputText(mApiKeyField));
        mPrefs.setBackupBeforeTinify(mOptionBackupCheck.isSelected());
    }

    public void reset() {
        mApiKeyField.setText(mPrefs.getApiKey());
        mOptionBackupCheck.setSelected(mPrefs.isBackupBeforeTinify());
    }

    private void createUIComponents() {
        mGetApiKeyLink = new ActionLink("", new AnAction() {
            @Override
            public void actionPerformed(AnActionEvent actionEvent) {
                BrowserUtil.browse(Constants.LINK_TINY_PNG_DEVELOPER);
            }
        });
    }

    private void setApiKeyPanel() {
        mApiKeySettingsPanel.setBorder(IdeBorderFactory.createTitledBorder("TinyPng"));
        mOptionsPanel.setBorder(IdeBorderFactory.createTitledBorder("Options"));
    }
}
