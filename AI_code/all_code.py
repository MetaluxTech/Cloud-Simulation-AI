#import data
import numpy as np
import pandas as pd
from keras.models import Sequential
from keras.layers import LSTM, Dense, Dropout, Bidirectional
from keras.callbacks import EarlyStopping
from sklearn.model_selection import train_test_split


df = pd.read_csv('data/dataCenters.csv')
df.index = [str(num)[-7:] for num in df.index] 
df.set_index('TimeStamp', inplace=True)
df.replace(0, pd.NA, inplace=True)
df = df.fillna(method='bfill')
df.fillna(method='ffill', inplace=True)
print(((df == 0) | df.isna()).sum())


# split the data to train and test 
def create_lstm_matrix(dataset, look_back=1):
    dataX, dataY = [], []
    for i in range(len(dataset)-look_back-1):
        a = dataset[i:(i+look_back), :]
        dataX.append(a)
        dataY.append(dataset[i + look_back, :])
    return np.array(dataX), np.array(dataY)
look_back = 15
train_precentage=0.7
X=df.drop('datacenter_score', axis=1)
Y=df['datacenter_score']
x_train, x_test, y_train, y_test = train_test_split(X,Y, test_size=0.3, random_state=42)

# Reshape the data for LSTM
x_train, y_train = create_lstm_matrix(x_train, look_back)
x_test, y_test = create_lstm_matrix(x_test, look_back)

# train the model
model = Sequential()
model.add(Bidirectional(LSTM(50, return_sequences=True), input_shape=(x_train.shape[1], x_train.shape[2])))
model.add(Dropout(0.2))
model.add(LSTM(50))
model.add(Dropout(0.2))
model.add(Dense(y_train.shape[1]))
model.compile(loss='mean_squared_error', optimizer='adam')
es = EarlyStopping(monitor='val_loss', mode='min', verbose=1, patience=50)
model.fit(x_train, y_train, validation_split=0.2, epochs=100, batch_size=32, verbose=2, callbacks=[es])


