package holocore.ssp;

public class HolocoreStage2 {
    private String dualChannelData;

    // Construtor para inicializar o canal com os dados processados do Stage 1
    public HolocoreStage2(String dualChannelData) {
        this.dualChannelData = dualChannelData;
    }

    // Método que processa os dados recebidos e prepara para o Hyper Channel
    public void continueProcessing() {
        System.out.println("Recebendo dados do Dual Channel: " + dualChannelData);
        processData();
        sendToHyperChannel();
    }

    // Simula processamento adicional no Stage 2
    private void processData() {
        System.out.println("Processando dados no Stage 2...");
        try {
            Thread.sleep(1500); // Simula o tempo de processamento adicional
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Processamento no Stage 2 completo.");
    }

    // Prepara e envia os dados para o Hyper Channel
    private void sendToHyperChannel() {
        System.out.println("Enviando dados processados para o Hyper Channel.");
        // Aqui, os dados seriam enviados para o HolocoreStage3 (Hyper Channel)
    }

    public static void main(String[] args) {
        // Dados simulados recebidos do Stage 1
        HolocoreStage2 stage2 = new HolocoreStage2("Dual Channel Processed Data");
        stage2.continueProcessing();
    }
}
