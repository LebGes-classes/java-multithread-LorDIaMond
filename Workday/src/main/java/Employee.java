import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Employee implements Runnable {
    private int id;
    private String name;
    private Queue<Integer> tasks;
    private int totalWorkTime = 0;
    private int idleTime = 0;
    private int tasksCompleted = 0;

    public Employee(int id, String name, List<Integer> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = new LinkedList<>(tasks);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalWorkTime() {
        return totalWorkTime;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public Queue<Integer> getTasks() {
        return tasks;
    }

    @Override
    public void run() {
        System.out.println(name + " начал работать...");

        while (!tasks.isEmpty()) {
            int remainingDayTime = 8;

            while (remainingDayTime > 0 && !tasks.isEmpty()) {
                int currentTask = tasks.peek();

                if (currentTask <= remainingDayTime) {

                    totalWorkTime += currentTask;
                    remainingDayTime -= currentTask;
                    tasks.poll();
                    tasksCompleted++;
                } else {

                    totalWorkTime += remainingDayTime;
                    tasks.poll();
                    tasks.add(currentTask - remainingDayTime);
                    remainingDayTime = 0;
                }
            }


            if (!tasks.isEmpty()) {
                System.out.println(name + " переносит задачи на следующий день.");
            }
        }

        // Расчет эффективности
        double efficiency = (double) totalWorkTime / (8 * (tasksCompleted + (tasks.isEmpty() ? 0 : 1)));
        System.out.printf("%s: %dч работы, %dч простоя, %d задач выполнено, эффективность: %.2f%%\n",
                name, totalWorkTime, idleTime, tasksCompleted, efficiency * 100);
    }
}