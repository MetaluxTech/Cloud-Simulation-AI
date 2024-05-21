from numpy import absolute
import pandas as pd
import matplotlib.pyplot as plt

# Read the CSV file

df = pd.read_csv("cloudlets_security_2000.csv")

task_status_counts = df["taskStatus"].value_counts()

sum_not_trusted = task_status_counts[task_status_counts.index != "Trusted Data"].sum()
sum_trusted = task_status_counts[task_status_counts.index == "Trusted Data"].sum()

sum_trusted_tasks_size = df.loc[df['taskStatus'] == "Trusted Data", 'TaskFileSize'].sum()
sum_not_trusted_tasks_size= df.loc[df['taskStatus'] != "Trusted Data", 'TaskFileSize'].sum()

sum_total_tasks=sum_trusted+sum_not_trusted


print(task_status_counts)

print("Total Tasks Count : ",f' {sum_total_tasks}')
print("sum  trusterd Tasks: ",f' {sum_trusted}')
print("sum none trusterd Tasks: ",f' {sum_not_trusted}')

print("Total Trusted Tasks Storage Utilization: ",f'{sum_trusted_tasks_size} MB')
print("Total Not Trusted Tasks Storage Utilization (Storage Saved): ",f'{sum_not_trusted_tasks_size} MB')
print("Total Tasks Storage Utilization: ",f'{sum_trusted_tasks_size+sum_not_trusted_tasks_size} MB')

import matplotlib.pyplot as plt

# Calculate time saved due to trusted data
storage_saved = (sum_total_tasks) -sum_not_trusted_tasks_size 

# Create a bar chart
plt.figure(figsize=(8, 6))
plt.bar("Total storage", sum_total_tasks, color="green")
plt.bar("Storage Saved", storage_saved, color="red")
plt.bar("Storage Trusted data", sum_trusted_tasks_size, color="blue")
plt.xlabel("Storage Components")
plt.ylabel("Storage (MBs)")
plt.title("Storage Analysis: Storage Utilization Security Model")
plt.legend(["Execution Time", "Encryption/Decryption Time", "Time Saved"])
plt.show()
