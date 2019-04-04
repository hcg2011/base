package com.chungo.baseapp;

import java.util.HashMap;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleInstrumentedTest {


    public static void useAppContext() {
        // Context of the app under test.
        //        Context appContext = InstrumentationRegistry.getTargetContext();
        //
        //        assertEquals("com.chungo.baseapp", appContext.getPackageName());
       // numJewelsInStones("aBC", "aaBBadkfojkllkC");
    }

    public static int numJewelsInStones(String J, String S) {
        HashMap map = new HashMap<String, String>();
        int size = 0;
        for (int i = 0; i < J.length(); i++)
            map.put(J.charAt(i), null);

        for (int i = 0; i < S.length(); i++) {
            char value = S.charAt(i);
            if (map.containsKey(value))
                size++;
        }
        System.out.print("size=" + size);
        return size;
    }
}
