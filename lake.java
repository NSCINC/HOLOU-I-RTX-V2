import java.sql.*;

public class InvestmentManager {
    private Connection connection;

    public InvestmentManager() {
        initializeDatabase();
    }

    // Inicializa o banco de dados
    private void initializeDatabase() {
        String dbName = "investments.db";
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            createTables();
            initializeAssets();
        } catch (SQLException e) {
            System.out.println("Erro ao abrir o banco de dados: " + e.getMessage());
        }
    }

    // Cria as tabelas no banco de dados
    private void createTables() {
        String createInvestorsTable = "CREATE TABLE IF NOT EXISTS Investors (" +
                                       "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                       "name TEXT NOT NULL, " +
                                       "email TEXT NOT NULL, " +
                                       "phone_number TEXT NOT NULL);";

        String createAssetsTable = "CREATE TABLE IF NOT EXISTS Assets (" +
                                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    "asset_name TEXT NOT NULL, " +
                                    "asset_type TEXT NOT NULL, " +
                                    "total_slots INTEGER NOT NULL, " +
                                    "annual_return REAL NOT NULL);";

        String createInvestmentsTable = "CREATE TABLE IF NOT EXISTS Investments (" +
                                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                         "investor_id INTEGER, " +
                                         "asset_id INTEGER, " +
                                         "invested_amount REAL, " +
                                         "FOREIGN KEY(investor_id) REFERENCES Investors(id), " +
                                         "FOREIGN KEY(asset_id) REFERENCES Assets(id));";

        executeSQL(createInvestorsTable);
        executeSQL(createAssetsTable);
        executeSQL(createInvestmentsTable);
    }

    // Executa um comando SQL
    private void executeSQL(String sql) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("SQL executado com sucesso: " + sql);
        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
        }
    }

    // Inicializa dados de ativos
    private void initializeAssets() {
        insertAsset("Ação XYZ", "Ação", 100, 12.0);
        insertAsset("Fundo Imobiliário ABC", "Fundo", 50, 8.0);
    }

    // Insere ativos no banco de dados
    private void insertAsset(String assetName, String assetType, int totalSlots, double annualReturn) {
        String insertSQL = "INSERT INTO Assets (asset_name, asset_type, total_slots, annual_return) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, assetName);
            pstmt.setString(2, assetType);
            pstmt.setInt(3, totalSlots);
            pstmt.setDouble(4, annualReturn);
            pstmt.executeUpdate();
            System.out.println("Ativo inserido com sucesso: " + assetName);
        } catch (SQLException e) {
            System.out.println("Erro ao inserir ativo: " + e.getMessage());
        }
    }

    // Método para fechar a conexão (opcional, pode ser chamado no final)
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Conexão com o banco de dados fechada.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        InvestmentManager investmentManager = new InvestmentManager();
        // Feche a conexão ao final do uso
        investmentManager.closeConnection();
    }
}
