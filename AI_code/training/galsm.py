# %%
import pandas as pd
import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt

from sklearn.preprocessing import StandardScaler,LabelEncoder
from sklearn.preprocessing import StandardScaler,LabelEncoder
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix

from keras.models import Sequential
from keras.layers import LSTM, Dense,Dropout
from keras.utils import to_categorical
from keras.optimizers import Adam
from keras.callbacks import EarlyStopping
import warnings
warnings.filterwarnings("ignore", category=DeprecationWarning, module="tensorflow")
warnings.resetwarnings()


# %%
df = pd.read_csv('../dataset/dataset_2.csv') 

bool_columns = df.select_dtypes(include=bool).columns

df = df.drop(columns=['StartTime','TaskID','DistanceFromDataCenter', 'DataCenterCpuCost', 'DataCenterRamCost',
                      'DataCenterStorageCost', 'DataCenterBwCost', 'DataCenterTotalLoad',
                      'NetworkDelay', 'CET', 'ObjectiveFunction'])
# Convert boolean columns to integers (True -> 1, False -> 0)
df[bool_columns] = df[bool_columns].astype(int)

# Print the modified DataFrame

X = df.drop('DataCenterID', axis=1)  # Features
y = df['DataCenterID']-3  # Label
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)
unique_labels = np.unique(y_train.values)
print(unique_labels)
print(df.info())
num_classes = len(unique_labels)
# Example of feature engineering


# %%
# Pairplot to visualize relationships
sns.pairplot(df, hue='DataCenterID')
plt.show()

# # Correlation heatmap
# correlation_matrix = df.corr()
# sns.heatmap(correlation_matrix, annot=True, cmap='coolwarm')
# plt.show()


# %%
if (len(X_train.shape)<3):
    X_train = X_train.values.reshape((X_train.shape[0], 1, X_train.shape[1]))
    X_test = X_test.values.reshape((X_test.shape[0], 1, X_test.shape[1]))

print(" xtrain ", X_train.shape, "\n", "ytrain ", y_train.shape, "\n", "xtest  ", X_test.shape, "\n", "ytest ", y_test.shape, "\n")




# %%
def trainModel(hyperparameters):
    # Build and train the model
    model = Sequential()
    model.add(LSTM(units=int(hyperparameters['units']), input_shape=(1, X_train.shape[2]), return_sequences=True))
    model.add(Dropout(hyperparameters['dropout_rate']))
    model.add(LSTM(units=int(hyperparameters['units']) // 2))
    model.add(Dense(units=num_classes, activation='softmax'))

    model.compile(optimizer=Adam(learning_rate=hyperparameters['learning_rate']),
                  loss='sparse_categorical_crossentropy',
                  metrics=['accuracy'])

    early_stopping = EarlyStopping(monitor='accuracy', patience=15)
    hist = model.fit(X_train, y_train,
                        epochs=100,
                        batch_size=int(hyperparameters['batch_size']),
                        validation_data=(X_test, y_test),
                        callbacks=[early_stopping])
    return model,hist
hyperparameters =   {'units': 70.04564, 'dropout_rate': 0.1, 'learning_rate': 0.0021, 'batch_size': 21}   
model,hist= trainModel(hyperparameters)
accuracy = hist.history["accuracy"][-1]
print("\n\n Accuracy: ",int(accuracy* 100) ,'%')


# %%


# %%
accuracy = max(hist.history["accuracy"])
print(" Accuracy: ",int(accuracy* 100) ,'%')


y_pred = model.predict(X_test)
y_pred_classes = np.argmax(y_pred, axis=1)



# Create a confusion matrix
cm = confusion_matrix(y_test, y_pred_classes)

# Create a colorful confusion matrix using seaborn
plt.figure(figsize=(8, 6))
sns.heatmap(cm, annot=True, fmt='g', cmap='Blues', xticklabels=np.unique(y_test), yticklabels=np.unique(y_test))
plt.title('Confusion Matrix')
plt.xlabel('Predicted')
plt.ylabel('Actual')
plt.show()

# %% [markdown]
# # GA

# %%
from gaft import GAEngine
from gaft.components import individual, Population,DecimalIndividual
from gaft.operators import RouletteWheelSelection, UniformCrossover, FlipBitMutation

# Define hyperparameter ranges
hyperparameter_ranges = {
    'units': (16, 18),
    'dropout_rate': (0.1, 0.5),
    'learning_rate': (0.01, 0.1),
    'batch_size': (10, 16)
}

indv_template = DecimalIndividual(ranges=list(hyperparameter_ranges.values()))

# Create population
population = Population(indv_template=indv_template, size=50).init()

# Define Genetic Algorithm operators
selection = RouletteWheelSelection()
crossover = UniformCrossover(pc=0.8, pe=0.5)
mutation = FlipBitMutation(pm=0.1)

# Create Genetic Algorithm engine
engine = GAEngine(population=population, selection=selection, crossover=crossover, mutation=mutation)

@engine.fitness_register
def fitness(indv):
    # Use hyperparameters from individual
    hyperparameters = dict(zip(hyperparameter_ranges.keys(), indv.solution))

    # Train the model with the hyperparameters
    _,hist = trainModel(hyperparameters)  # Call your trainModel function
    accuracy = hist.history['accuracy'][-1]  # Extract validation accuracy
    print("\nParameters: ",hyperparameters,f" accuracy: {accuracy}")
    print("\n\n\n *********************** New Generation Training Start *******************************\n ")
    return accuracy

# Run the Genetic Algorithm
engine.run(ng=5)

# Access the best individual
best_individual = engine.best_individual()
best_hyperparameters = dict(zip(hyperparameter_ranges.keys(), best_individual.solution))
print("Best hyperparameters: ", best_hyperparameters)


# %%



