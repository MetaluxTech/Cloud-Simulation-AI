import numpy as np
from keras.models import Sequential
from keras.layers import LSTM, Dense
from gaft import GAEngine
from gaft.components import BinaryIndividual, Population  # Import Population here
from gaft.operators import RouletteWheelSelection, UniformCrossover, FlipBitMutation

# decode ga indivaduals to lstm parameters 
def decode(indv):
    # Convert binary strings to integers
    n_steps = int(indv[0])
    n_features = int(indv[1])
    return n_steps, n_features


# Define LSTM model
def create_model(n_steps, n_features):
    model = Sequential()
    model.add(LSTM(50, activation='relu', input_shape=(n_steps, n_features)))
    model.add(Dense(1))
    model.compile(optimizer='adam', loss='mse')
    return model


# Define fitness function for GA
def fitness_function(indv):
    # Decode GA individual to LSTM parameters
    n_steps, n_features = decode(indv)

    # Create and train LSTM model
    model = create_model(n_steps, n_features)
    model.fit(X_train, y_train, epochs=50, verbose=0)

    # Calculate prediction error (MSE)
    y_pred = model.predict(X_test)
    mse = np.mean(np.square(y_test - y_pred))

    # Since GA is maximizing and we want to minimize MSE, return negative MSE
    return -mse

# Create GA engine
def run_ga_engine():
    # Create GA engine
    indv_template = BinaryIndividual(ranges=[(0, 100), (0, 100)], eps=0.001)
    population = Population(indv_template=indv_template, size=50).init()

    selection = RouletteWheelSelection()
    crossover = UniformCrossover(pc=0.8, pe=0.5)
    mutation = FlipBitMutation(pm=0.1)

    engine = GAEngine(population=population, selection=selection,
                      crossover=crossover, mutation=mutation,
                      fitness=fitness_function)
    
    # Run GA engine
    engine.run(ng=50)  # Run GA for 50 generations

# Run GA engine
if __name__ == '__main__':
    run_ga_engine()  # Run GA for 50 generations
