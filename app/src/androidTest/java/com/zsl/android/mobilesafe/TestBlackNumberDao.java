package com.zsl.android.mobilesafe;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TestBlackNumberDao {

    private Context mContext;

    @Before
    public void setUp() throws Exception {
        this.mContext = InstrumentationRegistry.getTargetContext();
        System.out.println("setUP()...");
    }

    @Test
    public void method1(){
        assertEquals(1,1);
    }
}
