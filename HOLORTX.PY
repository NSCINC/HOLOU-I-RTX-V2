import sqlite3

class InvestmentManager:
    def __init__(self):
        self.db_name = "investments.db"
        self.db = self.initialize_database()

    # Initializes the database
    def initialize_database(self):
        try:
            conn = sqlite3.connect(self.db_name)
            print(f"Connected to database: {self.db_name}")
            self.create_tables(conn)
            self.initialize_assets(conn)
            return conn
        except sqlite3.Error as e:
            print(f"Error opening database: {e}")
            return None

    # Creates the necessary tables
    def create_tables(self, conn):
        queries = [
            """
            CREATE TABLE IF NOT EXISTS Investors (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT NOT NULL,
                phone_number TEXT NOT NULL
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS Assets (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                asset_name TEXT NOT NULL,
                asset_type TEXT NOT NULL,
                total_slots INTEGER NOT NULL,
                annual_return REAL NOT NULL
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS Investments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                investor_id INTEGER,
                asset_id INTEGER,
                invested_amount REAL,
                FOREIGN KEY(investor_id) REFERENCES Investors(id),
                FOREIGN KEY(asset_id) REFERENCES Assets(id)
            );
            """
        ]
        
        for query in queries:
            self.execute_sql(conn, query)

    # Executes an SQL command
    def execute_sql(self, conn, sql):
        try:
            cursor = conn.cursor()
            cursor.execute(sql)
            conn.commit()
            print(f"SQL executed successfully: {sql}")
        except sqlite3.Error as e:
            print(f"SQL error: {e}")

    # Initializes default assets in the database
    def initialize_assets(self, conn):
        assets = [
            ("Ação XYZ", "Ação", 100, 12.0),
            ("Fundo Imobiliário ABC", "Fundo", 50, 8.0)
        ]
        
        for asset in assets:
            self.insert_asset(conn, asset)

    # Inserts an asset into the database
    def insert_asset(self, conn, asset):
        insert_sql = """
        INSERT INTO Assets (asset_name, asset_type, total_slots, annual_return)
        VALUES (?, ?, ?, ?);
        """
        
        try:
            cursor = conn.cursor()
            cursor.execute(insert_sql, asset)
            conn.commit()
            print(f"Asset inserted successfully: {asset[0]}")
        except sqlite3.Error as e:
            print(f"Error inserting asset: {e}")

    # Cleanup database connection
    def close(self):
        if self.db:
            self.db.close()
            print(f"Database connection closed: {self.db_name}")

# Usage
if __name__ == "__main__":
    investment_manager = InvestmentManager()

    # Cleanup database connection on exit
    import atexit
    atexit.register(investment_manager.close)
