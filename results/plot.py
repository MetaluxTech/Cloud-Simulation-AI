import pandas as pd
import matplotlib.pyplot as plt

# Create some example data
data = {
    "Number of Processed Tasks": [10, 20, 30, 40, 50],
    "Metric 1": [5, 10, 15, 20, 25],
    "Metric 2": [10, 8, 6, 4, 2],
}

df = pd.DataFrame(data)

# Create separate plots with customizations
for column_name, values in df.items():
    plt.figure()
    plt.plot(df["Number of Processed Tasks"], values)
    plt.xlabel("Number of Processed Tasks")
    plt.ylabel(column_name)
    plt.title(f"{column_name} vs Number of Processed Tasks")
    plt.grid(True)
    plt.savefig(f"{column_name}.png")  # Save the plot to a PNG image file
