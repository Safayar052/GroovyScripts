# Groovy Scripts
This project contains various groovy scripts for repetetive tasks, data mining, boomi related custom scripts.

## Setup
- Groovy : https://groovy-lang.org/install.html
- VSCode (recommended): https://code.visualstudio.com

## Getting started
To run scripts
```sh
groovy <script_path> <logfile_directory> <path_to_output_csv>
```

## Example
```sh
groovy logparse.groovy "C:\Rust\HelloWorld\ish_test_WA_log" "C:\serachterm.csv"
```
- This command will execute logparse.groovy script from current directory with command line argument
- The path to output csv file is optional

## Script Directory
- webadapter_logparse
    - This script extracts unique search terms from intershop webadapter logs
