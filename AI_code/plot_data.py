import matplotlib.pyplot as plt

def plot(df):
    df.head(10).plot(figsize=(12, 8), title='Workload Trace over Time')

    plt.xlabel('TimeStamp')
    plt.ylabel('Value')
    plt.legend(loc='upper left')

    plt.tight_layout()
    plt.show()