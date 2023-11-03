import numpy as np
from creat_model import predict, train_model
import plot_data
import import_data 
import split_data

df=import_data.import_df()
x_train,y_train,x_test,y_test=split_data.split_data(df)
model=train_model(x_train,y_train)
predicted_data=predict(model,x_test)
