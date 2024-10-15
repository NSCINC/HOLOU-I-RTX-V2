import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

// Estrutura de Tarefa do Kernel
class KernelTask {
    private String provider; // Provedor de armazenamento (AWS, Azure, Google, Local)
    private String path;     // Caminho no sistema de arquivos
    private int taskId;      // ID da tarefa

    public KernelTask(String provider, String path, int taskId) {
        this.provider = provider;
        this.path = path;
        this.taskId = taskId;
    }

    public String getProvider() {
        return provider;
    }

    public String getPath() {
        return path;
    }

    public int getTaskId() {
        return taskId;
    }
}

// Classe do Kernel Index para gerenciamento de tarefas
class KernelIndex {
    private List<KernelTask> tasks;
    private int currentTask;
    private final Object lock = new Object(); // Objeto para controle de acesso

    public KernelIndex(List<KernelTask> tasks) {
        this.tasks = tasks;
        this.currentTask = 0;
    }

    // Função de processamento de tarefa
    public void processTasks() {
        int numCores = Runtime.getRuntime().availableProcessors(); // Número de núcleos disponíveis
        CountDownLatch latch = new CountDownLatch(numCores); // Contador decrescente para aguardar as tarefas

        for (int i = 0; i < numCores; i++) {
            new Thread(() -> {
                while (true) {
                    KernelTask task;
                    synchronized (lock) {
                        if (currentTask >= tasks.size()) {
                            break; // Nenhuma tarefa restante
                        }
                        task = tasks.get(currentTask);
                        currentTask++;
                    }

                    // Simular processamento
                    System.out.println("Kernel: Processando tarefa " + task.getTaskId() + " em " + task.getProvider() + " - caminho: " + task.getPath());
                    try {
                        Thread.sleep(1000); // Simulação de tempo de processamento
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    System.out.println("Kernel: Finalizou tarefa " + task.getTaskId());
                }
                latch.countDown(); // Decrementa o contador ao finalizar a tarefa
            }).start();
        }

        try {
            latch.await(); // Espera todas as tarefas terminarem
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Kernel: Todas as tarefas foram processadas.");
    }
}

// Classe principal
public class KernelTasks {
    public static void main(String[] args) {
        // Definir tarefas para serem processadas pelo kernel
        List<KernelTask> tasks = new ArrayList<>();
        tasks.add(new KernelTask("AWS", "s3://bucket1/data", 1));
        tasks.add(new KernelTask("Azure", "azure://container/data", 2));
        tasks.add(new KernelTask("Google", "gcs://bucket2/data", 3));
        tasks.add(new KernelTask("Local", "/local/disk1/data", 4));
        tasks.add(new KernelTask("AWS", "s3://bucket2/data", 5));
        tasks.add(new KernelTask("Azure", "azure://container2/data", 6));
        tasks.add(new KernelTask("Google", "gcs://bucket3/data", 7));
        tasks.add(new KernelTask("Local", "/local/disk2/data", 8));

        // Rodar o kernel com as tarefas definidas
        KernelIndex kernelIndex = new KernelIndex(tasks);
        kernelIndex.processTasks();
    }
}
