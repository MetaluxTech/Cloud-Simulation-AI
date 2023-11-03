import numpy as np
# convert an array of values into a dataset matrix

# Create input matrix
def create_dataset(dataset, look_back=1):
    dataX, dataY = [], []
    for i in range(len(dataset)-look_back-1):
        a = dataset[i:(i+look_back), :]
        dataX.append(a)
        dataY.append(dataset[i + look_back, :])
    return np.array(dataX), np.array(dataY)

look_back = 15


def split_data(df):
    train_precentage=0.7
    train_size = int(len(df) * train_precentage)
    train, test = df[0:train_size,:], df[train_size:len(df),:]
    x_train, y_train = create_dataset(train, look_back)
    x_test, y_test = create_dataset(test, look_back)
    return (x_train,y_train,x_test,y_test)