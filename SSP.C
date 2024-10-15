import 'dart:ffi';
import 'dart:io';
import 'package:ffi/ffi.dart';
import 'package:sqlite3/sqlite3.dart';

const int MAX_INVESTORS = 100;

class InvestmentContract {
  List<String> investors = List.filled(MAX_INVESTORS, '');
  List<int> authorizedInvestors = List.filled(MAX_INVESTORS, 0);
  List<int> balances = List.filled(MAX_INVESTORS, 0);
  List<int> investedAmount = List.filled(MAX_INVESTORS, 0);
  int investorCount = 0;
}

// Callback function to handle SQL query results
int callback(InvestmentContract contract, int count, List<String> columnNames, List<dynamic> values) {
  if (contract.investorCount >= MAX_INVESTORS) {
    print('Max investors reached.');
    return 0; // To avoid exceeding the limit
  }
  // Store investor data
  contract.investors[contract.investorCount] = values[0];
  contract.authorizedInvestors[contract.investorCount] = values[1];
  contract.balances[contract.investorCount] = values[2];
  contract.investedAmount[contract.investorCount] = 0; // Initial investment
  contract.investorCount++;
  return 0;
}

// Function to connect to the database and initialize investors
void initializeContract(InvestmentContract contract) {
  contract.investorCount = 0;

  final db = sqlite3.open('investors.db');

  // Create the Investors table if it doesn't exist
  db.execute('''
    CREATE TABLE IF NOT EXISTS Investors (
      Name TEXT,
      Authorized INTEGER,
      Balance INTEGER
    );
  ''');

  // Insert sample investors
  db.execute('''
    INSERT INTO Investors (Name, Authorized, Balance)
    VALUES 
      ('Alice', 1, 1000), 
      ('Bob', 0, 500), 
      ('Charlie', 1, 1500);
  ''');

  // Query investors from the database
  final resultSet = db.select('SELECT Name, Authorized, Balance FROM Investors;');
  for (var row in resultSet) {
    callback(contract, row.length, [], row);
  }

  // Close the database
  db.dispose();
}

// Function to invest
void invest(InvestmentContract contract, String investor, int amount) {
  if (amount <= 0) {
    print('Investment amount must be greater than zero');
    return;
  }

  int investorIndex = -1;

  // Check if the investor is in the investor list
  for (int i = 0; i < contract.investorCount; i++) {
    if (contract.investors[i] == investor) {
      investorIndex = i;
      break;
    }
  }

  if (investorIndex == -1) {
    print("Investor '$investor' not found in the contract");
    return;
  }

  // Check if the investor is authorized
  if (contract.authorizedInvestors[investorIndex] == 0) {
    print("Investor '$investor' is not authorized to invest");
    return;
  }

  // Check if there are sufficient funds
  if (amount > contract.balances[investorIndex]) {
    print("Insufficient balance for investor '$investor'. Current balance: ${contract.balances[investorIndex]}");
    return;
  }

  // Update balances and invested amounts
  contract.balances[investorIndex] -= amount;
  contract.investedAmount[investorIndex] += amount;
  print("Investment of $amount made by '$investor'");
}

// Main function
void main() {
  InvestmentContract contract = InvestmentContract();
  initializeContract(contract);

  // Testing the investment function
  invest(contract, 'Alice', 200);   // Valid investment
  invest(contract, 'Bob', 100);     // Unauthorized investor
  invest(contract, 'Charlie', 2000); // Insufficient balance
  invest(contract, 'Dave', 100);    // Investor not found
}
