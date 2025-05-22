from tpot import TPOTRegressor
from sklearn.metrics import mean_squared_error
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from matplotlib import pyplot
from sklearn.model_selection import train_test_split
import seaborn as sns
import numpy


tpot_data = pd.read_csv('TPOTDATAFILE20162020CleanManualNormalized.csv', sep=';', dtype=np.float64)

#Create and plot a correlation matrix.
names = ['kmTravelled', 'kmCounterStart', 'kmCounterEnd', 'motorTime', 'drivingTime', 'drivingPercentage', 'stationaryTime', 'stationaryPercentage', 'averageSpeed', 'soloPercentage', 'soloKm', 'emtyPercentage', 'emptyKm', 'unknownPercentage', 'unKnownKm', 'LoadedKm', 'loadedPercentage', 'target']
correlations = tpot_data.corr()
fig = pyplot.figure()
ax = fig.add_subplot(111)
cax = ax.matshow(correlations, vmin=-1, vmax=1)
fig.colorbar(cax)
ticks = numpy.arange(0,18,1)
ax.set_xticks(ticks)
ax.set_yticks(ticks)
ax.set_xticklabels(names, rotation='vertical')
ax.set_yticklabels(names)
pyplot.savefig('correlationMatrixNormalized.png')
pyplot.show()

#Split data into features and target
features = tpot_data.drop('target', axis=1)
print(features)
print(tpot_data)
X_train, X_test, y_train, y_test = train_test_split(features, tpot_data['target'],
                                                    train_size=0.75, test_size=0.25, random_state=42)

#Create tpot instance
#n_jobs option to increase number of processes
#warm_start option to reuse population from previous calls to fit()
#Verbosity option for amount of print info
#random_state seed of the random number generator
tpot = TPOTRegressor(n_jobs=-1, generations=200, population_size=100, verbosity=3, random_state=42)
#optimize pipeline
tpot.fit(X_train, y_train)
#evaluate final pipeline
print(tpot.score(X_test, y_test))
#Export corresponding python code for optimal pipeline
tpot.export('pipelineNormalizedKm.py')

print('RMSE:')
print(np.sqrt(mean_squared_error(y_test, tpot.predict(X_test))))