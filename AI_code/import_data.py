import pandas as pd

def import_df():
    # Import and preprocess data
    df = pd.read_csv('data/dataCenters.csv')
    df.index = [str(num)[-7:] for num in df.index] 
    df.set_index('TimeStamp', inplace=True)

    # Handle zero values
    df.replace(0, pd.NA, inplace=True)
    df = df.fillna(method='bfill')
    df.fillna(method='ffill', inplace=True)

    # Display results
    print(((df == 0) | df.isna()).sum())
   
    return df