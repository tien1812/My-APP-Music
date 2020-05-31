package vn.tien.tienmusic.notification;

import androidx.annotation.StringDef;

@StringDef({NotificationAction.MAIN_ACTION, NotificationAction.PREV_ACTION,
        NotificationAction.PLAY_ACTION, NotificationAction.NEXT_ACTION,
        NotificationAction.START_FOREGROUND_ACTION,
        NotificationAction.STOP_FOREGROUND_ACTION})
public @interface NotificationAction {
    String MAIN_ACTION = "main_action";
    String PREV_ACTION = "prev_action";
    String PLAY_ACTION = "play_action";
    String NEXT_ACTION = "next_action";
    String START_FOREGROUND_ACTION = "start_foreground_action";
    String STOP_FOREGROUND_ACTION = "stop_foreground_action";
}