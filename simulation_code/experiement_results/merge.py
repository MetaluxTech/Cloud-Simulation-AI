
import os
import pandas as pd
modelName="functions" #SNAKE or GA or None or Functions or New_model
input_folder =os.getcwd()
output_file = "_"+modelName+"_results.csv"

def merge_csv_files(input_folder, output_file):
  # Get a list of all CSV files in the input folder
  csv_files = [f for f in os.listdir(input_folder) if f.endswith('.csv') and modelName.upper() in f and (  not "result" in f)]
  print("csvFiles: ",csv_files)
  # Initialize an empty DataFrame to store the merged data
  merged_data = pd.DataFrame()

  # Loop through each CSV file and append its data to the merged_data DataFrame
  for csv_file in csv_files:
    file_path = os.path.join(input_folder, csv_file)
    df = pd.read_csv(file_path)
    merged_data = pd.concat([merged_data, df])

  # Save the merged data to a new CSV file
    if (len(csv_files)>0): 
      merged_data.to_csv(output_file, index=False)

if __name__ == "__main__":
 
  merge_csv_files(input_folder, output_file)

  print(f"Merged data saved to {output_file}")
