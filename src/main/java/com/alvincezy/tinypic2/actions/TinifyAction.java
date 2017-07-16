package com.alvincezy.tinypic2.actions;

import com.alvincezy.tinypic2.Preferences;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import javax.annotation.Nonnull;

/**
 * Created by alvince on 17-7-11.
 *
 * @author alvince.zy@gmail.com
 * @version 2.0, 7/12/2017
 */
@SuppressWarnings("ComponentNotRegistered")
public class TinifyAction extends AnAction {

    Project mProject;
    Preferences mPrefs;

    TinifyAction() {
        super();
        mPrefs = Preferences.getInstance();
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        mProject = getEventProject(anActionEvent);
        performAction(anActionEvent, mProject);
    }

    protected void performAction(@Nonnull AnActionEvent anActionEvent, @Nonnull Project project) {
    }
}
