import os
import pandas as pd

def merge_csv_files(input_folder, output_file):
    # Get a list of all CSV files in the input folder
    csv_files = [f for f in os.listdir(input_folder) if f.endswith('.csv')]

    # Initialize an empty DataFrame to store the merged data
    merged_data = pd.DataFrame()

    # Loop through each CSV file and append its data to the merged_data DataFrame
    for csv_file in csv_files:
        file_path = os.path.join(input_folder, csv_file)
        df = pd.read_csv(file_path)
        merged_data = pd.concat([merged_data, df])

    # Save the merged data to a new CSV file
    merged_data.to_csv(output_file, index=False)

if __name__ == "__main__":
    input_folder = "C:/Users/mohsal/Desktop/app/metalux/cloudsim/ga_lstm/simulation_code"
    output_file = "all_results.csv"

    merge_csv_files(input_folder, output_file)

    print(f"Merged data saved to {output_file}")
