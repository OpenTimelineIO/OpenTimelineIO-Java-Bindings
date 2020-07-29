package io.opentimeline;

import io.opentimeline.opentime.RationalTime;
import io.opentimeline.opentimelineio.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DummyTest {

    @Test
    public void test() {
        try {
            AnyDictionary anyDictionary = new AnyDictionary();
            anyDictionary.put("foo", new Any("bar"));
            RationalTime rationalTime = new RationalTime(10, 24);
            Transition transition = new Transition("test", "test", rationalTime, rationalTime, anyDictionary);
            ErrorStatus errorStatus = new ErrorStatus();
            System.out.println(transition.toJSONString(errorStatus));
            transition.getNativeManager().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {

//        for (int i = 0; i < 1000; i++) {
//            Any any = OTIOFactory.getInstance().createAny("x");
//            Thread.sleep(10);
//            if (i % 100 == 0) System.gc();
//        }
//        Thread.sleep(1000);
//        System.gc();
//        System.gc();
//        System.gc();
//        System.out.println("////////////////");
//        OTIOFactory.getInstance().cleanUp();
//        for (int i = 1000; i < 10000; i++) {
//            Any any = OTIOFactory.getInstance().createAny("x");
//            Thread.sleep(10);
//            if (i % 100 == 0) System.gc();
//        }
    }

}
