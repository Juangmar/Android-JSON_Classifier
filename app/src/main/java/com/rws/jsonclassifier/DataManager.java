package com.rws.jsonclassifier;

import java.io.File;

public class DataManager {
    public static final int INITIALIZING = 000;

    public static final int ON_EXECUTION = 100;

    public static final int EXEPTION_COUGHT = 200;

    public static final int SUCCESS = 300;
    public static final int ALLSUCCESS = 301;

    public static final int ERROR = 400;
    public static final int FATALERROR = 401;

    public static int status;

    public static File mother_path;
    public static File original_path;
    public static File destiny_path;
}
