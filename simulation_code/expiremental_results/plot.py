import pandas as pd
import matplotlib.pyplot as plt

# Read data from Excel file
df = pd.read_csv("all_results.csv")  # Adjust the filename if needed

# Create separate plots with customizations
for column_name, values in df.items():
    plt.figure()
    plt.plot(df["Number of Processed Tasks"], values)
    plt.xlabel("Number of Processed Tasks")
    plt.ylabel(column_name)
    plt.title(f"{column_name} vs Number of Processed Tasks")
    plt.grid(True)
    plt.show()
