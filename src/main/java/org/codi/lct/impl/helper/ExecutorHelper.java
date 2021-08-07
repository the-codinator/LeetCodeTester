package org.codi.lct.impl.helper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import lombok.experimental.UtilityClass;
import org.codi.lct.core.LCException;

@UtilityClass
public class ExecutorHelper {

    public ExecutorService createSingleThreadExecutor(String threadName, Integer threadPriority) {
        return Executors.newSingleThreadExecutor(new ThreadFactory() {
            private final ThreadFactory delegateThreadFactory = Executors.defaultThreadFactory();

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = delegateThreadFactory.newThread(r);
                if (threadName != null) {
                    thread.setName(threadName);
                }
                if (threadPriority != null) {
                    thread.setPriority(threadPriority);
                }
                return thread;
            }
        });
    }

    public void warmExecutorService(ExecutorService executor) {
        Future<Void> future = executor.submit(() -> null);
        // TODO: the thread isn't getting freed when we have an infinite loop
        try {
            future.get();
        } catch (Exception e) {
            throw new LCException("Error warming executor service: " + executor, e);
        }
    }
}
