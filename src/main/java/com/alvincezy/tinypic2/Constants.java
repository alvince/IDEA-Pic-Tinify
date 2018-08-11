package com.alvincezy.tinypic2;

/**
 * Created by alvince on 17-6-28.
 *
 * @author alvince.zy@gmail.com
 * @version 1.0.3-SNAPSHOT, 2018/8/12
 * @since 1.0
 */
public interface Constants {
    boolean DEBUG = true;

    String APP_NAME = "Tinify Picture";    // Plugin name

    String DISPLAY_GROUP_PROMPT = "Tinify Picture Prompt";

    String HTML_DESCRIPTION_IGNORE = "#ignore";
    String HTML_DESCRIPTION_SETTINGS = "#settings";
    String HTML_LINK_IGNORE = "<a href='" + HTML_DESCRIPTION_IGNORE + "'>不再提示</a>";
    String HTML_LINK_SETTINGS = "<a href='" + HTML_DESCRIPTION_SETTINGS + "'>设置</a>";

    String LINK_TINY_PNG_DEVELOPER = "https://tinypng.com/developers";
}
