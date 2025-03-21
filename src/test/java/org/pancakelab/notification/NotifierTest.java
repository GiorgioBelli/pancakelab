package org.pancakelab.notification;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class NotifierTest {

    static final String subject1 = "subject1";
    static final String subject2 = "subject2";

    @Test
    void GivenSubscriber_WhenAddIt_IsAdded() {
        // setup
        var notifier = new Notifier<Void>() {
            public int subsCount = 0;

            @Override
            public void addSubscriber(Subscriber<Void> subscriber) {
                super.addSubscriber(subscriber);
                this.subsCount += 1;
            }
        };
        Subscriber<Void> subscriber = new Subscriber<>() {
            @Override
            public void update(Void voidUpdate) {}

            @Override
            public List<String> getSubject() {
                return List.of("subject1");
            }
        };

        // exercise
        notifier.addSubscriber(subscriber);

        //verify
        assertEquals(1, notifier.subsCount);
    }

    @Test
    void GivenSubscriber_WhenNotify_ThenSubscriberIsNotified() {

        // setup
        Notifier<String> notifier = new Notifier<>();
        AssertableSubscriber sub1 = new AssertableSubscriber(subject1);
        AssertableSubscriber sub2 = new AssertableSubscriber(subject2);
        notifier.addSubscriber(sub1);
        notifier.addSubscriber(sub2);

        // exercise
        notifier.notify("message", subject1);

        // verify
        sub1.assertUpdated(1);
        sub2.assertUpdated(0);
    }

    private static class AssertableSubscriber implements Subscriber<String> {
        private final List<String> subjects;
        private final AtomicInteger updatesCount;

        public AssertableSubscriber(String ...subjects) {
            this.subjects = Arrays.asList(subjects);
            this.updatesCount = new AtomicInteger(0);
        }

        @Override
        public void update(String update) {
            this.updatesCount.incrementAndGet();
        }

        @Override
        public List<String> getSubject() {
            return this.subjects;
        }

        public void assertUpdated(int count) {
            assertEquals(count, this.updatesCount.get());
        }

    }
}

