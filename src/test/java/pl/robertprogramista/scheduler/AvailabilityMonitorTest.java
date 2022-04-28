package pl.robertprogramista.scheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "check.availability.cron=*/1 * * * * *"
})
class AvailabilityMonitorTest {
    @Autowired
    private ScheduledTaskHolder scheduledTaskHolder;

    @Test
    void checkAvailability() {
        Set<ScheduledTask> scheduledTasks = scheduledTaskHolder.getScheduledTasks();
        long scheduledTasksCount = scheduledTasks.stream()
                .filter(cronTask -> cronTask.toString()
                        .equals("pl.robertprogramista.scheduler.AvailabilityMonitor.checkAvailability"))
                .count();
        assertThat(scheduledTasksCount).isEqualTo(1L);
    }
}