package org.dailyTasks;

import java.util.List;

public class DailyTaskProcessor {
    private final List<DailyTask> dailyTasks;
    /* TODO: consider a runnable.
    Consider a service with separate methods instead?
     consider monitoring
      this is probably when I should consider Spring Boot.*/

    public DailyTaskProcessor(List<DailyTask> dailyTasks) {
        this.dailyTasks = dailyTasks;
    }

    public void run() {
        dailyTasks.forEach(DailyTask::run);
    }
}
