import utils.RC4Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.RecursiveTask;

/**
 * 使用工作窃取算法执行生成邀请码任务
 * @author ChenHanLin 2020/2/27
 */
public class MyTask extends RecursiveTask<List<String>> {

    // 阈值
    private static final int THRESHOLD = 10000;
    private int start;
    private int end;

    public MyTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected List<String> compute() {
        List<String> list = new ArrayList<>();
        boolean canCompute = (end - start) <= THRESHOLD;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                list.add(RC4Utils.encry_RC4_string(String.format("%04d", i), UUID.randomUUID().toString()).toUpperCase());
            }
        } else {
            // 如果任务大于阈值，就分裂成两个子任务计算
            int middle = (start + end) / 2;
            MyTask leftTask = new MyTask(start, middle);
            MyTask rightTask = new MyTask(middle + 1, end);
            // 执行子任务（又会进入compute方法）
            leftTask.fork();
            rightTask.fork();
            // 等待子任务执行完，并得到其结果
            List<String> leftResult = leftTask.join();
            List<String> rightResult = rightTask.join();
            // 合并子任务
            leftResult.addAll(rightResult);
            list = leftResult;
        }
        return list;
    }
}
