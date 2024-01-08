{
 "cells": [
  {
   "cell_type": "code",
   "execution_count": 1,
   "id": "5b169ee5-9af7-43dd-ae0f-f7c7f7e10bab",
   "metadata": {},
   "outputs": [
    {
     "name": "stdout",
     "output_type": "stream",
     "text": [
      "WARNING:tensorflow:From C:\\Users\\mohsal\\Desktop\\app\\metalux\\cloudsim\\ga_lstm\\AI_code\\.venv10\\lib\\site-packages\\keras\\src\\losses.py:2976: The name tf.losses.sparse_softmax_cross_entropy is deprecated. Please use tf.compat.v1.losses.sparse_softmax_cross_entropy instead.\n",
      "\n"
     ]
    }
   ],
   "source": [
    "import pandas as pd\n",
    "import seaborn as sns\n",
    "import numpy as np\n",
    "import matplotlib.pyplot as plt\n",
    "\n",
    "from sklearn.preprocessing import StandardScaler,LabelEncoder\n",
    "from sklearn.model_selection import train_test_split\n",
    "from sklearn.metrics import accuracy_score, classification_report, confusion_matrix\n",
    "\n",
    "from keras.models import Sequential\n",
    "from keras.layers import LSTM, Dense,Dropout\n",
    "from keras.utils import to_categorical\n",
    "from keras.optimizers import Adam\n",
    "from keras.callbacks import EarlyStopping\n",
    "import warnings\n",
    "# Ignore FutureWarnings from both tensorflow and keras\n",
    "warnings.filterwarnings(\"ignore\", category=FutureWarning, module=\"tensorflow\")\n",
    "warnings.filterwarnings(\"ignore\", category=FutureWarning, module=\"keras\")\n"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": 2,
   "id": "9b79ea76-1981-4712-9bae-1673399c9f72",
   "metadata": {},
   "outputs": [],
   "source": [
    "df = pd.read_csv('../dataset/tasks_dataset.csv') \n",
    "bins = [0, 40, 80, 100]  # Example thresholds for TaskFileSize\n",
    "bin_labels = ['small', 'medium', 'large']\n",
    "df['TaskFileSize_binned'] = pd.cut(df['TaskFileSize'], bins, labels=bin_labels)\n",
    "df = pd.get_dummies(df, columns=['TaskFileSize_binned'])\n",
    "\n",
    "\n",
    "df['UserRegion'] = pd.cut(df['UserLatitude'], 4).astype(str) + '_' + pd.cut(df['UserLongitude'], 4).astype(str)\n",
    "df = pd.get_dummies(df, columns=['UserRegion'])\n",
    "\n",
    "df['TaskFileSize_DataCenterID_interaction'] = df['TaskFileSize'] * df['DataCenterID']\n",
    "\n",
    "\n",
    "\n",
    "\n",
    "def assign_tier(row):\n",
    "    if row['DataCenterID'] in [1, 2]:\n",
    "        return 'Tier 1'\n",
    "    elif row['DataCenterID'] in [3, 4]:\n",
    "        return 'Tier 2'\n",
    "    else:\n",
    "        return 'Tier 3'\n",
    "\n",
    "df['FileProcessingTime_Tier'] = df.apply(assign_tier, axis=1)\n",
    "df = pd.get_dummies(df, columns=['FileProcessingTime_Tier'])\n",
    "\n",
    "\n",
    "\n",
    "\n",
    "# Example of feature engineering\n"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": 3,
   "id": "babb98eb-956e-4161-a656-47f583bb2dda",
   "metadata": {},
   "outputs": [],
   "source": [
    "# # Pairplot to visualize relationships\n",
    "# sns.pairplot(df, hue='DataCenterID')\n",
    "# plt.show()\n",
    "\n",
    "# # Correlation heatmap\n",
    "# correlation_matrix = df.corr()\n",
    "# sns.heatmap(correlation_matrix, annot=True, cmap='coolwarm')\n",
    "# plt.show()\n"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": 4,
   "id": "349a23cd-d053-41d2-a2cd-f0573a949768",
   "metadata": {},
   "outputs": [
    {
     "name": "stdout",
     "output_type": "stream",
     "text": [
      "[0 1 2]\n"
     ]
    }
   ],
   "source": [
    "X = df.drop('DataCenterID', axis=1)  # Features\n",
    "y = df['DataCenterID']-3  # Label\n",
    "\n",
    "X_train_a, X_test_a, y_train, y_test = train_test_split(X, y, test_size=0.2)\n",
    "unique_labels = np.unique(y_train.values)\n",
    "print(unique_labels)\n",
    "num_classes = len(unique_labels)"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": 7,
   "id": "fbd6c389-d52c-43bc-bcae-82279ca62002",
   "metadata": {},
   "outputs": [
    {
     "name": "stdout",
     "output_type": "stream",
     "text": [
      " xtrain  (800, 1, 27) \n",
      " ytrain  (800,) \n",
      " xtest   (200, 1, 27) \n",
      " ytest  (200,) \n",
      "\n",
      "<class 'pandas.core.frame.DataFrame'>\n",
      "RangeIndex: 1000 entries, 0 to 999\n",
      "Data columns (total 28 columns):\n",
      " #   Column                                            Non-Null Count  Dtype  \n",
      "---  ------                                            --------------  -----  \n",
      " 0   TaskFileSize                                      1000 non-null   int64  \n",
      " 1   TaskOutputFileSize                                1000 non-null   int64  \n",
      " 2   TaskFileLength                                    1000 non-null   int64  \n",
      " 3   UserLatitude                                      1000 non-null   float64\n",
      " 4   UserLongitude                                     1000 non-null   float64\n",
      " 5   DataCenterID                                      1000 non-null   int64  \n",
      " 6   TaskFileSize_binned_small                         1000 non-null   bool   \n",
      " 7   TaskFileSize_binned_medium                        1000 non-null   bool   \n",
      " 8   TaskFileSize_binned_large                         1000 non-null   bool   \n",
      " 9   UserRegion_(-0.095, 44.832]_(-180.331, -89.98]    1000 non-null   bool   \n",
      " 10  UserRegion_(-0.095, 44.832]_(-89.98, 0.0105]      1000 non-null   bool   \n",
      " 11  UserRegion_(-0.095, 44.832]_(0.0105, 90.001]      1000 non-null   bool   \n",
      " 12  UserRegion_(-0.095, 44.832]_(90.001, 179.992]     1000 non-null   bool   \n",
      " 13  UserRegion_(-45.022, -0.095]_(-180.331, -89.98]   1000 non-null   bool   \n",
      " 14  UserRegion_(-45.022, -0.095]_(-89.98, 0.0105]     1000 non-null   bool   \n",
      " 15  UserRegion_(-45.022, -0.095]_(0.0105, 90.001]     1000 non-null   bool   \n",
      " 16  UserRegion_(-45.022, -0.095]_(90.001, 179.992]    1000 non-null   bool   \n",
      " 17  UserRegion_(-90.129, -45.022]_(-180.331, -89.98]  1000 non-null   bool   \n",
      " 18  UserRegion_(-90.129, -45.022]_(-89.98, 0.0105]    1000 non-null   bool   \n",
      " 19  UserRegion_(-90.129, -45.022]_(0.0105, 90.001]    1000 non-null   bool   \n",
      " 20  UserRegion_(-90.129, -45.022]_(90.001, 179.992]   1000 non-null   bool   \n",
      " 21  UserRegion_(44.832, 89.76]_(-180.331, -89.98]     1000 non-null   bool   \n",
      " 22  UserRegion_(44.832, 89.76]_(-89.98, 0.0105]       1000 non-null   bool   \n",
      " 23  UserRegion_(44.832, 89.76]_(0.0105, 90.001]       1000 non-null   bool   \n",
      " 24  UserRegion_(44.832, 89.76]_(90.001, 179.992]      1000 non-null   bool   \n",
      " 25  TaskFileSize_DataCenterID_interaction             1000 non-null   int64  \n",
      " 26  FileProcessingTime_Tier_Tier 2                    1000 non-null   bool   \n",
      " 27  FileProcessingTime_Tier_Tier 3                    1000 non-null   bool   \n",
      "dtypes: bool(21), float64(2), int64(5)\n",
      "memory usage: 75.3 KB\n",
      "None\n"
     ]
    }
   ],
   "source": [
    "X_train = X_train_a.values.reshape((X_train_a.shape[0], 1, X_train_a.shape[1]))\n",
    "X_test = X_test_a.values.reshape((X_test_a.shape[0], 1, X_test_a.shape[1]))\n",
    "\n",
    "print(\" xtrain \", X_train.shape, \"\\n\", \"ytrain \", y_train.shape, \"\\n\", \"xtest  \", X_test.shape, \"\\n\", \"ytest \", y_test.shape, \"\\n\")\n",
    "y_train = y_train.astype(int)\n",
    "y_test = y_test.astype(int)\n",
    "print( df.info())"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": 11,
   "id": "28e8a54f-e3f1-4480-a5d2-49b029b67855",
   "metadata": {},
   "outputs": [
    {
     "ename": "ValueError",
     "evalue": "Failed to convert a NumPy array to a Tensor (Unsupported object type int).",
     "output_type": "error",
     "traceback": [
      "\u001b[1;31m---------------------------------------------------------------------------\u001b[0m",
      "\u001b[1;31mValueError\u001b[0m                                Traceback (most recent call last)",
      "Cell \u001b[1;32mIn[11], line 21\u001b[0m\n\u001b[0;32m     19\u001b[0m     \u001b[38;5;28;01mreturn\u001b[39;00m model,hist\n\u001b[0;32m     20\u001b[0m hyperparameters \u001b[38;5;241m=\u001b[39m   {\u001b[38;5;124m'\u001b[39m\u001b[38;5;124munits\u001b[39m\u001b[38;5;124m'\u001b[39m: \u001b[38;5;241m70.77199999999999\u001b[39m, \u001b[38;5;124m'\u001b[39m\u001b[38;5;124mdropout_rate\u001b[39m\u001b[38;5;124m'\u001b[39m: \u001b[38;5;241m0.1\u001b[39m, \u001b[38;5;124m'\u001b[39m\u001b[38;5;124mlearning_rate\u001b[39m\u001b[38;5;124m'\u001b[39m: \u001b[38;5;241m0.0021\u001b[39m, \u001b[38;5;124m'\u001b[39m\u001b[38;5;124mbatch_size\u001b[39m\u001b[38;5;124m'\u001b[39m: \u001b[38;5;241m21\u001b[39m}   \n\u001b[1;32m---> 21\u001b[0m model,hist\u001b[38;5;241m=\u001b[39m \u001b[43mtrainModel\u001b[49m\u001b[43m(\u001b[49m\u001b[43mhyperparameters\u001b[49m\u001b[43m)\u001b[49m\n\u001b[0;32m     22\u001b[0m accuracy \u001b[38;5;241m=\u001b[39m hist\u001b[38;5;241m.\u001b[39mhistory[\u001b[38;5;124m\"\u001b[39m\u001b[38;5;124maccuracy\u001b[39m\u001b[38;5;124m\"\u001b[39m][\u001b[38;5;241m-\u001b[39m\u001b[38;5;241m1\u001b[39m]\n\u001b[0;32m     23\u001b[0m \u001b[38;5;28mprint\u001b[39m(\u001b[38;5;124m\"\u001b[39m\u001b[38;5;124m Accuracy: \u001b[39m\u001b[38;5;124m\"\u001b[39m,\u001b[38;5;28mint\u001b[39m(accuracy\u001b[38;5;241m*\u001b[39m \u001b[38;5;241m100\u001b[39m) ,\u001b[38;5;124m'\u001b[39m\u001b[38;5;124m%\u001b[39m\u001b[38;5;124m'\u001b[39m)\n",
      "Cell \u001b[1;32mIn[11], line 14\u001b[0m, in \u001b[0;36mtrainModel\u001b[1;34m(hyperparameters)\u001b[0m\n\u001b[0;32m      9\u001b[0m model\u001b[38;5;241m.\u001b[39mcompile(optimizer\u001b[38;5;241m=\u001b[39mAdam(learning_rate\u001b[38;5;241m=\u001b[39mhyperparameters[\u001b[38;5;124m'\u001b[39m\u001b[38;5;124mlearning_rate\u001b[39m\u001b[38;5;124m'\u001b[39m]),\n\u001b[0;32m     10\u001b[0m               loss\u001b[38;5;241m=\u001b[39m\u001b[38;5;124m'\u001b[39m\u001b[38;5;124msparse_categorical_crossentropy\u001b[39m\u001b[38;5;124m'\u001b[39m,\n\u001b[0;32m     11\u001b[0m               metrics\u001b[38;5;241m=\u001b[39m[\u001b[38;5;124m'\u001b[39m\u001b[38;5;124maccuracy\u001b[39m\u001b[38;5;124m'\u001b[39m])\n\u001b[0;32m     13\u001b[0m early_stopping \u001b[38;5;241m=\u001b[39m EarlyStopping(monitor\u001b[38;5;241m=\u001b[39m\u001b[38;5;124m'\u001b[39m\u001b[38;5;124maccuracy\u001b[39m\u001b[38;5;124m'\u001b[39m, patience\u001b[38;5;241m=\u001b[39m\u001b[38;5;241m5\u001b[39m)\n\u001b[1;32m---> 14\u001b[0m hist \u001b[38;5;241m=\u001b[39m \u001b[43mmodel\u001b[49m\u001b[38;5;241;43m.\u001b[39;49m\u001b[43mfit\u001b[49m\u001b[43m(\u001b[49m\u001b[43mX_train\u001b[49m\u001b[43m,\u001b[49m\u001b[43m \u001b[49m\u001b[43my_train\u001b[49m\u001b[43m,\u001b[49m\n\u001b[0;32m     15\u001b[0m \u001b[43m                    \u001b[49m\u001b[43mepochs\u001b[49m\u001b[38;5;241;43m=\u001b[39;49m\u001b[38;5;241;43m30\u001b[39;49m\u001b[43m,\u001b[49m\n\u001b[0;32m     16\u001b[0m \u001b[43m                    \u001b[49m\u001b[43mbatch_size\u001b[49m\u001b[38;5;241;43m=\u001b[39;49m\u001b[38;5;241;43m12\u001b[39;49m\u001b[43m,\u001b[49m\n\u001b[0;32m     17\u001b[0m \u001b[43m                    \u001b[49m\u001b[43mvalidation_data\u001b[49m\u001b[38;5;241;43m=\u001b[39;49m\u001b[43m(\u001b[49m\u001b[43mX_test\u001b[49m\u001b[43m,\u001b[49m\u001b[43m \u001b[49m\u001b[43my_test\u001b[49m\u001b[43m)\u001b[49m\u001b[43m,\u001b[49m\n\u001b[0;32m     18\u001b[0m \u001b[43m                    \u001b[49m\u001b[43mcallbacks\u001b[49m\u001b[38;5;241;43m=\u001b[39;49m\u001b[43m[\u001b[49m\u001b[43mearly_stopping\u001b[49m\u001b[43m]\u001b[49m\u001b[43m)\u001b[49m\n\u001b[0;32m     19\u001b[0m \u001b[38;5;28;01mreturn\u001b[39;00m model,hist\n",
      "File \u001b[1;32m~\\Desktop\\app\\metalux\\cloudsim\\ga_lstm\\AI_code\\.venv10\\lib\\site-packages\\keras\\src\\utils\\traceback_utils.py:70\u001b[0m, in \u001b[0;36mfilter_traceback.<locals>.error_handler\u001b[1;34m(*args, **kwargs)\u001b[0m\n\u001b[0;32m     67\u001b[0m     filtered_tb \u001b[38;5;241m=\u001b[39m _process_traceback_frames(e\u001b[38;5;241m.\u001b[39m__traceback__)\n\u001b[0;32m     68\u001b[0m     \u001b[38;5;66;03m# To get the full stack trace, call:\u001b[39;00m\n\u001b[0;32m     69\u001b[0m     \u001b[38;5;66;03m# `tf.debugging.disable_traceback_filtering()`\u001b[39;00m\n\u001b[1;32m---> 70\u001b[0m     \u001b[38;5;28;01mraise\u001b[39;00m e\u001b[38;5;241m.\u001b[39mwith_traceback(filtered_tb) \u001b[38;5;28;01mfrom\u001b[39;00m \u001b[38;5;28;01mNone\u001b[39;00m\n\u001b[0;32m     71\u001b[0m \u001b[38;5;28;01mfinally\u001b[39;00m:\n\u001b[0;32m     72\u001b[0m     \u001b[38;5;28;01mdel\u001b[39;00m filtered_tb\n",
      "File \u001b[1;32m~\\Desktop\\app\\metalux\\cloudsim\\ga_lstm\\AI_code\\.venv10\\lib\\site-packages\\tensorflow\\python\\framework\\constant_op.py:103\u001b[0m, in \u001b[0;36mconvert_to_eager_tensor\u001b[1;34m(value, ctx, dtype)\u001b[0m\n\u001b[0;32m    101\u001b[0m     dtype \u001b[38;5;241m=\u001b[39m dtypes\u001b[38;5;241m.\u001b[39mas_dtype(dtype)\u001b[38;5;241m.\u001b[39mas_datatype_enum\n\u001b[0;32m    102\u001b[0m ctx\u001b[38;5;241m.\u001b[39mensure_initialized()\n\u001b[1;32m--> 103\u001b[0m \u001b[38;5;28;01mreturn\u001b[39;00m \u001b[43mops\u001b[49m\u001b[38;5;241;43m.\u001b[39;49m\u001b[43mEagerTensor\u001b[49m\u001b[43m(\u001b[49m\u001b[43mvalue\u001b[49m\u001b[43m,\u001b[49m\u001b[43m \u001b[49m\u001b[43mctx\u001b[49m\u001b[38;5;241;43m.\u001b[39;49m\u001b[43mdevice_name\u001b[49m\u001b[43m,\u001b[49m\u001b[43m \u001b[49m\u001b[43mdtype\u001b[49m\u001b[43m)\u001b[49m\n",
      "\u001b[1;31mValueError\u001b[0m: Failed to convert a NumPy array to a Tensor (Unsupported object type int)."
     ]
    }
   ],
   "source": [
    "def trainModel(hyperparameters):\n",
    "    # Build and train the model\n",
    "    model = Sequential()\n",
    "    model.add(LSTM(units=int(hyperparameters['units']), input_shape=(1, X_train.shape[2]), return_sequences=True))\n",
    "    model.add(Dropout(hyperparameters['dropout_rate']))\n",
    "    model.add(LSTM(units=int(hyperparameters['units']) // 2))\n",
    "    model.add(Dense(units=num_classes, activation='softmax'))\n",
    "\n",
    "    model.compile(optimizer=Adam(learning_rate=hyperparameters['learning_rate']),\n",
    "                  loss='sparse_categorical_crossentropy',\n",
    "                  metrics=['accuracy'])\n",
    "\n",
    "    early_stopping = EarlyStopping(monitor='accuracy', patience=5)\n",
    "    hist = model.fit(X_train, y_train,\n",
    "                        epochs=30,\n",
    "                        batch_size=int(hyperparameters['batch_size']),\n",
    "                        validation_data=(X_test, y_test),\n",
    "                        callbacks=[early_stopping])\n",
    "    return model,hist\n",
    "hyperparameters =   {'units': 70.77199999999999, 'dropout_rate': 0.1, 'learning_rate': 0.0021, 'batch_size': 21.\n",
    "                     \n",
    "                    \\\\}   \n",
    "model,hist= trainModel(hyperparameters)\n",
    "accuracy = hist.history[\"accuracy\"][-1]\n",
    "print(\" Accuracy: \",int(accuracy* 100) ,'%')\n"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": null,
   "id": "d6a9b96c-13e3-4ea2-a912-8fd3c6469dea",
   "metadata": {},
   "outputs": [],
   "source": []
  },
  {
   "cell_type": "code",
   "execution_count": null,
   "id": "c07af766-34eb-4bc4-b9ab-1166d88f1cdc",
   "metadata": {},
   "outputs": [],
   "source": [
    "accuracy = hist.history[\"accuracy\"][-1]\n",
    "print(\" Accuracy: \",int(accuracy* 100) ,'%')\n",
    "\n",
    "\n",
    "y_pred = model.predict(X_test)\n",
    "y_pred_classes = np.argmax(y_pred, axis=1)\n",
    "\n",
    "\n",
    "\n",
    "# Create a confusion matrix\n",
    "cm = confusion_matrix(y_test, y_pred_classes)\n",
    "\n",
    "# Create a colorful confusion matrix using seaborn\n",
    "plt.figure(figsize=(8, 6))\n",
    "sns.heatmap(cm, annot=True, fmt='g', cmap='Blues', xticklabels=np.unique(y_test), yticklabels=np.unique(y_test))\n",
    "plt.title('Confusion Matrix')\n",
    "plt.xlabel('Predicted')\n",
    "plt.ylabel('Actual')\n",
    "plt.show()"
   ]
  },
  {
   "cell_type": "markdown",
   "id": "3092a6e5-70cf-4059-bac3-ea24a303f34d",
   "metadata": {},
   "source": [
    "# GA"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": null,
   "id": "333079b5-a452-4b7d-96f8-4c0838e1bf14",
   "metadata": {
    "scrolled": true
   },
   "outputs": [],
   "source": [
    "from gaft import GAEngine\n",
    "from gaft.components import individual, Population,DecimalIndividual\n",
    "from gaft.operators import RouletteWheelSelection, UniformCrossover, FlipBitMutation\n",
    "\n",
    "# Define hyperparameter ranges\n",
    "hyperparameter_ranges = {\n",
    "    'units': (16, 18),\n",
    "    'dropout_rate': (0.1, 0.5),\n",
    "    'learning_rate': (0.01, 0.1),\n",
    "    'batch_size': (10, 16)\n",
    "}\n",
    "\n",
    "indv_template = DecimalIndividual(ranges=list(hyperparameter_ranges.values()))\n",
    "\n",
    "# Create population\n",
    "population = Population(indv_template=indv_template, size=50).init()\n",
    "\n",
    "# Define Genetic Algorithm operators\n",
    "selection = RouletteWheelSelection()\n",
    "crossover = UniformCrossover(pc=0.8, pe=0.5)\n",
    "mutation = FlipBitMutation(pm=0.1)\n",
    "\n",
    "# Create Genetic Algorithm engine\n",
    "engine = GAEngine(population=population, selection=selection, crossover=crossover, mutation=mutation)\n",
    "\n",
    "@engine.fitness_register\n",
    "def fitness(indv):\n",
    "    # Use hyperparameters from individual\n",
    "    hyperparameters = dict(zip(hyperparameter_ranges.keys(), indv.solution))\n",
    "\n",
    "    # Train the model with the hyperparameters\n",
    "    _,hist = trainModel(hyperparameters)  # Call your trainModel function\n",
    "    accuracy = hist.history['accuracy'][-1]  # Extract validation accuracy\n",
    "    print(\"\\nParameters: \",hyperparameters,f\" accuracy: {accuracy}\")\n",
    "    print(\"\\n\\n\\n *********************** New Generation Training Start *******************************\\n \")\n",
    "    return accuracy\n",
    "\n",
    "# Run the Genetic Algorithm\n",
    "engine.run(ng=5)\n",
    "\n",
    "# Access the best individual\n",
    "best_individual = engine.best_individual()\n",
    "best_hyperparameters = dict(zip(hyperparameter_ranges.keys(), best_individual.solution))\n",
    "print(\"Best hyperparameters: \", best_hyperparameters)\n"
   ]
  },
  {
   "cell_type": "code",
   "execution_count": null,
   "id": "901bbda0-f3ad-46f3-af27-e73aff0ea8c1",
   "metadata": {},
   "outputs": [],
   "source": []
  }
 ],
 "metadata": {
  "kernelspec": {
   "display_name": "venv10(python 3.10.0)",
   "language": "python",
   "name": "venv10"
  },
  "language_info": {
   "codemirror_mode": {
    "name": "ipython",
    "version": 3
   },
   "file_extension": ".py",
   "mimetype": "text/x-python",
   "name": "python",
   "nbconvert_exporter": "python",
   "pygments_lexer": "ipython3",
   "version": "3.10.0"
  }
 },
 "nbformat": 4,
 "nbformat_minor": 5
}
