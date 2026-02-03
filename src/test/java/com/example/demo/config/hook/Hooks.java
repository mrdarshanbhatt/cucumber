package com.example.demo.config.hook;

import com.example.demo.config.factory.DriverFactory;
import org.junit.After;
import org.junit.Before;

public class Hooks {

    @Before
    public void setUp() {
        DriverFactory.initDriver();
    }

    @After
    public void tearDown() {
        DriverFactory.quitDriver();
    }

}
