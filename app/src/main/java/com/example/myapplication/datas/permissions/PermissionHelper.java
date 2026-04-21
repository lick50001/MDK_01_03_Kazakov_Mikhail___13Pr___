package com.example.myapplication.datas.permissions;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

public class PermissionHelper {
    public static boolean CheakPermission(Context context)   {
        Boolean AudioPerm = context.checkCallingOrSelfPermission(
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        return  AudioPerm;
    }
}
