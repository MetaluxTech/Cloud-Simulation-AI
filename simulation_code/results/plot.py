
import pandas as pd
import matplotlib.pyplot as plt
modelName="Functions" #SNAKE or  GA or None or Functions


df = pd.read_csv(modelName+"_results.csv") 

# Create separate plots with customizations
for column_name, values in df.items():
    plt.figure()
    plt.plot(df["Number of Processed Tasks"], values)
    plt.xlabel("Number of Processed Tasks")
    plt.ylabel(column_name)
    plt.title(f"{column_name} vs Number of Processed Tasks")
    plt.grid(True)
    plt.show()