package pelican.pelican;

import android.util.Log;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        ///assertEquals(4, 2 + 2);
        Set<String> videos = new HashSet<>();

        String s1 = "hello";
        System.out.print(videos.add(s1));

        String s2 = "hello";
        System.out.print(videos.add(s2));

        System.out.println(videos.toString());
    }
}