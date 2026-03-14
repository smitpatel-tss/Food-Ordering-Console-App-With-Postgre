package com.tss.test;

import com.tss.config.DBConnection;
import com.tss.core.App;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }
}

/*
FEEDBACKS:
searching using names , take less time in log in and fast retrieval
single log in page
switch case not in control panel
session time out
store password - hashed
 */