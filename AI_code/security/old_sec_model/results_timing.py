from numpy import absolute
import pandas as pd
import matplotlib.pyplot as plt

# Read the CSV file

df = pd.read_csv("cloudlets_security_2000.csv")
encryption_time=0.0325 # =~ 30 ms
decryption_time=0.0157 # =~ 10 ms

task_status_counts = df["taskStatus"].value_counts()
sum_execution_time = df["taskExecutionTime"].sum()

sum_not_trusted = task_status_counts[task_status_counts.index != "Trusted Data"].sum()
sum_trusted = task_status_counts[task_status_counts.index == "Trusted Data"].sum()

sum_trusted_exec_time = df.loc[df['taskStatus'] == "Trusted Data", 'taskExecutionTime'].sum()
sum_not_trusted_exec_time = df.loc[df['taskStatus'] != "Trusted Data", 'taskExecutionTime'].sum()

sum_total_data=sum_trusted+sum_not_trusted


print(task_status_counts)

print("sum  trusterd data: ", sum_trusted)
print("sum none trusterd data: ", sum_not_trusted)
print("Total encryption time: ", (sum_total_data) * encryption_time , " s")
print("Total decryption time: ", (sum_total_data) * decryption_time, " s")

print("Total Trusted Tasks Exce Time: ",sum_trusted_exec_time, " s")
print("Total Not Trusted Tasks Exce Time: ",sum_not_trusted_exec_time, " s")
print("Total Tasks Exce Time: ",sum_execution_time, " s")

# Create a pie chart
# plt.figure(figsize=(8, 6))
# plt.pie(task_status_counts, labels=task_status_counts.index, autopct='%1.1f%%',
#         pctdistance=0.85,
#         labeldistance=1.1)  # Increase labeldistance to move labels outward
# plt.title("Tasks Security Status")
# plt.axis('equal')  # Equal aspect ratio ensures that pie is drawn as a circle.
# plt.show()
# Create a pie chart
import matplotlib.pyplot as plt

# Calculate time saved due to trusted data
time_saved = sum_not_trusted_exec_time - (sum_total_data) * (encryption_time + decryption_time)

# Create a bar chart
plt.figure(figsize=(8, 6))
plt.bar(["Execution Time", "Encryption/Decryption Time"], [sum_execution_time, (sum_total_data) * (encryption_time + decryption_time)], color=["blue", "orange"])
plt.bar("Execution Time", time_saved, color="green")
plt.xlabel("Time Components")
plt.ylabel("Time (seconds)")
plt.title("Time Analysis: Execution vs. Encryption/Decryption")
plt.legend(["Execution Time", "Encryption/Decryption Time", "Time Saved"])
plt.show()
