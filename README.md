# HivelAssignment

## Description
**HivelAssignment** is a Java program that reads JSON files containing numbers in different bases, decodes them to decimal, and computes the **constant term (C)** of a polynomial using Gaussian elimination.  

It supports multiple test cases and handles large numbers efficiently.

---

## Features
- Dynamically reads JSON input files.  
- Decodes numbers from any base to decimal.  
- Computes polynomial coefficients.  
- Prints only the **constant term `C`**.  
- Handles large integer values without precision loss.  

---

## Requirements
- Java 11 or higher  
- [Gson library](https://github.com/google/gson) (`gson-2.10.1.jar`)  

---

## Project Structure
HivelAssignment/
├─ src/
│ └─ Main.java
├─ first.json
├─ second.json
├─ lib/
│ └─ gson-2.10.1.jar
└─ README.md

---

## How to Run
1. **Compile the Java program**:
```bash
javac -cp ".;lib/gson-2.10.1.jar" src/Main.java
Run the program with a JSON file:

java -cp ".;lib/gson-2.10.1.jar;src" Main first.json
java -cp ".;lib/gson-2.10.1.jar;src" Main second.json


Output:
The program prints the constant term C of the polynomial.

Example Output:

C = 3                       # for first.json
C = -6290016743746469796     # for second.json

JSON Input Format
{
  "keys": {"n": 4, "k": 3},
  "1": {"base": "10", "value": "4"},
  "2": {"base": "2", "value": "111"},
  "3": {"base": "10", "value": "12"},
  "6": {"base": "4", "value": "213"}
}


"n" → total number of keys

"k" → number of roots used for polynomial calculation

Each key contains:

base → number base (2, 8, 10, 16, etc.)

value → number in that base

