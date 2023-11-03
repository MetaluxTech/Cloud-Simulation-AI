
from keras.models import Sequential
from keras.layers import LSTM, Dense, Dropout, Bidirectional

def train_model(x_train,y_train):
    model = Sequential()
    model.add(Bidirectional(LSTM(20, return_sequences=True), input_shape=(x_train.shape[1], x_train.shape[2])))
    model.add(Dropout(0.2))
    model.add(LSTM(20))
    model.add(Dropout(0.2))
    model.add(Dense(y_train.shape[1]))
    model.compile(loss='mean_squared_error', optimizer='adam')
    model.fit(x_train, y_train, epochs=20, batch_size=1, verbose=2)
    return model


def predict(model,x_test):
    predicted_data=model.predict(x_test)
    return predicted_data
