package io.valkey;

import io.valkey.Protocol.ResponseKeyword;
import io.valkey.util.SafeEncoder;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JedisPubSubBaseTest extends TestCase {

    public void testProceed_givenThreadInterrupt_exitLoop() throws InterruptedException {
        // setup
        final JedisPubSubBase<String> pubSub = new JedisPubSubBase<String>() {

            @Override
            public void onMessage(String channel, String message) {
                fail("this should not happen when thread is interrupted");
            }

            @Override
            protected String encode(byte[] raw) {
                return SafeEncoder.encode(raw);
            }
        };

        final Connection mockConnection = mock(Connection.class);
        final List<Object> mockSubscribe = Arrays.asList(
                ResponseKeyword.SUBSCRIBE.getRaw(), "channel".getBytes(), 1L
        );
        final List<Object> mockResponse = Arrays.asList(
                ResponseKeyword.MESSAGE.getRaw(), "channel".getBytes(), "message".getBytes()
        );

        when(mockConnection.getUnflushedObject()).

                thenReturn(mockSubscribe, mockResponse);


        final CountDownLatch countDownLatch = new CountDownLatch(1);
        // action
        final Thread thread = new Thread(() -> {
            Thread.currentThread().interrupt();
            pubSub.proceed(mockConnection, "channel");

            countDownLatch.countDown();
        });
        thread.start();

        assertTrue(countDownLatch.await(10, TimeUnit.MILLISECONDS));

    }
}
