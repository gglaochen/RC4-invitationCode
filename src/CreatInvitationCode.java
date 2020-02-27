import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * 邀请码生成器
 *
 * @author ChenHanLin 2020/2/27
 */
public class CreatInvitationCode {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MyTask task = new MyTask(1, 20000);
        Future<List<String>> result = forkJoinPool.submit(task);

        //如果执行任务异常则打印异常
        if (task.isCompletedAbnormally()) {
            task.getException().printStackTrace();
        }
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            List<String> list = result.get();
            System.out.println("任务执行时长：" + Duration.between(localDateTime, LocalDateTime.now()).toMillis());
            System.out.println("list长度" + list.size());
            Set<String> set = new HashSet(list);
            System.out.println("set长度" + set.size());
            System.out.println("邀请码：" + set.iterator().next());
        } catch (InterruptedException e) {
            System.out.println("邀请码生成任务被打断");
        } catch (ExecutionException e) {
            System.out.println("邀请码生成失败");
        }
    }
}
