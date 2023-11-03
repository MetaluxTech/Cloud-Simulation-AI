from keras.models import Sequential
from keras.layers import LSTM, Dense
import numpy as np



# Define LSTM model
def create_model(n_steps, n_features):
    model = Sequential()
    model.add(LSTM(50, activation='relu', input_shape=(n_steps, n_features)))
    model.add(Dense(1))
    model.compile(optimizer='adam', loss='mse')
    return model

#train model
def train_model(x_train,y_train):
        n_steps, n_features = x_train.shape[1], x_train.shape[2]
        model = create_model(n_steps, n_features)
        model.fit(x_train, y_train, epochs=50, verbose=0)

def predict(x_new):
    y_predict = model.predict(x_new)

#test model
def test_model(model,x_test,y_test):
    y_pred = model.predict(x_test)
    mse = np.mean(np.square(y_test - y_pred))
    print("Test MSE: {}".format(mse))

    # Assuming you have new data in X_new
#predict the output
